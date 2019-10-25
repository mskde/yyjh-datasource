package com.mdq.yyjhservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdq.tools.ExcelOper;
import com.mdq.tools.YYJHTools;
import com.mdq.yyjhservice.domain.datasource.TDatasource;
import com.mdq.yyjhservice.enumeration.DatasourceEnum;
import com.mdq.yyjhservice.service.datasource.TDatasourceService;
import com.mdq.yyjhservice.utils.ExcelUtils;
import com.mdq.yyjhservice.vo.ControllerResult;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/excel")
public class ExcelController {
    @Value("${file.upload.path.datasource.excel}")
    private String excel_path;
    @Autowired
    private TDatasourceService tds;

    //上传
    @PostMapping("/excelUpload")
    @RequiresPermissions(value={"Insert"} , logical= Logical.AND)
    public Object excelUpload(@RequestParam("excel_upload")    MultipartFile[] files,
                              @RequestParam("excel_interpret") String excel_interpret,
                              @RequestParam("userId") String userId
    ) throws IOException {
        ControllerResult result=new ControllerResult();
        result.setCode(DatasourceEnum.FAIL.getCode());
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        if(null != files && files.length != 0){
            //创建目录
            File directory=new File(excel_path);
            if(!directory.exists())
                directory.mkdirs();
            //遍历
            for(MultipartFile file: files){
                String original=file.getOriginalFilename();
                String suffix=original.substring(original.lastIndexOf("."));
                if(!(".xls".equalsIgnoreCase(suffix.trim()) || ".xlsx".equalsIgnoreCase(suffix.trim())) ){
                    continue;
                }
                //生成UUID
                String uuid= YYJHTools.get32UUID();
                String real_path=excel_path+File.separator+uuid+suffix;
                File excel=new File(real_path);
                //存文件
                FileUtils.copyInputStreamToFile(file.getInputStream(),excel);
                //存库
                TDatasource excel_data=new TDatasource();
                excel_data.setDatabaseName(uuid+suffix);
                excel_data.setType("excel");
                excel_data.setUrl(real_path);
                excel_data.setAlias(original);
                ObjectMapper om=new ObjectMapper();
                //存encode
                List<Map<String,Object>> excel_interprets = om.readValue(excel_interpret,new TypeReference<List<Map<String,Object>>>(){});
                for(Map<String,Object> excel_in : excel_interprets){
                    if(original.equals(excel_in.get("filename").toString())){
                        String encode=om.writeValueAsString(excel_in);
                        excel_data.setEncode(encode);
                        break;
                    }
                }
                excel_data.setUserId(userId);
                excel_data.setCreatetime(new Date());
                boolean flag = tds.addTDatasource(excel_data);
                if(flag){
                    result.setCode(DatasourceEnum.SUCCESS.getCode());
                    result.setMsg(DatasourceEnum.SUCCESS.getMsg());
                }else{
                    result.setCode(DatasourceEnum.FAIL.getCode());
                    result.setMsg(DatasourceEnum.FAIL.getMsg());
                }
            }
        }
        return result;
    }

    //预览
    @PostMapping("/previewExcel")
    @RequiresPermissions(value={"Select"} , logical=Logical.AND)
    public Object previewEXCEL( @RequestParam("excel_upload") MultipartFile[] param_files) throws IOException {
        ControllerResult result=new ControllerResult();
        result.setCode(DatasourceEnum.NOFILE.getCode());
        result.setMsg(DatasourceEnum.NOFILE.getMsg());
        if(null != param_files && param_files.length != 0){
            List<Map<String,Object>> datas= ExcelOper.translateExcels(param_files);
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
            result.setPayload(datas);
        }
        return result;
    }

