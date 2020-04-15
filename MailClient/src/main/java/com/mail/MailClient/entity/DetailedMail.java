package com.mail.MailClient.entity;

import java.util.ArrayList;
import java.util.List;

public class DetailedMail {
    private BriefMail briefInfo;
    private StringBuilder content = new StringBuilder();
    private List<String> attachments = new ArrayList<>();

    public void setBriefInfo(BriefMail briefInfo) {
        this.briefInfo = briefInfo;
    }

    public BriefMail getBriefInfo() {
        return briefInfo;
    }

    public void setContent(StringBuilder content) {
        this.content = content;
    }

    public void addContent(String addition) {
        this.content.append(addition);
    }

    public String getContent() {
        return content.toString();
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    public void addAttachment(String attachment) {
        this.attachments.add(attachment);
    }

    public List<String> getAttachments() {
        return attachments;
    }
}
