package com.cse10.crawler.contentHandler;

import com.cse10.article.Article;
import com.cse10.filter.LengthFilter;
import edu.uci.ics.crawler4j.crawler.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TharinduWijewardane on 10.07.2014.
 */
public abstract class BasicContentHandler {

    protected List<Article> articles;

    public BasicContentHandler() {
        articles = new ArrayList<Article>();
    }

    /**
     * to be overridden
     */
    public abstract List extractArticles(Page page);

    protected boolean filterArticles(String content) {

        if (content == null) {
            return false;
        }

        // length filter
        if (!LengthFilter.filterContent(content)) {
            System.out.println("****** Filtered out due to low length of content ****** Content: " + content);
            return false;
        }

        // keywords filter // turned off
//        if (!KeywordsFilter.filterContent(content)) {
//            return false;
//        }

        return true;
    }

}
