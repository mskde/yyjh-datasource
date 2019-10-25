package com.mdq.yyjhservice;

import com.mdq.yyjhservice.config.jedis.JedisUtil;
import com.mdq.yyjhservice.dao.user.TUserMapper;
import com.mdq.yyjhservice.domain.auth.TUserRrole;
import com.mdq.yyjhservice.domain.datasource.TDatasource;
import com.mdq.yyjhservice.domain.user.TUser;
import com.mdq.yyjhservice.pojo.MyTUserRole;
import com.mdq.yyjhservice.service.auth.TRolePermissionService;
import com.mdq.yyjhservice.service.auth.TUserRroleService;
import com.mdq.yyjhservice.service.user.TUserService;
import org.apache.shiro.crypto.hash.Hash;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YyjhServiceApplicationTests {
    @Autowired
    private JedisUtil jedis;
    @Autowired
    private TUserMapper tuser;


    @Test
    public void contextLoads() {
//        jedis.set("mdq","123456");
//        System.out.print(jedis.get("mdq"));
    }

    @Autowired
    private JdbcTemplate jdbc;
    @Test
    public void test() {
        String sql = "select * from t";
        List<Map<String,Object>> list = jdbc.queryForList(sql);
        for(Map<String,Object> item:list) {
            System.out.println(item);
        }
    }

    @Autowired
    private TUserService tu;
    @Autowired
    private TUserRroleService tur;
    @Autowired
    private TRolePermissionService trp;
    @Test
    public void test2() {
        List<Integer> ids = new ArrayList<Integer>();
        boolean flag= trp.deleteSomeByPRId(ids,5);
        System.out.println(tuser);
    }
}
