package com.mail.MailClient.controller;

import com.mail.MailClient.entity.DataOperation;
import com.mail.MailClient.entity.Mail;
import com.mail.MailClient.entity.Result;
import com.mail.MailClient.statics.BasicInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 高战立
 */
@Controller
public class SendController
{
    /**
     * 存储MultipartFile类型附件并获取路径
     * @param file 附件
     */
    @CrossOrigin
    @PostMapping(value = "api/attachments") //定义访问REST端点的Request URI
    @ResponseBody
    public List<String> transform(MultipartFile[] file) {
        int size = file.length;
        List<String> paths = new ArrayList<>(); //存储附件路径
        for(int i = 0; i < size; i++) {
            try {
                byte[] bytes = file[i].getBytes();
                String base = System.getProperty("user.dir") + "\\";
                Path path = Paths.get(base + file[i].getOriginalFilename());
                //如果没有files文件夹，则创建
                if (!Files.isWritable(path)) {
                    Files.createDirectories(Paths.get(System.getProperty("user.dir")));
                }
                //文件写入指定路径
                Files.write(path, bytes);
                paths.add(base + file[i].getOriginalFilename());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return paths;
    }

    /**
     * 邮件发送
     * @param mail target mail ready to be post
     * @return 返回码为200代表发送成功
     */
    @CrossOrigin
    @PostMapping(value = "api/mail") //定义访问REST端点的Request URI
    @ResponseBody
    public Result mail(@RequestBody Mail mail) {
        Sender sender = new Sender(mail);
        Result result = sender.sendMail();

        DataOperation dataOperation = new DataOperation();
        int index = dataOperation.SearchAllSend().size(); //获取所有已发送的数量
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(new Date());           //当前时间
        StringBuilder attachment = new StringBuilder("");
        if(mail.getAttachments()!=null)
            for(String i:mail.getAttachments())
                attachment.append(i).append("#");
        dataOperation.AddSend(index+1, str, mail.getUsername(),mail.getTo()[0], mail.getUsername(),mail.getTo()[0], mail.getSubject(), mail.getContent(), attachment.toString());
        System.out.println(result.getCode());
        return result;
    }

    /**
     * 草稿保存
     * @param mail target mail ready to be post
     * @return 返回码为200代表保存成功
     */
    @CrossOrigin
    @PostMapping(value = "api/draft") //定义访问REST端点的Request URI
    @ResponseBody
    public Result draft(@RequestBody Mail mail) {
        Sender sender = new Sender(mail);
        DataOperation dataOperation = new DataOperation();
        int index = dataOperation.SearchAllDraft().size(); //获取所有已发送的数量
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(new Date());           //当前时间
        StringBuilder attachment = new StringBuilder("");
        if(mail.getAttachments()!=null)
            for(String i:mail.getAttachments())
                attachment.append(i).append("#");
        dataOperation.Adddraft(index+1, str, mail.getUsername(),mail.getTo()[0], mail.getUsername(),mail.getTo()[0], mail.getSubject(), mail.getContent(), attachment.toString());
        return new Result(250);
    }
}
