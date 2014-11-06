package com.cse10.crawler.contentHandler;

import com.cse10.article.Article;
import com.cse10.filter.KeywordsFilter;
import com.cse10.filter.LengthFilter;
import edu.uci.ics.crawler4j.crawler.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TharinduWijewardane on 10.07.2014.
 */
public abstract class PaperContentHandler {

    protected List<Article> articles;

    public PaperContentHandler() {
        articles = new ArrayList<Article>();
    }

    /**
     * to be overridden
     */
    public abstract List extractArticles(Page page);

    protected boolean filterArticles(String content) {

        // length filter
        if (!LengthFilter.filterContent(content)) {
            System.out.println("****** Filtered out due to low length of content ******");
            return false;
        }

        // keywords filter // turned off
//        if (!KeywordsFilter.filterContent(content)) {
//            return false;
//        }

        return true;
    }

}
