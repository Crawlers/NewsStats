package com.cse10.classifier;

import com.cse10.article.*;
import com.cse10.database.DatabaseConstants;
import com.cse10.database.DatabaseHandler;

import java.util.List;

/**
 * Created by TharinduWijewardane on 2014-11-15.
 */

/**
 * For manually managing tables
 */
public class TableManager {

    public static void main(String[] args) {

//        copyToTrainingTable();

    }

    private static void copyToTrainingTable(){

        Class articleClass = CeylonTodayArticle.class; // specify the source here

        List<Article> articles = DatabaseHandler.fetchArticlesByIdRange(articleClass, 1, 1102); // specify the id range here
        System.out.println("number of articles fetched: " + articles.size());

        String tableName = new DatabaseConstants().classToTableName.get(articleClass);

        // to prepare them as training articles
        List<TrainingArticle> trainingArticles = ArticleConverter.convertToTrainingArticle(articles, articleClass);
        System.out.println("number of training articles: " + trainingArticles.size());

        DatabaseHandler.insertArticles(trainingArticles);

    }

}
