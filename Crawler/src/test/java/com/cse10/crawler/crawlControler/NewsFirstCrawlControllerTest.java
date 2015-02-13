package com.cse10.crawler.crawlControler;

import com.cse10.article.NewsFirstArticle;
import com.cse10.crawler.paperCrawler.NewsFirstCrawler;
import com.cse10.database.DatabaseConstants;
import com.cse10.database.DatabaseHandler;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsFirstCrawlControllerTest extends BasicCrawlTest {

    NewsFirstCrawlController newsFirstCrawlController;
    String dateString = "2014-01-01";
    Date date;

    @Before
    public void setUp() throws Exception {

        changeDB();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            date = new Date(); // set today if fails
            e.printStackTrace();
        }

        newsFirstCrawlController = new NewsFirstCrawlController();
        newsFirstCrawlController.setStartDate(date);
        newsFirstCrawlController.setEndDate(date);

        String tableName = new DatabaseConstants().classToTableName.get(NewsFirstArticle.class);
        DatabaseHandler.executeUpdate("DELETE FROM " + tableName + " WHERE created_date = '" + dateString + "'");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCrawl() throws Exception {

        TestCase.assertTrue(DatabaseHandler.getRowCount(NewsFirstArticle.class, "createdDate", date) == 0);
        newsFirstCrawlController.crawl(NewsFirstCrawler.class);
        TestCase.assertTrue(DatabaseHandler.getRowCount(NewsFirstArticle.class, "createdDate", date) > 0);
    }
}
