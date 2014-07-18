package com.cse10.crawler.paperCrawler;

import com.cse10.article.Article;
import com.cse10.crawler.contentHandler.NewsFirstContentHandler;
import com.cse10.crawler.contentHandler.PaperContentHandler;
import com.cse10.crawler.crawlControler.NewsFirstCrawlController;
import com.cse10.database.DatabaseHandler;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.List;

/**
 * Created by TharinduWijewardane on 17.07.2014.
 */
public class NewsFirstCrawler extends BasicCrawler {

    private PaperContentHandler paperContentHandler;

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     */
    @Override
    public boolean shouldVisit(WebURL url) {
        String href = url.getURL().toLowerCase();
        return super.shouldVisit(url) && href.startsWith("http://newsfirst.lk" + NewsFirstCrawlController.current_path) && href.length() > 50;
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
        super.visit(page);
        System.out.println("=============");
        System.out.println("********* inside if news first ***********");
        paperContentHandler = new NewsFirstContentHandler();
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
