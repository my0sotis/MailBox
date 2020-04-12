package com.mail.MailClient.entity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        String[] strings = mailInfo.split("\n");
        List<String> list = new ArrayList<>();
        for (String string : strings) {
            if (!string.equals("")) {
                list.add(string.trim());
            }
        }
        int i = 0;
        // 是否获取到日期、发件人、回复地址、收件人、主题
        boolean haveDate = false, haveFrom = false, haveReplyTo = false, haveTo = false, haveSubject = false;
        // 日期
        Pattern datePattern = Pattern.compile("Date: (.+)");
        Pattern fromPattern = Pattern.compile("From: ([\\s\\S]*)<(.+)>");
        Pattern replyToPattern = Pattern.compile("Reply-To: ([\\s\\S]*)<(.+)>");
        Pattern toPattern = Pattern.compile("To: ([\\s\\S]*)<(.+)>");
        Pattern subjectPattern = Pattern.compile("Subject: (.+)");
        Pattern mimeCodePattern = Pattern.compile("=?.+?[QB]");
        Matcher matcher;
        for (; i < list.size(); i++) {
            if (haveDate && haveFrom && haveTo && haveSubject || haveReplyTo) {
                break;
            }
            matcher = haveDate ? null : datePattern.matcher(list.get(i));
            if (!haveDate && matcher.matches()) {
                datetime = matcher.group(1);
                // Test
                System.out.println(datetime);
                haveDate = true;
                continue;
            }
            matcher = haveFrom ? null : fromPattern.matcher(list.get(i));
            if (!haveFrom && matcher.matches()) {
                senderName = matcher.group(1).trim();
                senderEmail = matcher.group(2);
                // Test
                System.out.println(senderName);
                System.out.println(senderEmail);
                // TODO-ITEM 可能会出现编码不一致的情况
                haveFrom = true;
                continue;
            }
            matcher = haveReplyTo ? null : replyToPattern.matcher(list.get(i));
            if (!haveReplyTo && matcher.matches()) {
                replyToName = matcher.group(1).trim();
                replyToEmail = matcher.group(2);
                // Test
                System.out.println(replyToName);
                System.out.println(replyToEmail);
                // TODO-ITEM 编码不一致！
                haveReplyTo = true;
                continue;
            }
            matcher = haveTo ? null : toPattern.matcher(list.get(i));
            if (!haveTo && matcher.matches()) {
                receiverName = matcher.group(1).trim();
                receiverEmail = matcher.group(2);
                // Test
                System.out.println(receiverName);
                System.out.println(receiverEmail);
                // TODO-ITEM 编码问题
                haveTo = true;
                continue;
            }
            matcher = haveSubject ? null : subjectPattern.matcher(list.get(i));
            if (!haveSubject && matcher.matches()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(matcher.group(1));
                System.out.println(stringBuilder);
                // TODO-ITEM 编码问题
                // TODO-ITEM 多行Subject问题
                haveSubject = true;
                continue;
            }
        }
        // Test
        for (String string : list) {
            System.out.println(string);
        }
    }

//    public String get

    public static void main(String[] args) throws IOException {
        File file = new File("./Samples/Sample1.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (line != null) {
            stringBuilder.append(line).append('\n');
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        ReceivedMail receivedMail = new ReceivedMail(stringBuilder.toString());
    }
}
