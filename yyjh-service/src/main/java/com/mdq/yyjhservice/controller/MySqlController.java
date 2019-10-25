package com.mdq.yyjhservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mdq.yyjhservice.domain.datasource.TDatasource;
import com.mdq.yyjhservice.enumeration.DatasourceEnum;
import com.mdq.yyjhservice.service.datasource.TDatasourceService;
import com.mdq.yyjhservice.vo.ControllerResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

@RestController
@RequestMapping("/mysql")
public class MySqlController {
    @Autowired
    private TDatasourceService tds;

    //上传
    @PostMapping("/mysqlUpload")
    public Object mysqlUpload(@RequestBody String json ) throws IOException {
        ObjectMapper om=new ObjectMapper();
        TDatasource mysql_data=om.readValue(json, TDatasource.class);
        mysql_data.setCreatetime(new Date());
        mysql_data.setType("mysql");
        return tds.addTDatasource(mysql_data);
    }


    //sql数据入库
    @PostMapping("/mysqlToDb/{id}")
    public Object mysqlToDb(@PathVariable Integer id) throws IOException {
        ControllerResult result=new ControllerResult();
        result.setCode(DatasourceEnum.NOFILE.getCode());
        result.setMsg(DatasourceEnum.NOFILE.getMsg());
        TDatasource td = tds.findTDatasourceById(id);
        String url = td.getUrl();
        Integer port = td.getPort();
        String dbname = td.getDatabaseName();
        String sqldbname = "'"+dbname+"'";//给dbname添加''符号，以适应sql语句
        String username = td.getUsername();
        String pwd = td.getPassword();
        Statement sts = null;
        Statement sts2 = null;
        ArrayNode datas=new ObjectMapper().createArrayNode();

        ObjectNode json_file  = new ObjectMapper().createObjectNode();
        ArrayNode  file_datas = new ObjectMapper().createArrayNode();
        ArrayNode file_datas_source = new ObjectMapper().createArrayNode();
        ArrayNode file_datas_same = new ObjectMapper().createArrayNode();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String urlline = "jdbc:mysql://"+url+":"+port+"/"+dbname;
            Connection conn = DriverManager.getConnection(urlline,username,pwd);
            String sql = "select table_name from information_schema.tables where table_schema="+sqldbname;
            String sqlsource = "select table_name from information_schema.tables where table_schema='yyjh_datasource'";
            sts = conn.createStatement();
            sts2 = conn.createStatement();
            ResultSet rs= sts.executeQuery(sql);
            ResultSet rssource = sts2.executeQuery(sqlsource);
            int indexsouce = 1;
            while (rssource.next()){
                String tablesource = rssource.getString("table_name");
                String[] sourcecols = tablesource.split(",");
                ObjectNode json_cols_source = new ObjectMapper().createObjectNode();
                for (String col_source :sourcecols){
                    json_cols_source.put("sourceTablename"+indexsouce,col_source);
                    indexsouce++;
                }
                file_datas_source.add(json_cols_source);
            }
//            System.out.println(file_datas_source);

            int index = 1;
            while (rs.next()){
                String table = rs.getString("table_name");
//                list1.add(table);
//                System.out.println(table);
                String[] cols = table.split(",");
                ObjectNode  json_cols=new ObjectMapper().createObjectNode();

                for(String col : cols){//列
                    json_cols.put("tablename",col);
                    index++;
                }
                file_datas.add(json_cols);//行
            }
//            System.out.println(file_datas);   [{"tablename1":"city"},{"tablename2":"country"},{"tablename3":"countrylanguage"},{"tablename4":"name12"}]
            json_file.put("mysql_ip",url);
            json_file.put("mysql_port",port);
            json_file.put("mysql_dbname",dbname);
            json_file.put("mysql_tablename",file_datas);
//            datas.add(json_file);
//            System.out.println(json_file);

//            System.out.println(datas);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ObjectMapper om = new ObjectMapper();
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> map_source =new HashMap<>();
        List objDatas =new ArrayList();
        List objDatas_source = new ArrayList();

        for (int a=0;a<file_datas.size();a++){
            String jsonStr_source = file_datas.get(a).toString();
            map_source = om.readValue(jsonStr_source,map_source.getClass());
            List values_source = new ArrayList(map_source.values());
            objDatas_source.add(values_source);
        }
//        System.out.println(objDatas_source);

        List tables_source = new ArrayList();
        for (int b = 0;b<objDatas_source.size();b++){
            List tablenames_source = (List) objDatas_source.get(b);
            String tablename_source = (String) tablenames_source.get(0);
            tables_source.add(tablename_source);
        }

//        System.out.println(tables_source);

        for (int i =0;i<file_datas_source.size();i++){
            //jsonStr将获取的数据转字符串形式
            String jsonStr = file_datas_source.get(i).toString();
            //将json格式转成map键值对
            map = om.readValue(jsonStr,map.getClass());
//            System.out.println(map);
            //values保存所有的map里的所有值
            List values = new ArrayList(map.values());
//            System.out.println(values);
            objDatas.add(values);
        }
//        System.out.println(objDatas);
        //tables : 目标数据库的所有表名构成List
        List tables = new ArrayList();
        for (int j = 0;j<objDatas.size();j++){
            List tablenames = (List) objDatas.get(j);
            String tablename = (String) tablenames.get(0);
            tables.add(tablename);
        }
//        System.out.println(tables);
// tables是yyjh数据库表名

        List sametablename = new ArrayList();
        for (int i=0;i<tables_source.size();i++){
            String string1 = tables_source.get(i).toString();
            for (int j =0;j<tables.size();j++){
                String string2 = tables.get(j).toString();
                if (string1.equals(string2)){
                    sametablename.add(string1);
                }
            }
        }
        for (int i= 0;i<sametablename.size();i++){
            ObjectNode  json_cols_same=new ObjectMapper().createObjectNode();
            json_cols_same.put("tablename",sametablename.get(i).toString());
            file_datas_same.add(json_cols_same);
        }



        json_file.put("sametablename",file_datas_same);
        datas.add(json_file);

        //返回数据
        result.setCode(DatasourceEnum.SUCCESS.getCode());
        result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        result.setPayload(datas);
        return result;
    }


    @RequestMapping("/mysqlstore")
    public Object mysqlstore(@RequestBody String json) throws IOException {
        System.out.println(json);
        ObjectMapper om=new ObjectMapper();
        Map<String,Object> jsonMap=new HashMap<String, Object>();
        jsonMap=om.readValue(json,jsonMap.getClass());
        Integer datasourceId= (Integer) jsonMap.get("id");
        TDatasource tDatasource=tds.findTDatasourceById(datasourceId);
        String dbname=tDatasource.getDatabaseName();
        List<Map<String,String>> ls= (List<Map<String, String>>) jsonMap.get("value");
        for(int i=0;i<ls.size();i++){
            tds.moveTables(ls.get(i).get("tablename"),dbname);
        }
        return  true;
    }

}
