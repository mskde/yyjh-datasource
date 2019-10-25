package com.mdq.yyjhservice.service.datasource;

import com.mdq.yyjhservice.dao.datasource.TDatasourceMapper;
import com.mdq.yyjhservice.domain.datasource.TDatasource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Primary
@Slf4j
@Transactional
public class TDatasourceServiceImpl implements TDatasourceService {
    @Autowired
    private TDatasourceMapper tdm;

    @Override
    public boolean delTDatasourceById(Integer id){
        int count = tdm.delTDatasourceById(id);
        return count>0?true:false;
    }

    @Override
    public boolean addTDatasource(TDatasource record) {
        int count = tdm.addTDatasource(record);
        return count>0?true:false;
    }

    @Override
    public boolean updTDatasourceById(TDatasource record) {
        int count = tdm.updTDatasourceById(record);
        return count>0?true:false;
    }

    @Override
    public boolean delBatchTDatasource(int[] arr) {
        int count = tdm.delBatchTDatasource(arr);
        return count>0?true:false;
    }

    @Override
    public TDatasource findTDatasourceById(Integer id) {
        return tdm.findTDatasourceById(id);
    }



    @Override
    public List<TDatasource> getTDatasourceByAll(String info, String selectType, int beginNumber, int limit) {
        return tdm.getTDatasourceByAll(info,selectType,beginNumber,limit);
    }

    @Override
    public int getCountByAll(String info, String selectType) {
        return tdm.getCountByAll(info,selectType);
    }

    @Override
    public List<TDatasource> getTDatasourceByType(String selectType, int beginNumber, int limit) {
        return tdm.getTDatasourceByType(selectType,beginNumber,limit);
    }

    @Override
    public int getCountByType(String selectType) {
        return tdm.getCountByType(selectType);
    }

    @Override
    public List<TDatasource> getTDatasourceByOtherFields(String info, int beginNumber, int limit) {
        return tdm.getTDatasourceByOtherFields(info,beginNumber,limit);
    }

    @Override
    public int getCountByOtherFields(String info) {
        return tdm.getCountByOtherFields(info);
    }


    @Override
    public List<TDatasource> getAll() {
        return tdm.getAll();
    }




    @Override
    public List<TDatasource> getAllByPage(Map<String, Integer> map) {
        return tdm.getAllByPage(map);
    }

    @Override
    public int getCount() {
        return tdm.getCount();
    }




    @Override
    public List<Map<String, String>> getTableNames() {
        return tdm.getTableNames();
    }

    @Override
    public boolean aotoCreate(String name, List<String> filename) {
        int count = tdm.aotoCreate(name,filename);
        return count>0?true:false;
    }

    @Override
    public boolean findAndInsert(String name,List<String> filename, List coldatas) {
        int count = tdm.findAndInsert(name,filename,coldatas);
        return count>0?true:false;
    }

    @Override
    public boolean moveTables(String tablename, String dbname) {
        int count = tdm.moveTables(tablename,dbname);
        return count>0?true:false;
    }
}
