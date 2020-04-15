package com.mail.MailClient.entity;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;

public class Mail {
    private String username;
    private String password;
    private String[] to;
    private String[] cc;
    private String[] bcc;
    /**
     * 所有接收者
     */
    private List<String> receivers;
    private String[] attachments;
    // 附件内容类型
    private String defaultAttachmentContentType;
    private String subject;
    private String content;
    // 内容类型
    private String contentType;
    private String contentDisposition;
    private String contentTransferEncoding;
    // 文件类型映射
    private Map<String, String> contentTypeMap;
    // 编码集
    private String charset;
    private String boundary;
    private String boundaryNextPart;
    //日期格式
    private String simpleDatePattern;
    private List<Mail> partSet;


    public Mail() {
        init();
    }
    public Mail(String username, String password) {
        this.username = username;
        this.password = password;
    }
    /**
     * 有参构造函数
     * @param username 发送者
     * @param password 密码
     * @param to 接收者
     * @param cc 抄送
     * @param bcc 密送
     * @param subject 主题
     * @param content 内容
     * @param attachments 附件地址
     */
    public Mail(String username, String password, String[] to, String[] cc, String[] bcc,
                String subject, String content, String[] attachments) {
        init();
        this.username = username;
        this.password = password;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.subject = subject;
        this.content = content;
        this.attachments = attachments;
    }

    /**
     * 初始化
     */
    public void init() {
        defaultAttachmentContentType = "application/octet-stream";
        simpleDatePattern = "yyyy-MM-dd HH:mm:ss";
        boundary = "--=_NextPart_zlz_3907_" + System.currentTimeMillis();
        boundaryNextPart = "--" + boundary;
        contentTransferEncoding = "base64";
        contentType = "multipart/alternative";
        charset = Charset.defaultCharset().name();
        partSet = new ArrayList<>();
        receivers = new ArrayList<>();

        // MIME Media Types
        contentTypeMap = new HashMap<>();
        contentTypeMap.put("xls", "application/vnd.ms-excel");
        contentTypeMap.put("xlsx", "application/vnd.ms-excel");
        contentTypeMap.put("xlsm", "application/vnd.ms-excel");
        contentTypeMap.put("xlsb", "application/vnd.ms-excel");
        contentTypeMap.put("doc", "application/msword");
        contentTypeMap.put("dot", "application/msword");
        contentTypeMap.put("docx", "application/msword");
        contentTypeMap.put("docm", "application/msword");
        contentTypeMap.put("dotm", "application/msword");
    }

    public String getBoundaryNextPart() {
        return boundaryNextPart;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentTransferEncoding() {
        return contentTransferEncoding;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getCharset() {
        return charset;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String[] getBcc() {
        return bcc;
    }

    public void setBcc(String[] bcc) {
        this.bcc = bcc;
    }

    public void setAttachments(String[] attachments) {
        this.attachments = attachments;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public String getDefaultAttachmentContentType() {
        return defaultAttachmentContentType;
    }

    public Map<String, String> getContentTypeMap() {
        return contentTypeMap;
    }

    public String[] getAttachments() {
        return attachments;
    }

    public String getSimpleDatePattern() {
        return simpleDatePattern;
    }

    public String getBoundary() {
        return boundary;
    }

    public List<Mail> getPartSet() {
        return partSet;
    }

    public Mail getPartSetAt(int index) {
        return partSet.get(index);
    }

    public int getPartSetSize() {
        return partSet.size();
    }

    public void clearPartSet() {
        partSet.clear();
    }

    public void addPartSet(Mail mail) {
        partSet.add(mail);
    }

    public void addReceivers(String s) {
        receivers.add(s);
    }

    public List<String> getReceivers() {
        return receivers;
    }
}
