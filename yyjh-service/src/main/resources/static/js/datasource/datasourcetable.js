$(document).ready(function(){
    let datasource_table_url='/datasource/getAllByPage';
    init_datasource_table(datasource_table_url);



    //搜素按钮
    $("#search_datasource").click(function () {
        search_datasourceByData();
    })
    /*//搜索框监听文本变化
    $("#input_search").bind('input propertychange', function(){
        search_datasourceByData();
    })*/
    //筛选分类按钮
    $('#select_search').change(function(){
        search_datasourceByData();
    })



    //批量删除
    $("#batch_delete").click(function () {
        batchDelete();
        refresh_datasource();
    })



    //数据源更新信息表单退出按钮
    $("#datasource_form_close").click(function(){
        $("#datasource_form").modal("hide");
    })
    //数据更新信息表单重置按钮
    $("#datasource_reset").click(function (){
        datasource_update(row_data)
    })
    //数据更新信息表单提交按钮
    $("#datasource_submit").click(function () {
        datasource_update_submit();
    })



    //encode字段表单退出按钮
    $("#encode_form_close").click(function(){
        $("#encode_form").modal("hide");
    })
    $("#encode_exit").click(function(){
        $("#encode_form").modal("hide");
    })
    //encode字段表单修改
    $("#encode_update").click(function(){
        //encode处理表单中现有数据
        get_encode_newData();
        $("#encode_form").modal("hide");
    })






    //mysql入库模态框退出按钮
    $("#datasource_mysql_store_close").click(function(){
        $("#datasource_mysql_store").modal("hide");
    })
    //mysql入库模态框退出按钮
    $("#mysql_store_quit").click(function(){
        $("#datasource_mysql_store").modal("hide");
    })
    //mysql入库模态框完成按钮
    $("#mysql_store_success").click(function(){
        $("#datasource_mysql_store").modal("hide");
        mysql_data_submit();
    })





    //redis入库模态框退出按钮
    $("#datasource_redis_store_close").click(function(){
        $("#datasource_redis_store").modal("hide");
    })
    //redis入库模态框退出按钮
    $("#redis_store_quit").click(function(){
        $("#datasource_redis_store").modal("hide");
    })
    //redis入库模态框完成按钮
    $("#redis_store_success").click(function(){
        $("#datasource_redis_store").modal("hide");
        redis_data_submit();

    })
    //点击redis下拉菜单
    $(".redis_number_select").click(function(){
        var redis_flag=0
        for(i=0;i<invalid_redis_number.length;i++){
            if($(this).attr("id") == invalid_redis_number[i]){
                redis_flag=1;
                break;
            }
        }
        if(redis_flag ==0){
            display_select_redis_number($(this).attr("id"))
        }
    })
})

//搜索内容的值
var info
var select_type
//搜索内容
function search_datasourceByData() {
    info=$("#input_search").val()
    select_type=$("#select_search option:checked").val()
    info=info.replace(/\s*/g, '');
    if(select_type==""){
        select_type="any"
    }
    console.log(info)
    console.log(select_type)
    if( info == "" && select_type=="any"){
        console.log("asdfasdf")
        refresh_datasource();
    }else{
        $('#datasource_table').bootstrapTable('destroy');
        var datasource_table_url='/datasource/getTDatasourceByAll';
        init_datasource_table(datasource_table_url);

    }


}





