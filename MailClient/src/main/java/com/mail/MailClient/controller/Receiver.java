package com.mail.MailClient.controller;

import com.mail.MailClient.entity.POPServer;
import com.mail.MailClient.entity.Result;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author Mavericks
 */
public class Receiver {
    private final String OK = "+OK";
    private String username;
    private String password;
    private Map.Entry<String, Integer> pop;
    private Socket popSocket;
    boolean isDebug = false;

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


    public void listOne(int mailNum, BufferedReader in, BufferedWriter out) throws IOException {
        String result;
        result = getResult(sendServer("list " + mailNum, in, out));
        if (!OK.equals(result)) {
            // list 出错
            throw new IOException("7");
        }
    }

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

    public String retr(int mailNum, BufferedReader in, BufferedWriter out) throws IOException, InterruptedException {
        String result;
        result = getResult(sendServer("retr " + mailNum, in, out));
        if (!OK.equals(result)) {
            // 接收邮件时出错
            throw new IOException("8");
        }
        result = getMessageDetail(in);
        Thread.sleep(3000);
        return result;
    }

    public void quit(BufferedReader in, BufferedWriter out) throws IOException {
        String result;
        result = getResult(sendServer("QUIT", in, out));
        if (!OK.equals(result)) {
            // 未能正确退出
            throw new IOException("9");
        }
    }

    public Result receiveMail() throws IOException {
        Result result = new Result(0);
        // 设置POP3服务器
        if (setServerInfo(username).getCode() != 250) {
            return result;
        }
        result = POP3Client();
        if (result.getCode() != 250) {
            return result;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(popSocket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(popSocket.getOutputStream()));
        user(username, in, out);
        pass(password,in,out);

        result.setCode(250);
        return result;
    }
}
