package com.mail.MailClient.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mavericks
 */
public class DetailedMail {
    private BriefMail briefInfo;
    private StringBuilder content = new StringBuilder();
    private List<String> attachments = new ArrayList<>();
    private List<String> resources = new ArrayList<>();

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

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    public void setResource(List<String> resources) {
        this.resources = resources;
    }

    public void addResource(String resource) {
        this.resources.add(resource);
    }

    public void addAttachment(String attachment) {
        this.attachments.add(attachment);
    }

    public String getContent() {
        return content.toString();
    }

    public List<String> getResources() {
        return resources;
    }

    public List<String> getAttachments() {
        return attachments;
    }
}