//更新表格
function refresh_datasource() {
    $('#datasource_table').bootstrapTable('destroy');
    var datasource_table_url='/datasource/getAllByPage';
    init_datasource_table(datasource_table_url);
}
//初始化表格
function init_datasource_table(url) {
    $('#datasource_table').bootstrapTable({
        url: url,     //请求后台的URL（*）
        method: 'post',           //请求方式（*）
        toolbar: '#test',        //工具按钮用哪个容器
        striped: true,           //是否显示行间隔色
        cache: false,            //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        sortable: true,           //是否启用排序
        sortName:"id",
        sortOrder: "desc",          //排序方式
        sidePagination: "server",      //分页方式：client客户端分页，server服务端分页（*）
        pageNumber:1,            //初始化加载第一页，默认第一页
        pagination: true,          //是否显示分页（*）
        pageSize: 5,            //每页的记录行数（*）
        pageList: [5,10, 15, 20],    //可供选择的每页的行数（*）
        showColumns: true,         //是否显示所有的列
        minimumCountColumns: 2,       //最少允许的列数
        uniqueId: "id",           //每一行的唯一标识，一般为主键列
        contentType: "application/x-www-form-urlencoded", //解决POST提交问题
        queryParamsType:'',
        //得到查询的参数
        queryParams : function (params) {
            return{
                pageSize: params.pageSize,
                pageNumber: params.pageNumber,
                info: info,
                selectType: select_type,
            }
        },

        columns: [
            //encode   password
            {align: 'center',checkbox: true,},

            {align: 'center',title:'id',field: 'id',visible:true},
            {align: 'center',title:'userId',field: 'userId',visible:true},
            {align: 'center',title:'type',field: 'type',visible:true},
            {align: 'center',title:'createtime',field: 'createtime',visible:true},
            {align: 'center',title:'updatetime',field: 'updatetime',visible:true},
            {align: 'center',title:'databaseName',field: 'databaseName',visible:true},
            {align: 'center',title:'alias',field: 'alias',visible:true},
            {align: 'center',title:'driver',field: 'driver',visible:true},
            {align: 'center',title:'url',field: 'url',visible:true},
            {align: 'center',title:'port',field: 'port',visible:true},
            {align: 'center',title:'auth',field: 'auth',visible:true},
            {align: 'center',title:'username',field: 'username',visible:true},
            {align: 'center',title:'description',field: 'description',visible:true},

            {align: 'center',title:'encode',valign: 'middle',formatter:"getEncode",events:operateEvents},
            {align: 'center',title: '操作',valign: 'middle',formatter:"operateFormatter",events:operateEvents},
            {align: 'center',title: '入库',valign: 'middle',formatter:"dataStore",events:operateEvents},
        ],


        //点击复选框相关事件
        onCheckAll: function (rows) {
            getAllId();
        },
        onUncheckAll: function (rows) {
            checked_set=new Set();
        },
        onCheck: function (row) {
            checked_set.add(row.id)
        },
        onUncheck: function (row) {
            checked_set.delete(row.id)
        },
        onLoadSuccess: function () {
            load_checked();
        },

    });

}


//点击全选框获取数据库所有数据id
function getAllId() {
    $.ajax({
        type : "post",
        url : "/datasource/getAll",
        success : function(data) {
            for(var i=0;i<data.length;i++){
                checked_set.add(data[i]["id"])
            }
        },
        error : function(e) {
            console.log(e);
            alert("无权操作");
        }
    })
}
//已被选中数组
var checked_set=new Set();
//加载被选中项复选框
function load_checked() {
    $("#datasource_table").bootstrapTable("checkBy",
        { field:"id", values:Array.from(checked_set)}
    );
}


//批量删除
function batchDelete() {
    var checked_arr=Array.from(checked_set);
    var json=[];
    for(var i=0;i<checked_arr.length;i++){
        json.push({"id":checked_arr[i]})
    }
    $.ajax({
        type:"POST",
        contentType : "application/json",
        url:"/datasource/delBatchTDatasource",
        data: JSON.stringify(json),
        success:function (data) {
            if(data== true){
                console.log("删除成功")
            }else{
                console.log("删除失败")
            }
        },
        error : function(e) {
            console.log(e);
            alert("无权操作");
        }
    });
}




//表格显示按钮
function getEncode(value, row, index) {
    if(row.returnMsg != '成功'&& row.type == "excel"){
        return [
            '<button id="encode_display" class="btn btn-primary">编辑</button>'
        ].join('');
    }else{
        return [
            '<button  class="btn btn-default">无内容</button>'
        ].join('');
    }
}
//修改、删除按钮
function operateFormatter(value, row, index) {
    if(row.returnMsg != '成功'){
        return [
            '<button id="table_update" class="btn btn-info">修改</button>&nbsp;&nbsp;',
            '<button id="table_delete" class="btn btn-danger">删除</button>',
        ].join('');
    }
}
//入库按钮
function dataStore(value, row, index) {
    if(row.returnMsg != '成功'){
        return [
            '<button id="table_data_store" class="btn btn-success">入库</button>'
        ].join('');
    }
}

