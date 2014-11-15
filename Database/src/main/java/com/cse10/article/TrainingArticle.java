package com.cse10.article;

import java.util.Date;

/**
 * Created by Tharindu on 2014-11-14.
 */
public class TrainingArticle extends Article {

    private String newspaper;

    public TrainingArticle() {
    }

    public TrainingArticle(int id, String title, String content, String author,
                           Date createdDate, String newspaper) {
        super(id, title, content, author, createdDate);
        this.newspaper = newspaper;
    }

    public String getNewspaper() {
        return newspaper;
    }

    public void setNewspaper(String newspaper) {
        this.newspaper = newspaper;
    }

}
