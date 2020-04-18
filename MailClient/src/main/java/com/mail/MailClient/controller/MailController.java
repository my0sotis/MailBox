package com.mail.MailClient.controller;

import com.mail.MailClient.entity.BriefMail;
import com.mail.MailClient.entity.DetailedMail;
import com.mail.MailClient.entity.Mail;
import com.mail.MailClient.entity.Result;
import com.mail.MailClient.statics.BasicInfo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author gao
 * @Date 2020/4/18 15:50
 * @Version 1.0
 */

@RestController
@CrossOrigin
public class MailController
{
    private Receiver receiver;
    //以json形式返回邮件简要信息
    @GetMapping("api/briefMails")
    public List<BriefMail> getJson(){
        receiver = new Receiver("2017302580244@whu.edu.cn", "zpc888wsadjkl,./");
        receiver.list.clear();
        receiver.receiveMails();
        return receiver.list;
    }

    //转发某一邮件
    @GetMapping("api/transmit/{no}")
    public DetailedMail transmit(@PathVariable(name = "no") Integer no){
        receiver = new Receiver("2017302580244@whu.edu.cn", "zpc888wsadjkl,./");
        receiver.receiveMailAt(no);
        return receiver.mail;
    }

    //删除邮件
    @PostMapping("api/deleteMail/{list}")
    public Result delete(@PathVariable(name = "list")Integer[] list){
        receiver = new Receiver(BasicInfo.username, BasicInfo.passworld);
        Result result = null;
        for(int i : list){
            result = receiver.deleteMailAt(i);
            if(result.getCode() != 200)
                return result;
        }
        return new Result(200);
    }
}
