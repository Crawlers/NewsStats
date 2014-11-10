package com.cse10.crawler.paperCrawler;

/**
 * Created by TharinduWijewardane on 29.06.2014.
 */

import com.cse10.article.Article;
import com.cse10.crawler.contentHandler.TheIslandContentHandler;
import com.cse10.crawler.contentHandler.PaperContentHandler;
import com.cse10.database.DatabaseHandler;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.List;

public class TheIslandCrawler extends BasicCrawler {

    private PaperContentHandler paperContentHandler;

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     */
    @Override
    public boolean shouldVisit(WebURL url) {
        String href = url.getURL().toLowerCase();
        boolean x =  super.shouldVisit(url) && href.startsWith("http://www.island.lk/index.php")
            && href.contains("code_title") && href.contains("page_cat=article-details") && href.contains("page=article-details");
        return x;
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
        super.visit(page);
        System.out.println("=============");
        System.out.println("********* sthe island ***********");
        paperContentHandler = new TheIslandContentHandler();
        List<Article> articles = paperContentHandler.extractArticles(page);

        for (Article article : articles) {
            System.out.println("***********************************start");
            System.out.println(article.getContent());
            if (!article.getContent().equals(""))
                DatabaseHandler.insertArticle(article);
            System.out.println("***********************************end");
        }
    }
}
