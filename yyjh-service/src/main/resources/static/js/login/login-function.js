/**
 * 面板切换
 */
function loginSwitch(){
	let li = $("#navbar_content").find("ul:eq(0)").find("li");
	$(li).click(function(){
		let content = $(this).find("a:eq(0)").text();
		//登陆注册面板切换
		if(content == "登陆" && $("#login").css("display") == "none"){
			//按钮文字切换
			$("#loginbtn").find("span:eq(0)").text(content);
			//判断当前显示的是哪个页面
			let panel_show = getPanelShow();
			//面板切换
			changePanel(panel_show,"#login");
			//初始化表格
			$("#login_form").find("input[type='reset']").click();
		}
		else if(content == "注册" && $("#registe").css("display") == "none"){
			//按钮文字切换
			$("#loginbtn").find("span:eq(0)").text(content);
			//判断当前显示的是哪个页面
			let panel_show = getPanelShow();
			//面板切换
			changePanel(panel_show,"#registe");
			//初始化表格
			$("#registe_form").find("input[type='reset']").click();
		}
		else if(content == "找回密码" && $("#getPwd").css("display") == "none"){
			//按钮文字切换
			$("#loginbtn").find("span:eq(0)").text(content);
			//判断当前显示的是哪个页面
			let panel_show = getPanelShow();
			//面板切换
			changePanel(panel_show,"#getPwd");
            //初始化找回密码界面数据
            init_getPwd();
		}
		else if(content == "修改密码" && $("#changePwd").css("display") == "none"){
			//按钮文字切换
			$("#loginbtn").find("span:eq(0)").text(content);
			//判断当前显示的是哪个页面
			let panel_show = getPanelShow();
			//面板切换
			changePanel(panel_show,"#changePwd");
            //初始化表格
            $("#changePwd_form").find("input[type='reset']").click();
		}
	})
}
/**
 * 生成验证码
 */
var new_code = "";   //生成登陆的验证码
var r_new_code = ""; //生成注册的验证码
var c_new_code = ""; //生成修改密码的验证码
function getCode(obj,rl){
	//rl判断是登陆还是注册
	let rand = Math.random()*5;
	let plus_minus = 1;
	let canvas = document.getElementById(obj);
	let ctx    = canvas.getContext("2d");
	//清空画布
	ctx.clearRect(0,0,canvas.width,canvas.height);
	//清空验证码
	if(rl == "login")
		new_code = "";
	else if(rl == "registe")
		r_new_code = "";
	//页面显示随机验证码
	for(let i=0;i<4;i++){
		let word = getRandomWord();
		if(rl == "login")
			new_code += word;
		else if(rl == "registe")
			r_new_code += word;
		plus_minus *= -1;
		let value  = rand * plus_minus;
		ctx.font   	  = (100+value)+"px Calibri";
		ctx.fillStyle = "rgb("+(value*20)+","+(value*20)+","+(value*20)+")";
		ctx.rotate(value*Math.PI/180);
		ctx.fillText(word,10+i*75,120);
	}
	// $("#l_user").val("admin");
	// $("#l_pwd").val("admin");
	// $("#mycode").val(new_code);
}
/**
 * 判断验证码是否正确
 */
function checkCode(div_this,container){
	let inputs = $(container).find("input");
	let value = $(div_this).val();
	//添加结尾正确与否标记
	let obj = $(div_this).next().children("span");
	//判断是登陆还是注册
	let code = "";
	if(container == "#login_form")
		code = new_code;
	else
		code = r_new_code;
	//判断验证码是否正确
	if(value.length<4){
		obj.prop("class","glyphicon glyphicon-minus");
		obj.css("color","#555");
	}
	else if(value.toLowerCase() == code.toLowerCase()){
		obj.css("color","green");
		obj.attr("class","glyphicon glyphicon-ok mine-badge");
	}
	else{
		obj.css("color","red");
		obj.attr("class","glyphicon glyphicon-remove mine-badge");
	}
}
/**
 * 登陆form
 */
