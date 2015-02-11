package com.cse10.classifier;

import com.cse10.article.Article;
import com.cse10.article.CeylonTodayArticle;
import com.cse10.article.CrimeArticle;
import com.cse10.database.DatabaseConstants;
import com.cse10.database.DatabaseHandler;
import com.cse10.util.ArticleConverter;
import junit.framework.TestCase;
import org.junit.*;
import org.junit.Test;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GenericDataHandlerTest {

    private GenericDataHandler genericDataHandler;
    static String previousDB;

    @BeforeClass
    public static void setUpClass() throws Exception {
        previousDB = DatabaseConstants.DB_URL;
        DatabaseConstants.DB_URL = "jdbc:mysql://localhost:3306/newsstats_test";
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        DatabaseConstants.DB_URL = previousDB;
    }

    @Before
    public void setUp() throws Exception {
        genericDataHandler = new GenericDataHandler();
    }

    @After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void testPrintDescription() throws Exception {
        String s = "Data Handler -> This Data Handler will Load all of the Training Data";
        TestCase.assertEquals(s, genericDataHandler.printDescription());
    }

    @Test
    public void testLoadTrainingData() throws Exception {
        FeatureVectorTransformer featureVectorTransformer = new FeatureVectorTransformer();
        Instances trainingData = genericDataHandler.loadTrainingData(featureVectorTransformer);
        //test number of articles
        TestCase.assertEquals(151, trainingData.numInstances());
        int crimeCount = 0;
        int otherCount = 0;
        for (int i = 0; i < trainingData.numInstances(); i++) {
            if (trainingData.instance(i).classValue() == 0.0)
                crimeCount++;
            else
                otherCount++;
        }
        //test number of crime articles and other articles
        TestCase.assertEquals(39, crimeCount);
        TestCase.assertEquals(112, otherCount);

    }


    @Test
    public void testLoadTestData() throws Exception {
        Instances instances = genericDataHandler.loadTestData(CeylonTodayArticle.class, "where id<100", false);
        TestCase.assertEquals(91, instances.numInstances());
    }

    @Test
    public void testGetArticleIds() throws Exception {
        Instances instances = genericDataHandler.loadTestData(CeylonTodayArticle.class, "where id<100", false);
        HashMap<Integer, Integer> ids = genericDataHandler.getArticleIds();
        TestCase.assertEquals(instances.numInstances(), ids.keySet().size());

        instances = genericDataHandler.loadTestData(CeylonTodayArticle.class, "where id<100", true);
        ids = genericDataHandler.getArticleIds();
        TestCase.assertEquals(instances.numInstances(), ids.keySet().size());
    }

    @Test
    public void testGetFileName() throws Exception {
        TestCase.assertEquals("generic", genericDataHandler.getFileName());
    }

    @Test
    public void testIsFeatureVectorTransformerRequired() throws Exception {
        TestCase.assertEquals(true, genericDataHandler.isFeatureVectorTransformerRequired());
    }

    @Test
    public void testFetchArticlesWithNullLabels() throws Exception {
        List<Article> articles = genericDataHandler.fetchArticlesWithNullLabels(CeylonTodayArticle.class, new Date());
        TestCase.assertEquals(0, articles.size());
    }

    @Test
    public void testFetchArticlesByIdList() throws Exception {
        List<Integer> idList = new ArrayList<>();
        idList.add(1);
        idList.add(2);
        idList.add(3);
        List<Article> articles = genericDataHandler.fetchArticlesByIdList(CeylonTodayArticle.class, idList);
        TestCase.assertEquals(3, articles.size());
    }

    @Test
    public void testInsertCrimeArticleAndUpdatePprArticle() throws Exception {
        int dataSize = DatabaseHandler.fetchArticles(CrimeArticle.class).size();
        List<Article> articles = DatabaseHandler.fetchArticles(CeylonTodayArticle.class);
        Article updatedArticle = null;
        //find article with other label
        for (Article article : articles) {
            if (article.getLabel().equals("other")) {
                updatedArticle = article;
                break;
            }
        }

        updatedArticle.setLabel("crime");
        articles = new ArrayList<>();
        articles.add(updatedArticle);
        List<CrimeArticle> crimeArticles = ArticleConverter.convertToCrimeArticle(articles, CeylonTodayArticle.class);
        genericDataHandler.insertCrimeArticleAndUpdatePprArticle(crimeArticles.get(0), updatedArticle);

        TestCase.assertEquals(dataSize + 1, DatabaseHandler.fetchArticles(CrimeArticle.class).size());

        List<Integer> ids = new ArrayList<>();
        ids.add(updatedArticle.getId());
        TestCase.assertEquals("crime", DatabaseHandler.fetchArticlesByIdList(CeylonTodayArticle.class, ids).get(0).getLabel());

        //restore to original state
        updatedArticle.setLabel("other");
        genericDataHandler.updateArticle(updatedArticle);
    }

    @Test
    public void testUpdateArticle() throws Exception {
        List<Article> articles = DatabaseHandler.fetchArticles(CeylonTodayArticle.class);
        Article article = articles.get(0);
        String originalLabel = article.getLabel();
        String upDatedLabel = "";
        if (originalLabel.equals("crime")) {
            upDatedLabel = "other";
        } else if (originalLabel.equals("other")) {
            upDatedLabel = "crime";
        } else {
            upDatedLabel = "other";
        }

        article.setLabel(upDatedLabel);
        genericDataHandler.updateArticle(article);

        articles = DatabaseHandler.fetchArticles(CeylonTodayArticle.class);
        article = articles.get(0);
        TestCase.assertEquals(upDatedLabel, article.getLabel());

        //restore to original state
        article.setLabel(originalLabel);
        genericDataHandler.updateArticle(article);

        articles = DatabaseHandler.fetchArticles(CeylonTodayArticle.class);
        article = articles.get(0);
        TestCase.assertEquals(originalLabel, article.getLabel());

    }


}