package com.mail.MailClient.entity;

import java.util.HashMap;

/**
 * @author Mavericks
 */
public class SMTPServer {

    public static HashMap<String, Integer> smtpServer = new HashMap<>();

    public static void initServer() {
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
}
