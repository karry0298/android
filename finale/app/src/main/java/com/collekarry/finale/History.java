package com.collekarry.finale;

import java.io.Serializable;
import java.util.List;

public class History implements Serializable {
    private String title;
    private String description;
    private String date;
    private List<String> attachments;
    private List<com.collekarry.finale.Comment> comments;

    public History() {
    }

    public History(String title, String description, String date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getAttachments() {
        return attachments;
    }
    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    public List<com.collekarry.finale.Comment> getComments() {
        return comments;
    }
    public void setComments(List<com.collekarry.finale.Comment> comments) {
        this.comments = comments;
    }
}
