package com.mail.demo.entity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

public class Mail
{
    private static final String LINE_END = "\r\n";  //结束符
    private String username;
    private String password;
    private String[] to;
    private String[] cc;
    private String[] bcc;
    private List<String> receivers;                 //所有接收者
    private Map.Entry<String, Integer> smtp;        //smtp服务器
    private Map.Entry<String, Integer> pop;
    private String[] attachments;
    private String defaultAttachmentContentType;    //附件内容类型
    private String subject;
    private String content;
    private String contentType;                     //内容类型
    private String contentDisposition;
    private String contentTransferEncoding;
    private Map<String, String> contentTypeMap;     //文件类型映射
    private String charset;                         //编码集
    private String boundary;
    private String boundaryNextPart;
    private String simpleDatePattern;               //日期格式
    private List<Mail> partSet;


    public Mail() {
        init();
    }
    public Mail(String username, String password){
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
    public Mail(String username, String password, String[] to, String[] cc, String[] bcc, String subject, String content, String[] attachments){
        init();
        this.username = username;
        this.password = password;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.subject = subject;
        this.content = content;
        this.attachments = attachments;
        contentTypeMap = new HashMap<>();
    }

    //初始化
    public void init(){
        defaultAttachmentContentType = "application/octet-stream";
        simpleDatePattern = "yyyy-MM-dd HH:mm:ss";
        boundary = "--=_NextPart_zlz_3907_" + System.currentTimeMillis();
        boundaryNextPart = "--" + boundary;
        contentTransferEncoding = "base64";
        contentType = "multipart/alternative";
        charset = Charset.defaultCharset().name();
        partSet = new ArrayList<Mail>();
        receivers = new ArrayList<>();

        // MIME Media Types
        contentTypeMap = new HashMap<String, String>();
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
    /**
     * 设置smtp和pop服务器信息
     *
     * @param username 发件人邮箱
     */
    public Result setServerInfo(String username){
        if (null == username || !username.contains("@"))
            return new Result(0);
        MailServer.init();
        String suffix = username.split("@")[1];  //邮箱后缀
        for (Map.Entry<String, Integer> entry : MailServer.smtpServer.entrySet())
        {
            if (entry.getKey().contains(suffix))
            {
                smtp = entry;
                break;
            }
        }
        for (Map.Entry<String, Integer> entry : MailServer.popServer.entrySet())
        {
            if (entry.getKey().contains(suffix))
            {
                pop = entry;
                break;
            }
        }
        return new Result(250);
    }

    //所有接收者
    public void getAllReceivers(){
        for(int i = 0; i < to.length; i++)
            if(! to[i].equals("") && to[i] != null)
                receivers.add(to[i]);

        for(int i = 0; i < cc.length; i++)
            if(!cc[i].equals("") && cc[i] != null)
                receivers.add(cc[i]);

        for(int i = 0; i < bcc.length; i++)
            if(!bcc[i].equals("") && bcc[i] != null)
                receivers.add(bcc[i]);
    }

    /**
     * 根据后缀获取文件类型
     * @param fileName 文件名
     * @return 类型
     */
    private String getFileType(String fileName) {
        String type = null;
        if (null != fileName) {
            int flag = fileName.lastIndexOf(".");
            if (0 <= flag && flag < fileName.length() - 1) {
                fileName = fileName.substring(flag + 1);
            }
            type = contentTypeMap.get(fileName);
        }
        if (null == type) {
            type = defaultAttachmentContentType;
        }
        return type;
    }

    //合并所有单元
    private String getAllParts() {
        int count = partSet.size();
        StringBuilder info = new StringBuilder(LINE_END);
        for (int i = count - 1; i >= 0; i--) {
            Mail mail = partSet.get(i);
            String attachmentContent = mail.getContent();
            if (null != attachmentContent && 0 < attachmentContent.length()) {
                info.append(getBoundaryNextPart()).append(LINE_END);
                info.append("Content-Type: ");
                info.append(mail.getContentType());
                info.append(LINE_END);
                info.append("Content-Transfer-Encoding: ");
                info.append(mail.getContentTransferEncoding());
                info.append(LINE_END);
                if (i != count - 1) {
                    info.append("Content-Disposition: ");
                    info.append(mail.getContentDisposition());
                    info.append(LINE_END);
                }

                info.append(LINE_END);
                info.append(mail.getContent());
                info.append(LINE_END);
            }
        }
        info.append(LINE_END);
        info.append(LINE_END);
        partSet.clear();
        return info.toString();
    }

    //添加邮件正文单元
    private void addContent() {
        if (null != content) {
            Mail part = new Mail();
            part.setContent(Base64.getEncoder().encodeToString(content.getBytes()));
            part.setContentType("text/html;charset=\"" + charset + "\"");
            partSet.add(part);
        }
    }

    //联系人数组转换为字符串
    private String listToMailString(String[] receivers) {
        StringBuilder info = new StringBuilder();
        if (null != receivers) {
            for(String r : receivers)
                info.append("<").append(r).append(">");
        }
        return info.toString();
    }

    //添加附件
    public void addAttachment(String filePath) {
        addAttachment(filePath, null);
    }
    public void addAttachment(String filePath, String charset) {
        if (null != filePath && filePath.length() > 0) {
            File file = new File(filePath);
            try {
                addAttachment(file.getName(), new FileInputStream(file), charset);
            } catch (FileNotFoundException e) {
                System.out.println("错误：" + e.getMessage());
                System.exit(1);
            }
        }
    }
    public void addAttachment(String fileName, InputStream attachmentStream, String charset) {
        try {
            byte[] bs = null;
            if (null != attachmentStream) {
                int buffSize = 1024;
                byte[] buff = new byte[buffSize];
                byte[] temp;
                bs = new byte[0];
                int readTotal = 0;
                while (-1 != (readTotal = attachmentStream.read(buff))) {
                    temp = new byte[bs.length];
                    System.arraycopy(bs, 0, temp, 0, bs.length);
                    bs = new byte[temp.length + readTotal];
                    System.arraycopy(temp, 0, bs, 0, temp.length);
                    System.arraycopy(buff, 0, bs, temp.length, readTotal);
                }
            }

            if (null != bs) {
                Mail mail = new Mail();
                charset = null != charset ? charset : Charset.defaultCharset().name();
                String contentType = getFileType(fileName) + ";name=\"=?" + charset + "?B?" + Base64.getEncoder().encodeToString(fileName.getBytes()) + "?=\"";
                mail.setCharset(charset);
                mail.setContentType(contentType);
                mail.setContentDisposition("attachment;filename=\"=?" + charset + "?B?" + Base64.getEncoder().encodeToString(fileName.getBytes()) + "?=\"");
                mail.setContent(Base64.getEncoder().encodeToString(bs));
                partSet.add(mail);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != attachmentStream) {
                try {
                    attachmentStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Runtime.getRuntime().gc();
            Runtime.getRuntime().runFinalization();
        }
    }

    //发送邮件
    public Result sendMail() {
        Socket socket = null;
        PrintWriter writer = null;
        BufferedReader reader = null;
        if(attachments != null)                      //添加附件
            for(String path : attachments)
                addAttachment(path);
        if(setServerInfo(username).getCode() != 250) //设置SMTP服务器
            return new Result(0);

        try {
            socket = new Socket(smtp.getKey(), smtp.getValue());
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 与服务器建立连接
            readResponse(writer, reader, "220");
            writer.write("HELO ".concat(smtp.getKey()).concat(LINE_END)); // 连接到邮件服务
            readResponse(writer, reader, "250");
            writer.write("AUTH LOGIN".concat(LINE_END));  // 登录
            if (!readResponse(writer, reader, "334"))
                return new Result(1);

            writer.write(Base64.getEncoder().encodeToString(username.getBytes()).concat(LINE_END));     // 输入用户名
            readResponse(writer, reader, "334");

            writer.write(Base64.getEncoder().encodeToString(password.getBytes()).concat(LINE_END)); // 输入密码
            if (!readResponse(writer, reader, "235"))
                return new Result(2);

            getAllReceivers();     //获取所有收件人
            if(receivers == null)  //用于登陆检测
                return new Result(200);

            writer.write("MAIL FROM:<" + username + ">" + LINE_END);      // 发件人邮箱地址
            readResponse(writer, reader, "250");


            for(String receiver : receivers) {
                writer.write("RCPT TO:<" + receiver + ">" + LINE_END);
                readResponse(writer, reader, "250");
            }

            writer.write("DATA" + LINE_END);           // 开始输入邮件
            readResponse(writer, reader, "354");
            writer.flush();

            StringBuilder mail = new StringBuilder();
            // 设置邮件头信息
            mail .append("From:").append(username).append(LINE_END);   // 发件人
            mail.append("To:").append(listToMailString(to)).append(LINE_END);  // 收件人
            mail.append("Cc:").append(listToMailString(cc)).append(LINE_END);
            mail.append("Bcc:").append(listToMailString(bcc)).append(LINE_END);
            mail.append("Subject:").append(subject).append(LINE_END);          // 邮件主题
            SimpleDateFormat sdf = new SimpleDateFormat(simpleDatePattern);
            mail.append("Date:").append(sdf.format(new Date()));
            mail.append(LINE_END);              // 发送时间
            mail.append("Content-Type:");
            mail.append(contentType);
            mail.append(";");
            mail.append("boundary=\"");
            mail.append(boundary).append("\""); // 邮件类型设置
            mail.append(LINE_END);

            addContent();               // 添加邮件正文单元
            mail.append(getAllParts()); // 合并所有单元，正文和附件。

            mail.append(LINE_END).append(".").append(LINE_END);
            writer.write(mail.toString());
            if(! readResponse(writer, reader, "250"))
                return new Result(3);
            writer.flush();

            writer.write("QUIT" + LINE_END); // QUIT退出
            writer.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 释放资源
            try {
                if (null != socket)
                    socket.close();
                if (null != writer)
                    writer.close();
                if (null != reader)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(partSet != null)
                this.partSet.clear();
        }
        return new Result(250);
    }

    private boolean readResponse(PrintWriter pw, BufferedReader br, String msgCode) throws IOException {
        pw.flush();
        String message = br.readLine();
        if (null == message || !message.contains(msgCode)) {
            pw.write("QUIT".concat(LINE_END));
            pw.flush();
            return false;
        }
        return true;
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

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String[] getTo()
    {
        return to;
    }

    public void setTo(String[] to)
    {
        this.to = to;
    }

    public String[] getCc()
    {
        return cc;
    }

    public void setCc(String[] cc)
    {
        this.cc = cc;
    }

    public String[] getBcc()
    {
        return bcc;
    }

    public void setBcc(String[] bcc)
    {
        this.bcc = bcc;
    }

    public void setAttachments(String[] attachments)
    {
        this.attachments = attachments;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public static void main(String[] args) {
        String[] to = {"2017302580230@whu.edu.cn"};
        String[] a = {"E:\\图片\\avater.jpg"};
        Mail mail = new Mail("2017302580230@whu.edu.cn", "qaq", to, null, null, "CC Test Mail!! 哈哈", "\"这是一个测试，请不要回复！\"", a);
        System.out.println(mail.sendMail().getCode()); // 发送
    }
}
