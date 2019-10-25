package com.mdq.tools;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class YYJHTools {
    public static final String DEFAULT_FORMAT_PARAM = "yyyy-MM-dd HH:mm:ss";

    //根据格式化参数求格式化日期字符串
    public static Date parseStrToDate(String date, String param) throws ParseException {
        if(null == param || "".equals(param.trim())){
            param = YYJHTools.DEFAULT_FORMAT_PARAM;
        }
        DateFormat df = new SimpleDateFormat(param);
        return df.parse(date);
    }

    //根据格式化参数求格式化日期字符串
    public static String formatDateToStr(Date date, String param) throws ParseException {
        if(null == param || "".equals(param.trim())){
            param = YYJHTools.DEFAULT_FORMAT_PARAM;
        }
        DateFormat df = new SimpleDateFormat(param);
        return df.format(date);
    }

    //url格式化
    public static URL formatURL(String url) throws MalformedURLException {
        return new URL(url);
    }

    //判断是否为email
    public static boolean isEmail(String email){
        return email.matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
    }

    //去除所有，包括字符间的空格
    public static String filterInnerBlank(String str){
        return str.replaceAll(" ","");
    }

    //生成UUID
    public static String get32UUID(){
        return UUID.randomUUID().toString().trim().replace("-","");
    }

    //创建一封简单邮件
    public static Message createMimeMessage(Session session, String sendMail, String receiveMail,String password) throws Exception {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sendMail));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail));
        // 设置邮件标题
        message.setSubject("密码找回");
        // 设置邮件正文
        message.setText(password);
        message.setSentDate(new Date());
        //保存设置
        message.saveChanges();
        return message;
    }

    //发送一份邮件
    public static void sendEmail(Map<Object,String> datas) throws Exception {
        // 发件人的邮箱地址
        String sendEmailAccount = "304137776@qq.com";
        //如果有授权码，此处填写授权码(840384304)
        String sendEmailPassword = "eweoybwuralxbiif";
        // 发件人邮箱的 SMTP 服务器地址, 可以登录web邮箱查询
        String sendEmailSMTPHost = "smtp.qq.com";
        // 收件人邮箱地址
        String receiveMailAccount = datas.get("email");
        // 参数配置
        String password=datas.get("data");
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", sendEmailSMTPHost);
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        // 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);   // 设置为debug模式, 可以查看详细的发送 log
        // 创建一封邮件
        Message message = YYJHTools.createMimeMessage(session, sendEmailAccount, receiveMailAccount,password);
        // 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();
        // 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则会报错
        transport.connect(sendEmailAccount, sendEmailPassword);
        // 发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        // 关闭连接
        transport.close();
    }
}