//绑定上述按钮事件
window.operateEvents={
    "click #encode_display":function (e,value,row,index) {
        getEncodeData(row.id)
    },
    "click #table_update":function (e,value,row,index) {
        datasource_update(row)
    },
    "click #table_delete":function (e,value,row,index) {
        datasource_delete(row.id)
    },
    "click #table_data_store":function (e,value,row,index) {
        datasource_data_store(row)
    }
}

//数据源删除事件
function datasource_delete(id){
    var url="/datasource/delTDatasourceById/"+id;
    $.ajax({
        type : "post",
        url : url,
        success : function(data) {
            refresh_datasource();
        },
        error : function(e) {
            console.log(e);
            alert("无权操作");
        }
    })
}

var row_data={};
//数据源更新事件
function datasource_update(row){
    row_data=row;
    if(row.id != null){
        $("#form_id").val(row.id)
    }
    if(row.userId != null){
        $("#form_userId").val(row.userId)
    }
    if(row.type != null){
        $("#form_type").val(row.type)
    }
    if(row.databaseName != null){
        $("#form_databaseName").val(row.databaseName)
    }
    if(row.alias != null){
        $("#form_alias").val(row.alias)
    }
    if(row.driver != null){
        $("#form_driver").val(row.driver)
    }
    if(row.url != null){
        $("#form_url").val(row.url)
    }
    if(row.port != null){
        $("#form_port").val(row.port)
    }
    if(row.auth != null){
        $("#form_auth").val(row.auth)
    }
    if(row.username != null){
        $("#form_username").val(row.username)
    }
    if(row.description != null){
        $("#form_description").val(row.description)
    }
    $("#datasource_form").modal("show");

}
//数据源更新信息模态框
function datasource_update_submit() {
    var new_row_data={}
    new_row_data['id']=$("#form_id").val();
    new_row_data['userId']=$("#form_userId").val();
    new_row_data['type']=$("#form_type").val();
    new_row_data['databaseName']=$("#form_databaseName").val();
    new_row_data['alias']=$("#form_alias").val();
    new_row_data['driver']=$("#form_driver").val();
    new_row_data['url']=$("#form_url").val();
    new_row_data['port']=$("#form_port").val();
    new_row_data['auth']=$("#form_auth").val();
    new_row_data['username']=$("#form_username").val();
    new_row_data['description']=$("#form_description").val();

    $.ajax({
        type:"POST",
        dataType:'json',    //期待返回的数据类型text, json
        contentType : "application/json",    //如果想以json格式把数据提交到后台的话，这个必须有，否则只会当做表单提交
        url:"/datasource/updTDatasourceById",    //传递页面
        data: JSON.stringify(new_row_data),    //json类型
        success:function (data) {
            if(data== true){
                console.log("修改成功")
                $("#datasource_form").modal("hide");
                refresh_datasource()
            }else{
                console.log("修改失败")
            }
        }
    });

}

//显示encode
function getEncodeData(id) {
    var url="/datasource/findTDatasourceById/"+id;
    $.ajax({
        type : "post",
        url : url,
        success : function(data) {
            var json=JSON.parse(data["encode"])
            $("#encode_form").modal("show");
            encode_manmage(json,data["id"])
        },
        error : function(e) {
            console.log(e);
            alert("无权操作");
        }
    })
}

