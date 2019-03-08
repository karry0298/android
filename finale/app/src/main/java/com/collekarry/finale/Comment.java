package com.collekarry.finale;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {
    private String writer;
    private String content;
    private Date date;

    public Comment() {
    }

    public Comment(String writer, String content, Date date) {
        this.writer = writer;
        this.content = content;
        this.date = date;
    }

    public String getWriter() {
        return writer;
    }
    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
}

