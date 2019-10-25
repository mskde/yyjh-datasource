//总全局变量
var maxFileSize = 1048576;
/**
 *  ********************************************
 *  ********************************************
 *  csv操作*************************************
 *  ********************************************
 *  ********************************************
 */
var csv_datas=[];//多文件处理
/**
 * csv文件加载
 * 在datasource.js中调用
 */
function csv_load(element_id){
    $(element_id).fileupload({
        url: '/csv/csvUpload',
        type: 'POST',
        dataType: 'json',  //服务器返回的数据类型
        // autoUpload: true,    //无效
        acceptFileTypes: /(\.|\/)(csv)$/i,
        // maxFileSize:1048576,  //无效
        // minFileSize:0,
        // previewMaxWidth : 图片预览区域最大宽度,单位px
        add: function(e,data) {
            //每添加一个文件，执行一次

            //过滤（过大文件）
            //后台配置最大文件不超过1048576（1M）
            let size = data.files[0].size;
            if(size > maxFileSize){
                alert("过滤过大文件");
                return;
            }
            if(!csv_datas.hasOwnProperty("files") || csv_datas.files.length<=0){
                //第一次添加，初始化
                csv_datas=data;
            }
            else{
                csv_datas.files.push(data.files[0]);
            }
            //网页显示文件名
            let data_content = "<div class='mine_csv_datas'>" + data.files[0].name + "</div>";
            $("#csv_datas").html($("#csv_datas").html()+data_content);
        },
        done: function(e,data) {
            //成功执行
            let result = data.result;
            if(result.code == 0){
                if(result.hasOwnProperty("payload") && result.payload != null) {
                    //上传成功
                    $("#csv_import").modal("hide");
                    $("#csv_preview").modal("show");
                    //创建按钮组
                    create_csv_btngroup(result.payload);
                } else {
                    //初始化
                    alert("上传成功！");
                    init_csv_datas();
                }
            } else if(result.code == -1) {
                alert("操作失败！")
            }
        },
        fail : function(e,data){
            //失败执行
        },
        always:function(e,data){
            //总是执行
        }
    });
}
/**
 * 初始化csv
 */
function init_csv_datas(){
    csv_datas=[];
    globle_csv_datas=[];
    $("#csv_datas").html("");
}
/**
 * csv预览传值
 * 修改file-upload中url的值（预览和上传的切换）
 */
function csv_url(element_id,url) {
    $(element_id).bind('fileuploadsubmit',function(e,data) {
        let separator = " ";
        //jquery-file-upload插件问题，要上传属性，就得在formData中添加
        data.formData = {
            csv_interpret: separator,
            userId:owninfo.loginid
        };
    });
    //修改url属性值
    $(element_id).fileupload('option','url',url);
}
/**
 * csv预览中
 * 创建按钮组
 */
var globle_csv_datas = [];
function create_csv_btngroup(payloads) {
    globle_csv_datas = payloads;
    let btn_group = "<div class='btn-group'>";
    for(i=0; i<globle_csv_datas.length; i++) {
        let data = globle_csv_datas[i];
        if(i == 0)
            btn_group += "<button onclick='create_csv_preview_table(this)' class='btn btn-primary'>" + data.file_name + "</button>";
        else
            btn_group += "<button onclick='create_csv_preview_table(this)' class='btn btn-default'>" + data.file_name + "</button>";
    }
    btn_group += "</div>";
    $("#csv_btngroup").html(btn_group);
    //打开默认选中第一个文件
    //显示第一个文件内容的表格
    if (globle_csv_datas != []) {
        init_csv_preview_table(globle_csv_datas[0].file_name);
    }
}
/**
 * csv预览中
 * 创建对应的表格
 */
function create_csv_preview_table(btn) {
    if($(btn).attr("class") == "btn btn-default"){
        let btns = $(btn).siblings();
        //按钮组点击样式切换
        for(i=0;i<btns.length;i++)
            $(btns[i]).attr("class","btn btn-default");
        $(btn).attr("class","btn btn-primary");
        //初始化表格
        let filename = $(btn).text();
        init_csv_preview_table(filename);
    }
}
/**
 * csv预览
 * 初始化表格
 */