var encode_data;
var num;
var encode_id;
//显示encode数据处理
function encode_manmage(data,id){
    encode_id=id;
    encode_data=data;
    if(encode_data["isfilter"]==true){
        num=encode_data["interpret_filter"]["index_string"].length
    }else{
        num=0;
    }
    $("#encode_filename").val(data["filename"])
    if(	data["isfilter"] == true ){
        $("#inlineRadio2").prop("checked",true);
        $("#inlineRadio1").prop("checked",false);

        var encode_interpret=data["interpret_filter"];
        //append
        var start_line_label='<div class="form-group"><label for="form_start_line" class="col-sm-2 control-label">start_line</label>'+
            '<div class="col-sm-10"><input id="form_start_line" type="text" class="form-control" value="'+
            encode_interpret["start_line"]+'"></div></div>';
        var end_line_label='<div class="form-group"><label for="form_end_line" class="col-sm-2 control-label">end_line</label>'+
            '<div class="col-sm-10"><input id="form_end_line" type="text" class="form-control" value="'+
            encode_interpret["end_line"]+'"></div></div>';
        var test=start_line_label+end_line_label
        var index_logic_radio;

        if(encode_interpret["index_logic"] == "or"){
            index_logic_radio='<div class="form-group">' +
                '                <label class="col-sm-2 control-label">index_logic</label>' +
                '                <label class="radio-inline col-sm-2 col-sm-offset-1">' +
                '                <input type="radio" name="index_logicRadioOptions" id="index_logicRadio1" value="or" checked>或者' +
                '                </label>' +
                '                <label class="radio-inline col-sm-2 ">' +
                '                <input type="radio" name="index_logicRadioOptions" id="index_logicRadio2" value="and">合并' +
                '                </label>' +
                '            </div>'
        }else{
            index_logic_radio='<div class="form-group">' +
                '                <label class="col-sm-2 control-label">index_logic</label>' +
                '                <label class="radio-inline col-sm-2 col-sm-offset-1">' +
                '                <input type="radio" name="index_logicRadioOptions" id="index_logicRadio1" value="or">或者' +
                '                </label>' +
                '                <label class="radio-inline col-sm-2 ">' +
                '                <input type="radio" name="index_logicRadioOptions" id="index_logicRadio2" value="and" checked>合并' +
                '                </label>' +
                '            </div>'
        }
        test+=index_logic_radio;
        var encode_interpret_index_string=encode_interpret["index_string"];
        for(i=0;i<encode_interpret_index_string.length;i++){
            var start_string_id="form_start_string"+i;
            var content_string_id="form_content_string"+i;
            var end_string_id="form_end_string"+i;
            var encode_index_string=encode_interpret_index_string[i];
            var start_string_label='<div class="form-group"><label for='+start_string_id+' class="col-sm-2 control-label">start_string'+i+'</label>'+
                '<div class="col-sm-10"><input id='+start_string_id+' type="text" class="form-control" value="'+
                encode_index_string["start_string"]+'"></div></div>';
            var content_string_label='<div class="form-group"><label for='+content_string_id+' class="col-sm-2 control-label">content_string'+i+'</label>'+
                '<div class="col-sm-10"><input id='+content_string_id+' type="text" class="form-control" value="'+
                encode_index_string["content_string"]+'"></div></div>';
            var end_string_label='<div class="form-group"><label for='+end_string_id+' class="col-sm-2 control-label">end_string'+i+'</label>'+
                '<div class="col-sm-10"><input id='+end_string_id+' type="text" class="form-control" value="'+
                encode_index_string["end_string"]+'"></div></div>';
            test+=start_string_label+content_string_label+end_string_label;
        }
        $("#encode_form_add").html(test)
    }else{
        $("#inlineRadio1").prop("checked",true);
        $("#inlineRadio2").prop("checked",false);
        $("#encode_form_add").html("")
    }
    var add_string='<span id="filter_string_add" class="glyphicon glyphicon-plus-sign" style="float: right; font-size: larger;" onclick="create_filter_string()" ></span>';
    $("#encode_form_add").append(add_string)
}
//增加过滤的三行条件
function create_filter_string(){
    $("#filter_string_add").remove();
    var start_string_id="form_start_string"+num;
    var content_string_id="form_content_string"+num;
    var end_string_id="form_end_string"+num;
    var start_string_label='<div class="form-group"><label for='+start_string_id+' class="col-sm-2 control-label">start_string'+num+'</label>'+
        '<div class="col-sm-10"><input id='+start_string_id+' type="text" class="form-control" ></div></div>';
    var content_string_label='<div class="form-group"><label for='+content_string_id+' class="col-sm-2 control-label">content_string'+num+'</label>'+
        '<div class="col-sm-10"><input id='+content_string_id+' type="text" class="form-control" ></div></div>';
    var end_string_label='<div class="form-group"><label for='+end_string_id+' class="col-sm-2 control-label">end_string'+num+'</label>'+
        '<div class="col-sm-10"><input id='+end_string_id+' type="text" class="form-control" ></div></div>';
    var test=start_string_label+content_string_label+end_string_label;
    $("#encode_form_add").append(test)
    var add_string='<span id="filter_string_add" class="glyphicon glyphicon-plus-sign" style="float: right; font-size: larger;" onclick="create_filter_string()" ></span>';
    $("#encode_form_add").append(add_string)
    num++;

}
//encode处理表单中现有数据
function get_encode_newData(){
    var new_encode_data={};
    new_encode_data["filename"]=encode_data["filename"]

    var isfilter_checked=$("input[name='inlineRadioOptions']:checked").val()
    if(isfilter_checked){
        new_encode_data["isfilter"]=true;
        var interpret_filter_json={};
        interpret_filter_json["index_logic"]=$("input[name='index_logicRadioOptions']:checked").val()
        interpret_filter_json["start_line"]=$("#form_start_line").val();
        interpret_filter_json["end_line"]=$("#form_end_line").val();
        var index_string_json=[];
        for(i=0;i<num;i++){
            var index_string_element={};
            var start="form_start_string"+i;
            var content="form_content_string"+i;
            var end="form_end_string"+i;
            index_string_element["start_string"]=$("#"+start).val()
            index_string_element["content_string"]=$("#"+content).val()
            index_string_element["end_string"]=$("#"+end).val()
            index_string_json.push(index_string_element)
        }
        interpret_filter_json["index_string"]=index_string_json;
        new_encode_data["interpret_filter"]=interpret_filter_json;
    }else{
        new_encode_data["isfilter"]=false;
    }
    var new_encode_id_data={};
    new_encode_id_data["id"]=encode_id;
    new_encode_id_data["encode"]=JSON.stringify(new_encode_data)
    $.ajax({
        type:"POST",
        dataType:'json',    //期待返回的数据类型text, json
        contentType : "application/json",    //如果想以json格式把数据提交到后台的话，这个必须有，否则只会当做表单提交
        url:"/datasource/updTDatasourceById",    //传递页面
        data: JSON.stringify(new_encode_id_data),    //json类型
        success:function (data) {
            if(data== true){
                console.log("修改encode成功")
            }else{
                console.log("修改encode失败")
            }
        }
    });

}

