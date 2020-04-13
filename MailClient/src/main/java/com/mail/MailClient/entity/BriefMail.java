package com.mail.MailClient.entity;

public class BriefMail {
    private String datetime;
    private String senderName;
    private String senderEmail;
    private String replyToName;
    private String replyToEmail;
    private String receiverName;
    private String receiverEmail;
    private String subject;

    public BriefMail() { }

    public String getDatetime() {
        return datetime;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReplyToEmail() {
        return replyToEmail;
    }

    public String getReplyToName() {
        return replyToName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSubject() {
        return subject;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public void setReplyToEmail(String replyToEmail) {
        this.replyToEmail = replyToEmail;
    }

    public void setReplyToName(String replyToName) {
        this.replyToName = replyToName;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
