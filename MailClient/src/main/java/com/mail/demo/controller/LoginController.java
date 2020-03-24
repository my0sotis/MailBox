package com.mail.demo.controller;

import com.mail.demo.entity.Result;
import com.mail.demo.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController
{
    /**
     * 登陆验证
     * @param user 发送者
     * @return 返回码为200代表邮箱密码正确
     */
    @CrossOrigin
    @PostMapping(value = "api/login") //定义访问REST端点的Request URI
    @ResponseBody
    public Result login(@RequestBody User user) {
        if(user == null || user.getUsername() == null || user.getPassword() == null)
            return new Result(0);
        return user.checkInfo();
    }
}