package com.mail.MailClient.entity;

import com.mail.MailClient.controller.Sender;

/**
 * 用户类
 * @author 高战立
 */
public class User
{
    private String username;
    private String password;
    public User() { }
    public User(String name, String password) {
        this.username = name;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    /**
     * 检查邮箱和密码
     */
    public Result checkInfo() {
        Mail mail = new Mail(username, password);
        Sender sender = new Sender(mail);
        return sender.sendMail();
    }
}
