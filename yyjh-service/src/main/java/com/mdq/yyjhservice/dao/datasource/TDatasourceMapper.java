package com.mdq.yyjhservice.dao.datasource;

import com.mdq.yyjhservice.domain.datasource.TDatasource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TDatasourceMapper {
    int delTDatasourceById(Integer id);

    int addTDatasource(TDatasource record);

    int updTDatasourceById(TDatasource record);

    int delBatchTDatasource(int[] arr);


    List<TDatasource> getAll();

    TDatasource findTDatasourceById(Integer id);

    //搜索框搜素
    List<TDatasource> getTDatasourceByAll(
            @Param("info")String info, @Param("selectType")String selectType,
            @Param("beginNumber")int beginNumber,@Param("limit")int limit);
    int getCountByAll(@Param("info")String info, @Param("selectType")String selectType);
    List<TDatasource> getTDatasourceByType(
            @Param("selectType")String selectType,
            @Param("beginNumber")int beginNumber,@Param("limit")int limit);
    int getCountByType(@Param("selectType")String selectType);
    List<TDatasource> getTDatasourceByOtherFields(
            @Param("info")String info,
            @Param("beginNumber")int beginNumber,@Param("limit")int limit);
    int getCountByOtherFields(@Param("info")String info);


    //分页查询
    List<TDatasource> getAllByPage(Map<String, Integer> map );
    //当前所有信息数量
    int getCount();



    //    查询当前数据库已有表名
    List<Map<String,String>> getTableNames();

    //    动态建表
    int aotoCreate(@Param("name")String name, @Param("filename")List<String> filename);
    //    动态插入数据
    int findAndInsert(@Param("name")String name,@Param("filename")List<String> filename,@Param("coldatas")List coldatas);
    //    本机数据库仪表
    int moveTables(@Param("tablename")String tablename,@Param("dbname")String dbname);

}