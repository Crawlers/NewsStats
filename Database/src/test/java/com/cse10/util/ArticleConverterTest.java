package com.cse10.util;

import com.cse10.article.Article;
import com.cse10.article.CeylonTodayArticle;
import com.cse10.article.CrimeArticle;
import com.cse10.article.TrainingArticle;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleConverterTest {

    List<Article> articles = new ArrayList<>();

    @Before
    public void setUp() throws Exception {

        Article article1 = new CeylonTodayArticle();
        article1.setId(1);
        article1.setTitle("article title goes here");
        article1.setContent("article content goes here");
        article1.setAuthor("author's name goes here");
        article1.setCreatedDate(new Date());
        articles.add(article1);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testConvertToTrainingArticle() throws Exception {

        List<TrainingArticle> trainingArticles = ArticleConverter.convertToTrainingArticle(articles, CeylonTodayArticle.class);

        for (TrainingArticle trainingArticle : trainingArticles) {
            TestCase.assertTrue(trainingArticle instanceof TrainingArticle);
        }

    }

    @Test
    public void testConvertToCrimeArticle() throws Exception {
        List<CrimeArticle> crimeArticles = ArticleConverter.convertToCrimeArticle(articles, CeylonTodayArticle.class);

        for (CrimeArticle crimeArticle : crimeArticles) {
            TestCase.assertTrue(crimeArticle instanceof CrimeArticle);
        }
    }
}