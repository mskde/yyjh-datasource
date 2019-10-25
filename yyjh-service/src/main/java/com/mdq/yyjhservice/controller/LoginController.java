package com.mdq.yyjhservice.controller;

import com.mdq.tools.MD5Util;
import com.mdq.tools.YYJHTools;
import com.mdq.yyjhservice.domain.user.TUser;
import com.mdq.yyjhservice.enumeration.DatasourceEnum;
import com.mdq.yyjhservice.pojo.ChangePwdFormData;
import com.mdq.yyjhservice.service.myService.RegisteService;
import com.mdq.yyjhservice.service.user.TUserService;
import com.mdq.yyjhservice.vo.ControllerResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    //密码后缀（安全性）
    public static final String MD5WORDS = "wjs";
    //登陆时长（cookie保存时间）
//    public static final int COOKIETIME = 30;
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private TUserService tu;
    @Autowired
    private RegisteService registe;

    /**
     * 登陆
     */
    @RequestMapping("/login_form")
    public Object login_form(String user, String pwd){
        ControllerResult result=new ControllerResult();
        result.setCode(DatasourceEnum.FAIL.getCode());
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        Jedis jedis = jedisPool.getResource();
        if(null != user){
            //根据账号获取loginId(账号可能是loginId,邮箱，手机号)
            String loginid = tu.selectLoginIdByUser(user);
            if(null != loginid){
                //根据账号查询密码
                String init_password = tu.getPwdByLoginid(loginid);
                String password = init_password;
                //md5双重加密密码（pwd+"wjs"）
                password = MD5Util.getStrMD5(password+MD5WORDS);
                password = MD5Util.getStrMD5(password);
                if(null != pwd && pwd.equals(password)) {
                    String redis_user = jedis.get(loginid);
                    if(null == redis_user){
                        //未登陆过
                        //登陆成功
                        //添加用户认证信息
                        Subject subject = SecurityUtils.getSubject();
                        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginid,init_password);
                        //进行验证，这里可以捕获异常，然后返回对应信息
                        subject.login(usernamePasswordToken);
                        //方法一:将账号密码存入cookie（不安全）
//                        Cookie userCookie = new Cookie("user",user);
//                        userCookie.setMaxAge(COOKIETIME);
//                        Cookie pwdCookie = new Cookie("pwd",init_password);
//                        pwdCookie.setMaxAge(COOKIETIME);
//                        response.addCookie(userCookie);
//                        response.addCookie(pwdCookie);
                        //方法二：将账号存入redis
                        jedis.set(loginid,loginid);
                        jedis.close();
                        result.setCode(DatasourceEnum.SUCCESS.getCode());
                        result.setMsg(DatasourceEnum.SUCCESS.getMsg());
                    }
                    else if(!loginid.equals(redis_user)){
                        jedis.del(loginid);
                        result.setPayload("系统出错！\n请稍后登录！");
                    }
                    else{
                        //账号已登陆
                        //账号防登录
                        result.setPayload("账号已登陆！\n即将跳转！");
                    }
                }
                else{
                    result.setPayload("密码有误！请核对账号信息！");
                }
            }
            else{
                //账号信息有误！
                result.setPayload("该账号尚未注册！");
            }
        }
        else{
            result.setPayload("请输入账号！");
        }
        return result;
    }
    /**
     * 根据loginid获取用户头像
     */
    @RequestMapping("/getUserHeadImg")
    public Object getUserHeadImg(String loginid){
        ControllerResult result=new ControllerResult();
        result.setCode(DatasourceEnum.FAIL.getCode());
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        String head_path = tu.getUserimgByLoginid(loginid);
        if(null != head_path){
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
            result.setPayload(head_path);
        }
        return result;
    }
    /**
     * 注册
     */
    @RequestMapping("/registe_form")
    public Object registe_form(TUser tuser, @RequestParam(value="r_headfile",required=false)MultipartFile file) throws IOException {
        ControllerResult result=new ControllerResult();
        result = registe.registe(tuser,file);
        if(null == result){
            result.setCode(DatasourceEnum.FAIL.getCode());
            result.setMsg(DatasourceEnum.FAIL.getMsg());
        }
        return result;
    }
    /**
     * 用户名是否重复
     */
    @RequestMapping("/isLoginIdRepeat")
    public Object isLoginIdRepeat(@RequestParam("loginid") String loginid){
        ControllerResult result=new ControllerResult();
        result.setCode(DatasourceEnum.FAIL.getCode());
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        boolean flag = tu.isLoginIdRepeat(loginid);
        if(flag){
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        return result;
    }
    /**
     * 邮箱是否重复
     */
    @RequestMapping("/isEmailRepeat")
    public Object isEmailRepeat(String email){
        ControllerResult result=new ControllerResult();
        result.setCode(DatasourceEnum.FAIL.getCode());
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        boolean flag = tu.isEmailRepeat(email);
        if(flag){
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        return result;
    }
    /**
     * 手机号是否重复
     */
    @RequestMapping("/isTelRepeat")
    public Object isTelRepeat(String tel){
        ControllerResult result=new ControllerResult();
        result.setCode(DatasourceEnum.FAIL.getCode());
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        boolean flag = tu.isTelRepeat(tel);
        if(flag){
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        return result;
    }
    /**
     * 找回密码
     * 验证邮箱
     * 根据邮箱发送邮件
     */
    @RequestMapping("/getPwd")
    public Object getPwd(String loginId , String email) throws Exception {
        ControllerResult result=new ControllerResult();
        result.setCode(DatasourceEnum.FAIL.getCode());
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        //验证账号是否正确
        Map<Object,String> datas = tu.getEmail_and_PasswordByLoginid(loginId);
        System.out.println(null == datas);
        System.out.println(datas);
        if(null == datas){
            result.setPayload("用户账号不存在！");
        }
        else{
            //验证邮箱是否正确
            if(datas.get("email")!=null && datas.get("email").equals(email)){
                Map<Object,String> newdatas = new HashMap<Object,String>();
                newdatas.put("email",email);
                newdatas.put("data",datas.get("password"));
                //邮箱正确，发送邮件
                YYJHTools.sendEmail(newdatas);
                result.setCode(DatasourceEnum.SUCCESS.getCode());
                result.setMsg(DatasourceEnum.SUCCESS.getMsg());
            }
            else{
                result.setPayload("用户邮箱填写有误！");
            }
        }
        return result;
    }
    /**
     * 发送验证码给用户邮箱
     */
    @RequestMapping("/changePwd_getCode")
    public Object changePwd_getCode(String loginId,String email,String code) throws Exception {
        ControllerResult result=new ControllerResult();
        result.setCode(DatasourceEnum.FAIL.getCode());
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        //判断账号和邮箱是否一致
        String thisemail = tu.getEmailByLoginid(loginId);
        if(null != thisemail && thisemail.equals(email)) {
            Map<Object,String> datas = new HashMap<Object,String>();
            datas.put("email",email);
            datas.put("data",code);
            //邮箱正确，发送邮件
            YYJHTools.sendEmail(datas);
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        else{
            result.setPayload("用户账号与邮箱不一致！");
        }
        return result;
    }
    /**
     * 修改密码
     */
    @RequestMapping("/changePwd_form")
    public Object changePwd_form(ChangePwdFormData datas){
        ControllerResult result=new ControllerResult();
        result.setCode(DatasourceEnum.FAIL.getCode());
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        //判断账号与原始密码是否一致
        String pwd = tu.getPwdByLoginid(datas.getLoginId());
        if(null == pwd){
            //账号不存在
            result.setPayload("用户账号不存在！");
        }
        else if(!pwd.equals(datas.getOldpassword())){
            //账号与原始密码不一致
            result.setPayload("用户密码有误！");
        }
        else{
            //md5双重解密+后缀wjs解密验证码
            String mycode = MD5Util.getStrMD5(datas.getCode().toLowerCase()+MD5WORDS);
            mycode        = MD5Util.getStrMD5(mycode);
            //判断验证码是否正确
            if(null != mycode && mycode.equals(datas.getRecode())){
                //验证通过
                //修改密码
                boolean flag = tu.updatePasswordByLoginid(datas.getLoginId(),datas.getNewpassword());
                if(flag){
                    //修改密码成功
                    result.setCode(DatasourceEnum.SUCCESS.getCode());
                    result.setMsg(DatasourceEnum.SUCCESS.getMsg());
                }
                else{
                    //修改密码失败
                    result.setPayload("网络出错！");
                }
            }
            else{
                //验证码有误
                result.setPayload("验证码有误！");
            }
        }
        return result;
    }
}
