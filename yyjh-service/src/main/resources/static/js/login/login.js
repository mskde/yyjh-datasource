var getPwd_step = 0; 										//找回密码的步骤
var getPwd_stepName = ["您的账号：","您的邮箱：","您的密码："];	//标签名
var getPwd_datas = {										//用户的账号，密码
    "loginId" : "",
    "email"   : ""
};
$(document).ready(function(){
    /**
     * *****************************
     * *****************************
     * *****************************
     * 全局事件*********************
     * *****************************
     * *****************************
     * *****************************
     */
    //聚焦文本框
    $("#l_user").focus();
    //登陆注册切换
    loginSwitch();
    //加载页面生成验证码
    getCode("code","login");
    getCode("r_code","registe");
    /**
     * *****************************
     * *****************************
     * *****************************
     * 登陆*************************
     * *****************************
     * *****************************
     * *****************************
     */
    //登陆表格回调方法
    $('#login_form').ajaxForm(function (data) {
        let flag = data.code == 0 ? true : false;
        let user = $("#l_user").val();
        //刷新验证码
        getCode("code","login");
        //重置表格
        $("#login_form").find("input[type='reset']").click();
        if(flag){
            //登陆成功
            window.location.href = "toDatasource?user="+user;
        }
        else{
            //登陆失败
            if(data.hasOwnProperty("payload") && data.payload != null){
                alert(data.payload);
                if(data.payload == "账号已登陆！\n即将跳转！") {
                    setTimeout(function () {
                        //页面跳转
                        window.location.href = "toDatasource?user="+user;
                    }, 1000);
                }
            }
            else{
                alert("登陆失败！");
            }
            $("#login_form").find("input[type='text']:eq(0)").focus();
        }
    });
    //找回密码标签
	$("#getPwd_a").click(function(){
        gotoGetPwd();
	})
	//修改密码标签
	$("#changePwd_a").click(function(){
        gotoChangePwd();
	})
    //验证码点击切换
    $("#code").click(function(){
        getCode("code","login");
    })
    //重置
    $("#login_form").find("input[type='reset']").on("click",function(){
        $("#mycode").next().children("span").prop("class","glyphicon glyphicon-minus");
        $("#mycode").next().children("span").css("color","#555");
        //生成新验证码
        getCode("code","login");
        //聚焦文本框
        $("#l_user").focus();
    })
    /**
     * *****************************
     * *****************************
     * *****************************
     * 找回密码**********************
     * *****************************
     * *****************************
     * *****************************
     */
    //文本框键盘事件
	$("#g_value").keyup(function(e) {
        e.preventDefault();
        if (e.keyCode === 13) {
            $("#getPwd_next").click();
        }
    })
    //上一步按钮
    $("#getPwd_prev").click(function(){
        if(getPwd_step == 0){
            return ;
        }
        getPwd_step--;
        //修改标签名
        $("#g_label").text(getPwd_stepName[getPwd_step]);
        //修改按钮名
        getPwd_setMenuName(getPwd_step);
        //赋值
        getPwd_datas.email   = "";
        //清空文本框值
        $("#g_value").val("");
        //聚焦文本框
        $("#g_value").focus();
        //container切换
        $("#getPwd_container2").css("display","none");
        $("#getPwd_container1").css("display","table");
    })
    //下一步按钮
    $("#getPwd_next").click(function(){
        let data = $("#g_value").val();
        switch(getPwd_step){
			case 0 :
				//输入你的账号
				getPwd_step++;
				if($("#g_value").val() == ""){
					alert("请输入用户信息！");
					$("#g_value").focus();
					return;
				}
				//赋值
				getPwd_datas.loginId = data;
                //清空文本框值
                $("#g_value").val("");
				//修改标签名
				$("#g_label").text(getPwd_stepName[getPwd_step]);
				//修改按钮名
				getPwd_setMenuName(getPwd_step);
				//聚焦文本框
				$("#g_value").focus();
				break;
			case 1 :
				//发送邮件
				if($("#g_value").val() == ""){
					alert("请输入用户信息！");
					$("#g_value").focus();
					return;
				}
				alert("请求已提交！请耐心等待！");
				//赋值
                getPwd_datas.email   = data;
				//按钮组失效
				$("#getPwd_prev").prop("disabled","disabled");
				$("#getPwd_next").prop("disabled","disabled");
				//ajax
				$.ajax({
					url : "/login/getPwd",
					type: "post",
					data: getPwd_datas,
					success:function(data){
						console.log(data);
                        //按钮组显示
                        $("#getPwd_prev").removeAttr("disabled");
                        $("#getPwd_next").removeAttr("disabled");
                        if(data.code == 0){
                        	//邮件发送成功！
							alert("邮件发送成功！");
							//container切换
							$("#getPwd_container1").css("display","none");
                            $("#getPwd_container2").css("display","block");
                            if(getPwd_step < 2)
                                getPwd_step++;
                            //修改按钮名
                            getPwd_setMenuName(getPwd_step);
                        }
						else{
							//邮件发送失败
							alert("邮件发送失败！\n"+data.payload);
							if(data.payload == "用户账号不存在！") {
                                $("#getPwd_prev").click();
                            }
                            //赋值
                            getPwd_datas.email = "";
                            //清空文本框值
                            $("#g_value").val("");
						}
					},
					error:function(e){
						alert("出错："+e);
					}
				})
				break;
			case 2 :
				//返回登陆界面
                gotoLogin();
				//初始化找回密码界面数据
				init_getPwd();
        }
    })
    $("#getPwd_next").keyup(function(e){
    	e.preventDefault();
	})
	/**
     * *****************************
     * *****************************
     * *****************************
     * 修改密码**********************
     * *****************************
     * *****************************
     * *****************************
     */
	//表格回调函数
    $('#changePwd_form').ajaxForm(function (data){
      console.log(data);
      if(data.hasOwnProperty("code") && (data.code == 0)){
          //验证通过，修改密码成功！
          alert("修改密码成功！");
          //初始化表格
          $("#changePwd_form").find("input[type='reset']").click();
          //跳转到登陆界面
            gotoLogin();
      }
      else{
          //验证失败，提示信息
          alert("修改密码失败！\n"+data.payload);
      }
    })
    //账号
    $("#c_loginid").change(function(){
    	let obj = $(this).next();
    	if($(this).val() == "")
            showInitAddon(obj);
        else
        	showSuccessAddon(obj)
    })
    //原始密码
    $("#c_oldpwd").change(function(){
    	let obj = $(this).next();
    	if($(this).val() == "")
            showInitAddon(obj);
        else
        	showSuccessAddon(obj)
    })
    //新密码
    $("#c_newpwd").change(function(){
        let pwd = $(this).val();
    	let obj = $(this).next();
    	if(pwd == "")
            showInitAddon(obj);
        else if(pwd.length >= 6 && pwd.length <= 20){
            showSuccessAddon(obj);;
        }
        else
            showFailAddon(obj);

    })
     //确认密码
    $("#c_renewpwd").blur(function(){
    	let value = $(this).val();
    	let newpwd = $("#c_newpwd").val();
    	let obj = $(this).next();
    	if(value == "")
            showInitAddon(obj);
        else if(value != newpwd)
        	showFailAddon(obj);
        else
        	showSuccessAddon(obj)
    })
    //发送邮件
    $("#changePwd_sendEmail").click(chengePwd_sendEmail);
    //重置
    $("#changePwd_form").find("input[type='reset']").on("click",function(){
        let addons = $("#changePwd_form").find("span[flag='1']");
        showInitAddon(addons);
        //初始化修改密码界面数据
        c_new_code = "";
        //聚焦文本框
        $("#c_loginid").focus();
    })
})