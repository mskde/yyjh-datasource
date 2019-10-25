package com.mdq.yyjhservice.controller;

import com.mdq.yyjhservice.enumeration.DatasourceEnum;
import com.mdq.yyjhservice.service.auth.TPermissionService;
import com.mdq.yyjhservice.service.auth.TRolePermissionService;
import com.mdq.yyjhservice.service.auth.TRoleService;
import com.mdq.yyjhservice.service.auth.TUserRroleService;
import com.mdq.yyjhservice.service.myService.AuthorityManageService;
import com.mdq.yyjhservice.service.user.TUserService;
import com.mdq.yyjhservice.utils.MyUtils;
import com.mdq.yyjhservice.vo.ControllerResult;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ToPage {
    //datasource
    @RequestMapping("/toDatasource")
    public String toDatasource(){
        return "datasource/datasource";
    }
    //登陆界面
    @RequestMapping("/login")
    public String toLogin(){
        return "login/login";
    }
    //errorPage
    @RequestMapping("/toErrorPage")
    public String toErrorPage(){
        return "login/errorPage";
    }


    @RequestMapping("/toDateFormat")
    public String toDateFormat(){
        return "date/dateformat";
    }
}