function init_csv_preview_table(init_filename){
    let data = [];
    //获取对应文件内容
    for (i=0;i<globle_csv_datas.length;i++) {
        if (globle_csv_datas[i].file_name == init_filename) {
            data = globle_csv_datas[i].file_datas;
            break;
        }
    }
    //根据init_filename动态生成table,注意每个文件表头可能不同
    let ths=[];
    if(data.length>0){
        for (key in data[0]) {
            th = {
                field: key,
                title: key,
                visible: true
            };
            ths.push(th);
        }
    }
    //初始化表格
    $("#csv_preview_table").bootstrapTable("destroy");
    //加载表格
    $('#csv_preview_table').bootstrapTable({
        striped: true, //是否显示行间隔色
        cache: false,//是否使用缓存，默认true，所以一般情况下需要
        pagination: true,//是否显示分页
        sortable:false,//是否启用排序
        sidePagination:"client",//分页方式：client客户端分页
        pageNumber:1,//初始化加载第一页，默认第一页
        pageSize:10,//每页的记录行数
        pageList:[10,20,30,40],//可供选择的每页的行数(*)
        search:true,//是否显示表格搜索，此搜索是客户端搜索
        strictSearch:false,
        showColumns:true,//是否显示所有 的列
        showRefresh:true,//是否显示刷新按钮
        minimumCountColumns:2,//最少允许的列数
        clickToSelect:true,//是否启用点击选中行
        showToggle:false,//是否显示详细视图和列表视图的切换按钮
        cardView:false,//是否显示详细视图
        detailView:false,
        columns:ths
    });
    $("#csv_preview_table").bootstrapTable("load",data);
}
/**
 *  ********************************************
 *  ********************************************
 *  Excel操作***********************************
 *  ********************************************
 *  ********************************************
 */
var excel_datas = []; //多文件处理
var excel_interpret=[]; //文件配置信息
/**
 * excel文件加载
 * 在datasource.js中调用
 */
function excel_load(element_id) {
    $(element_id).fileupload({
        url: '/excel/excelUpload',
        type: 'POST',
        dataType: 'json',
        autoUpload: false,
        acceptFileTypes: /(\.|\/)(xls|xlsx)$/i,
        add: function(e,data) {
            //过滤（过大文件）
            //后台配置最大文件不超过1048576（1M）
            let size = data.files[0].size;
            if(size > maxFileSize){
                alert("过滤过大文件");
                return;
            }
            if(!excel_datas.hasOwnProperty("files") || excel_datas.files.length<=0){
                //只执行一次（初始化csv_datas格式）
                excel_datas=data;
            }
            else{
                excel_datas.files.push(data.files[0]);
            }
            //表格添加行
            let filename = data.files[0].name;
            let row_data = {};
            row_data["excel_filename"] = filename;
            let excel_filter = "<div>";
            excel_filter += "<label class=\"radio-inline\"><input type=\"radio\" style=\"outline:none;\" name=\"filter" + filename +" \" value=\"yes\">&nbsp;&nbsp;&nbsp;是</label>";
            excel_filter += "<label class=\"radio-inline\"><input type=\"radio\" style=\"outline:none;\" name=\"filter" + filename +" \" value=\"no\" checked>&nbsp;&nbsp;&nbsp;否</label>";
            excel_filter += "</div>";
            row_data["excel_filter"] = excel_filter;
            row_data["open_config"] = "<button class=\"btn btn-default\" onclick=\"open_config('" + filename + "')\">打开配置</button>"
            $("#excel_table").bootstrapTable("append",row_data);
            //初始化默认过滤条件
            let this_excel_interpret={"filename": filename,"isfilter": false};
            excel_interpret.push(this_excel_interpret);
            //初始化过滤模态框
            init_excel_filter_form();
        },
        done: function(e,data) {
            let result = data.result;
            if(result.code == 0) {
                if(result.hasOwnProperty("payload") && result.payload != null) {
                    //预览（payload有值）
                    preview_excel(result.payload);
                } else {
                    //上传
                    alert("上传成功！");
                    //初始化
                    init_excel_datas();
                }
            } else if(result.code == -1) {
                alert("操作失败！");
            }
        }
    });
}
/**
 * excel初始化
 */
