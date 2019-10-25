package com.mdq.yyjhservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mdq.tools.YYJHTools;
import com.mdq.yyjhservice.domain.datasource.TDatasource;
import com.mdq.yyjhservice.enumeration.DatasourceEnum;
import com.mdq.yyjhservice.service.datasource.TDatasourceService;
import com.mdq.yyjhservice.vo.ControllerResult;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/csv")
public class CsvController {
    @Value("${file.upload.path.datasource.csv}")
    private String csv_path;
    @Autowired
    private TDatasourceService tds;

    //csv上传
    @RequestMapping("/csvUpload")
    @RequiresPermissions(value={"Insert"} , logical=Logical.AND)
    public Object csvUpload(@RequestParam("csv_upload")MultipartFile[] files,
                            @RequestParam("userId") String userId) throws IOException {
        ControllerResult result=new ControllerResult();
        result.setCode(DatasourceEnum.FAIL.getCode());
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        if(null != files && files.length != 0){
            //创建目录
            File directory=new File(csv_path);
//            System.out.println(directory);    yyjhfiles\datasource\csv
            if(!directory.exists())
                directory.mkdirs();
            //遍历
            for(MultipartFile file: files){
                String original=file.getOriginalFilename();
                String suffix=original.substring(original.lastIndexOf("."));
                if(!".csv".equalsIgnoreCase(suffix.trim()))
                    continue;
                //生成UUID
                String uuid= YYJHTools.get32UUID();
                String real_path=csv_path+ File.separator+uuid+suffix;
                File csv=new File(real_path);
                //存文件
                FileUtils.copyInputStreamToFile(file.getInputStream(),csv);
                //存库
                TDatasource csv_data=new TDatasource();
                csv_data.setDatabaseName(uuid+suffix);
                csv_data.setType("csv");
                csv_data.setUrl(real_path);
                csv_data.setAlias(original);
                csv_data.setCreatetime(new Date());
                csv_data.setUserId(userId);
                boolean flag=tds.addTDatasource(csv_data);
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

    //csv预览
    @RequestMapping("/previewCSV")
    @RequiresPermissions(value={"Select"} , logical=Logical.AND)
    public Object previewCSV(   @RequestParam("csv_upload") MultipartFile[] files,
                                @RequestParam("csv_interpret") String interpret)
            throws IOException {
        ControllerResult result=new ControllerResult();
        result.setCode(DatasourceEnum.NOFILE.getCode());
        result.setMsg(DatasourceEnum.NOFILE.getMsg());
        if(null != files && files.length != 0){
            ArrayNode datas=new ObjectMapper().createArrayNode();
            //循环file
            for(MultipartFile file:files){
                if(file.isEmpty())
                    continue;
                ObjectNode json_file  = new ObjectMapper().createObjectNode();
                ArrayNode  file_datas = new ObjectMapper().createArrayNode();
                //流操作
                InputStream in=file.getInputStream();
                Reader r_in=new InputStreamReader(in,"UTF-8");
                BufferedReader bufferedReader=new BufferedReader(r_in);
                String line=null;
                while ((line=bufferedReader.readLine()) != null){
                    int index=1;
                    String[] cols=line.split(interpret);
                    ObjectNode  json_cols=new ObjectMapper().createObjectNode();
                    for(String col : cols){//列
                        json_cols.put("col"+index,col);
                        index++;
                    }
                    file_datas.add(json_cols);//行
//                    System.out.println(file_datas);
                }
                json_file.put("file_name",file.getOriginalFilename());
                json_file.put("file_datas" ,file_datas);
                datas.add(json_file);
// System.out.println(json_file);
// {"file_name":"test_csv.csv",
// "file_datas":[{"col1":"a","col2":"b","col3":"c","col4":"d"},{"col1":"1","col2":"2","col3":"3","col4":"4"}]}
            }
            //返回数据
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
            result.setPayload(datas);
        }
        return result;
    }


    //CSV入库
    @RequestMapping("/csvToDB/{id}")
    @RequiresPermissions(value={"Insert"} , logical=Logical.AND)
    public Object csvToDB( @PathVariable Integer id) throws IOException{
        String interpret=" ";
        ControllerResult result=new ControllerResult();
        ObjectMapper om = new ObjectMapper();
        TDatasource td = tds.findTDatasourceById(id);
        String filename = td.getDatabaseName();
//        System.out.println(filename);
        File diretory = new File(csv_path);
        File[] files =diretory.listFiles();
        ArrayNode datas=new ObjectMapper().createArrayNode();
        List<String> keys = new ArrayList<String>();
        for (File file:files){
            if (filename.equals(file.getName())){
                List<String> csv_text = FileUtils.readLines(file,"UTF-8");
                for (String line:csv_text){
                    int index= 1;
                    String[] cols = line.split(interpret);
                    ObjectNode on = om.createObjectNode();
                    for (String col:cols){
                        if (keys.size()<cols.length){
                            keys.add("col"+index);
                        }
                        on.put("col"+index,col);
                        index++;
                    }
//                    System.out.println(keys);
                    datas.add(on);
                }
                break;
            }
        }
//        System.out.println(keyvalues);
// [{"col1":"e","col2":"f","col3":"g","col4":"h"},{"col1":"5","col2":"6","col3":"7","col4":"8"}]
        Map<String,Object> map = new LinkedHashMap<>();
        List objDatas =new ArrayList();

        //获取数据库的表名 去掉后缀
        String sqlName = "csv_"+filename.substring(0,filename.lastIndexOf("."));
        boolean flag = tds.aotoCreate(sqlName,keys);
        //循环获取表内数值，并保存
        for (int i =0;i<datas.size();i++){
            //jsonStr将获取的数据转字符串形式
            String jsonStr = datas.get(i).toString();
            //将json格式转成map键值对
            map = om.readValue(jsonStr,map.getClass());
            //values保存所有的map里的所有值
            List values = new ArrayList(map.values());
            objDatas.add(values);
            List coldatas = (List) objDatas.get(i);
            tds.findAndInsert(sqlName,keys,coldatas);
            System.out.println(objDatas.get(i));
        }
        if (flag){
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        return result;
    }


}
