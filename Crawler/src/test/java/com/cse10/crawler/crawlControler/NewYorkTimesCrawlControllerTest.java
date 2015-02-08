package com.cse10.crawler.crawlControler;

import com.cse10.crawler.paperCrawler.NewYorkTimesCrawler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewYorkTimesCrawlControllerTest extends BasicCrawlTest{

    NewYorkTimesCrawlController newYorkTimesCrawlController;
    String startDateString = "2014-01-01";
    String endDateString = "2014-07-01";
    Date startDate;
    Date endDate;

    @Before
    public void setUp() throws Exception {

        changeDB();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            startDate = sdf.parse(startDateString);
            endDate = sdf.parse(endDateString);
        } catch (ParseException e) {
            startDate = new Date(); // set today if fails
            endDate = new Date(); // set today if fails
            e.printStackTrace();
        }

        newYorkTimesCrawlController = new NewYorkTimesCrawlController();
        newYorkTimesCrawlController.setStartDate(startDate);
        newYorkTimesCrawlController.setEndDate(endDate);

//        String tableName = DatabaseConstants.classToTableName.get(NewYorkTimesArticle.class);
//        DatabaseHandler.executeUpdate("DELETE FROM " + tableName + " WHERE created_date = '" + dateString + "'");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCrawl() throws Exception {

//        TestCase.assertEquals(true, DatabaseHandler.getRowCount(NewYorkTimesArticle.class, "createdDate", date) == 0);
        newYorkTimesCrawlController.crawl(NewYorkTimesCrawler.class);
//        TestCase.assertEquals(true, DatabaseHandler.getRowCount(NewYorkTimesArticle.class, "createdDate", date) > 0);
    }
}
