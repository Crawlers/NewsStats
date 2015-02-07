package com.cse10.classifier;

import com.cse10.article.Article;
import com.cse10.article.CrimeArticle;
import com.cse10.article.DailyMirrorArticle;
import com.cse10.database.DatabaseHandler;
import junit.framework.TestCase;
import org.junit.*;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class DailyMirrorClassifierUIHandlerTest {

    DailyMirrorClassifierUIHandler dailyMirrorClassifierUIHandler;

    @Before
    public void setUp() throws Exception {
        dailyMirrorClassifierUIHandler=new DailyMirrorClassifierUIHandler();
        dailyMirrorClassifierUIHandler.setEndDate(new Date());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetClassifierConfigurator() throws Exception {
        TestCase.assertTrue(ClassifierConfigurator.class.isInstance(dailyMirrorClassifierUIHandler.getClassifierConfigurator()));
    }

    @Test
    public void testSetEndDate() throws Exception {
        dailyMirrorClassifierUIHandler.setEndDate(new Date());
        TestCase.assertEquals(new Date(),dailyMirrorClassifierUIHandler.getEndDate());
    }

    @Test
    public void testGetEndDate() throws Exception {
        TestCase.assertTrue(Date.class.isInstance(dailyMirrorClassifierUIHandler.getEndDate()));
    }

    //test the functionality of the thread
    @Test
    public void testConfigurator(){
        DatabaseHandler.executeUpdate("UPDATE `article_daily_mirror` SET label = NULL");
        int previousSize=DatabaseHandler.fetchArticles(CrimeArticle.class).size();
        dailyMirrorClassifierUIHandler.setEndDate(new Date());
        dailyMirrorClassifierUIHandler.run();
        List<Article> articleList=DatabaseHandler.fetchArticles(DailyMirrorArticle.class);
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
        TestCase.assertEquals(0,DatabaseHandler.fetchArticlesWithNullLabels(DailyMirrorArticle.class,new Date()).size());

    }
}