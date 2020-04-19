package com.mail.MailClient.entity;
/**
 * @author zhaobiao
 */
public class MailObject
{
    public  int ID;
    public String date;
    public String sendName;
    public String receiveName;
    public String sendAddress;
    public String receiver;
    public String subject;
    public String content;
    public String[] attachments;
    public MailObject(int ID,String date,String sendName,String receiveName,String sendAddress,String receiveAddress,String subject,String content,String[] attachments){
        this.ID=ID;
        this.date=date;
        this.sendName=sendName;
        this.receiveName=receiveName;
        this.sendAddress=sendAddress;
        this.receiver=receiveAddress;
        this.subject = subject;
        this.content=content;
        this.attachments=attachments;
    }
}
