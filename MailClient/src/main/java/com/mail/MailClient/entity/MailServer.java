package com.mail.MailClient.entity;
import java.util.HashMap;

// smtp服务器和pop服务器名称及端口
public class MailServer
{
    public static HashMap<String, Integer> smtpServer = new HashMap<>();
    public static HashMap<String, Integer> popServer = new HashMap<>();

    public static void init(){
        setSmtpServer();
        setPopServer();
    }

    public static void setSmtpServer()
    {
        smtpServer.put("smtp.gmail.com", 587);
        smtpServer.put("smtp.263.net.cn", 25);
        smtpServer.put("smtp.21cn.com", 25);
        smtpServer.put("SMTP.foxmail.com", 25);
        smtpServer.put("smtp.sina.com.cn", 25);
        smtpServer.put("smtp.vip.sina.com", 25);
        smtpServer.put("smtp.tom.com", 25);
        smtpServer.put("smtp.sohu.com", 25);
        smtpServer.put("smtp.mail.yahoo.com.cn", 25);
        smtpServer.put("smtp.qq.com", 25);
        smtpServer.put("smtp.exmail.qq.com", 587);
        smtpServer.put("smtp.126.com", 25);
        smtpServer.put("smtp.163.com", 25);
        smtpServer.put("smtp.live.com", 587);
        smtpServer.put("whu.edu.cn", 25);
    }

    public static void setPopServer()
    {
        popServer.put("pop.gmail.com", 995);
        popServer.put("pop.263.net.cn", 110);
        popServer.put("pop.21cn.com", 110);
        popServer.put("POP.foxmail.com", 110);
        popServer.put("pop3.sina.com.cn", 110);
        popServer.put("pop3.vip.sina.com", 110);
        popServer.put("pop.tom.com", 110);
        popServer.put("pop3.sohu.com", 110);
        popServer.put("pop.mail.yahoo.com.cn", 995);
        popServer.put("pop.qq.com", 110);
        popServer.put("pop.exmail.qq.com ", 995);
        popServer.put("pop.126.com", 110);
        popServer.put("pop.163.com", 110);
        popServer.put("pop.live.com", 995);
        popServer.put("whu.edu.cn", 110);
    }
}
