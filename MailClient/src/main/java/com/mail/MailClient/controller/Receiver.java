package com.mail.MailClient.controller;

import com.mail.MailClient.entity.BriefMail;
import com.mail.MailClient.entity.CodeHelper;
import com.mail.MailClient.entity.POPServer;
import com.mail.MailClient.entity.Result;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mavericks
 */
public class Receiver {
    private final String OK = "+OK";
    private final String username;
    private final String password;
    private Map.Entry<String, Integer> pop;
    private Socket popSocket;
    boolean isDebug = false;
    private final BufferedReader in = null;
    private final BufferedWriter out = null;

    private final static Pattern datePattern = Pattern.compile("^Date:\\s?(.+)$");
    private final static Pattern fromPattern = Pattern.compile("^From:\\s?\"?([a-zA-Z0-9\\s=?/\\-.@]*)\"? (<(.+)>)?$");
    private final static Pattern replyToPattern = Pattern.compile("^Reply-To:\\s?\"?([a-zA-Z0-9\\s=?/\\-.@]*)\"? (<(.+)>)?$");
    private final static Pattern toPattern = Pattern.compile("^To:\\s?\"?([a-zA-Z0-9\\s=?/\\-.@]*)\"?\\s?(<(.+)>)?$");
    private final static Pattern subjectPattern = Pattern.compile("^Subject:\\s?(.+)$");
    private final Pattern mimeCodePattern = Pattern.compile("^=\\?([a-zA-Z0-9\\-]*)\\?([QBqb])\\?(.+)\\?=$");

