package com.cse10.database;

import com.cse10.article.*;
import com.cse10.entities.CrimeEntityGroup;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by Tharindu on 2014-11-14.
 */

/**
 * store constants related to database
 */
public class DatabaseConstants {

    private static Properties prop;

    // load constants from dbConnection.properties file
    static {
        prop = new Properties();
        InputStream input = null;
        try {
            prop.load(DatabaseConstants.class.getResourceAsStream("/dbConnection.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final String DB_USERNAME = prop.getProperty("hibernate.connection.username");
    public static final String DB_PASSWORD = prop.getProperty("hibernate.connection.password");
    public static final String DB_URL = prop.getProperty("hibernate.connection.url"); // there is a entry in DatabaseUtils.props file (top level) that does not use this

    /* class to table name mappings */
    public HashMap<Class, String> classToTableName;

    public DatabaseConstants() {

        classToTableName = new HashMap<Class, String>();

        classToTableName.put(CeylonTodayArticle.class, "article_ceylon_today"); // these are not used in hibernate xml files
        classToTableName.put(DailyMirrorArticle.class, "article_daily_mirror");
        classToTableName.put(HiruNewsArticle.class, "article_hiru_news");
        classToTableName.put(NewsFirstArticle.class, "article_news_first");
        classToTableName.put(TheIslandArticle.class, "article_the_island");
        classToTableName.put(CrimeArticle.class, "article_crime");
        classToTableName.put(TrainingArticle.class, "article_training");

        classToTableName.put(CrimeEntityGroup.class, "crime_entity_group");
    }


}