var globle_excel_datas = [];
function init_excel_datas() {
    excel_datas=[];
    globle_excel_datas = [];
    excel_interpret=[];
    $('#excel_table').bootstrapTable({
        striped: true, // 是否显示行间隔色
        cache: false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: false, // 是否显示分页（*）
        sortable: false, // 是否启用排序
        sidePagination: "client", // 分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1, // 初始化加载第一页，默认第一页
        pageSize: 10, // 每页的记录行数（*）
        pageList: [10, 20, 30, 40], // 可供选择的每页的行数（*）
        search: true, // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: false,
        showColumns: false, // 是否显示所有的列
        showRefresh: false, // 是否显示刷新按钮
        minimumCountColumns: 3, // 最少允许的列数
        clickToSelect: false, // 是否启用点击选中行
        showToggle: false, // 是否显示详细视图和列表视图的切换按钮
        cardView: false, // 是否显示详细视图
        detailView: false,
        columns: [{
            field: "excel_filename",
            title: "文件名称",
            visible: true
        }, {
            field: "excel_filter",
            title: "是否过滤",
            visible: true
        }, {
            field: "open_config",
            title: "文件配置",
            visible: true
        }],
        formatNoMatches:function(){
            //空数据时：提示文本
            return "";
        }
    });
    $("#excel_table").bootstrapTable("removeAll"); //清空数据
}
/**
 * url切换
 */
function excel_url(element_id, url) {
    $(element_id).bind('fileuploadsubmit', function(e, data) {
        data.formData = {
            excel_interpret: JSON.stringify(excel_interpret),
            userId:owninfo.loginid
        };
    });
    $(element_id).fileupload('option','url',url);
}
/**
 * excel预览按钮
 */
function preview_excel(payload){
    $("#excel_import").modal("hide");
    $("#excel_preview").modal("show");
    globle_excel_datas = payload;
    let btn_group = "<div class=\"btn-group\" role=\"group\">";
    for(i=0;i<payload.length;i++) {
        let data = globle_excel_datas[i];
        if(i == 0)
            btn_group += "<button onclick=\"create_excel_select(this)\" type=\"button\" class=\"btn btn-primary\">" + data.file_name + "</button>";
        else
            btn_group += "<button onclick=\"create_excel_select(this)\" type=\"button\" class=\"btn btn-default\">" + data.file_name + "</button>";
    }
    btn_group += "</div>";
    $("#excel_preview_btngroup").html(btn_group);
    //默认表格
    if(globle_excel_datas != []) {
        let filename  = globle_excel_datas[0].file_name;
        let sheetname = globle_excel_datas[0].file_datas[0].sheet_name;
        //下拉菜单
        let sel="<select id='"+filename+"' class='selectpicker' onchange='choose_excel_select(this)'>";
        let f_datas = globle_excel_datas[0].file_datas;
        for(x=0;x<f_datas.length;x++)
            sel+="<option value='"+f_datas[x].sheet_name+"'>"+f_datas[x].sheet_name+"</option>";
        sel+="</select>";
        $("#excel_preview_select").html(sel);
        //默认表格
        init_excel_table(filename,sheetname);
    }
}
/**
 * excel预览
 * 创建下拉框
 */