    public Receiver(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * 设置POP服务器信息
     * @param username 发件人邮箱
     * @return 结果
     */
    private Result setServerInfo(String username) {
        Result result = new Result(0);
        if (null == username || !username.contains("@")) {
            return result;
        }
        POPServer.initServer();
        String suffix = username.split("@")[1];
        for (Map.Entry<String, Integer> entry : POPServer.popServer.entrySet()) {
            if (entry.getKey().contains(suffix)) {
                pop = entry;
                result.setCode(250);
                break;
            }
        }
        if (result.getCode() != 250) {
            result.setCode(4);
        }
        return result;
    }

    private Result POP3Client() {
        Result result = new Result(250);
        try {
            // 新建Socket连接
            popSocket = new Socket(pop.getKey(), pop.getValue());
        } catch (IOException e) {
            e.printStackTrace();
            result.setCode(1);
            return result;
        }
        return result;
    }

    /**
     * 得到服务器返回的一行命令
     * @param in 读入流
     * @return 命令
     */
    public String getReturn(BufferedReader in) {
        String line = "";
        try {
            line = in.readLine();
            if (isDebug) {
                System.out.println("服务器状态："+line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    /**
     * 从返回的命令中得到第一个字段,也就是服务器的返回状态码(+OK或者-ERR)
     * @param line 返回的命令
     * @return 返回状态码(+OK或者-ERR)
     */
    public String getResult(String line) {
        StringTokenizer st = new StringTokenizer(line, " ");
        return st.nextToken();
    }

    private String sendServer(String str, BufferedReader in, BufferedWriter out) throws IOException {
        // 发送命令
        out.write(str);
        // 发送空行
        out.newLine();
        // 清空缓存区
        out.flush();
        // 如果软件在测试中使用
        if (isDebug) {
            System.out.println("已发送命令:" + str);
        }
        return getReturn(in);
    }

    public void user(String user, BufferedReader in, BufferedWriter out) throws IOException {
        String result;
        result = getResult(getReturn(in));
        if (!OK.equals(result)) {
            // 连接服务器失败
            throw new IOException("1");
        }
        // 发送user命令
        result = getResult(sendServer("user " + user, in, out));
        if (!OK.equals(result)) {
            // 用户名错误
            throw new IOException("0");
        }
    }

    public void pass(String password, BufferedReader in, BufferedWriter out) throws IOException {
        String result;
        result = getResult(sendServer("pass " + password, in, out));
        if (!OK.equals(result)) {
            throw new IOException("5");
        }
    }

    public int stat(BufferedReader in, BufferedWriter out) throws IOException {
        String result;
        String line;
        int mailNum = 0;
        line = sendServer("stat", in, out);
        StringTokenizer st = new StringTokenizer(line, " ");
        result = st.nextToken();
        if (st.hasMoreTokens()) {
            mailNum = Integer.parseInt(st.nextToken());
        }
        if (!OK.equals(result)) {
            // 查看邮箱状态出错!
            throw new IOException("6");
        }
        return mailNum;
    }

    /**
     * 无参List命令
     * @param in 输入流
     * @param out 输出流
     * @throws IOException 报错
     */
    public void list(BufferedReader in, BufferedWriter out) throws IOException {
        StringBuilder message = new StringBuilder();
        String line;
        line = sendServer("list", in, out);
        // 邮件信息
        while (!".".equalsIgnoreCase(line)) {
            message.append(line).append("\n");
            line = in.readLine();
        }
        System.out.println(message);
    }

    /**
     * 有参List命令
     * @param mailNum 需要查看的邮件号
     * @param in 输入流
     * @param out 输出流
     * @throws IOException 报错
     */
    public void listOne(int mailNum, BufferedReader in, BufferedWriter out) throws IOException {
        String result;
        result = getResult(sendServer("list " + mailNum, in, out));
        if (!OK.equals(result)) {
            // list 出错
            throw new IOException("7");
        }
    }

    /**
     * 获取邮件的具体内容
     * @param in 输入流
     * @return 邮件内容
     */
    public String getMessageDetail(BufferedReader in) {
        StringBuilder message = new StringBuilder();
        String line;
        try {
            line = in.readLine();
            while (!".".equalsIgnoreCase(line)) {
                message.append(line).append("\n");
                line = in.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message.toString();
    }

    /**
     * retr命令
     * @param mailNum 邮件编号
     * @param in 输入流
     * @param out 输出流
     * @return 邮件具体信息
     */
    public String retr(int mailNum, BufferedReader in, BufferedWriter out) throws IOException {
        String result;
        result = getResult(sendServer("retr " + mailNum, in, out));
        if (!OK.equals(result)) {
            // 接收邮件时出错
            throw new IOException("8");
        }
        result = getMessageDetail(in);
        return result;
    }

    public String top(int mailNum, int lineNum, BufferedReader in, BufferedWriter out) throws IOException {
        String result;
        result = getResult(sendServer("top " + mailNum + " " + lineNum, in, out));
        if (!OK.equals(result)) {
            throw new IOException("8");
        }
        result = getMessageDetail(in);
        return result;
    }

    /**
     * 删除邮件
     * @param mailNum 要操作的邮件编号
     * @param in 输入流
     * @param out 输出流
     * @throws IOException 报错
     */
    public void dele(int mailNum, BufferedReader in, BufferedWriter out) throws IOException {
        String result;
        result = getResult(sendServer("dele " + mailNum, in, out));
        if (!OK.equals(result)) {
            // 删除邮件时出错
            throw new IOException("10");
        }
    }

    /**
     * 退出邮箱
     * @param in 输入流
     * @param out 输出流
     * @throws IOException 报错
     */
    public void quit(BufferedReader in, BufferedWriter out) throws IOException {
        sendServer("QUIT", in, out);
    }

    private void printErrorString(String str) {
        switch (str) {
            case "0":
                System.out.println("用户名错误");
                break;
            case "1":
                System.out.println("连接邮箱服务器失败");
                break;
            case "2":
                System.out.println("登陆失败");
                break;
            case "3":
                System.out.println("发送失败");
                break;
            case "4":
                System.out.println("未收录对应邮箱SMTP或POP3地址");
                break;
            case "5":
                System.out.println("密码错误");
                break;
            case "6":
                System.out.println("查看邮箱状态出错");
                break;
            case "7":
                System.out.println("list出错");
                break;
            case "8":
                System.out.println("接收邮件出错");
                break;
            case "9":
                System.out.println("未能正确退出");
                break;
            default:
                System.out.println(str);
                break;
        }
    }

    /**
     * 获取被解码的信息
     * @param string Target String
     * @return MIME Decoded String
     */
    private String getDecodedMsg(String string) {
        Matcher matcher = mimeCodePattern.matcher(string);
        if (matcher.find()) {
            if ("Q".equalsIgnoreCase(matcher.group(2))) {
                try {
                    return CodeHelper.QPConvertCharset(matcher.group(3), matcher.group(1));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return CodeHelper.Base64ConvertCharset(matcher.group(3), matcher.group(1));
            }
        }
        return null;
    }

    private BriefMail getBriefMailInfo(String mailInfo) {
        BriefMail mail = new BriefMail();
        String[] strings = mailInfo.split("\n");
        List<String> list = new ArrayList<>();
        for (String string : strings) {
            if (!"".equals(string)) {
                list.add(string.trim());
            }
        }
        int i = 0;
        // 是否获取到日期、发件人、回复地址、收件人、主题
        boolean haveDate = false, haveFrom = false, haveReplyTo = false, haveTo = false, haveSubject = false;
        // 日期
        Matcher matcher, codeMatcher;
        for (; i < list.size(); i++) {
            if (haveDate && haveFrom && haveTo && haveSubject) {
                break;
            }
            matcher = haveDate ? null : datePattern.matcher(list.get(i));
            if (!haveDate && matcher.matches()) {
                mail.setDatetime(matcher.group(1));
                haveDate = true;
                continue;
            }
            matcher = haveFrom ? null : fromPattern.matcher(list.get(i));
            if (!haveFrom && matcher.matches()) {
                String senderName = matcher.group(1).trim();
                codeMatcher = mimeCodePattern.matcher(senderName);
                if (codeMatcher.matches()) {
                    senderName = getDecodedMsg(senderName);
                }
                mail.setSenderName(senderName);
                mail.setSenderEmail(matcher.group(3));
                haveFrom = true;
                continue;
            }
            matcher = haveReplyTo ? null : replyToPattern.matcher(list.get(i));
            if (!haveReplyTo && matcher.matches()) {
                String replyToName = matcher.group(1).trim();
                codeMatcher = mimeCodePattern.matcher(replyToName);
                if (codeMatcher.matches()) {
                    replyToName = getDecodedMsg(replyToName);
                }
                mail.setReplyToName(replyToName);
                mail.setSenderEmail(matcher.group(3));
                haveReplyTo = true;
                continue;
            }
            matcher = haveTo ? null : toPattern.matcher(list.get(i));
            if (!haveTo && matcher.matches()) {
                String receiverName = matcher.group(1).trim();
                codeMatcher = mimeCodePattern.matcher(receiverName);
                if (codeMatcher.matches()) {
                    receiverName = getDecodedMsg(receiverName);
                }
                mail.setReceiverName(receiverName);
                String receiverEmail = matcher.group(3) == null ? receiverName : matcher.group(3);
                mail.setSenderEmail(receiverEmail);
                haveTo = true;
                continue;
            }
            matcher = haveSubject ? null : subjectPattern.matcher(list.get(i));
            if (!haveSubject && matcher.matches()) {
                StringBuilder stringBuilder = new StringBuilder();
                String tmpString = matcher.group(1);
                codeMatcher = mimeCodePattern.matcher(tmpString);
                if (!codeMatcher.matches()) { stringBuilder.append(tmpString); }
                while (codeMatcher.matches()) {
                    tmpString = getDecodedMsg(tmpString);
                    stringBuilder.append(tmpString);
                    i++;
                    tmpString = list.get(i).trim();
                    codeMatcher = mimeCodePattern.matcher(tmpString);
                }
                mail.setSubject(stringBuilder.toString());
                haveSubject = true;
            }
        }
        return mail;
    }

    public Result receiveMails() {
        Result result = new Result(0);
        // 设置POP3服务器
        if (setServerInfo(username).getCode() != 250) {
            System.out.println(result.getCode());
            return result;
        }
        result = POP3Client();
        if (result.getCode() != 250) {
            System.out.println(result.getCode());
            return result;
        }
        List<BriefMail> list = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(popSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(popSocket.getOutputStream()));
            user(username, in, out);
            pass(password,in,out);
            int mailNum = stat(in, out);
            BriefMail mail;
            for (int i = 1; i <= mailNum; i++) {
                mail = getBriefMailInfo(top(i, 0, in, out));
                list.add(mail);
            }
            quit(in, out);
        } catch (IOException e) {
            printErrorString(e.getMessage());
        }
        result.setCode(250);
        return result;
    }

    public static void main(String[] args) {
        Receiver receiver = new Receiver("", "");
        receiver.receiveMails();
    }
}
