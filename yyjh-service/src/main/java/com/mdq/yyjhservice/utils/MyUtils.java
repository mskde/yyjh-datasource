package com.mdq.yyjhservice.utils;

import com.mdq.yyjhservice.enumeration.DatasourceEnum;
import com.mdq.yyjhservice.service.user.TUserService;
import com.mdq.yyjhservice.vo.ControllerResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class MyUtils {
//    public static boolean isAuthenticated(String sessionID, HttpServletRequest request, HttpServletResponse response){
//        boolean status = false;
//        SessionKey key = new WebSessionKey(sessionID,request,response);
//        try{
//            Session se = SecurityUtils.getSecurityManager().getSession(key);
//            Object obj = se.getAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY);
//            if(obj != null){
//                status = (Boolean) obj;
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }finally{
//            Session se = null;
//            Object obj = null;
//        }
//        return status;
//    }
    //loginid,email,tel查重
    public static ControllerResult isMyAccountRepeat(TUserService tu , Map<Object, String> datas){
        ControllerResult result=new ControllerResult();
        result.setCode(DatasourceEnum.FAIL.getCode());
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        //账号查重
        boolean isLoginIdRepeat = tu.isLoginIdRepeat(datas.get("loginid"));
        boolean isEmailRepeat = tu.isLoginIdRepeat(datas.get("email"));
        boolean isTelRepeat = tu.isLoginIdRepeat(datas.get("tel"));
        if(isLoginIdRepeat){
            //重复
            result.setPayload("账号已被注册过，请重新输入！");
        }
        else if(isEmailRepeat){
            //重复
            result.setPayload("邮箱已被注册过，请重新输入！");
        }
        else if(isTelRepeat){
            //重复
            result.setPayload("手机号已被注册过，请重新输入！");
        }
        return result;
    }
}
