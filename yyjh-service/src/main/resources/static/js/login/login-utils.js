/**
 * 生成随机数
 */
function getRandomWord(){
	let WORD = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
	let RAND = Math.floor(Math.random()*WORD.length);
	let GETWORD = WORD.charAt(RAND); 
	return GETWORD;
}
/**
 * 登陆注册面盘切换
 * panel1切换到panel2
 */
function changePanel(panel1,panel2){
	//panel1隐藏
	panelFadeout(panel1);
	//panel2显示
	panelFadein(panel2);
}
//获取当前显示的面板是哪个
function getPanelShow(){
	let panel_show = "";
	if($("#registe").css("display") != "none")
		panel_show = "#registe";
	else if($("#getPwd").css("display") != "none")
		panel_show = "#getPwd";
	else if($("#login").css("display") != "none")
		panel_show = "#login";
	else if($("#changePwd").css("display") != "none")
		panel_show = "#changePwd";
	return panel_show;
}
//面板隐藏
function panelFadeout(panel){
	$(panel).css({
		"animation":"fadeout 0.6s infinite",
		"animation-iteration-count":"1",
		"animation-fill-mode":"forwards"
	});
	setTimeout(function(){
		$(panel).css("display","none");
	},600);
}
//面板显示
function panelFadein(panel){
	$(panel).css({
		"display":"block",
		"transform":"translate(0,-100px)",
		"transition":"transform 0s"
	});
	$(panel).css({
		"animation":"fadein 0.6s infinite",
		"animation-iteration-count":"1",
		"animation-fill-mode":"forwards"
	});
	setTimeout(function(){
		$(panel).css({
			"transform":"translate(0,0)",
		});
	},600);
}


var addon_success = "input-group-addon glyphicon glyphicon-ok";
var addon_init    = "input-group-addon glyphicon glyphicon-minus";
var addon_fail    = "input-group-addon glyphicon glyphicon-remove";
/**
 * 显示正确标签
 */
function showSuccessAddon(obj){
	obj.prop("class",addon_success);
	obj.css("color","green");
}
/**
 * 显示错误标签
 */
function showFailAddon(obj){
	obj.prop("class",addon_fail);
	obj.css("color","red");
}
/**
 * 显示初始标签
 */
function showInitAddon(obj){
	obj.prop("class",addon_init);
	obj.css("color","#555");
}

/**
 * 找回密码
 * 修改按钮名
 */
function getPwd_setMenuName(step){
	switch(step){
		case 0 : $("#getPwd_next").val("下一页");   break;
		case 1 : $("#getPwd_next").val("发送邮件"); break;
		case 2 : $("#getPwd_next").val("返回登陆"); break;
	}
}
/**
 * 找回密码
 * 初始化
 */
function init_getPwd(){
    $("#g_label").text("您的账号：");
    $("#g_value").val("");
    $("#getPwd_next").val("下一步");
    //container切换
    $("#getPwd_container2").css("display","none");
    $("#getPwd_container1").css("display","table");
    getPwd_step = 0;
    getPwd_datas = {
        "loginId" : "",
        "email"   : ""
    }
    //聚焦文本框
    $("#g_value").focus();
}
/**
 * 跳转到登陆界面
 */
function gotoLogin(){
    let li = $("#navbar_content").find("ul:eq(0)").find("li:eq(0)");
    li.click();
}
/**
 * 跳转到注册界面
 */
function gotoRegiste(){
    let li = $("#navbar_content").find("ul:eq(0)").find("li:eq(1)");
    li.click();
}
/**
 * 跳转到忘记密码界面
 */
function gotoGetPwd(){
    let li = $("#navbar_content").find("ul:eq(0)").find("li:eq(2)");
    li.click();
}
/**
 * 跳转到修改密码 界面
 */
function gotoChangePwd(){
    let li = $("#navbar_content").find("ul:eq(0)").find("li:eq(3)");
    li.click();
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
 * file上传文件获取url问题
 * 转换
 */
function getObjectURL(file) {  
     let url = null;
     if (window.createObjcectURL != undefined) {  
         url = window.createOjcectURL(file);  
     } else if (window.URL != undefined) {  
         url = window.URL.createObjectURL(file);  
     } else if (window.webkitURL != undefined) {  
         url = window.webkitURL.createObjectURL(file);  
     }  
     return url;  
 }