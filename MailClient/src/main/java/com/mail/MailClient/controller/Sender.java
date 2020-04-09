package com.mail.MailClient.controller;

import com.mail.MailClient.entity.Mail;
import com.mail.MailClient.entity.SMTPServer;
import com.mail.MailClient.entity.Result;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Mavericks
 */
public class Sender {
    private static final String LINE_END = "\r\n";
    private Mail currentMail;
    private Map.Entry<String, Integer> smtp;

    public Sender(Mail mail) {
        currentMail = mail;
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
            type = currentMail.getContentTypeMap().get(fileName);
        }
        if (null == type) {
            type = currentMail.getDefaultAttachmentContentType();
        }
        return type;
    }

    /**
     * 设置SMTP服务器信息
     * @param username 发件人邮箱
     */
    public Result setServerInfo(String username) {
        Result result = new Result(0);
        if (null == username || !username.contains("@")) {
            return result;
        }
        SMTPServer.initServer();
        //邮箱后缀
        String suffix = username.split("@")[1];
        for (Map.Entry<String, Integer> entry : SMTPServer.smtpServer.entrySet()) {
            if (entry.getKey().contains(suffix)) {
                smtp = entry;
                result.setCode(250);
                break;
            }
        }
        if (result.getCode() != 250) {
            result.setCode(4);
        }
        return result;
    }

    /**
     * 设置附件路径
     */
    public void transform(String[] paths){
        currentMail.setAttachments(paths);
    }

    /**
     * 添加附件
     */
    public void addAttachment(String path, String charset) {
        try {
            File f = new File(path);
            addAttachment(f.getName(), new FileInputStream(f), charset);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
                int readTotal;
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
                currentMail.addPartSet(mail);
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

    /**
     *  所有接收者
     */
    public void getAllReceivers() {
        String[] to = currentMail.getTo();
        if (to != null) {
            for (String s : to) {
                if (s != null && !"".equals(s)) {
                    currentMail.addReceivers(s);
                }
            }
        }

        String[] cc = currentMail.getCc();
        if (cc != null) {
            for (String s : cc) {
                if (s != null && !"".equals(s)) {
                    currentMail.addReceivers(s);
                }
            }
        }

        String[] bcc = currentMail.getBcc();
        if (bcc != null) {
            for (String s : bcc) {
                if (s != null && !"".equals(s)) {
                    currentMail.addReceivers(s);
                }
            }
        }
    }

    /**
     * 并所有单元
     * @return 合并后的单元格
     */
    private String getAllParts() {
        int count = currentMail.getPartSetSize();
        StringBuilder info = new StringBuilder(LINE_END);
        for (int i = count - 1; i >= 0; i--) {
            Mail mail = currentMail.getPartSetAt(i);
            String attachmentContent = mail.getContent();
            if (null != attachmentContent && 0 < attachmentContent.length()) {
                info.append(currentMail.getBoundaryNextPart()).append(LINE_END);
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
        currentMail.clearPartSet();
        return info.toString();
    }

    /**
     * 添加邮件正文单元
     */
    private void addContent() {
        if (null != currentMail.getContent()) {
            Mail part = new Mail();
            part.setContent(Base64.getEncoder().encodeToString(currentMail.getContent().getBytes()));
            part.setContentType("text/html;charset=\"" + currentMail.getCharset() + "\"");
            currentMail.addPartSet(part);
        }
    }

    /**
     * 联系人数组转换为字符串
     * @param receivers 联系人数组
     * @return 字符串
     */
    private String listToMailString(String[] receivers) {
        StringBuilder info = new StringBuilder();
        if (null != receivers) {
            for(String r : receivers) {
                info.append("<").append(r).append(">");
            }
        }
        return info.toString();
    }

    public Result sendMail() {
        Socket socket = null;
        PrintWriter writer = null;
        BufferedReader reader = null;
        //添加附件
        String[] attachments = currentMail.getAttachments();
        if(attachments != null)
        {
            for(String file : attachments) {
                if(file != null)
                    addAttachment(file, null);
            }
        }

        //设置SMTP服务器
        if(setServerInfo(currentMail.getUsername()).getCode() != 250)
        {
            return new Result(0);
        }
        try {
            socket = new Socket(smtp.getKey(), smtp.getValue());
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 与服务器建立连接
            readResponse(writer, reader, "220");
            // 连接到邮件服务
            writer.write("HELO ".concat(smtp.getKey()).concat(LINE_END));
            readResponse(writer, reader, "250");
            // 登录
            writer.write("AUTH LOGIN".concat(LINE_END));
            if (!readResponse(writer, reader, "334")) {
                return new Result(1);
            }

            // 输入用户名
            writer.write(Base64.getEncoder().encodeToString(currentMail.getUsername().getBytes()).concat(LINE_END));
            readResponse(writer, reader, "334");

            // 输入密码
            writer.write(Base64.getEncoder().encodeToString(currentMail.getPassword().getBytes()).concat(LINE_END));
            if (!readResponse(writer, reader, "235")) {
                return new Result(2);
            }

            //获取所有收件人
            getAllReceivers();
            List<String> receivers = currentMail.getReceivers();
            //用于登陆检测
            if(receivers == null)
            {
                return new Result(200);
            }

            // 发件人邮箱地址
            writer.write("MAIL FROM:<" + currentMail.getUsername() + ">" + LINE_END);
            readResponse(writer, reader, "250");


            for(String receiver : receivers) {
                writer.write("RCPT TO:<" + receiver + ">" + LINE_END);
                readResponse(writer, reader, "250");
            }

            // 开始输入邮件
            writer.write("DATA" + LINE_END);
            readResponse(writer, reader, "354");
            writer.flush();

            StringBuilder mail = new StringBuilder();
            // 设置邮件头信息
            // 发件人
            mail .append("From:").append(currentMail.getUsername()).append(LINE_END);
            // 收件人
            mail.append("To:").append(listToMailString(currentMail.getTo())).append(LINE_END);
            mail.append("Cc:").append(listToMailString(currentMail.getCc())).append(LINE_END);
            mail.append("Bcc:").append(listToMailString(currentMail.getBcc())).append(LINE_END);
            // 邮件主题
            mail.append("Subject:").append(currentMail.getSubject()).append(LINE_END);
            SimpleDateFormat sdf = new SimpleDateFormat(currentMail.getSimpleDatePattern());
            mail.append("Date:").append(sdf.format(new Date()));
            // 发送时间
            mail.append(LINE_END);
            mail.append("Content-Type:");
            mail.append(currentMail.getContentType());
            mail.append(";");
            mail.append("boundary=\"");
            // 邮件类型设置
            mail.append(currentMail.getBoundary()).append("\"");
            mail.append(LINE_END);

            // 添加邮件正文单元
            addContent();
            // 合并所有单元，正文和附件。
            mail.append(getAllParts());

            mail.append(LINE_END).append(".").append(LINE_END);
            writer.write(mail.toString());
            if(! readResponse(writer, reader, "250")) {
                return new Result(3);
            }
            writer.flush();

            // QUIT退出
            writer.write("QUIT" + LINE_END);
            writer.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 释放资源
            try {
                if (null != socket) {
                    socket.close();
                }
                if (null != writer) {
                    writer.close();
                }
                if (null != reader) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(currentMail.getCharset() != null) {
                currentMail.clearPartSet();
            }
        }
        return new Result(250);
    }

    public static void main(String[] args) {
        // Receiver Mail
//        String[] to = {""};
//        String[] a = {"E:\\图片\\avater.jpg"};
//
//        // Sender Mail
//        Mail mail = new Mail("", "", to, null,
//                null, "CC Test Mail!! 哈哈", "\"这是一个测试，请不要回复！\"", file);
//        Sender sender = new Sender(mail);
//        System.out.println(sender.sendMail().getCode());
        System.out.println(System.getProperty("user.dir"));
    }
}
