/**
 * 个人信息
 */
$(document).ready(function(){
    /**
     * 头像事件
     */
    //头像上传文件file插件
    infoChangeUserimg();
    $("#info_userimg").mouseenter(function(){
        $(this).css({
            "opacity" : "0.4",
            "filter":"brightness(70%)",
        })
        $("#i_u_message").css({
            "display" : "block",
        })
    })
    $("#info_userimg").mouseleave(function(){
        $(this).css({
            "opacity" : "1",
            "filter":"brightness(100%)",
        })
        $("#i_u_message").css({
            "display" : "none",
        })
    })
    $("#info_userimg").click(function(){
        if(owninfo != null && owninfo.loginid != null && owninfo.loginid != "")
            $("#info_userimg_file").click();
        else
            alert("账号登陆状态出错！\n获取不到用户数据！");
    })
    /**
     * 昵称点击修改
     */
    var info_oldNickname;
    $("#info_nickname").click(function(){
        info_oldNickname = $(this).val();
        $("#info_nickname").css({
            "cursor":"text",
        })
        $("#info_nickname").select();
    })
    $("#info_nickname").blur(function(){
        $("#info_nickname").css({
            "cursor":"pointer",
        })
    })
    $("#info_nickname").change(function(){
        let nickname = $(this).val();
        if(nickname <= 0){
            $(this).val(info_oldNickname);
            return ;
        }
        if(confirm("确定修改？\n昵称："+nickname)){
            //进后台
            $.ajax({
                url:"/datasource/infoChangeSimpleInfo",
                type:"post",
                data:{
                    "loginid"  : owninfo.loginid,
                    "infoname" : "nickname",
                    "value"    : nickname
                },
                success:function(data){
                    console.log(data);
                    if(data.code == 0){
                        //修改成功！
                        alert("修改成功！");
                        //修改导航栏中的昵称
                        $("#navbar_nickname").text(nickname);
                        //修改owninfo.niackname
                        owninfo.nickname=nickname;
                        //文本失去聚焦
                        $("#info_nickname").blur();
                        //明信片修改
                        changePostcard();
                        setTimeout(function(){
                            $("#i_p_nickname").text(nickname);
                        },1500);
                    }
                    else{
                        alert("修改失败！");
                    }
                },
                error:function(e){
                    console.log(e);
                    alert("无权操作");
                }
            })
        }
        else{
            $("#info_nickname").val(info_oldNickname);
            $("#info_nickname").focus();
            $("#info_nickname").select();
        }
    })
    /**
     * 修改按钮
     */
    var info_oldEmail = ""; //记录修改前的数据
    var info_oldTel = ""; //记录修改前的数据
    $(".info_btns").click(function(){
        let text_obj   = $(this).parent().children("input");
        let change_obj = text_obj.attr("name");
        let value      = text_obj.val();
        if($(this).text() == "修改"){
            if(change_obj == "email")
                info_oldEmail = value;
            else if(change_obj == "tel")
                info_oldTel   = value;
            //将text的disabled属性解开
            text_obj.prop("disabled","");
            //修改样式
            text_obj.css("color","rgba(128,128,128)");
            text_obj.focus();
            text_obj.select();
            //修改按钮名
            $(this).text("完成");
        }
        else{
            if(change_obj == "email"){
                let flag = checkEmailFormat(value);
                if(value.length == 0){
                    text_obj.val(info_oldEmail);
                    //修改按钮名
                    $(this).text("修改");
                    //添加text的disabled属性
                    text_obj.prop("disabled","disabled");
                    //修改样式
                    text_obj.css("color","rgba(128,128,128,0.5)");
                    return;
                }
                if(!flag){
                    alert("邮箱格式有误！");
                    text_obj.focus();
                    return;
                }
            }
            else if(change_obj == "tel"){
                if(value.length == 0){
                    text_obj.val(info_oldTel);
                    //修改按钮名
                    $(this).text("修改");
                    //添加text的disabled属性
                    text_obj.prop("disabled","disabled");
                    //修改样式
                    text_obj.css("color","rgba(128,128,128,0.5)");
                    return;
                }
                else if(value.length < 5){
                    alert("电话格式有误！");
                    text_obj.focus();
                    return;
                }
            }
            //提交后台
            $.ajax({
                url:"/datasource/infoChangeSimpleInfo",
                type:"post",
                data:{
                    "loginid"  : owninfo.loginid,
                    "infoname" : change_obj,
                    "value"    : value
                },
                success:function(data){
                    console.log(data);
                    if(data.code == 0){
                        //修改成功！
                        alert("修改成功！");
                        if(change_obj == "email"){
                            //更新owninfo.email
                            owninfo.email = value;
                            //明信片修改
                            changePostcard();
                            setTimeout(function(){
                                $("#i_p_email").text(value);
                            },1500);
                        }
                        else if(change_obj == "tel"){
                            //更新owninfo.email
                            owninfo.tel=value;
                            //明信片修改
                            changePostcard();
                            setTimeout(function(){
                                $("#i_p_tel").text(value);
                            },1500);
                        }
                    }
                    else{
                        alert("修改失败！\n"+data.payload);
                        if(change_obj == "email")
                            text_obj.val(info_oldEmail);
                        else if(change_obj == "tel")
                            text_obj.val(info_oldTel);
                    }
                },
                error:function(e){
                    console.log(e);
                    alert("无权操作");
                },
            })
            //修改按钮名
            $(this).text("修改");
            //添加text的disabled属性
            text_obj.prop("disabled","disabled");
            //修改样式
            text_obj.css("color","rgba(128,128,128,0.5)");
        }
    })
})

