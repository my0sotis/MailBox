package com.mail.MailClient.controller;

import com.mail.MailClient.entity.Mail;
import com.mail.MailClient.entity.POPServer;
import com.mail.MailClient.entity.Result;
import com.mail.MailClient.entity.SMTPServer;

import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

/**
 * @author Mavericks
 */
public class Receiver {
    private final String LINE_END = "\r\n";
    private Mail mail;
    private Map.Entry<String, Integer> pop;
    private Socket popSocket;
    private InputStream popIn;

    public Receiver(String username) {
        mail = new Mail();
        setServerInfo(username);
    }

    /**
     * 设置POP服务器信息
     * @param username 发件人邮箱
     * @return 结果
     */
    private Result setServerInfo(String username) {
        if (null == username || !username.contains("@")) {
            return new Result(0);
        }
        POPServer.initServer();
        String suffix = username.split("@")[1];
        for (Map.Entry<String, Integer> entry : POPServer.popServer.entrySet()) {
            if (entry.getKey().contains(suffix)) {
                pop = entry;
                break;
            }
        }
        return new Result(250);
    }


}