    //入库
    @RequestMapping("/excelStore/{id}")
    @RequiresPermissions(value={"Insert"} , logical=Logical.AND)
    public Object excelStore(@PathVariable Integer id) throws Exception {
        TDatasource tDatasource=tds.findTDatasourceById(id);
        String tableName=tDatasource.getDatabaseName();
        tableName=tableName.substring(0,tableName.lastIndexOf("."));
        tableName="excel_"+tableName;
        File filess = new File("d:"+File.separator+"CODE"+File.separator +"yyjh-datasource"+File.separator+tDatasource.getUrl());
        String filenames = tDatasource.getDatabaseName();
        File diretory = new File(excel_path);
        File[] files =diretory.listFiles();
        List<String> keys = new ArrayList<String>();
        for (File file:files){
            if (filenames.equals(file.getName())){
                filess=file;
                break;
            }
        }

        int index=ExcelUtils.getSheetNum(filess);
        for(int m=0;m<index;m++){
            List<Map<String, Object>> datas = new ArrayList<Map<String,Object>>();
            datas = ExcelUtils.findDate(filess,m);//解析

            //总数据
            List<List<String>> sumColdatas=new ArrayList<List<String>>();
            //列名
            List<String> filename=new ArrayList<String>();
            //列长度
            int filenameLength=0;

            for(int i=0;i<datas.size();i++){
                List<String> coldataOne=new ArrayList<String>();
                Map<String, Object> data = datas.get(i);
                filenameLength=data.size();
                Collection<Object> ds = data.values();
                //获取数据
                for(Object d : ds){
                    coldataOne.add(String.valueOf(d));
                }
                if (!coldataOne.isEmpty()){
                    sumColdatas.add(coldataOne);
                }
            }
            //确定列名
            for(int i=0;i<filenameLength;i++){
                filename.add("col"+String.valueOf(i));
            }

            System.out.println(sumColdatas);


            //过滤配置
            // 内容
            String encode=tDatasource.getEncode();
            ObjectMapper om=new ObjectMapper();
            Map<String,Object> tableFilter=new HashMap<String, Object>();
            tableFilter=om.readValue(encode,tableFilter.getClass());
            if((boolean)tableFilter.get("isfilter")){
                LinkedHashMap<String,Object>  table_interpret_filter=
                        (LinkedHashMap<String, Object>) tableFilter.get("interpret_filter");

                //按行过滤
                int start_line=Integer.parseInt((String) table_interpret_filter.get("start_line"));
                int end_line=Integer.parseInt((String) table_interpret_filter.get("end_line"));
                for(int i=sumColdatas.size()-1;i>=0;i--){
                    if( i>(end_line-1) || i<(start_line-1) ){
                        sumColdatas.remove(i);
                    }
                }

                String index_logic= (String) table_interpret_filter.get("index_logic");
                List<Map<String,String>> table_interpret_filter_filterString=
                        (List<Map<String, String>>) table_interpret_filter.get("index_string");
                //根据已有过滤条件过滤内容
                for(int i=0; i<sumColdatas.size();i++){
                    int filterFlag=0;
                    if(index_logic.equals("or")){
                        for(int j=0;j<table_interpret_filter_filterString.size();j++){
                            Map<String, String> filterOne =table_interpret_filter_filterString.get(j);
                            if(ExcelUtils.isAccordFilter(sumColdatas.get(i),filterOne)){
                                filterFlag=1;
                                break;
                            }
                        }
                    }else if(index_logic.equals("and")){
                        for(int j=0;j<table_interpret_filter_filterString.size();j++){
                            Map<String, String> filterOne =table_interpret_filter_filterString.get(j);
                            if(!ExcelUtils.isAccordFilter(sumColdatas.get(i),filterOne)){
                                filterFlag=-1;
                                break;
                            }
                        }
                    }

                    if(index_logic.equals("or") && filterFlag != 1){
                        sumColdatas.remove(i);
                        i--;
                    }
                    if(index_logic.equals("and") && filterFlag == -1){
                        sumColdatas.remove(i);
                        i--;
                    }
                }
            }

            String tableNameSheet=tableName+"_"+String.valueOf(m);
            //建表
            tds.aotoCreate(tableNameSheet,filename);
            //插入信息
            int sumrows=sumColdatas.size();
            for(int i=0;i<sumrows;i++){
                tds.findAndInsert(tableNameSheet,filename,sumColdatas.get(i));
            }
        }

        return true;


    }

}