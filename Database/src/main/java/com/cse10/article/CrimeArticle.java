package com.cse10.article;

import java.util.Date;

/**
 * Created by Tharindu on 2014-11-14.
 */
public class CrimeArticle extends Article {

    private String newspaper;
    private int newspaperId;

    public CrimeArticle() {
    }

    public CrimeArticle(int id, String title, String content, String author,
                        Date createdDate, String newspaper, int newspaperId) {
        super(id, title, content, author, createdDate);
        this.newspaper = newspaper;
        this.newspaperId = newspaperId;
    }

    public String getNewspaper() {
        return newspaper;
    }

    public void setNewspaper(String newspaper) {
        this.newspaper = newspaper;
    }

    public int getNewspaperId() {
        return newspaperId;
    }

    public void setNewspaperId(int newspaperId) {
        this.newspaperId = newspaperId;
    }
}
