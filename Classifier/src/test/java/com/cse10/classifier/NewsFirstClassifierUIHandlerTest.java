package com.cse10.classifier;

import com.cse10.article.Article;
import com.cse10.article.CrimeArticle;
import com.cse10.article.NewsFirstArticle;
import com.cse10.database.DatabaseHandler;
import junit.framework.TestCase;
import org.junit.*;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class NewsFirstClassifierUIHandlerTest {

    private NewsFirstClassifierUIHandler newsFirstClassifierUIHandler;
    @Before
    public void setUp() throws Exception {
        newsFirstClassifierUIHandler=new NewsFirstClassifierUIHandler();
        newsFirstClassifierUIHandler.setEndDate(new Date());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetClassifierConfigurator() throws Exception {
        TestCase.assertTrue(ClassifierConfigurator.class.isInstance(newsFirstClassifierUIHandler.getClassifierConfigurator()));
    }

    @Test
    public void testSetEndDate() throws Exception {
        newsFirstClassifierUIHandler.setEndDate(new Date());
        TestCase.assertEquals(new Date(),newsFirstClassifierUIHandler.getEndDate());
    }

    @Test
    public void testGetEndDate() throws Exception {
        TestCase.assertTrue(Date.class.isInstance(newsFirstClassifierUIHandler.getEndDate()));
    }

    //test the functionality of the thread
    @Test
    public void testConfigurator(){
        DatabaseHandler.executeUpdate("UPDATE `article_news_first` SET label = NULL");
        int previousSize=DatabaseHandler.fetchArticles(CrimeArticle.class).size();
        newsFirstClassifierUIHandler.setEndDate(new Date());
        newsFirstClassifierUIHandler.run();
        List<Article> articleList=DatabaseHandler.fetchArticles(NewsFirstArticle.class);
        int crimeCount=0;
        int otherCount=0;
        for(Article article:articleList){
            if(article.getLabel().equals("crime")){
                crimeCount++;
            }else{
                otherCount++;
            }
        }
        int afterSize=DatabaseHandler.fetchArticles(CrimeArticle.class).size();
        TestCase.assertEquals(articleList.size(),crimeCount+otherCount);
        TestCase.assertEquals(afterSize-previousSize,crimeCount);
        TestCase.assertEquals(0,DatabaseHandler.fetchArticlesWithNullLabels(NewsFirstArticle.class,new Date()).size());
    }
}