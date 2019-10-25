package com.mdq.yyjhservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdq.tools.StringChange;
import com.mdq.yyjhservice.config.jedis.JedisUtil;
import com.mdq.yyjhservice.domain.datasource.TDatasource;
import com.mdq.yyjhservice.service.datasource.TDatasourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    private TDatasourceService tds;
    @Autowired
    private JedisUtil jedisUtil;
    //导入
    @PostMapping("/redisUpload")
    public Object mysqlUpload(@RequestBody String json ) throws IOException {
        ObjectMapper om=new ObjectMapper();
        TDatasource mysql_data=om.readValue(json, TDatasource.class);
        mysql_data.setCreatetime(new Date());
        mysql_data.setType("redis");
        return tds.addTDatasource(mysql_data);
    }

    //入库查询redis信息操作
    @RequestMapping("/redisStoreQuery/{id}")
    public Object redisStoreQuery(@PathVariable Integer id){
        TDatasource tDatasource=tds.findTDatasourceById(id);
        Map<String,Object> redisMap=new HashMap<String, Object>();
        String redisMapUrl=tDatasource.getUrl()+":"+String.valueOf(tDatasource.getPort());
        redisMap.put("url",redisMapUrl);
        redisMap.put("username",tDatasource.getUsername());
        List<String> redisMapTableNames=new ArrayList<String>();
        for (Map<String,String> map : tds.getTableNames()){
            if(map.get("table_name").contains("redis")
                    && map.get("table_name").contains(tDatasource.getUsername())){
                redisMapTableNames.add(map.get("table_name"));
            }
        }
        redisMap.put("table_name",redisMapTableNames);
        return redisMap;
    }

    //依据前端返回的redis 库号以及 datasource的id进行入库操作；
    @RequestMapping("/redisStore")
    public Object redisStoreDatabase(@RequestBody String json) throws IOException {
        ObjectMapper om=new ObjectMapper();
        Map<String,Object> redisMap=new HashMap<>();
        redisMap=om.readValue(json,redisMap.getClass());

        String result=(String) redisMap.get("value");
        Integer[] num=new Integer[result.length()];
        for (int i = 0; i < result.length(); i++) {
            System.out.println(result.substring(i, i+1));
            num[i]=Integer.valueOf(result.substring(i, i+1));
        }

        TDatasource tDatasource=tds.findTDatasourceById((Integer) redisMap.get("id"));
        String username=tDatasource.getUsername();
        //创建连接池对象
        JedisPool jedispool = new JedisPool(tDatasource.getUrl(),tDatasource.getPort());
        //从连接池中获取一个连接
        Jedis jedis = jedispool.getResource();
        jedis.auth(tDatasource.getPassword());
        for(int i=0;i<num.length;i++){
            String database_num=String.valueOf(num[i]);
            //表名
            String tableName="redis_"+username+"_"+database_num;
            jedis.select(num[i]);
            //使用jedis操作redis
            List<String> filename=new ArrayList<String>();
            List<String> coldatas=new ArrayList<String>();
            for(String s: jedis.keys("*")){
                if(jedis.type(s).equals("string")){
                    String string1=jedis.get(s);
                    filename.add(s);
                    coldatas.add(string1);
                }else if(jedis.type(s).equals("list")){
                    String string2 = String.join("-",jedis.lrange(s,0,-1));
                    filename.add(s);
                    coldatas.add(string2);
                }else if(jedis.type(s).equals("set")){
                    String string3 = String.join("-",jedis.smembers(s));
                    filename.add(s);
                    coldatas.add(string3);
                }else if(jedis.type(s).equals("zset")){
                    String string4 = String.join("-",jedis.zrange(s,0,-1));
                    filename.add(s);
                    coldatas.add(string4);
                }else if(jedis.type(s).equals("hash")){
                    String string5= StringChange.getMapToString(jedis.hgetAll(s));
                    filename.add(s);
                    coldatas.add(string5);
                }
            }
            //建表 插入信息
            tds.aotoCreate(tableName,filename);
            tds.findAndInsert(tableName,filename,coldatas);

        }

        //使用完毕 ，关闭连接，连接池回收资源
        jedis.close();
        //关闭连接池
        jedispool.close();
        return true;
    }



}
