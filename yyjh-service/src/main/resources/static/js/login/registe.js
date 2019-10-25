$(document).ready(function(){
    /**
     * *****************************
     * *****************************
     * *****************************
     * 注册*************************
     * *****************************
     * *****************************
     * *****************************
     */
    //登陆界面
    //注册表格回调方法
    $('#registe_form').ajaxForm(function (data) {
        let flag = data.code == 0 ? true : false;
        //刷新验证码
        getCode("r_code","registe");
        //重置表格
        $("#registe_form").find("input[type='reset']").click();
        if(flag){
            //提示
            alert("注册成功！");
            //面板切换
            gotoLogin();
        }
        else{
            //提示
            alert(data.payload);
        }
    });
    //datasource界面
    //添加用户回调方法
    $('#insertUser_form').ajaxForm(function (data) {
        let flag = data.code == 0 ? true : false;
        //重置表格
        $("#insertUser_form").find("input[type='reset']").click();
        if(flag){
            //提示
            alert("添加成功！");
            $("#authorityAddUser_modal").modal("hide");
            //刷新table
            $("#authority_table").bootstrapTable("refresh");
        }
        else{
            //提示
            alert(data.payload);
        }
    });
    //上传图片
    $("#r_headimg_container").click(function(){
        //添加图片
        $("#r_headfile").click();
    })
    $("#r_headfile").change(function(){
        if($(this).val() != ""){
            let file = this.files[0];
            let url  = getObjectURL(file);
            $("#r_headimg").css("display","block");
            $("#r_headimg").prop("src",url);
            $("#r_headimg").prev().css("display","none");
        }
        else{
            $("#r_headimg").css("display","none");
            $("#r_headimg").prev().css("display","block");
        }
    })
    //昵称事件
    $("#r_nickname").keyup(function(){
        let obj = $(this).next();
        if($(this).val().length>0){
            showSuccessAddon(obj);
        }
        else{
            showInitAddon(obj);
        }
    })
    //用户名
    $("#r_user").change(function(){
        let loginid = $(this).val();
        $.ajax({
            url:"/login/isLoginIdRepeat",
            type:"post",
            data:{
                "loginid":loginid
            },
            success:(data)=>{
                let flag = data.code == 0?true:false;
                let obj  = $(this).next();
                if(loginid == ""){
                    showInitAddon(obj);
                    return;
                }
                if(flag){
                    alert("账号已存在！");
                    showFailAddon(obj);//loginid已存在
                    $("#r_user").focus();
                }
                else
                    showSuccessAddon(obj);//loginid可用
            },
            error:function(e){
                alert("出错："+e);
            }
        });
    })
    //密码
    $("#r_pwd").keyup(function(){
        let pwd = $(this).val();
        let obj = $(this).next();
        if(pwd.length == 0)
            showInitAddon(obj);
        else if(pwd.length >= 6 && pwd.length <= 20)
            showSuccessAddon(obj);
        else
            showFailAddon(obj);
    })
    //确认密码
    $("#r_repwd").blur(function(){
        let repwd = $(this).val();
        let pwd   = $("#r_pwd").val();
        let obj = $(this).next();
        if(repwd.length == 0){
            showInitAddon(obj);
            return;
        }
        if(repwd == pwd && pwd.length >= 6)
            showSuccessAddon(obj);
        else
            showFailAddon(obj);
    })
    //邮箱
    $("#r_email").change(function(){
        let email = $(this).val();
        let obj = $(this).next();
        let flag = checkEmailFormat(email);
        if(email.length == 0){
            showInitAddon(obj);
            return;
        }
        if(flag){
            //邮箱格式正确
            //验证邮箱是否已注册过
            $.ajax({
                url:"/login/isEmailRepeat",
                type:"post",
                data:{
                    "email":email
                },
                success:(data)=>{
                    let flag = data.code == 0?true:false;
                    let obj  = $(this).next();
                    if(flag) {
                        alert("邮箱已注册过！");
                        showFailAddon(obj);//loginid已存在
                    }
                    else
                        showSuccessAddon(obj);    //loginid可用
                },
                fail:function(e){
                    alert("出错："+e);
                }
            });
        }
        else{
            showFailAddon(obj);
        }
    })
    //电话
    $("#r_tel").change(function(){
        let tel = $(this).val();
        let obj = $(this).next();
        if(tel.length == 0){
            showInitAddon(obj);
            return;
        }
        else if(tel.length>5) {
            //邮箱格式正确
            //验证邮箱是否已注册过
            $.ajax({
                url: "/login/isTelRepeat",
                type: "post",
                data: {
                    "tel": tel
                },
                success: (data) => {
                    let flag = data.code == 0 ? true : false;
                    let obj = $(this).next();
                    if (flag) {
                        alert("手机号已注册过！");
                        showFailAddon(obj);
                        $("#r_tel").focus();
                    }
                    else
                        showSuccessAddon(obj);
                },
                error: function (e) {
                    alert("出错：" + e);
                }
            });
        }
        else
            showFailAddon(obj);
    })
    //验证码
    $("#r_code").click(function(){
        getCode("r_code","registe");
    })
    $("#r_mycode").keyup(function(){
        checkCode(this,"#registe_form");
    })
    //验证码点击切换
    $("#mycode").keyup(function(){
        checkCode(this,"#login_form");
    })
    //重置
    $("#registe_form").find("input[type='reset']").on("click",function(){
        //初始化addons
        let addons = $(this).parents("form").find("span[flag='1']");
        showInitAddon(addons);
        $("#r_mycode").next().children("span").prop("class","glyphicon glyphicon-minus");
        $("#r_mycode").next().children("span").css("color","#555");
        $("#r_headplus").css("display","block");
        $("#r_headimg").css("display","none");
        //生成新验证码
        getCode("r_code","registe");
        //聚焦文本框
        $("#r_nickname").focus();
    })
    $("#insertUser_form").find("input[type='reset']").on("click",function(){
        //初始化addons
        let addons = $(this).parents("form").find("span[flag='1']");
        showInitAddon(addons);
        $("#r_mycode").next().children("span").prop("class","glyphicon glyphicon-minus");
        $("#r_mycode").next().children("span").css("color","#555");
        $("#r_headplus").css("display","block");
        $("#r_headimg").css("display","none");
        //聚焦文本框
        $("#r_nickname").focus();
    })
})

