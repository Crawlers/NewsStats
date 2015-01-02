package com.cse10.util;

import com.cse10.article.Article;
import com.cse10.article.CrimeArticle;
import com.cse10.article.TrainingArticle;
import com.cse10.database.DatabaseConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TharinduWijewardane on 2014-11-15.
 */

/**
 * to convert articles into crime articles or training articles
 */
public class ArticleConverter {

    /**
     *
     * @param articles
     * @param articleClass Class of input articles
     * @return
     */
    public static List<TrainingArticle> convertToTrainingArticle(List<Article> articles, Class articleClass) {

        String tableName = new DatabaseConstants().classToTableName.get(articleClass); // to set newspaper value
        List<TrainingArticle> trainingArticles = new ArrayList<TrainingArticle>();

        for (Article article : articles) {
            TrainingArticle trainingArticle = new TrainingArticle();
            trainingArticle.setTitle(article.getTitle());
            trainingArticle.setContent(article.getContent());
            trainingArticle.setAuthor(article.getAuthor());
            trainingArticle.setCreatedDate(article.getCreatedDate());
            trainingArticle.setNewspaper(tableName);
            trainingArticle.setLabel(article.getLabel());

            trainingArticles.add(trainingArticle);
        }

        return trainingArticles;
    }

    /**
     *
     * @param articles
     * @param articleClass Class of input articles
     * @return
     */
    public static List<CrimeArticle> convertToCrimeArticle(List<Article> articles, Class articleClass) {

        String tableName = new DatabaseConstants().classToTableName.get(articleClass); // to set newspaper value
        List<CrimeArticle> crimeArticles = new ArrayList<CrimeArticle>();

        for (Article article : articles) {
            CrimeArticle crimeArticle = new CrimeArticle();
            crimeArticle.setTitle(article.getTitle());
            crimeArticle.setContent(article.getContent());
            crimeArticle.setAuthor(article.getAuthor());
            crimeArticle.setCreatedDate(article.getCreatedDate());
            crimeArticle.setNewspaper(tableName);
            crimeArticle.setLabel("crime");

            crimeArticles.add(crimeArticle);
        }

        return crimeArticles;
    }

}
