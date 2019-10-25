package com.mdq.yyjhservice.service.datasource;

import com.mdq.yyjhservice.domain.datasource.TDatasource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TDatasourceServiceImplTest {
    @Autowired
    private TDatasourceService tds;

    @Test
    public void delTDatasourceById() {
        System.out.println(tds.delTDatasourceById(2));
    }

    @Test
    public void addTDatasource() {
        TDatasource td = new TDatasource();
        td.setEncode("qqqq");
        td.setId(111);
        td.setAuth("qqqqqqqq");
        td.setCreatetime(new Date(System.currentTimeMillis()));
        System.out.println(tds.addTDatasource(td));
    }

    @Test
    public void findTDatasourceById() {
        System.out.println(tds.findTDatasourceById(3));
    }

    @Test
    public void updTDatasourceById() {
        TDatasource td = new TDatasource();
        td.setEncode("wwww");
        td.setId(111);
        td.setAuth("wwwwwww");
        td.setCreatetime(new Date(System.currentTimeMillis()));
        System.out.println(tds.updTDatasourceById(td));
    }

    @Test
    public void getAll() {
        List<TDatasource> list = tds.getAll();
        for(TDatasource item:list){
            System.out.println(item.toString());
        }
    }
}