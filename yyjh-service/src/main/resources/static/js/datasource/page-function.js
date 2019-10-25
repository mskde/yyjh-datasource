/**
 * 页面
 */
var login_user = ""; 	 //可能是loginid,email,tel
var owninfo = {			 //个人信息
	// "id" : "",
	// "loginid" :"",
	// "nickname":"",
	// "userimg":"",
	// "email":"",
	// "tel":"",
	// "create_time":"",
	// "update_time":"",
	// "remark":"",
	// "state":"",
	// "roles":["超级管理员"],
};
{
    //获取url中的user值
    let url = window.location.search;
    login_user = url.substr(url.indexOf("user") + "user".length + 1);
}
$(document).ready(function(){
	//从后台获取用户个人信息
	getOwnInfo(login_user);
	/**
	 * 网页头部下拉框事件
	 */
	$("#mine-headbtns").find("li").click(function(){
		let index = $(this).index();
		let panels = $("#panels_c").children("div");
		panels.css("display","none");
        panels.eq(index).css("display","block");
        //添加按钮点击样式
        let buts = $("#mine-content-btns").find("button");
		buts.css({
			"border"      : "5px solid #CCFFFF",
			"background" : "#CCCCFF"
		});
		buts.eq(index).css({
			"border"     : "3px solid #FFCC99",
			"background" : "#99CCCC"
		});
		//注销按钮
		if($(this).text() == "注销"){
			//注销
			logout();
		}
		//权限按钮
		else if($(this).text() == "权限管理"){
			//表格右侧操作初始化
            $("#ar_container1").css("display","block");
            $(".ar_container21").css("display","none");
		}
		//个人信息按钮
		else if($(this).text() == "个人信息"){
            //赋值
			if(JSON.stringify(owninfo) != "{}"){
                $("#info_id").text(owninfo.id);
                $("#info_loginid").text(owninfo.loginid);
                $("#i_p_loginid").text(owninfo.loginid);
                if(owninfo.userimg != null && owninfo.userimg != "") {
                    $("#info_userimg").attr("src", owninfo.userimg);
                    $("#info_postcard").css({
                        "background":"url('"+owninfo.userimg+"')",
                        "background-size":"cover"
                    })
                }
                $("#info_email").val(owninfo.email);
                $("#i_p_email").text(owninfo.email);
                $("#info_tel").val(owninfo.tel);
                $("#i_p_tel").text(owninfo.tel);
                $("#info_nickname").val(owninfo.nickname);
                $("#i_p_nickname").text(owninfo.nickname);
                if(owninfo.roles != null && owninfo.roles.length>0){
                    $("#info_roles").text(owninfo.roles);
                    $("#i_p_roles").text(owninfo.roles);
                }
                if(owninfo.remark != "" && owninfo.remark != null){
                    $("#info_remark").css("display","inline-block");
                    $("#info_remark").children("span").text(owninfo.remark);
                }
                else{
                    $("#info_remark").css("display","none");
                }
            }
		}
	})
	//页面初始化
	$("#mine-headbtns").find("li:eq(0)").click();
	/**
	 * 网页左侧按钮组*****
	 */
	$("#mine-content-btns").find("button").click(function(){
		let index = $(this).index();
		$("#mine-headbtns").find("li").eq(index).click();
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
 * 注销
 */
function logout(){
    if(!confirm("确认注销账号？"))
        return;
    //安全退出
    $.ajax({
        url  : "/datasource/datasource_logout",
        type : 'post',
        data : {
            "user":login_user
        },
        async:false,
        success:function(data){
            console.log(data);
            let result = data.code == 0 ? true : false;
            if(result){
                //注销成功
                alert("注销成功");
                window.location.href = "login";
                $.ajax({
                    url  : "/datasource/logout",
                    type : "post",
                    async:false,
                    success: function(data){

                    },
                    error: function(e){
                        console.log(e);
                        alert("无权操作");
                    }
                })
            }
            else{
                //注销失败
                let error_msg = "";
                if(data.hasOwnProperty("payload") && data.payload != null)
                    alert("注销失败！"+data.payload+"\n即将退出！");
                else
                    alert("注销失败！账号存在异常！\n即将退出！");
                window.location.href = "login";
            }
        },
        error:function(e){
            console.log(e);
            alert("无权操作");
        }
    })
}