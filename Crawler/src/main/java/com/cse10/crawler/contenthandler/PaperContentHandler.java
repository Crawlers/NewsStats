package com.cse10.crawler.contenthandler;

import com.cse10.article.Article;
import edu.uci.ics.crawler4j.crawler.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TharinduWijewardane on 10.07.2014.
 */
public abstract class PaperContentHandler {

    protected List<Article> articles;

    public PaperContentHandler(){
        articles = new ArrayList<Article>();
    }

    /** to be overridden */
    public abstract List extractArticles(Page page);

}
