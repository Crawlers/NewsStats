package com.cse10.extractor.gate;

import com.cse10.article.Article;
import com.cse10.article.NewsFirstArticle;
import com.cse10.entities.CrimeEntityGroup;
import gate.Corpus;
import gate.CorpusController;
import gate.Factory;
import gate.Gate;
import gate.util.persistence.PersistenceManager;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class EntityExtractorTest {

    EntityExtractor testEntityExtractor;
    Article testArticle;
    List<Article> testArticleList;
    CorpusController application;
    Corpus corpus;


    @Before
    public void setUp() throws Exception {
//        Gate.setGateHome(new File(System.getenv("GATE_HOME")));
//        Gate.init();
        testEntityExtractor = new EntityExtractor();
        testArticle = new NewsFirstArticle();
        testArticle.setId(1);
        testArticle.setAuthor("Madura Ranwala");
        testArticle.setTitle("Chinese restaurant raided, turtles saved from pot");
        testArticle.setContent("A Chinese national, running a restaurant in Narehenpita, was arrested by the police on Saturday, for possessing 14 turtles. Police spokesman SSP Prishantha Jayakody told The Island that the Narahenpita police, acting on a tip off, raided the restaurant and found the turtles in the kitchen, being readied for the pot. They arrested the owner, a 40-year-old Chinese, who had obtained Sri Lankan citizenship and produced him before the Maligakanda Magistrate, who ordered his release on bail in Rs. 25,000 cash. Court also ordered the police to send the turtles to the Dehiwela Zoo. The Narahenpita police said that they were unable to trace those who had sold the turtles to the suspect as he had claimed that he purchased them to be reared for medical purposes. Police disclosed that those who patronised the restaurant were only Chinese nationals, who are in Sri Lanka for various purposes.");
        testArticle.setCreatedDate(new Date(2013-01-22));
        testArticle.setLabel("crime");
        testArticleList = new ArrayList<Article>();
        testArticleList.add(testArticle);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testStartExtraction() throws Exception {
        TestCase.assertEquals(true, testEntityExtractor.startExtraction());
    }

    @Test
    public void testStopExtraction() throws Exception {

    }

    @Test
    public void testExecuteProcessPipeline() throws Exception {
        application = (CorpusController) PersistenceManager.loadObjectFromFile(new File("Extractor/src/main/resources/Complete_v1.gapp"));
        corpus = Factory.newCorpus("BatchProcessApp Corpus");
        application.setCorpus(corpus);
        ArrayList<CrimeEntityGroup> testCrimeEntityGroupList = testEntityExtractor.executeProcessPipeline(testArticleList, corpus, application);
        CrimeEntityGroup testCrimeEntityGroup = testCrimeEntityGroupList.get(0);
        TestCase.assertEquals("Narehenpita", testCrimeEntityGroup.getLocation().toString());
        TestCase.assertEquals("Colombo", testCrimeEntityGroup.getDistrict().toString());
        TestCase.assertEquals("Narahenpita", testCrimeEntityGroup.getPolice().toString());
        TestCase.assertEquals("Maligakanda", testCrimeEntityGroup.getCourt().toString());
    }
}