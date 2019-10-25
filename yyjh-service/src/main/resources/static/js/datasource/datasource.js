$(document).ready(function(){
    //选择数据源按钮弹窗
    db_btns=$("#showDrivers_choosen").find("div").find(".modal-body").find("button");
/**
 *  选择数据源
 *  modal操作
 */
    //csv弹窗
    $(db_btns[0]).click(function(){
        $("#showDrivers_choosen").modal("hide");
        $("#csv_import").modal("show");
        //初始化csv内容
        init_csv_datas();
    })
    //excel弹窗
    $(db_btns[1]).click(function(){
        $("#showDrivers_choosen").modal("hide");
        $("#excel_import").modal("show");
        //初始化excel内容
        init_excel_datas();
    })
    //mysql弹窗
    $(db_btns[2]).click(function(){
        $("#showDrivers_choosen").modal("hide");
        reset_mysql_form()
        $("#mysql_import").modal("show");
    })
    //redis弹窗
    $(db_btns[3]).click(function(){
        $("#showDrivers_choosen").modal("hide");
        reset_redis_form()
        $("#redis_import").modal("show");
    })


    //关闭数据源配置导入窗口
    $(".import_close_showDrivers").click(function(){
        $("#showDrivers_choosen").modal("show");
        refresh_datasource();
    })

/**
 * csv
 * modal操作
 */
    //全部上传
    $("#csv_submit_btn").click(function(){
        if(confirm("确定上传？")) {
            csv_url("#csv_upload","/csv/csvUpload");
            if(csv_datas.originalFiles != undefined) {
                csv_datas.submit();
            }
            else{
                alert("请先选择上传文件");
                $("#csv_upload").click();
            }
        }
    })
    //全部预览
    $("#csv_preview_btn").click(function(){
        csv_url("#csv_upload", "/csv/previewCSV");
        if(csv_datas.originalFiles != undefined) {
            csv_datas.submit();
        }
        else{
            alert("请先添加上传文件");
            $("#csv_upload").click();
        }
    })
    //csv添加按钮
    $("#csv_addbtn").click(function(){
        $("#csv_upload").click();
    })
    //csv清空按钮
    $("#csv_resetbtn").click(function(){
        init_csv_datas();
    })




/**
 * csv预览
 * modal操作
 */
    //csv上传
    csv_load("#csv_upload");
    //关闭全部预览时开启csv上传页面
    $("#csv_preview_close").click(function() {
        $("#csv_import").modal("show");
    })
/**
 * excel
 * modal操作
 */
    //excel全部上传
    $("#e_submit").click(function() {
        if(confirm("确定上传？")) {
            excel_url("#excel_upload","/excel/excelUpload");
            if(excel_datas.originalFiles != undefined) {
                //上传文件不为空
                excel_datas.submit();
            }
            else{
                alert("请先选择上传文件");
                $("#excel_upload").click();
            }
        }
    })
    //excel全部预览
    $("#e_preview").click(function() {
        excel_url("#excel_upload", "/excel/previewExcel");
        if(excel_datas.originalFiles != undefined) {
            excel_datas.submit();
        }
        else{
            alert("请先添加上传文件");
            $("#excel_upload").click();
        }
    })

    //excel清空按钮
    $("#excel_resetbtn").click(function(){
        init_excel_datas();
    })
    //excel添加按钮
    $("#excel_addbtn").click(function(){
        $("#excel_upload").click();
    })
    /**
     * excel预览
     * modal操作
     */
    //excel上传
    excel_load("#excel_upload");
    //关闭excel预览窗口
    $("#excel_preview_close").click(function(){
        $("#excel_import").modal(true);
    })
    /**
     * excel配置
     * modal操作
     */
    //excel配置文件退出按钮
    $("#excel_interpret_close").click(function(){
        $("#excel_interpret").modal("hide");
        $("#excel_import").modal("show");
    })




    //mysql重置表单
    $("#mysql_reset").click(function(){
        reset_mysql_form()
    })

    //mysql提交表单
    $("#mysql_submit").click(function(){
        submit_mysql_form()
    })

    //redis重置表单
    $("#redis_reset").click(function(){
        reset_redis_form()
    })

    //redis提交表单
    $("#redis_submit").click(function(){
        submit_redis_form()
    })


});