function create_excel_select(btn){
    let btns = $(btn).siblings();
    for(i=0;i<btns.length;i++)
        $(btns[i]).attr("class", "btn btn-default");
    $(btn).attr("class","btn btn-primary");
    //表格
    let filename = $(btn).text();
    let sheetname = "";
    // let init_sheet="";
    //下拉菜单
    let sel="<select id='"+filename+"' class='selectpicker' onchange='choose_excel_select(this)'>";
    for(i=0;i<globle_excel_datas.length;i++){
        let e_data=globle_excel_datas[i];
        if(filename==e_data.file_name){
            let f_datas=e_data.file_datas;
            for(x=0;x<f_datas.length;x++){
                if(x ==0){
                    sheetname=f_datas[x].sheet_name;
                }
                sel+="<option value='"+f_datas[x].sheet_name+"'>"+f_datas[x].sheet_name+"</option>";
            }
            break;
        }
    }
    sel+="</select>";
    $("#excel_preview_select").html(sel);
    //初始化表格
    init_excel_table(filename,sheetname)
}
/**
 * excel预览
 * 下拉框切换
 */
function choose_excel_select(sel){
    let sheetname=$(sel).val();
    let filename=$(sel).attr("id");
    //初始化表格
    init_excel_table(filename,sheetname);
}
/**
 * 初始化表格
 */
function init_excel_table(filename,sheetname){
    let datas=[];
        let ths=[];
        //获取datas:excel对应sheet的数据，ths:对应sheet的columns
        for(i=0;i<globle_excel_datas.length;i++) {
            let e_data=globle_excel_datas[i];
            if(e_data.file_name == filename) {
                let f_datas=e_data.file_datas;
                for(x=0;x<f_datas.length;x++){
                    if(f_datas[x].sheet_name == sheetname){
                        let sheetdatas=f_datas[x].sheet_datas;
                        for(let key in sheetdatas[0]) {
                            let th = {
                                field: key,
                                title: key,
                                visible: true
                            }
                            ths.push(th);
                        }
                        datas=f_datas[x].sheet_datas;
                        break;
                    }
                }
            }
    }
    //初始化表格
    $('#excel_preview_table').bootstrapTable("destroy");
    //加载表格
    $('#excel_preview_table').bootstrapTable({
        striped: true, // 是否显示行间隔色
        cache: false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, // 是否显示分页（*）
        sortable: false, // 是否启用排序
        sidePagination: "client", // 分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1, // 初始化加载第一页，默认第一页
        pageSize: 10, // 每页的记录行数（*）
        pageList: [10, 20, 30, 40], // 可供选择的每页的行数（*）
        search: true, // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: false,
        showColumns: true, // 是否显示所有的列
        showRefresh: true, // 是否显示刷新按钮
        minimumCountColumns: 2, // 最少允许的列数
        clickToSelect: true, // 是否启用点击选中行
        showToggle: false, // 是否显示详细视图和列表视图的切换按钮
        cardView: false, // 是否显示详细视图
        detailView: false,
        columns: ths,
        formatNoMatches:function(){
            //空数据时：提示文本
            return "";
        }
    });
    $("#excel_preview_table").bootstrapTable("load", datas);
}
/**
 * excel
 * 每一个文件配置按钮
 */
function open_config(filename){
    $("#excel_import").modal("hide");
    $("#excel_interpret").modal("show");
    $("#ex_filename").val(filename);
    init_excel_filter_form();
}

/**
 * excel配置
 * excel配置框中
 * 完成按钮
 */
function ex_interpret_submit(){
    $("#excel_interpret").modal("hide");
    $("#excel_import").modal("show");

    let filename=$("#ex_filename").val();
    let interpret_filter={};
    let start_line=$("#start_line").val();
    let end_line=$("#end_line").val();
    let index_logic=$("#index_string").find("input[name='index_logic']:checked").val();

    if(start_line != "")
        interpret_filter["start_line"]=start_line;
    if(end_line != "")
        interpret_filter["end_line"]=end_line;
    if(index_logic != undefined)
        interpret_filter["index_logic"]=index_logic;

    let index_string=[]
    let oper_string_div=$(".oper_string")
    for(i=0;i<oper_string_div.length;i++){
        let oper=oper_string_div[i];
        let oper_string={};
        let start_string=$(oper).find("input:eq(0)").val();
        let end_string=$(oper).find("input:eq(1)").val();
        let content_string=$(oper).find("input:eq(2)").val();
        if(start_string != "" || end_string != "" || content_string != ""){
            oper_string["start_string"]=start_string;
            oper_string["end_string"]=end_string;
            oper_string["content_string"]=content_string;
            index_string.push(oper_string)
        }
    }
    if(index_string.length != 0){
        interpret_filter["index_string"]=index_string;
    }
    if(JSON.stringify(interpret_filter) != "{}"){
        for(i=0;i<excel_interpret.length;i++){
            if(excel_interpret[i]["filename"] == filename ){
                excel_interpret[i]["isfilter"]=true;
                excel_interpret[i]["interpret_filter"]=interpret_filter;
                break;
            }
        }
    }
    console.log(excel_interpret);
}