//数据源入库操作
function datasource_data_store(row) {
    if(row.type == "excel"){
        console.log("excel")
        excel_data_store(row.id)
    }else if(row.type == "csv"){
        csv_data_store(row.id)
    }else if(row.type == "mysql"){
        init_mysql_store();
        $("#datasource_mysql_store").modal("show");
        datasource_mysql_id=row.id;
        get_mysql_data(row.id);
    }else if(row.type == "redis"){
        init_redis_store();
        $("#datasource_redis_store").modal("show");
        datasource_redis_id=row.id;
        get_redis_data(row.id);

    }
}

//csv入库输入
function csv_data_store(id){
    $.ajax({
        type:"POST",
        dataType:'json',
        contentType : "application/json",
        url: "/csv/csvToDB/"+id,
        success:function (data) {
            console.log(data);
        }
    });
}
//excel入库输入
function excel_data_store(id){
    $.ajax({
        type:"POST",
        dataType:'json',
        contentType : "application/json",
        url: "/excel/excelStore/"+id,
        success:function (data) {
            console.log(data);
        }
    });
}




//点击mysql入库按钮获取相关数据
function get_mysql_data(id) {
    var url="/mysql/mysqlToDb/"+id;
    $.ajax({
        type : "post",
        url : url,
        success : function(data) {
            console.log(data)
            $("#mysql_url").val(data["payload"][0]["mysql_ip"]+":"+data["payload"][0]["mysql_port"]);
            $("#mysql_dbname").val(data["payload"][0]["mysql_dbname"])
            mysql_tablename=data["payload"][0]["mysql_tablename"];
            sametablename=data["payload"][0]["sametablename"];
            load_drop_down_list()
            mysql_select_invalid()
        },
        error : function(e) {
            console.log(e);
            alert("无权操作");
        }
    })
}

var datasource_mysql_id

//数据库所有表名
var mysql_tablename=[]
// 数据库相同表名
var sametablename=[]
//选择的数据库表名
var mysql_database_number=[];


