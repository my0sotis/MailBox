package com.mail.MailClient.controller;

import com.mail.MailClient.entity.*;

import java.io.*;
import java.net.Socket;
import java.util.*;
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
    // 所有邮件基本信息
    List<BriefMail> list = new ArrayList<>();
    //某一详细邮件
    DetailedMail mail = new DetailedMail();

    private final static Pattern messageIDPattern = Pattern.compile("^Message-ID:\\s?<([a-zA-Z0-9@\\-_.]*)>$");
    private final static Pattern datePattern = Pattern.compile("^Date:\\s?(.+)$");
    private final static Pattern fromPattern = Pattern.compile("^From:\\s?\"?([a-zA-Z0-9\\s=?/\\-.@]*)\"? (<(.+)>)?$");
    private final static Pattern replyToPattern =
            Pattern.compile("^Reply-To:\\s?\"?([a-zA-Z0-9\\s=?/\\-.@]*)\"? (<(.+)>)?$");
    private final static Pattern toPattern = Pattern.compile("^To:\\s?\"?([a-zA-Z0-9\\s=?/\\-.@]*)\"?\\s?(<(.+)>)?$");
    private final static Pattern subjectPattern = Pattern.compile("^Subject:\\s?(.+)$");
    private final static Pattern mimeCodePattern = Pattern.compile("^=\\?([a-zA-Z0-9\\-]*)\\?([QBqb])\\?(.+)\\?=$");
    private final static Pattern contentTypePattern =
            Pattern.compile("^Content-Type:\\s?([a-zA-Z/]*);\\s?(charset=\"?([a-zA-Z0-9\\-]*)\"?)?$");
    private final static Pattern boundaryPattern = Pattern.compile("^boundary=\"([a-zA-z_.0-9\\-=]*)\";?$");
    private final static Pattern contentTransferEncodingPattern =
            Pattern.compile("^Content-Transfer-Encoding:\\s?([a-zA-Z0-9\\-]*)$");
    private final static Pattern textPattern = Pattern.compile("^text/([a-zA-z]*)$");
    private final static Pattern multipartPattern = Pattern.compile("^multipart/([a-zA-z]*)$");
    private final static Pattern charsetPattern = Pattern.compile("^charset=\"?([a-zA-Z0-9\\-]*)\"?;?$");
    private final static Pattern namePattern = Pattern.compile("^name=\"([a-zA-Z0-9.@]*)\"$");
    private final static Pattern contentIDPattern = Pattern.compile("^Content-ID:\\s?<([a-zA-Z0-9@.]*)>$");

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
                result.setCode(200);
                break;
            }
        }
        if (result.getCode() != 200) {
            result.setCode(4);
        }
        return result;
    }

    private Result POP3Client() {
        Result result = new Result(200);
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

    /**
     * 发送指令到服务器
     * @param str 指令
     * @param in 输入流
     * @param out 输出流
     * @return 收到接收
     * @throws IOException 报错
     */
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

    /**
     * 登录邮件账号
     * @param user 账号名
     * @param in 输入流
     * @param out 输出流
     * @throws IOException 报错
     */
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

    /**
     * 登录邮箱密码
     * @param password 密码
     * @param in 输入流
     * @param out 输出流
     * @throws IOException 报错
     */
    public void pass(String password, BufferedReader in, BufferedWriter out) throws IOException {
        String result;
        result = getResult(sendServer("pass " + password, in, out));
        if (!OK.equals(result)) {
            throw new IOException("5");
        }
    }

    /**
     * 获取邮件数目
     * @param in 输入流
     * @param out 输出流
     * @return 邮件数目
     * @throws IOException 报错
     */
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

    /**
     * top指令
     * @param mailNum 邮件编号
     * @param lineNum 要展示的行数
     * @param in 输入流
     * @param out 输出流
     * @return 对应邮件前lineNum行的数据
     * @throws IOException 报错
     */
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
     * 根据下标删除对应邮件
     * @param index 邮件索引
     * @return 返回执行结果类
     */
    public Result deleteMailAt(int index) {
        // Test Mode
        setServerInfo(username);
        Result result = POP3Client();
        if (result.getCode() != 200) {
            System.out.println(result.getCode());
            return result;
        }
        try {
            // Initialize input and output streams
            BufferedReader in = new BufferedReader(new InputStreamReader(popSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(popSocket.getOutputStream()));
            // Login
            user(username, in, out);
            pass(password,in,out);
            int mailNum = stat(in, out);
            if (index > mailNum || index <= 0) {
                result.setCode(10);
                return result;
            }
            dele(index, in, out);
            quit(in, out);
            in.close();
            out.close();
        } catch (IOException e) {
            printErrorString(e.getMessage());
        }
        return result;
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
                return CodeHelper.QPConvertCharset(matcher.group(3).getBytes(), matcher.group(1));
            } else {
                return CodeHelper.Base64ConvertCharset(matcher.group(3), matcher.group(1));
            }
        }
        return null;
    }

    /**
     * 获取信件基本信息
     * @param index 信件的序号
     * @param mailInfo 邮件头部信息
     * @return 邮件基本信息
     */
    private BriefMail getBriefMailInfo(int index, String mailInfo) {
        BriefMail mail = new BriefMail(index);
        String[] strings = mailInfo.split("\n");
        List<String> list = new ArrayList<>();
        for (String string : strings) {
            list.add(string.trim());
        }
        int i = 0;
        // 是否获取到Message ID
        boolean haveMessageID = false;
        // 是否获取到日期、发件人、回复地址、收件人、主题
        boolean haveDate = false, haveFrom = false, haveReplyTo = false, haveTo = false, haveSubject = false;
        // 是否获取到编码类型、分隔符、内容传送编码
        boolean haveContentType = false, haveBoundary = false, haveContentTransferEncoding = false, haveCharset = false;
        // 日期
        Matcher matcher, codeMatcher;
        for (; i < list.size(); i++) {
            // 获取日期信息，此处三目运算符为了防止资源浪费，添加开销
            matcher = haveDate ? null : datePattern.matcher(list.get(i));
            if (!haveDate && matcher.matches()) {
                mail.setDatetime(matcher.group(1));
                haveDate = true;
                continue;
            }
            matcher = haveMessageID ? null : messageIDPattern.matcher(list.get(i));
            if (!haveMessageID && matcher.matches()) {
                mail.setMessageID(matcher.group(1));
                haveMessageID = true;
                continue;
            }
            // 获取发件人信息
            matcher = haveFrom ? null : fromPattern.matcher(list.get(i));
            if (!haveFrom && matcher.matches()) {
                String senderName = matcher.group(1).trim();
                // 如果发件人名被加密，则进行解码
                codeMatcher = mimeCodePattern.matcher(senderName);
                if (codeMatcher.matches()) {
                    senderName = getDecodedMsg(senderName);
                }
                // 设置对应信息
                mail.setSenderName(senderName);
                mail.setSenderEmail(matcher.group(3));
                haveFrom = true;
                continue;
            }
            // 获取回复地址信息
            matcher = haveReplyTo ? null : replyToPattern.matcher(list.get(i));
            if (!haveReplyTo && matcher.matches()) {
                String replyToName = matcher.group(1).trim();
                // 如果回复地址名被加密，则进行解码
                codeMatcher = mimeCodePattern.matcher(replyToName);
                if (codeMatcher.matches()) {
                    replyToName = getDecodedMsg(replyToName);
                }
                // 设置对应信息
                mail.setReplyToName(replyToName);
                mail.setReplyToEmail(matcher.group(3));
                haveReplyTo = true;
                continue;
            }
            // 获取收件人信息
            matcher = haveTo ? null : toPattern.matcher(list.get(i));
            if (!haveTo && matcher.matches()) {
                String receiverName = matcher.group(1).trim();
                // 如果收件人名被加密，则进行解码
                codeMatcher = mimeCodePattern.matcher(receiverName);
                if (codeMatcher.matches()) {
                    receiverName = getDecodedMsg(receiverName);
                }
                mail.setReceiverName(receiverName);
                // 有可能出现省略收件人邮件的情况，将收件人名赋予即可
                String receiverEmail = matcher.group(3) == null ? receiverName : matcher.group(3);
                mail.setReceiverEmail(receiverEmail);
                haveTo = true;
                continue;
            }
            // 获取主题信息
            matcher = haveSubject ? null : subjectPattern.matcher(list.get(i));
            if (!haveSubject && matcher.matches()) {
                // 由于邮件主题可能放在多行，需要逐行向下扫描
                StringBuilder stringBuilder = new StringBuilder();
                String tmpString = matcher.group(1);
                // 主题内容可能有加密，每一行分别加密
                codeMatcher = mimeCodePattern.matcher(tmpString);
                // 如果没有加密，将原内容加入即可
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
                continue;
            }
            // 获取内容类型
            matcher = haveContentType ? null : contentTypePattern.matcher(list.get(i));
            if (!haveContentType && matcher.matches()) {
                // 获取ContentType信息
                mail.setContentType(matcher.group(1));
                if (!haveCharset) {
                    mail.setCharset(matcher.group(3));
                    haveCharset = !(matcher.group(3) == null);
                }
                haveContentType = true;
                continue;
            }
            matcher = haveBoundary ? null : boundaryPattern.matcher(list.get(i));
            if (!haveBoundary && matcher.matches()) {
                mail.setBoundary(matcher.group(1));
                haveBoundary = true;
                continue;
            }
            matcher = haveContentTransferEncoding ? null : contentTransferEncodingPattern.matcher(list.get(i));
            if (!haveContentTransferEncoding && matcher.matches()) {
                mail.setContentTransferEncoding(matcher.group(1));
                haveContentTransferEncoding = true;
                continue;
            }
            matcher = haveCharset ? null : charsetPattern.matcher(list.get(i));
            if (!haveCharset && matcher.matches()) {
                mail.setCharset(matcher.group(1));
                haveCharset = true;
            }
        }
        return mail;
    }

    /**
     * 获取所有邮件基本信息
     * @return 运行信息
     */
    public Result receiveMails() {
        Result result = new Result(0);
        // 设置POP3服务器
        if (setServerInfo(username).getCode() != 200) {
            System.out.println(result.getCode());
            return result;
        }
        result = POP3Client();
        if (result.getCode() != 200) {
            System.out.println(result.getCode());
            return result;
        }

        try {
            // Initialize input and output streams
            BufferedReader in = new BufferedReader(new InputStreamReader(popSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(popSocket.getOutputStream()));
            // Login
            user(username, in, out);
            pass(password,in,out);
            int mailNum = stat(in, out);
            BriefMail mail;
            for (int i = mailNum; i >= 1; i--) {
                mail = getBriefMailInfo(i, top(i, 0, in, out));
                mail.setNum(mailNum-i+1);
                list.add(mail);
            }
            quit(in, out);
            in.close();
            out.close();
        } catch (IOException e) {
            printErrorString(e.getMessage());
        }
        result.setCode(200);
        return result;
    }

    /**
     * 接收某一特定序号上的邮件
     * @param index 序号
     * @apiNote This Function should be execute after receiveMails()
     * it's convinced that popServer have been confirmed
     */
    public Result receiveMailAt(int index) {
        // Test Mode
        setServerInfo(username);
        Result result = POP3Client();
        if (result.getCode() != 200) {
            System.out.println(result.getCode());
            return result;
        }
        try {
            // Initialize input and output streams
            BufferedReader in = new BufferedReader(new InputStreamReader(popSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(popSocket.getOutputStream()));
            // Login
            user(username, in, out);
            pass(password,in,out);
            int mailNum = stat(in, out);
            if (index > mailNum || index <= 0) {
                result.setCode(10);
                return result;
            }
            String mailInfo = retr(index, in, out);
            quit(in, out);
            in.close();
            out.close();

            String[] mailInfos = mailInfo.split("\n");
            // 获取邮件头部信息
            StringBuilder mailHead = new StringBuilder();
            int i;
            for (i = 0; i < mailInfo.length(); i++) {
                if ("".equals(mailInfos[i])) {
                    break;
                }
                mailHead.append(mailInfos[i]).append("\n");
            }
            // 设置对应的基本信息
            mail.setBriefInfo(getBriefMailInfo(index, mailHead.toString()));
            // TODO 分析内容信息
            i += 1;
            if ("This is a multi-part message in MIME format.".equals(mailInfos[i])) {
                i += 2;
            }
            if (("--" + mail.getBriefInfo().getBoundary()).equals(mailInfos[i].trim())) {
                i++;
            }
            List<String> mailInfoList = new ArrayList<>();
            for (; i < mailInfos.length; i++) {
                mailInfoList.add(mailInfos[i]);
            }
            // 分析邮件内容
            analyzeMailInfo(mail, mailInfoList, mail.getBriefInfo().getBoundary(), null, null, null, null);
        } catch (IOException e) {
            printErrorString(e.getMessage());
        }
        return result;
    }

    private static String lastType = "";

    public static void analyzeMailInfo(DetailedMail mail, List<String> mailInfos, String boundary,
                                       String contentType, String charset, String encoding,
                                       String name) throws IOException {
        // 获取内容类型
        Matcher textMatcher, multipartMatcher;
        if (contentType == null) {
            textMatcher = textPattern.matcher(mail.getBriefInfo().getContentType());
            multipartMatcher = multipartPattern.matcher(mail.getBriefInfo().getContentType());
        } else {
            textMatcher = textPattern.matcher(contentType);
            multipartMatcher = multipartPattern.matcher(contentType);
        }
        StringBuilder mailContents = new StringBuilder();
        int i = 0;
        if (textMatcher.matches()) {
            // 默认为UTF-8
            if (charset == null && encoding == null) {
                charset = mail.getBriefInfo().getCharset() == null ? "UTF-8" : mail.getBriefInfo().getCharset();
                encoding = mail.getBriefInfo().getContentTransferEncoding();
            }
            charset = charset == null ? "UTF-8" : charset;
            String mailContent;
            while (i < mailInfos.size()) {
                // 去除行尾多余的符号
                mailContent = mailInfos.get(i).trim();
                if (mailContent.endsWith("=")) {
                    mailContent = mailContent.substring(0, mailContent.length() - 1);
                }
                mailContents.append(mailContent);
                i++;
            }
            // 根据传输类型 选择Base64编码或者quoted-printable编码
            if (!"".equals(mailContents.toString())) {
                if ("quoted-printable".equalsIgnoreCase(encoding)) {
                    mailContent = CodeHelper.QPConvertCharset(mailContents.toString().getBytes(), charset);
                } else if ("base64".equalsIgnoreCase(encoding)) {
                    mailContent = CodeHelper.Base64ConvertCharset(mailContents.toString(), charset);
                } else {
                    mailContent = mailContents.toString();
                }
            } else {
                mailContent = "";
            }
            // 得到结果
            mail.addContent(mailContent);
        } else if (multipartMatcher.matches()) {
            String type = multipartMatcher.group(1);
            Matcher matcher;
            String subContentType = "", subCharset = "", subEncoding = "", subBoundary = "", subName;
            boolean haveContentType = false, haveCharset = false, haveEncoding = false, haveBoundary = false;
            boolean haveName;
            List<String> subContent = new ArrayList<>();
            // 如果为
            if ("alternative".equals(type)) {
                // 跳过分界线
                if (("--" + boundary).equals(mailInfos.get(i))) {
                    i++;
                }
                // 遍历类型
                for (; i < mailInfos.size() && !mailInfos.get(i).trim().equals(""); i++) {
                    matcher = haveContentType ? null : contentTypePattern.matcher(mailInfos.get(i));
                    if (!haveContentType && matcher.matches()) {
                        subContentType = matcher.group(1);
                        if (!haveCharset) {
                            subCharset = matcher.group(3);
                            haveCharset = !(matcher.group(3) == null);
                        }
                        haveContentType = true;
                        continue;
                    }
                    matcher = haveCharset ? null : charsetPattern.matcher(mailInfos.get(i).trim());
                    if (!haveCharset && matcher.matches()) {
                        subCharset = matcher.group(1);
                        haveCharset = true;
                        continue;
                    }
                    matcher = haveEncoding ? null : contentTransferEncodingPattern.matcher(mailInfos.get(i).trim());
                    if (!haveEncoding && matcher.matches()) {
                        subEncoding = matcher.group(1);
                        haveEncoding = true;
                        continue;
                    }
                    matcher = haveBoundary ? null : boundaryPattern.matcher(mailInfos.get(i).trim());
                    if (!haveBoundary && matcher.matches()) {
                        subBoundary = matcher.group(1);
                        haveBoundary = true;
                    }
                }
                for (i += 1; i < mailInfos.size(); i++) {
                    if (("--" + boundary).equals(mailInfos.get(i))) {
                        break;
                    }
                    subContent.add(mailInfos.get(i));
                }
                analyzeMailInfo(mail, subContent, subBoundary, subContentType, subCharset, subEncoding, null);
                // 跳过分界线
                i++;
                // 获取内容类型
                // 重置标志位
                haveContentType = false;
                subContentType = "";
                haveCharset = false;
                subCharset = "";
                haveEncoding = false;
                subEncoding = "";
                haveBoundary = false;
                subBoundary = "";
                for (; i < mailInfos.size() && !mailInfos.get(i).trim().equals(""); i++) {
                    matcher = haveContentType ? null : contentTypePattern.matcher(mailInfos.get(i));
                    if (!haveContentType && matcher.matches()) {
                        subContentType = matcher.group(1);
                        if (!haveCharset) {
                            subCharset = matcher.group(3);
                            haveCharset = !(matcher.group(3) == null);
                        }
                        haveContentType = true;
                        continue;
                    }
                    matcher = haveCharset ? null : charsetPattern.matcher(mailInfos.get(i));
                    if (!haveCharset && matcher.matches()) {
                        subCharset = matcher.group(1);
                        haveCharset = true;
                        continue;
                    }
                    matcher = haveEncoding ? null : contentTransferEncodingPattern.matcher(mailInfos.get(i));
                    if (!haveEncoding && matcher.matches()) {
                        subEncoding = matcher.group(1);
                        haveEncoding = true;
                        continue;
                    }
                    matcher = haveBoundary ? null : boundaryPattern.matcher(mailInfos.get(i));
                    if (!haveBoundary && matcher.matches()) {
                        subBoundary = matcher.group(1);
                        haveBoundary = true;
                    }
                }
                subContent.clear();
                for (i += 1; i < mailInfos.size(); i++) {
                    if (("--" + boundary + "--").equals(mailInfos.get(i))) {
                        break;
                    }
                    subContent.add(mailInfos.get(i));
                }
                analyzeMailInfo(mail, subContent, subBoundary, subContentType, subCharset, subEncoding, null);
            } else if ("related".equals(type) || "mixed".equals(type)) {
                boolean isOver = false;
                // 获取内容类型
                do {
                    // 重置标志位
                    haveContentType = false;
                    subContentType = "";
                    haveCharset = false;
                    subCharset = "";
                    haveEncoding = false;
                    subEncoding = "";
                    haveBoundary = false;
                    subBoundary = "";
                    haveName = false;
                    subName = "";
                    // 获取对应信息
                    for (; i < mailInfos.size() && !mailInfos.get(i).trim().equals(""); i++) {
                        matcher = haveContentType ? null : contentTypePattern.matcher(mailInfos.get(i));
                        if (!haveContentType && matcher.matches()) {
                            subContentType = matcher.group(1);
                            if (!haveCharset) {
                                subCharset = matcher.group(3);
                                haveCharset = !(matcher.group(3) == null);
                            }
                            haveContentType = true;
                            continue;
                        }
                        matcher = haveCharset ? null : charsetPattern.matcher(mailInfos.get(i));
                        if (!haveCharset && matcher.matches()) {
                            subCharset = matcher.group(1);
                            haveCharset = true;
                            continue;
                        }
                        matcher = haveEncoding ? null : contentTransferEncodingPattern.matcher(mailInfos.get(i).trim());
                        if (!haveEncoding && matcher.matches()) {
                            subEncoding = matcher.group(1);
                            haveEncoding = true;
                            continue;
                        }
                        matcher = haveBoundary ? null : boundaryPattern.matcher(mailInfos.get(i).trim());
                        if (!haveBoundary && matcher.matches()) {
                            subBoundary = matcher.group(1);
                            haveBoundary = true;
                            continue;
                        }
                        matcher = haveName ? null : namePattern.matcher(mailInfos.get(i).trim());
                        if (!haveName && matcher.matches()) {
                            subName = matcher.group(1);
                            haveName = true;
                        }
                    }
                    subContent.clear();
                    for (i += 1; i < mailInfos.size(); i++) {
                        if (("--" + boundary).equals(mailInfos.get(i))) {
                            break;
                        }
                        if (("--" + boundary + "--").equals(mailInfos.get(i))) {
                            isOver = true;
                            break;
                        }
                        subContent.add(mailInfos.get(i));
                    }
                    lastType = type;
                    analyzeMailInfo(mail, subContent, subBoundary, subContentType, subCharset, subEncoding, subName);
                } while (!isOver);
            }
        } else {
            StringBuilder fileContent = new StringBuilder();
            for (; i < mailInfos.size(); i++) {
                if (!"".equals(mailInfos.get(i))) {
                    fileContent.append(mailInfos.get(i));
                } else {
                    break;
                }
            }
            byte[] result;
            if ("base64".equalsIgnoreCase(encoding)) {
                result = CodeHelper.Base64Decode(fileContent.toString());
            } else if ("quoted-printable".equalsIgnoreCase(encoding)) {
                result = CodeHelper.QPConvertCharset(fileContent.toString().getBytes(), "UTF-8").getBytes();
            } else {
                result = fileContent.toString().getBytes();
            }
            StringBuilder fileLocation = new StringBuilder();
            fileLocation.append("./").append(mail.getBriefInfo().getMessageID()).append("/").append(lastType);
            File dir = new File(fileLocation.toString());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            fileLocation.append("/").append(name);
            File file = new File(fileLocation.toString());
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStream out = new FileOutputStream(file);
            out.write(result);
            out.close();
            lastType = "";
        }
    }

}