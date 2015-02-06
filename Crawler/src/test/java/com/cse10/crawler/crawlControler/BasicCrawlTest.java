package com.cse10.crawler.crawlControler;

import com.cse10.database.DatabaseConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class BasicCrawlTest {

    public static void main(String[] args) {
        changeDB();
    }

    /**
     * change the database at runtime. dbConnection.properties file in "target" directory will change.
     * the file in src directory will not change. so changed name exist only till the next compilation of the project
     */
    protected static void changeDB() {

        String resourceName = "/dbConnection.properties";
        String propertyName = "hibernate.connection.url";

        Properties prop = new Properties();
        try {
            prop.load(DatabaseConstants.class.getResourceAsStream(resourceName));

            prop.setProperty(propertyName, "jdbc:mysql://localhost:3306/newsstats_test");

            URL url = DatabaseConstants.class.getResource(resourceName);
            prop.store(new FileOutputStream(new File(url.toURI())), null);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

}
