package com.cse10.crawler.crawlControler;

import com.cse10.database.DatabaseConstants;

public class BasicCrawlTest {

    /**
     * change the db into test database
     */
    protected static void changeDB() {

        DatabaseConstants.DB_URL = "jdbc:mysql://localhost:3306/newsstats_test";

    }

}
