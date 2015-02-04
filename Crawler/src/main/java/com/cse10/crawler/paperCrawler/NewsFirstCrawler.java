package com.cse10.crawler.paperCrawler;

import com.cse10.article.Article;
import com.cse10.crawler.contentHandler.NewsFirstContentHandler;
import com.cse10.crawler.contentHandler.BasicContentHandler;
import com.cse10.crawler.crawlControler.NewsFirstCrawlController;
import com.cse10.database.DatabaseHandler;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.List;

/**
 * Created by TharinduWijewardane on 17.07.2014.
 */
public class NewsFirstCrawler extends BasicCrawler {

    private BasicContentHandler basicContentHandler;

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
        logger.info("=============");
        logger.info("********* inside if news first ***********");
        basicContentHandler = new NewsFirstContentHandler();
        List<Article> articles = basicContentHandler.extractArticles(page);

        for (Article article : articles) {
            logger.info("***********************************start");
            logger.info(article.getContent());
            if (!article.getContent().equals(""))
                DatabaseHandler.insertArticle(article);
            logger.info("***********************************end");
        }
    }

}
