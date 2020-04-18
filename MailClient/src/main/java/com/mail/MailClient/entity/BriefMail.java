package com.mail.MailClient.entity;

public class BriefMail {
    private int index;
    private int num;
    private String messageID;
    private String datetime;
    private String senderName;
    private String senderEmail;
    private String replyToName;
    private String replyToEmail;
    private String receiverName;
    private String receiverEmail;
    private String subject;
    private String contentType;
    private String charset;
    private String boundary;
    private String contentTransferEncoding;

    public BriefMail(int index) {
        this.index = index;
    }

    public BriefMail(){}

    public int getIndex() {
        return index;
    }

    public String getMessageID() {
        return messageID;
    }

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

    public String getBoundary() {
        return boundary;
    }

    public String getContentTransferEncoding() {
        return contentTransferEncoding;
    }

    public String getContentType() {
        return contentType;
    }

    public String getCharset() {
        return charset;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
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

    public void setBoundary(String boundary) {
        this.boundary = boundary;
    }

    public void setContentTransferEncoding(String contentTransferEncoding) {
        this.contentTransferEncoding = contentTransferEncoding;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public int getNum()
    {
        return num;
    }

    public void setNum(int num)
    {
        this.num = num;
    }
}
