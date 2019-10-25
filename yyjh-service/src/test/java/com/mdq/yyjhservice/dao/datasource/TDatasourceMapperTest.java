package com.mdq.yyjhservice.dao.datasource;

import com.mdq.yyjhservice.domain.datasource.TDatasource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TDatasourceMapperTest {
    @Autowired
    private TDatasourceMapper td;

    @Test
    public void delTDatasourceById() {
        td.delTDatasourceById(1);
    }

    @Test
    public void addTDatasource() {
        TDatasource t = new TDatasource();
        t.setAlias("zzxczxcxc");
        t.setCreatetime(new Date(System.currentTimeMillis()));
        t.setId(3);
        t.setEncode("azxczxczxscasc");
        td.addTDatasource(t);
    }

    @Test
    public void findTDatasourceById() {
        td.findTDatasourceById(1);
    }

    @Test
    public void updTDatasourceById() {
        TDatasource t = new TDatasource();
        t.setAlias("mdq");
        t.setCreatetime(new Date(System.currentTimeMillis()));
        t.setId(2);
        t.setEncode("mdq");
        td.updTDatasourceById(t);
    }

    @Test
    public void getAll() {
        List<TDatasource> list = td.getAll();
        System.out.println(list);
    }
}