/**
 * **************************************
 * **************************************
 * 方法
 * **************************************
 * **************************************
 */
/**
 * 从后台获取个人信息
 */
function getOwnInfo(user){
    $.ajax({
        url:"/datasource/getOwnInfo",
        type:"post",
        data:{
            "user" : user,
        },
        async:false,
        success:function(data){
            console.log(data);
            if(data.code == 0){
                //获取成功
                if(data.hasOwnProperty("payload") && data.payload != null){
                    owninfo = data.payload;
                    //修改owninfo.userimg值
                    if(owninfo.userimg != null && owninfo.userimg != "")
                        owninfo.userimg = "../../imgs/" + owninfo.userimg;
                    else{
                        //使用默认头像
                        owninfo.userimg = "../../imgs/init_headimg.jpg";
                    }
                    //修改导航栏中用户的信息
                    if(JSON.stringify(owninfo) != "{}"){
                        if(owninfo.userimg != null)
                            $("#navbar_headimg").attr("src",owninfo.userimg);
                        $("#navbar_nickname").text(owninfo.nickname);
                    }
                }
                else{
                    alert("系统后台返回值数据值出错！");
                }
            }
            else{
                alert("获取个人信息失败！\n用户:"+user);
            }
        },
        error:function(e){
            console.log(e);
            alert("无权操作");
        }
    })
}
/**
 * 判断邮件格式是否正确
 */
function checkEmailFormat(email){
    let reg = "^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$";
    let flag = email.match(reg);
    return flag;
}
/**
 *个人信息中头像上传
 * */
var info_oldUserimg = "";
function infoChangeUserimg(){
    $("#info_userimg_file").fileupload({
        url: '/infoChangeUserimg',
        type: 'post',
        dataType: 'json',  //服务器返回的数据类型
        add: function(e,data) {
            //过滤（过大文件）
            //后台配置最大文件不超过1048576（1M）
            let size = data.files[0].size;
            if(size > 1024*1024){
                alert("图片文件过大");
                $("#info_userimg").attr("src",info_oldUserimg);
                return;
            }
            if(confirm("确定修改该账号头像？")){
                //进后台
                //上传用户login_id
                $("#info_userimg_file").bind('fileuploadsubmit',function(e,data) {
                    //jquery-file-upload插件问题，要上传属性，就得在formData中添加
                    data.formData = {
                        "loginid": owninfo.loginid
                    };
                });
                data.submit();
            }
        },
        done: function(e,data) {
            console.log(data);
            //成功执行
            let result = data.result;
            if(result.code == 0){
                if(result.hasOwnProperty("payload") && result.payload != null) {
                    //上传失败
                    alert("头像修改失败！\n"+result.payload);
                } else {
                    //上传成功
                    alert("头像修改成功！");
                    //因为要重新启动服务器才能拿到图片，这里选用input[type=file]中文本图片路径
                    let file = data.files[0];
                    let url = "";
                    if(window.createObjectURL != undefined) { // basic
                        url = window.createObjectURL(file);
                    } else if(window.URL != undefined) { // mozilla(firefox)
                        url = window.URL.createObjectURL(file);
                    } else if(window.webkitURL != undefined) { // web_kit or chrome
                        url = window.webkitURL.createObjectURL(file);
                    }
                    //修改owninfo中userimg
                    owninfo.userimg = url;
                    //修改三处头像
                    $("#navbar_headimg").attr("src",url);
                    $("#info_userimg").attr("src",url);
                    //明信片修改
                    changePostcard();
                    setTimeout(function(){
                        $("#info_postcard").css({
                            "background":"url('"+url+"')",
                            "background-size":"cover"
                        });
                    },1500);
                }
            } else if(result.code == -1) {
                alert("操作失败！");
            }
        },
        error : function(e,data){
            //失败执行
            console.log(e);
            alert("无权操作");
        },
        always:function(e,data){
            //总是执行
        }
    });
}
/**
 * 明信片修改
 */
function changePostcard(){
    $("#info_postcard").css({
        "-webkit-transform":"rotateX(90deg)",
        "-webkit-transition":"-webkit-transform 1s"
    });
    setTimeout(function (){
        $("#info_postcard").css({
            "-webkit-transform":"none"
        });
    },1500);
}