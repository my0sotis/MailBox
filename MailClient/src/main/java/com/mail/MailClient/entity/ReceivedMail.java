package com.mail.MailClient.entity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReceivedMail {
    private String datetime;
    private String senderName;
    private String senderEmail;
    private String replyToName;
    private String replyToEmail;
    private String receiverName;
    private String receiverEmail;

    public ReceivedMail(String mailInfo) {

        String[] strings = mailInfo.replaceAll(" {2}", "\r").split("\r");
        List<String> list = new ArrayList<>();
        for (String string : strings) {
            if (!string.equals("")) {
                list.add(string.trim());
            }
        }
        int i = 0;
        // 日期
        Pattern pattern = Pattern.compile("Date: (.+)");
        Matcher matcher;
        for (;i < list.size(); i++) {
            matcher = pattern.matcher(list.get(i));
            if (matcher.matches()) {
                datetime = matcher.group(1);
                System.out.println(datetime);
                break;
            }
        }
        // 发件人信息
        pattern = Pattern.compile("From: (.+) <(.+)>");
        i += 1;
        matcher = pattern.matcher(list.get(i));
        if (matcher.matches()) {
            senderName = matcher.group(1);
            senderEmail = matcher.group(2);
            System.out.println(senderName);
            System.out.println(senderEmail);
        }
        // 回复人邮箱
        i += 1;
        boolean haveReplyTo = false;
        pattern = Pattern.compile("Reply-To: (.+) <(.+)>");
        matcher = pattern.matcher(list.get(i));
        if (matcher.matches()) {
            replyToName = matcher.group(1);
            replyToEmail = matcher.group(2);
            System.out.println(replyToName);
            System.out.println(replyToEmail);
            haveReplyTo = true;
        }
        // 收件人信息
        if (haveReplyTo) {
            i += 1;
        }
        pattern = Pattern.compile("To: (.+) <(.+)>");
        matcher = pattern.matcher(list.get(i));
        if (matcher.matches()) {
            receiverName = matcher.group(1);
            receiverEmail = matcher.group(2);
            System.out.println(receiverName);
            System.out.println(receiverEmail);
        }
        // 主题
        pattern = Pattern.compile("Subject: (.+)");
        StringBuilder stringBuilder = new StringBuilder();
        for (i += 1; i < list.size(); i++) {
            matcher = pattern.matcher(list.get(i));
            if (matcher.matches()) {
                stringBuilder.append(matcher.group(1));
                break;
            }
        }
        // TODO-ITEM 以防有两行主题

        for (String string : list) {
            System.out.println(string);
        }
    }

    public void processDateFormat(String str) {
        String[] fragment = str.split(" ");

    }

    public static void main(String[] args) throws IOException {
        File file = new File("./Samples/Sample2.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (line != null) {
            stringBuilder.append(line);
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        ReceivedMail receivedMail = new ReceivedMail(stringBuilder.toString());
    }
}
