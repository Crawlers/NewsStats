package com.cse10.article;

/**
 * Created by TharinduWijewardane on 02.07.2014.
 */

import java.util.Date;

public abstract class Article implements java.io.Serializable {

    private int id;
    private String title;
    private String content;
    private String author;
    private Date createdDate;
    private String label; // for classification purpose

    public Article() {
    }

    public Article(int id, String title, String content, String author,
                   Date createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdDate = createdDate;
        this.label = "";
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
