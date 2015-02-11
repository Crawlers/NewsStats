package com.cse10.database;

import com.cse10.article.*;
import com.cse10.entities.CrimeEntityGroup;
import com.cse10.entities.CrimePerson;
import com.cse10.entities.LocationDistrictMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by Tharindu on 2014-11-14.
 */

/**
 * store constants related to database
 */
public class DatabaseConstants {

    public static String DB_USERNAME;
    public static String DB_PASSWORD;
    public static String DB_URL; // there is a entry in DatabaseUtils.props file (top level) that does not use this

    public static String WEBGUIDB_USERNAME;
    public static String WEBGUIDB_PASSWORD;
    public static String WEBGUIDB_DATABASE;
    public static String WEBGUIDB_HOST;
    public static String WEBGUIDB_PORT;

    /* class to table name mappings */
    public static HashMap<Class, String> classToTableName;

    private static Properties prop;

    // runs the init method automatically when the class loads
    static {
        init();

        classToTableName = new HashMap<Class, String>();

        classToTableName.put(CeylonTodayArticle.class, "article_ceylon_today"); // these are not used in hibernate xml files
        classToTableName.put(DailyMirrorArticle.class, "article_daily_mirror");
        classToTableName.put(HiruNewsArticle.class, "article_hiru_news");
        classToTableName.put(NewsFirstArticle.class, "article_news_first");
        classToTableName.put(TheIslandArticle.class, "article_the_island");
        classToTableName.put(NewYorkTimesArticle.class, "article_new_york_times");

        classToTableName.put(CrimeArticle.class, "article_crime");
        classToTableName.put(TrainingArticle.class, "article_training");

        classToTableName.put(CrimePerson.class, "crime_person");
        classToTableName.put(CrimeEntityGroup.class, "crime_entity_group");
        classToTableName.put(LocationDistrictMapper.class, "location_district_mapper");
    }

    // load constants from dbConnection.properties file
    public static void init() {

        prop = new Properties();
        try {
            prop.load(DatabaseConstants.class.getResourceAsStream("/dbConnection.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        DB_USERNAME = prop.getProperty("hibernate.connection.username");
        DB_PASSWORD = prop.getProperty("hibernate.connection.password");
        DB_URL = prop.getProperty("hibernate.connection.url"); // there is a entry in DatabaseUtils.props file (top level) that does not use this

        WEBGUIDB_USERNAME = prop.getProperty("webguidb.connection.username");
        WEBGUIDB_PASSWORD = prop.getProperty("webguidb.connection.password");
        WEBGUIDB_DATABASE = prop.getProperty("webguidb.connection.database");
        WEBGUIDB_HOST = prop.getProperty("webguidb.connection.host");
        WEBGUIDB_PORT = prop.getProperty("webguidb.connection.port");
    }

    public DatabaseConstants() {

        classToTableName = new HashMap<Class, String>();

        classToTableName.put(CeylonTodayArticle.class, "article_ceylon_today"); // these are not used in hibernate xml files
        classToTableName.put(DailyMirrorArticle.class, "article_daily_mirror");
        classToTableName.put(HiruNewsArticle.class, "article_hiru_news");
        classToTableName.put(NewsFirstArticle.class, "article_news_first");
        classToTableName.put(TheIslandArticle.class, "article_the_island");
        classToTableName.put(NewYorkTimesArticle.class, "article_new_york_times");

        classToTableName.put(CrimeArticle.class, "article_crime");
        classToTableName.put(TrainingArticle.class, "article_training");

        classToTableName.put(CrimePerson.class, "crime_person");
        classToTableName.put(CrimeEntityGroup.class, "crime_entity_group");
        classToTableName.put(LocationDistrictMapper.class, "location_district_mapper");
    }

}
