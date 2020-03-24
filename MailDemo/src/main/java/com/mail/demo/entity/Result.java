package com.mail.demo.entity;

public class Result
{
    //0:用户名错误; 1:连接邮箱服务器失败; 2:登陆失败; 3:发送失败; 4:200 登陆成功
    private int code; //状态码

    public Result(int code) { this.code = code; }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }
}
