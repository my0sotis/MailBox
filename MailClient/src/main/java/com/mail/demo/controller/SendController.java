package com.mail.demo.controller;

import com.mail.demo.entity.Mail;
import com.mail.demo.entity.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SendController
{
    /**
     * 邮件发送
     * @param mail 邮件
     * @return 返回码为250代表发送成功
     */
    @CrossOrigin
    @PostMapping(value = "api/mail") //定义访问REST端点的Request URI
    @ResponseBody
    public Result mail(@RequestBody Mail mail){
        Result result = mail.sendMail();
        System.out.println(result.getCode());
        return result;
    }
}