function login(){
	let inputs = $("#login_form").find("input");
	let user = $(inputs).eq(0).val();
	let pwd  = $(inputs).eq(1).val();
	let code = $(inputs).eq(2).val();
	if(user == "" || pwd == "" || code == ""){
		alert("信息不能为空！");
        return false;
	}
	//验证码相同
	if(code.toLowerCase() == new_code.toLowerCase()){
		//密码加密
		/**
		 * 1.密码最后加wts
		 * 2.用md5加密两次
		 */
        let new_pwd = $.md5($.md5(pwd+"wjs"));
        $(inputs).eq(1).val(new_pwd);
        return true;
	}
	else{
		alert("验证码有误！\n请重新输入");
		return false;
	}
}
/**
 * 修改密码form
 */
function changePwd(){
	let inputs    = $("#changePwd_form").find("input");
	let new_pwd   = $("#c_newpwd").val();
	let renew_pwd = $("#c_renewpwd").val();
	let code	  = $("#c_code").val();
	let email	  = $("#c_email").val();
	let flag      = checkEmailFormat(email);
	//判断用户信息是否存在空值
	for(let i=0;i<inputs.length;i++){
		if(inputs.eq(i).val() == ""){
			alert("请填写完整用户信息！");
			inputs.eq(i).focus();
			return false;
		}
	}
	//判断新密码是否六位以上
	if((new_pwd.length < 6) || (new_pwd.length>20)){
		alert("密码至少6位以上，20位一下！\n请重新输入密码！");
		$("#c_newpwd").focus();
		return false;
	}
	//判断新密码与确认密码是否相同
	if(new_pwd != renew_pwd){
		alert("两次密码输入不一致！\n请重新输入！");
		$("#c_renewpwd").focus();
		return false;
	}
	//判断邮件格式
	if(!flag){
		alert("邮箱格式有误！\n请重新输入！");
		$("#c_email").focus();
		return false;
	}
	//判断验证码是否为四位数
	if(code.length != 4){
		alert("验证码至少四位数！\n请重新输入！");
		$("#c_code").focus();
		return false;
	}
	//给表单隐藏文本赋值（验证码）
	//验证码
	// 后缀+wjs
	// md5双重加密
	let mycode = c_new_code.toLowerCase()+"wjs";
	mycode = $.md5($.md5(mycode));
	$("#c_recode").val(mycode);
	return true;
}
/**
 * 修改密码
 * 发送邮件按钮
 */
var changePwd_timer = 0;    //发送邮件按钮的setInterval
var changePwd_init_t = 30;  //发送邮件按钮的初始值
var changePwd_t = 30;       //发送邮件按钮的计时器
function chengePwd_sendEmail(){
    let loginId = $("#c_loginid").val();
    let email   = $("#c_email").val();
    let flag    = checkEmailFormat(email);
    if(loginId == ""){
        alert("请输入您的账号！");
        $("#c_loginid").focus();
        return;
    }
    if(email == ""){
        alert("请输入您的邮箱！");
        $("#c_email").focus();
        return;
    }
    if(flag){
        //邮件格式正确
        //发送邮件失效
        $("#changePwd_sendEmail").unbind("click");
        $("#changePwd_sendEmail").css("background","grey");
        //生成验证码
        c_new_code = "";
        for(let i=0;i<4;i++){
            c_new_code += getRandomWord();
        }
        //修改按钮名
        changePwd_timer = setInterval(function() {
            if(changePwd_t >=0){
                $("#changePwd_sendEmail").text("还剩" + changePwd_t + "s");
                changePwd_t--;
            }
            else{
                clearInterval(changePwd_timer);
                $("#changePwd_sendEmail").text("发送邮件");
                changePwd_t=changePwd_init_t;
                //发送邮件按钮生效
                $("#changePwd_sendEmail").bind("click",chengePwd_sendEmail);
                $("#changePwd_sendEmail").css("background","#eee");
            }
        },1000);
        $.ajax({
            url:"/login/changePwd_getCode",
            type:"get",
            data:{
                "loginId" : loginId,
                "email"   : email,
                "code"    : c_new_code,
            },
            success:function(data){
                console.log(data);
                if(data.code == 0){
                    //邮件发送成功！
                    alert("邮件发送成功！");
                    //聚焦文本
                    $("#c_code").focus();
                }
                else{
                    //邮件发送失败！
                    alert("邮件发送失败！\n"+data.payload);
                    //聚焦文本
                    $("#c_email").focus();
                }
            },
            error:function(e){
                alert("出错："+e);
            }
        });
    }
    else{
        //邮件格式有误
        alert("邮件格式有误！\n请重新输入");
        //聚焦文本框
        $("#c_email").focus();
    }
}