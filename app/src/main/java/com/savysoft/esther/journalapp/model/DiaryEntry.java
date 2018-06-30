package com.savysoft.esther.journalapp.model;


import java.util.Date;

public class DiaryEntry {
    private String title;
    private String description;
    private Date publishedAt;
    private Date upDatedAt;

    public DiaryEntry(String title, String description, Date publishedAt, Date upDatedAt) {
        this.title = title;
        this.description = description;
        this.publishedAt = publishedAt;
        this.upDatedAt = upDatedAt;
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

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Date getUpDatedAt() {
        return upDatedAt;
    }

    public void setUpDatedAt(Date upDatedAt) {
        this.upDatedAt = upDatedAt;
    }
}