//mysql入库按钮初始化
function init_mysql_store() {
    mysql_tablename=[];
    sametablename=[];
    mysql_database_number=[];

    $("#mysql_dropdowmmenu").empty();
    $("#mysql_name_ul").empty();
}
//加载下拉菜单表名
function load_drop_down_list() {
    for(i=0;i<mysql_tablename.length;i++){
        var test='<li><a onclick="click_mysql_table_select($(this))" id="'+mysql_tablename[i]["tablename"]+'">'+mysql_tablename[i]["tablename"]+'</a>'
        $("#mysql_dropdowmmenu").append(test)
    }
}
//添加的下拉菜单绑定点击事件
function click_mysql_table_select(clickednum) {
    var mysql_flag=0
    var clickedname=clickednum.context.innerText
    for(i=0;i<sametablename.length;i++){
        if(clickedname == sametablename[i]["tablename"]){
            mysql_flag=1;
            break;
        }
    }
    if(mysql_flag ==0){
        display_select_mysql_table(clickedname)
    }
}
//点击显示所选择的数据库表格
function display_select_mysql_table(name) {
    var test={};
    test["tablename"]=name;
    mysql_database_number.push(test)
    sametablename.push(test)
    mysql_select_invalid()
    mysql_select_generate(name)

}
//mysql下拉菜单无效化变色
function mysql_select_invalid(){
    for(i=0;i<sametablename.length;i++){
        $("#"+sametablename[i]["tablename"]).css("background-color","#D3D3D3")
    }
}
//mysql下拉菜单选中下方产生数据库编号
function mysql_select_generate(name){
    var test='<li class="list-group-item">'+name+'</li>'
    $("#mysql_name").children().append(test)
}
//mysql提交选中数据表
function mysql_data_submit(){
    var url="/mysql/mysqlstore"
    $.ajax({
        type:"POST",
        dataType:'json',
        contentType : "application/json",
        url: url,
        data: JSON.stringify({
            "id" : datasource_mysql_id,
            "value": mysql_database_number
        }),
        success:function (data) {
            if(data== true){
                console.log("入库成功")
                $("#datasource_form").modal("hide");
            }else{
                console.log("入库失败")
            }
        }
    });
}





//点击redis入库按钮获取相关数据
function get_redis_data(id){
    var url="/redis/redisStoreQuery/"+id;
    $.ajax({
        type : "post",
        url : url,
        success : function(data) {
            $("#redis_url").val(data["url"])
            $("#redis_username").val(data["username"])
            for(i=0;i<data["table_name"].length;i++){
                invalid_redis_number.push(parseInt(data["table_name"][i].replace(/[^0-9]/ig,"")))
            }
            redis_select_invalid();
        },
        error : function(e) {
            console.log(e);
            alert("无权操作");
        }
    })
}
var datasource_redis_id;
//无效化按扭组合
var invalid_redis_number=[];
//被选中按钮组
var redis_database_number=[];
//点击下拉菜单显示所选的数据编号
function display_select_redis_number(id){
    invalid_redis_number.push(id)
    redis_database_number.push(id);
    redis_select_generate(id)
    redis_select_invalid();
}
//redis入库按钮初始化
function init_redis_store() {
    redis_database_number=[];
    invalid_redis_number=[];
    for(i=0;i<16;i++){
        $("#"+i).css("background-color","#FFFFFF")
    }
    $("#redis_number_ul").empty();
}
//redis下拉菜单无效化变色
function redis_select_invalid(){
    for(i=0;i<invalid_redis_number.length;i++){
        $("#"+invalid_redis_number[i]).css("background-color","	#D3D3D3")
    }
}
//redis下拉菜单选中下方产生数据库编号
function redis_select_generate(id){
    var test='<li class="list-group-item">'+id+'号数据库</li>'
    $("#redis_number").children().append(test)
}
//redis提交选中数据编号
function redis_data_submit(){
    var redis_num="";
    for(i=0;i<redis_database_number.length;i++){
        redis_num+=redis_database_number[i]
    }
    console.log({
        "id" : datasource_redis_id,
        "value": redis_num
    })
    var url="/redis/redisStore"
    $.ajax({
        type:"POST",
        dataType:'json',
        contentType : "application/json",
        url: url,
        data: JSON.stringify({
            "id" : datasource_redis_id,
            "value": redis_num
        }),
        success:function (data) {
            if(data== true){
                console.log("入库成功")
                $("#datasource_form").modal("hide");
            }else{
                console.log("入库失败")
            }
        }
    });
}
