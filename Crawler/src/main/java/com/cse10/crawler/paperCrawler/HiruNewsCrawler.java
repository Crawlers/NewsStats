package com.cse10.crawler.paperCrawler;

import com.cse10.article.Article;
import com.cse10.crawler.contentHandler.HiruNewsContentHandler;
import com.cse10.crawler.contentHandler.BasicContentHandler;
import com.cse10.database.DatabaseHandler;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.List;

/**
 * Created by Tharindu on 2014-11-13.
 */
public class HiruNewsCrawler extends BasicCrawler {

    private BasicContentHandler basicContentHandler;

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     */
    @Override
    public boolean shouldVisit(WebURL url) {
        String href = url.getURL().toLowerCase();
        boolean condition = href.startsWith("http://www.hirunews.lk/") && !href.endsWith(".php") && !href.endsWith(".css") && href.length() > 40;
        if (href.startsWith("http://www.hirunews.lk/robots.txt")) {
            condition = true;
        }
        return super.shouldVisit(url) && condition;
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
        super.visit(page);
        System.out.println("=============");
        System.out.println("********* inside if hiru news ***********");
        basicContentHandler = new HiruNewsContentHandler();
        List<Article> articles = basicContentHandler.extractArticles(page);

        for (Article article : articles) {
            System.out.println("***********************************start");
            System.out.println(article.getContent());
            if (!article.getContent().equals(""))
                DatabaseHandler.insertArticle(article);
            System.out.println("***********************************end");
        }
    }


}
