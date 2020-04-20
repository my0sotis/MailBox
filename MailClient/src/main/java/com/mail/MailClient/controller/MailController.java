package com.mail.MailClient.controller;

import com.mail.MailClient.entity.*;
import com.mail.MailClient.statics.BasicInfo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author iGao101
 * @Date 2020/4/18 15:50
 * @Version 1.0
 */

@RestController
@CrossOrigin
public class MailController
{
    private Receiver receiver;
    private DataOperation dataOperation = new DataOperation();
    //以json形式返回邮件简要信息
    @GetMapping("api/briefMails")
    public List<BriefMail> getJson(){
        receiver = new Receiver(BasicInfo.username, BasicInfo.passworld);
        receiver.list.clear();
        receiver.receiveMails();
        return receiver.list;
    }
    //转发某一邮件
    @GetMapping("api/transmit/{no}")
    public DetailedMail transmit(@PathVariable(name = "no") Integer no){
        receiver = new Receiver(BasicInfo.username, BasicInfo.passworld);
        receiver.receiveMailAt(no);
        return receiver.mail;
    }
    //删除邮件
    @PostMapping("api/deleteMail/{list}")
    public Result delete(@PathVariable(name = "list")Integer[] list){
        receiver = new Receiver(BasicInfo.username, BasicInfo.passworld);
        Result result = null;
        for(int i : list){
            receiver.receiveMailAt(i);
            DetailedMail detailedMail = receiver.mail;
            dataOperation.moveMails(detailedMail);
            result = receiver.deleteMailAt(i);
            if(result.getCode() != 200)
                return result;
        }
        return new Result(200);
    }


    //以json形式返回某类型数据表的邮件简要信息
    @GetMapping("api/briefDBMails/{type}")
    public List<BriefMail> getPostMail(@PathVariable(name = "type") Integer type){
        return dataOperation.getBriefMail(type);
    }
    //获得数据表中某一详细邮件
    @GetMapping("api/transmitPost/{no}&&{type}")
    public DetailedMail transmitPost(@PathVariable(name = "no") Integer no, @PathVariable(name = "type") Integer type){
        return dataOperation.getDetailMail(no, type);
    }
    //删除数据表中部分邮件
    @PostMapping("api/deletePostMail/{list}&&{type}")
    public Result deletePost(@PathVariable(name = "list")Integer[] list, @PathVariable(name = "type") Integer type){
        return dataOperation.deleteMails(list, type);
    }

    //垃圾箱中的邮件移动到已发送或者草稿箱
    @GetMapping("api/moveMails/{list}&&{type}")
    public Result moveMails(@PathVariable(name = "list")Integer[] list, @PathVariable(name = "type") Integer type){
        return dataOperation.moveMails(list, type);
    }
}
