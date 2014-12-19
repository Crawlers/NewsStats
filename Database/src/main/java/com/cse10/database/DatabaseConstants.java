package com.cse10.database;

import com.cse10.article.*;
import com.cse10.entities.CrimeEntities;

import java.util.HashMap;

/**
 * Created by Tharindu on 2014-11-14.
 */

/**
 * store constants related to database
 */
public class DatabaseConstants {

    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "";
    public static final String DB_URL = "jdbc:mysql://localhost:3306/crawler_my"; // there is a entry in DatabaseUtils.props file (top level) that does not use this

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

        classToTableName.put(CrimeEntities.class, "crime_entities");
    }

}
