package com.mdq.yyjhservice.service.datasource;

import com.mdq.yyjhservice.domain.datasource.TDatasource;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TDatasourceService {
    boolean delTDatasourceById(Integer id);

    boolean addTDatasource(TDatasource record);

    TDatasource findTDatasourceById(Integer id);

    boolean delBatchTDatasource(int[] arr);


    List<TDatasource> getAll();

    boolean updTDatasourceById(TDatasource record);



    List<TDatasource> getTDatasourceByAll(String info, String selectType, int beginNumber, int limit);
    int getCountByAll(@Param("info") String info, @Param("selectType") String selectType);
    List<TDatasource> getTDatasourceByType(String selectType, int beginNumber, int limit);
    int getCountByType(@Param("selectType") String selectType);
    List<TDatasource> getTDatasourceByOtherFields(String info, int beginNumber, int limit);
    int getCountByOtherFields(@Param("info") String info);



    List<TDatasource> getAllByPage(Map<String, Integer> map);

    int getCount();



    List<Map<String,String>> getTableNames();

    boolean aotoCreate(String name, List<String> filename);

    boolean findAndInsert(String name, List<String> filename, List coldatas);

    boolean moveTables(String tablename, String dbname);

}