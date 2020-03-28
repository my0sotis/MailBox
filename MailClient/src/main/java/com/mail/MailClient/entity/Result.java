package com.mail.MailClient.entity;

/**
 * @author iGao101
 */
public class Result
{
    /**
     * 状态码
     * 0:用户名错误; 1:连接邮箱服务器失败; 2:登陆失败; 3:发送失败; 4:200 登陆成功
     */
    private int code;

    public Result(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
