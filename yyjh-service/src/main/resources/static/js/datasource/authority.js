/**
 * 权限管理
 */
var dataBaseTable  = ["t_user","t_role","t_permission","t_user_role","t_role_permission"]; //表工具栏中删除按钮value属性的取值(表名)
var canChangeRoles = ["超级管理员","管理员"]; //能够修改用户角色权限的人
var canDeleteUser  = ["超级管理员"]; //能够删除用户账号的人
var canDeleteRole  = ["超级管理员"]; //能够删除角色的人
var canDeletePerm  = ["超级管理员"]; //能够删除权限的人
$(document).ready(function(){
    //用户信息表按钮
    $("#arc1_getUserTableBtn").click(function(){
        let url = "/datasource/getAuthorityUserDatas";
        //初始化表格
        authority_init_table(dataBaseTable[0],url);
        //切换右侧container
        // changeAuthorityContainer(0);
        //修改表工具栏中删除按钮value属性
        $("#authority_deleteAll").attr("value",dataBaseTable[0]);
        $("#authority_btns").css("display","block");
    })
    //角色信息表按钮
    $("#arc1_getRoleTableBtn").click(function(){
        let url = "/datasource/getAuthorityRoleDatas";
        //初始化表格
        authority_init_table(dataBaseTable[1],url);
        //切换右侧container
        // changeAuthorityContainer(1);
        //修改表工具栏中删除按钮value属性
        // $("#authority_deleteAll").attr("value",dataBaseTable[1]);
        $("#authority_btns").css("display","none");
    })
    //权限信息表按钮
    $("#arc1_getPermTableBtn").click(function(){
        let url = "/datasource/getAuthorityPermDatas";
        //初始化表格
        authority_init_table(dataBaseTable[2],url);
        //切换右侧container
        // changeAuthorityContainer(2);
        //修改表工具栏中删除按钮value属性
        // $("#authority_deleteAll").attr("value",dataBaseTable[2]);
        $("#authority_btns").css("display","none");
    })
    /**
     * 表格工具栏中
     */
    //删除按钮
    $("#authority_deleteAll").click(function(){
        authorityDeleteBtnClick(this);
    })
    $("#ar_deleteAll").click(function() {
        authorityDeleteBtnClick(this);
    })
    //添加按钮
    $("#authority_insertOne").click(function(){
        $("#authorityAddUser_modal").modal("show");
    })
    $("#ar_insertOne").change(function(){
        let permission = $(this).find("option:selected").text();
        let role_id = $(this).attr("role_id");
        $.ajax({
            url:"/datasource/authorityAddRolePermission",
            type:"post",
            data:{
                "permission":permission,
                "role_id":role_id
            },
            success:(data)=>{
                console.log(data);
                if(data.code == 0){
                    //添加成功！
                    // alert("添加成功！");
                    //刷新表格数据
                    $(this).parents(".bootstrap-table").find("table").bootstrapTable("refresh");
                }
                else{
                    if(data.hasOwnProperty("payload") && data.payload != null){
                        if(data.payload == "映射已存在！"){

                        }
                        else{
                            alert("添加失败！");
                        }
                    }
                }
            }
        })
    })
    //回退按钮
    $(".arc2_refresh_btn").click(function(){
        //表格初始化
        $("#authority_table").bootstrapTable("removeAll");
        //表格右侧操作栏隐藏
        $("#ar_container1").css("display","block");
        $(".ar_container2").css("display","none");
    })
    //全选按钮
    $("#a_m_selectAll").click(function(){
        $("#authority_madal_form").find("input[type='radio'][flag='1']").prop("checked","checked");
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
 * 初始化表格
 */
//用户，角色，权限
function authority_init_table(obj,url){
    //获取表的表列名
    let ths = getAuthorityTableThs(obj);
    //清空表格
    $("#authority_table").bootstrapTable("destroy");
    //加载表格
    $('#authority_table').bootstrapTable({
        url:url,
        method:"post",
        pageNumber:1,//初始化加载第一页，默认第一页
        pageSize:10,//单页记录数
        contentType:"application/x-www-form-urlencoded; charset=UTF-8",
        queryParamsType:'',
        //前台向后台传参
        queryParams : function (params) {
            return{
                pageSize: params.pageSize,
                pageNumber: params.pageNumber,
                text:params.searchText
            }
        },
        sidePagination:"server",//分页方式：client客户端分页
        pageList:[10,20,30,40],//分页步进值
        striped: true, //是否显示行间隔色
//	    minHeight:"300",  //表格高度
        cache: false,//是否使用缓存，默认true，所以一般情况下需要
        toolbar:"#authority_btns",
        pagination: true,//是否显示分页
        sortable:false,//是否启用排序
        search:true,//是否显示表格搜索，
        // strictSearch:false, //是否启用严格搜索
        // searchOnEnterKey:true, //按回车搜索
        showColumns:true,//是否显示所有 的列
        showRefresh:true,//是否显示刷新按钮
        minimumCountColumns:2,//最少允许的列数
        clickToSelect:true,//是否启用点击选中行
        showToggle:false,//是否显示详细视图和列表视图的切换按钮
        cardView:false,//是否显示详细视图
        detailView:false,
        formatNoMatches:function(){
            //空数据时：提示文本
            return "尚未搜索到相关用户数据";
        },
        onClickRow: function (row) {
            // console.log(row)
        },
        columns:ths
    });
}
//角色用户，角色权限
function ar_init_table(table,url,role_id){
    //获取表的表列名
    let ths = [];
    switch (table) {
        case dataBaseTable[3]:
            //角色用户表
            ths = getAuthorityTableThs(dataBaseTable[3]);break;
        case dataBaseTable[4]:
            //角色权限表
            ths = getAuthorityTableThs(dataBaseTable[4]);break;
    }
    //清空表格
    $("#ar_table").bootstrapTable("destroy");
    //加载表格
    $('#ar_table').bootstrapTable({
        url:url,
        method:"post",
        contentType:"application/x-www-form-urlencoded; charset=UTF-8",
        queryParamsType:'',
        //前台向后台传参
        queryParams : function (params) {
            return{
                pageSize: params.pageSize,
                pageNumber: params.pageNumber,
                role_id:role_id,
                text:params.searchText
            }
        },
        sidePagination:"server",//分页方式：client客户端分页
        striped: true, //是否显示行间隔色
//	    minHeight:"300",  //表格高度
        cache: false,//是否使用缓存，默认true，所以一般情况下需要
        toolbar:"#ar_btns",
        pagination: true,//是否显示分页
        sortable:false,//是否启用排序
        pageNumber:1,//初始化加载第一页，默认第一页
        pageSize:5,//单页记录数
        pageList:[5,10,15,20],//分页步进值
        search:true,//是否显示表格搜索，此搜索是客户端搜索
        strictSearch:false, //是否启用严格搜索
        showColumns:true,//是否显示所有 的列
        showRefresh:true,//是否显示刷新按钮
        minimumCountColumns:2,//最少允许的列数
        clickToSelect:true,//是否启用点击选中行
        showToggle:false,//是否显示详细视图和列表视图的切换按钮
        cardView:false,//是否显示详细视图
        detailView:false,
        formatNoMatches:function(){
            //空数据时：提示文本
            return "未搜索到相关信息";
        },
        columns:ths
    });
}
/**
 * 删除
 */
var globalRole_id = 0;
//用户信息表 删除按钮
function deleteOne(mythis,id,url){
    //判断权限
    let flag = isUserHasAuthority(canDeleteUser);
    if(!flag){
        return;
    }
    if(confirm("确定删除该条数据？")) {
        $.ajax({
            url: url,
            type: "post",
            data: {
                "id": id
            },
            success:(data)=> {
                console.log(data);
                if (data.code == 0) {
                    alert("删除成功！");
                    //刷新表数据
                    console.log( $(mythis).text());
                    console.log( $(mythis).parents("table"));
                    $(mythis).parents("table").bootstrapTable("refresh");
                }
                else {
                    alert("删除失败！");
                }
            },
            error: function (e,data) {
                console.log(e);
                alert("无权操作");
            }
        })
    }
}
//角色用户表 删除按钮
function deleteRoleUserOne(user_id){
    let role_id = globalRole_id;
    console.log(role_id);
    console.log(user_id);
    if(confirm("确定删除该条数据？")) {
        $.ajax({
            url: "/datasource/deleteAuthorityRoleUser",
            type: "post",
            data: {
                "role_id": role_id,
                "user_id": user_id
            },
            success: function (data) {
                console.log(data);
                if (data.code == 0) {
                    alert("删除成功！");
                    //刷新表数据
                    $("#ar_table").bootstrapTable("refresh");
                }
                else {
                    alert("删除失败！");
                }
            },
            error: function (e,data) {
                console.log(e);
                alert("无权操作");
            }
        })
    }
}
//角色权限表 删除按钮
function deleteRolePermOne(permission_id){
    let role_id = globalRole_id;
    if(confirm("确定删除该条数据？")) {
        $.ajax({
            url: "/datasource/deleteAuthorityRolePerm",
            type: "post",
            data: {
                "role_id": role_id,
                "permission_id": permission_id
            },
            success: function (data) {
                console.log(data);
                if (data.code == 0) {
                    alert("删除成功！");
                    //刷新表数据
                    $("#ar_table").bootstrapTable("refresh");
                }
                else {
                    alert("删除失败！");
                }
            },
            error: function (e) {
                console.log(e);
                alert("无权操作");
            }
        })
    }
}
//多删按钮
function authorityDeleteBtnClick(mythis){
    let table = $(mythis).attr("value");
    let role_id = $(mythis).attr("role_id");
    let htmlTableId = $(mythis).attr("table");
    let objs = $(htmlTableId).find("input[name='a_cb']");
    let values  = [];
    for(let i=0;i<objs.length;i++){
        let obj = objs.eq(i);
        if(obj.prop("checked"))
            values.push(obj.attr("value"));
    }
    let url = "";
    switch(table){
        case dataBaseTable[0]:url="/datasource/deleteTUserSome";break;
        case dataBaseTable[1]:url="/datasource/deleteTRoleSome";break;
        case dataBaseTable[2]:url="/datasource/deleteTPermSome";break;
        case dataBaseTable[3]:url="/datasource/deleteTUserRoleSome";break;
        case dataBaseTable[4]:url="/datasource/deleteTRolePermSome";break;
    }
    //判断用户是否选择
    if(values.length>0){
        if(!confirm("确定删除这"+values.length+"条数据？"))
            return;
        $.ajax({
            url: url,
            type:"post",
            traditional:"true",
            data:{
                "table" : table,
                "ids"   : values,
                "role_id":role_id != null ? role_id : 0 //角色用户表和角色权限表中使用
            },
            success:(data)=>{
                console.log(data);
                if(data.code == 0){
                    //删除成功
                    alert("删除成功！");
                    //重新加载数据
                    $(mythis).parents(".bootstrap-table").find("table").bootstrapTable("refresh");
                }
                else{
                    //删除失败
                    if(data.hasOwnProperty("payload") && data.payload != null)
                        alert("删除失败！\n"+data.payload);
                    else
                        alert("删除失败！");
                }
            },
            error:function(e){
                console.log(e);
                alert("无权操作");
            }
        })
    }
    else{
        alert("请选择操作对象！");
    }
}
/**
 * 状态切换按钮
 */
//用户信息表
function bstableStateBtnClick(mythis){
    let id = $(mythis).attr("oid");
    let state = $(mythis).attr("state");
    let table = $(mythis).attr("table");
    let url = "";
    switch(table){
        case dataBaseTable[0]:
            url = "/datasource/changeTUserState";
            break;
        case dataBaseTable[1]:
            url = "/datasource/changeTRoleState";
            break;
        case dataBaseTable[2]:
            url = "/datasource/changeTPermState";
            break;
    }
    $.ajax({
        url: url,
        type: "post",
        data: {
            "id"    : id,
            "state" : state
        },
        success: (data)=> {
            console.log(data);
            if (data.code == 0) {
                alert("修改成功！");
                //按钮样式切换
                let btns = $(mythis).parent().children("button");
                btns.attr("class","btn btn-default");
                $(mythis).attr("class","btn btn-primary");
            }
            else {
                alert("修改失败！");
            }
        },
        error: function (e) {
            console.log(e);
            alert("无权操作");
        }
    })
}
/**
 * 复选下拉框
 */
//角色栏点击事件
function bstableRolesClick(mythis){
    //根据角色勾选复选框
    let roles = $(mythis).parent().find("span[name='roles']").text().split(',');
    let checkbox  = $(mythis).parent().find("input[type='checkbox']");
    //取消所有checkbox的选中
    checkbox.prop("checked","");
    //勾选
    for(let i=0;i<roles.length;i++){
        for(let j=0;j<checkbox.length;j++){
            if(roles[i] == checkbox.eq(j).attr("name"))
                checkbox.eq(j).prop("checked","checked");
        }
    }
}
//角色栏中完成按钮
function bstableRolesCompleteBtnClick(mythis,url){
    //判断账号是否授权
    let flag = isUserHasAuthority(canChangeRoles);
    if(flag){
        //有权执行
        let lis = $(mythis).parent().parent().find("input[type='checkbox']");
        let rolesname = [];
        let roles     = [];
        let id   = $(mythis).attr("value");
        for(let i=0;i<lis.length;i++){
            let li = lis.eq(i);
            if(li.prop("checked")){
                rolesname.push(li.attr("name"));
                roles.push(parseInt(li.attr("value")));
            }
        }
        //进后台
        $.ajax({
            url: url,
            type: "post",
            dataType:"json",
            traditional:true,//防止深度序列化
            data: {
                "id": id,
                "roles"  :roles
            },
            success: (data)=> {
                console.log(data);
                if (data.code == 0) {
                    alert("设置成功！");
                    //修改按钮值
                    $(mythis).parents(".dropdown").find("span[name='roles']").text(rolesname);
                    //判断修改的是否是自己的角色
                    if(owninfo.id == id){
                        owninfo.roles = rolesname;
                    }
                }
                else {
                    if(data.hasOwnProperty("payload") && data.payload != null)
                        alert("设置失败！\n"+data.payload);
                    else
                        alert("设置失败！");
                }
            },
            error: function (e) {
                console.log(e);
                alert("无权操作");
            }
        })
    }
    else{
        //无权
        //还原修改操作
        let old_roles = $(mythis).parents(".dropdown").find("span[name='roles']").text().split(",");
        let checkbox  = $(mythis).parents("ul").find("input[type='checkbox']");
        //取消所有checkbox的选中
        checkbox.prop("checked","");
        //还原
        for(let i=0;i<old_roles.length;i++){
            for(let j=0;j<checkbox.length;j++){
                if(old_roles[i] == checkbox.eq(j).attr("name"))
                    checkbox.eq(j).prop("checked","checked");
            }
        }
    }
    //隐藏下拉框
    $(mythis).parents(".dropdown").find("button[name='dropdownBtn']").dropdown("toggle");
}
//角色下拉框阻止默认事件
function bstableRolesStopPropagation(e){
    e.stopPropagation();
}
//角色下拉框li点击选中
// function bstableRolesLiClick(mythis){
//     alert(1)
//     if($(mythis).find("input").prop("checked") == true){
//         $(mythis).find("input").removeAttr("checked");
//     }
//     else{
//         $(mythis).find("input").prop("checked","true");
//     }
// }
/**
 * 角色信息表
 * 模态框按钮
 */
//用户管理按钮
function bstableManageUserBtnClick(mythis){
    //显示模态框
    $("#authorityRole_modal").modal("show");
    //获取role_id
    let role_id = $(mythis).attr("value");
    console.log(role_id);
    //将role_id存全局变量
    globalRole_id = role_id;
    let url = "/datasource/getRoleUserDatas";
    //初始化并加载角色用户表
    ar_init_table(dataBaseTable[3],url,role_id);
    //修改表工具栏中删除按钮value属性
    $("#ar_deleteAll").attr("value",dataBaseTable[3]);
    //将role_id存入删除,添加按钮中
    $("#ar_deleteAll").attr("role_id",$(mythis).attr("value"));
    $("#ar_insertOne").attr("role_id",$(mythis).attr("value"));
    //添加按钮隐藏
    $("#ar_insertOne").css("display","none");
}
//权限管理按钮
function bstableManagePermBtnClick(mythis){
    //显示模态框
    $("#authorityRole_modal").modal("show");
    //获取role_id
    let role_id = $(mythis).attr("value");
    //将role_id存全局变量
    globalRole_id = role_id;
    let url = "/datasource/getRolePermDatas";
    //初始化并加载角色权限表
    ar_init_table(dataBaseTable[4],url,role_id);
    //修改表工具栏中删除按钮value属性
    $("#ar_deleteAll").attr("value",dataBaseTable[4]);
    //将role_id存入删除按钮中
    $("#ar_deleteAll").attr("role_id",$(mythis).attr("value"));
    $("#ar_insertOne").attr("role_id",$(mythis).attr("value"));
    //添加按钮显示
    $("#ar_insertOne").css("display","inline-block");
    //修改按钮选中
    $("#ar_insertOne").find("option[flag='1']").attr("selected","selected");
}

///////////////////////////////
///////////////////////////////
//静态方法
///////////////////////////////
///////////////////////////////
/**
 * 获取权限管理中表的表列名
 */
function getAuthorityTableThs(obj){
    //获取用户信息表
    if(obj == dataBaseTable[0]){
        return [
            {
                title:"<input type='checkbox' id='auth_selectAll' class='authority_table_checkbox' onclick='authorityDeleteAll(this)' />",
                field:"checkbox",
                visible:true,
                width:20,
                formatter:"bstableCheckbox",
                events:authorityOperateEvents
            },
            {
                title   : "头像",
                field   : "userimg",
                visible : true,
                width   : 100,
                formatter:"bstableUserimg"
            },
            {
                title   : "账号",
                field   : "loginid",
                visible : true
            },
            {
                title   : "昵称",
                field   : "nickname",
                visible : true
            },
            {
                title   : "角色",
                field   : "roles",
                visible : true,
                width   : 200,
                formatter:"bstableTRoleRoles",
                events:authorityOperateEvents
            },
            {
                title   : "是否启用",
                field   : "state",
                visible : true,
                width   :150,
                formatter:"bstableTUserState",
                events:authorityOperateEvents
            },
            {
                title   : "",
                field   : "delete",
                visible : true,
                width   : 40,
                formatter:"bstableTUserDelete"
            },
        ]
    }
    //获取角色信息表
    else if(obj == dataBaseTable[1]){
        return [
            {
                title:"<input type='checkbox' id='auth_selectAll' class='authority_table_checkbox' onclick='authorityDeleteAll(this)' />",
                field:"checkbox",
                visible:true,
                width:20,
                formatter:"bstableCheckbox",
                events:authorityOperateEvents
            },
            {
                title   : "角色",
                field   : "rolename",
                visible : true
            },
            {
                title   : "角色描述",
                field   : "roledesc",
                visible : true
            },
            {
                title   : "成员",
                field   : "members",
                visible : true,
                formatter:"bstableMembers",
                events:authorityOperateEvents
            },
            {
                title   : "权限",
                field   : "permissions",
                visible : true,
                formatter:"bstablePermissions",
                events:authorityOperateEvents
            },
            {
                title   : "是否启用",
                field   : "state",
                visible : true,
                width   :150,
                formatter:"bstableTRoleState",
                events:authorityOperateEvents
            },
        ]
    }
    //获取权限信息表
    else if(obj == dataBaseTable[2]){
        return [
            {
                title:"<input type='checkbox' id='auth_selectAll' class='authority_table_checkbox' onclick='authorityDeleteAll(this)' />",
                field:"checkbox",
                visible:true,
                width:20,
                formatter:"bstableCheckbox",
                events:authorityOperateEvents
            },
            {
                title   : "权限",
                field   : "permission",
                visible : true
            },
            {
                title   : "角色",
                field   : "roles",
                visible : true,
                formatter:"bstableTPermRoles",
                events:authorityOperateEvents
            },
            {
                title   : "是否启用",
                field   : "state",
                visible : true,
                formatter:"bstableTPermissionState",
                events:authorityOperateEvents
            },
        ]
    }
    //获取角色用户表
    else if(obj == dataBaseTable[3]){
        return [
            {
                title:"<input type='checkbox' id='auth_selectAll' class='authority_table_checkbox' onclick='authorityDeleteAll(this)' />",
                field:"checkbox",
                visible:true,
                width:20,
                formatter:"bstableCheckbox",
                events:authorityOperateEvents
            },
            {
                title   : "头像",
                field   : "userimg",
                visible : true,
                width   : 100,
                formatter:"bstableUserimg"
            },
            {
                title   : "账号",
                field   : "loginid",
                visible : true
            },
            {
                title   : "昵称",
                field   : "nickname",
                visible : true
            },
            {
                title   : "拥有角色",
                field   : "roles",
                visible : true,
                width   : 200,
                formatter:"bstableSelect",
                events  : authorityOperateEvents
            },
            {
                title   : "是否启用",
                field   : "state",
                visible : true,
                width   :150,
            },
            {
                title   : "",
                field   : "delete",
                visible : true,
                width   : 40,
                formatter:"bstableTUserRoleDelete",
                events:authorityOperateEvents
            },
        ]
    }
    //角色权限表
    else if(obj == dataBaseTable[4]){
        return    [
            {
                title:"<input type='checkbox' id='auth_selectAll' class='authority_table_checkbox' onclick='authorityDeleteAll(this)' />",
                field:"checkbox",
                visible:true,
                width:20,
                formatter:"bstableCheckbox",
                events:authorityOperateEvents
            },
            {
                title   : "权限",
                field   : "permission",
                visible : true
            },
            {
                title   : "拥有角色",
                field   : "roles",
                visible : true,
                formatter:"bstableSelect",
                events  : authorityOperateEvents
            },
            {
                title   : "是否启用",
                field   : "state",
                visible : true,
                width   :150
            },
            {
                title   : "",
                field   : "delete",
                visible : true,
                width   : 40,
                formatter:"bstableTRolePermDelete",
                events:authorityOperateEvents
            }
        ]
    }
}
/**
 * 下拉框事件
 */
function addSelectEvent(){
    $("#ar_table").find("select").mouseenter(function(){
        let count = $(this).children("option").length+1;
        $(this).prop("size",count);
    })
    $("#ar_table").find("select").mouseleave(function(){
        $(this).prop("size","1");
    })
}
/**
 * 表列名第一列
 * 多选框
 */
function authorityDeleteAll(obj){
    let checkbox = $(obj).parents("table").find("input[name='a_cb']");
    if($(obj).prop("checked"))
        checkbox.prop("checked","true");
    else
        checkbox.prop("checked","");
}
/**
 * 权限管理
 * 切换右侧container
 */
function changeAuthorityContainer(index){
    $("#ar_container1").css("display","none");
    for(let i=0;i<$(".ar_container2").length;i++){
        if(index == i)
            $(".ar_container2").eq(i).css("display","block");
        else
            $(".ar_container2").eq(i).css("display","none");
    }
}
/**
 * 判断账号权限
 */
function isUserHasAuthority(canRoles){
    let myRoles = [];
    let flag = false;
    if(owninfo == null || owninfo.roles == null){
        alert("登陆异常");
        return ;
    }
    else
        myRoles = owninfo.roles;
    for(let i=0;i<myRoles.length;i++){
        for(let j=0;j<canRoles.length;j++){
            if(myRoles[i] == canRoles[j]){
                flag = true;
                break;
            }
        }
        if(flag == true)
            break;
    }
    if(!flag){
        //无权操作
        alert("无权执行操作！");
    }
    return flag;
}

/**
 * bstable中修改内容显示格式
 */
//checkbox
function bstableCheckbox(value,row,index){
    return "<input type='checkbox' name='a_cb' value='"+row.id+"' class='authority_table_checkbox'/>";
}
//userimg
function bstableUserimg(value,row,index){
    //修改图片数据格式
    let userimg = row.userimg;
    if(userimg == null || userimg == ""){
        //默认头像
        userimg = "init_headimg.jpg";
    }
    return "<img src='../../imgs/"+userimg+"' width='50' height='50'/> ";
}
//roles
function bstableRoles(value,row,index,btnClassName){
    let data = "<div class='dropdown'>"
        + "<button class='btn btn-default dropdown-toggle a_userRoleBtn' name='dropdownBtn' type=button' data-toggle='dropdown'>"
        + "<span name='roles' class='a_userRoleSpan'>"+row.roles+"</span>"
        + "<span class='caret'></span>"
        + "</button>"
        + "<ul class='dropdown-menu bstableRolesUl'>"
        + "<li><a href='#'><input type='checkbox' name='超级管理员' value='1'/>超级管理员</a></li>"
        + "<li><a href='#'><input type='checkbox' name='管理员'     value='2'/>管理员</a></li>"
        + "<li><a href='#'><input type='checkbox' name='普通用户'   value='3'/>普通用户</a></li>"
        + "<li><a href='#'><input type='checkbox' name='日期总监'   value='4'/>日期总监</a></li>"
        + "<li class='divider'></li>"
        + "<li><button class='btn btn-primary a_userRoleUlBtn "+btnClassName+"' value='"+row.id+"'>完成</button></li>"
        + "</ul>"
        + "</div>";
    return data;
}
    //用户信息表中
function bstableTRoleRoles(value,row,index){
    let className = "bstableTRoleRoleUlBtn";
    return bstableRoles(value,row,index,className);
}
    //权限信息表中
function bstableTPermRoles(value,row,index){
    let className = "bstableTPermRoleUlBtn";
    return bstableRoles(value,row,index,className);
}
//state
function bstableState(value,row,index,table){
    let id = row.id;
    let state = row.state;
    let style1 = "",style2 = "";
    if(state == 0){
        style1 = "btn-primary";style2 = "btn-default";
    }
    else{
        style2 = "btn-primary";style1 = "btn-default";
    }
    return "<div class='btn-group' role='group'>"
        +"<button type='button' class='btn "+style1+" bstableStateBtn' oid='"+id+"' state='0' table='"+table+"'>禁用</button>"
        +"<button type='button' class='btn "+style2+" bstableStateBtn' oid='"+id+"' state='1' table='"+table+"'>启用</button>"
        +"</div>";
}
    //用户信息表state
function bstableTUserState(value,row,index){      return bstableState(value,row,index,dataBaseTable[0]);}
    //角色信息表state
function bstableTRoleState(value,row,index){      return bstableState(value,row,index,dataBaseTable[1]);}
    //权限信息表state
function bstableTPermissionState(value,row,index){return bstableState(value,row,index,dataBaseTable[2]);}
//members
function bstableMembers(value,row,index){     return "<button class='showARUModal btn btn-default' value='"+row.id+"'>管理成员</button>";}
//permissions
function bstablePermissions(value,row,index){ return "<button class='showARPModal btn btn-default' value='"+row.id+"'>管理权限</button>";}
//delete
    //用户信息表delete
function bstableTUserDelete(value,row,index){
    let url = "/datasource/deleteAuthorityUser";
    return "<button class='btn btn-default delete_btn' onclick=deleteOne(this,"+row.id+",'"+url+"')>删除</button>";
}
    //角色用户表delete
function bstableTUserRoleDelete(value,row,index){
    return "<button class='delete_btn btn btn-default' onclick=deleteRoleUserOne("+row.id+")>删除</button>";
}
    //角色权限表delete
function bstableTRolePermDelete(value,row,index){
    return "<button class='delete_btn btn btn-default' onclick=deleteRolePermOne("+row.id+")>删除</button>";
}
//select
function bstableSelect(value,row,index){
    let roles = row.roles.split(',');
    let str = "";
    str = "<select class='btn btn-default ' style='width:100px;overflow:hidden;'>";
    for(let i=0;i<roles.length;i++)
        str += "<option disabled>"+roles[i]+"</option>";
    str += "</select>";
    return str;
}

/**
 * 表格中的事件绑定
 */
window.authorityOperateEvents = {
    //state切换状态
    "click .bstableStateBtn" : function(e,value,row,index){
        bstableStateBtnClick(this);},
    /**
     * 用户信息表*/
    //roles最外面按钮
    "click .a_userRoleBtn" : function(e,value,row,index){
        bstableRolesClick(this);},
    //roles下拉框中完成按钮
    "click .bstableTRoleRoleUlBtn" : function(e,value,row,index){
        let url = "/datasource/changeAuthorityUserRoles";
        bstableRolesCompleteBtnClick(this,url);},
    //roles阻止消失事件
    "click .bstableRolesUl" :function(e,value,row,index){
        bstableRolesStopPropagation(e);},
    /**
     * 角色信息表*/
    //用户管理按钮
    "click .showARUModal" : function(e,value,row,index){
        bstableManageUserBtnClick(this);},
    //权限管理按钮
    "click .showARPModal" : function(e,value,row,index){
        bstableManagePermBtnClick(this);},
    /**
     * 权限信息表*/
    //roles下拉框中完成按钮
    "click .bstableTPermRoleUlBtn" : function(e,value,row,index){
        let url = "/datasource/changeAuthorityRolePermissions";
        bstableRolesCompleteBtnClick(this,url);},
}