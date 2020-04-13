package com.mail.MailClient.controller;

import com.mail.MailClient.entity.Mail;
import com.mail.MailClient.entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class SendController
{
    String[] paths; //存储附件路径
    /**
     * 存储MultipartFile类型附件并获取路径
     * @param file 附件
     */
    @CrossOrigin
    @PostMapping(value = "api/attachments") //定义访问REST端点的Request URI
    @ResponseBody
    public void transform(MultipartFile[] file) {
        paths = new String[file.length];
        for(int i = 0; i < file.length; i++) {
            try {
                byte[] bytes = file[i].getBytes();
                String base = System.getProperty("user.dir") + "/";
                Path path = Paths.get(base + file[i].getOriginalFilename());
                //如果没有files文件夹，则创建
                if (!Files.isWritable(path)) {
                    Files.createDirectories(Paths.get(System.getProperty("user.dir")));
                }
                //文件写入指定路径
                Files.write(path, bytes);
                paths[i] = base + file[i].getOriginalFilename();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 邮件发送
     * @param mail target mail ready to be post
     * @return 返回码为250代表发送成功
     */
    @CrossOrigin
    @PostMapping(value = "api/mail") //定义访问REST端点的Request URI
    @ResponseBody
    public Result mail(@RequestBody Mail mail) {
        Sender sender = new Sender(mail);
        sender.transform(paths);
        Result result = sender.sendMail();
        System.out.println(result.getCode());
        return result;
    }
}