/**
 * excel配置
 * 添加新的文本配置
 */
function create_inter_div(){
    console.log($(".oper_string").prop("outerHTML"));
    $("#index_string").find("div:eq(0)").append($(".oper_string").prop("outerHTML"))
}
/**
 * excel配置
 * 初始化excel过滤form
 */
function init_excel_filter_form(){
    let oper_string = $(".oper_string");
    for(i=1;i<oper_string.length;i++)
        $(oper_string[i]).remove();
    $("#excel_interpret form")[0].reset();
}



//mysql重置表单
function reset_mysql_form(){
    $("#mysql_ip").val("");
    $("#mysql_port").val("");
    $("#mysql_databasename").val("");
    $("#mysql_uername").val("");
    $("#mysql_password").val("");
    $("#mysql_driver").val("");
    $("#mysql_alias").val("");
    $("#mysql_description").val("");
}
//mysql提交表单
function submit_mysql_form(){
    var mysql_form={};
    mysql_form["url"]=$("#mysql_ip").val();
    mysql_form["port"]=$("#mysql_port").val();
    mysql_form["databaseName"]=$("#mysql_databasename").val();
    mysql_form["username"]=$("#mysql_uername").val();
    mysql_form["password"]=$("#mysql_password").val();
    mysql_form["driver"]=$("#mysql_driver").val();
    mysql_form["alias"]=$("#mysql_alias").val();
    mysql_form["description"]=$("#mysql_description").val();
    mysql_form["userId"]=owninfo.loginid;
    $.ajax({
        type:"POST",
        dataType:'json',    //期待返回的数据类型text, json
        contentType : "application/json",    //如果想以json格式把数据提交到后台的话，这个必须有，否则只会当做表单提交
        url:"/mysql/mysqlUpload",    //传递页面
        data: JSON.stringify(mysql_form),    //json类型
        success:function (data) {
            if(data== true){
                console.log("修改成功")
                $("#mysql_import").modal("hide");
                alert("上传成功！");
            }else{
                console.log("修改失败")
            }
        }
    });
}


//redis重置表单
function reset_redis_form(){
    $("#redis_ip").val("");
    $("#redis_port").val("");
    $("#redis_uername").val("");
    $("#redis_password").val("");
    $("#redis_description").val("");
}
//redis提交表单
function submit_redis_form(){
    var redis_form={};
    redis_form["url"]=$("#redis_ip").val();
    redis_form["port"]=$("#redis_port").val();
    redis_form["username"]=$("#redis_uername").val();
    redis_form["password"]=$("#redis_password").val();
    redis_form["description"]=$("#redis_description").val();
    redis_form["userId"]=owninfo.loginid;
    $.ajax({
        type:"POST",
        dataType:'json',    //期待返回的数据类型text, json
        contentType : "application/json",    //如果想以json格式把数据提交到后台的话，这个必须有，否则只会当做表单提交
        url:"/redis/redisUpload",    //传递页面
        data: JSON.stringify(redis_form),    //json类型
        success:function (data) {
            if(data== true){
                console.log("修改成功")
                $("#redis_import").modal("hide");
                alert("上传成功！");
            }else{
                console.log("修改失败")
            }
        }
    });

}
