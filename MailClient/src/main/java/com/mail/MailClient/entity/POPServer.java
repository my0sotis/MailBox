package com.mail.MailClient.entity;

import java.util.HashMap;

/**
 * @author Mavericks
 */
public class POPServer {

    public static HashMap<String, Integer> popServer = new HashMap<>();

    public static void initServer() {
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