//方法

/**
 * 登陆界面
 * 注册form
 */
function registe(){
    let addons = $("#registe_form").find("span[flag='1']");
    let flag = false;
    //判断验证码是否正确
    let code = $("#r_mycode").val();
    if(code.toLowerCase() != r_new_code.toLowerCase()){
        alert("验证码有误！\n请重新输入！");
        $("#r_mycode").focus();
        return false;
    }
    //判断信息是否有效
    let i=0;
    for(;i<addons.length;i++){
        if(addons.eq(i).prop("class") != "input-group-addon glyphicon glyphicon-ok")
            break;
    }
    if(i == addons.length)
        flag = true;
    if(!flag){
        alert("输入的信息有误！\n请核对完重新输入！");
        $("#r_nickname").focus();
        return false;
    }
    return true;
}

/**
 * datasource界面
 * 添加form（也算是注册）
 */
function insertUser(){
    let addons = $("#insertUser_form").find("span[flag='1']");
    let flag = false;
    //判断信息是否有效
    let i=0;
    for(;i<addons.length;i++){
        if(addons.eq(i).prop("class") != "input-group-addon glyphicon glyphicon-ok")
            break;
    }
    console.log(i);
    if(i == addons.length)
        flag = true;
    if(!flag){
        alert("输入的信息有误！\n请核对完重新输入！");
        $("#r_nickname").focus();
        return false;
    }
    return true;
}