package com.cse10.database;

import com.cse10.article.Article;
import com.cse10.article.NewsFirstArticle;
import com.cse10.article.TrainingArticle;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.junit.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandlerTest {

    private static Logger logger = Logger.getLogger(DatabaseHandlerTest.class);
    private static String previousDB;

    @BeforeClass
    public static void setUpClass() throws Exception {
        previousDB = DatabaseConstants.DB_URL;
        DatabaseConstants.DB_URL = "jdbc:mysql://localhost:3306/newsstats_test";
        logger.info("Database changed into test db");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        DatabaseConstants.DB_URL = previousDB;
        logger.info("Database changed back");
        DatabaseHandler.closeDatabase();
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testInsertArticle() throws Exception {
        Article article = new NewsFirstArticle();
        article.setTitle("test_insert_123456789");
        article.setContent("content");
        article.setAuthor("author");
        article.setCreatedDate(new Date());

        DatabaseHandler.insertArticle(article);

        int id = 0;
        boolean flag = false;
        for (Article article2 : DatabaseHandler.fetchArticles(NewsFirstArticle.class)) {
            if (article2.getTitle().equals("test_insert_123456789")) {
                id = article2.getId();
                flag = true;
                break;
            }
        }
        TestCase.assertTrue(flag);
        DatabaseHandler.delete(NewsFirstArticle.class, id);
    }

    @Test
    public void testUpdateArticle() throws Exception {

        Article article = new NewsFirstArticle();
        article.setTitle("test_update_123456789");
        article.setContent("content");
        article.setAuthor("author");
        article.setCreatedDate(new Date());

        DatabaseHandler.insertArticle(article);

        for (Article article2 : DatabaseHandler.fetchArticles(NewsFirstArticle.class)) {
            if (article2.getTitle().equals("test_update_123456789")) {
                article2.setTitle("test_update_123456789_updated");
                DatabaseHandler.updateArticle(article2);
                break;
            }
        }

        int id = 0;
        boolean flag = false;
        for (Article article2 : DatabaseHandler.fetchArticles(NewsFirstArticle.class)) {
            if (article2.getTitle().equals("test_update_123456789_updated")) {
                id = article2.getId();
                flag = true;
                break;
            }
        }
        TestCase.assertTrue(flag);
        DatabaseHandler.delete(NewsFirstArticle.class, id);
    }

    @Test
    public void testInsertCrimeArticleAndUpdatePprArticle() throws Exception {

    }

    @Test
    public void testInsertArticles() throws Exception {

    }

    @Test
    public void testFetchArticles() throws Exception {

    }

    @Test
    public void testFetchTrainingArticles() throws Exception {

    }

    @Test
    public void testFetchArticlesByIdList() throws Exception {

        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        ids.add(3);
        ids.add(4);
        ids.add(5);
        List<Article> articles = DatabaseHandler.fetchArticlesByIdList(TrainingArticle.class, ids);
        for (Article article : articles) {
            TestCase.assertTrue(article.getId() > 0 && article.getId() < 6);
        }

    }

    @Test
    public void testFetchArticlesByIdRange() throws Exception {
        List<Article> articles = DatabaseHandler.fetchArticlesByIdRange(TrainingArticle.class, 1, 5);
        for (Article article : articles) {
            TestCase.assertTrue(article.getId() > 0 && article.getId() < 6);
        }
    }

    @Test
    public void testFetchArticlesByIdStarting() throws Exception {
        int maxId = DatabaseHandler.getMaxIdOf(TrainingArticle.class);
        TestCase.assertTrue(DatabaseHandler.fetchArticlesByIdStarting(TrainingArticle.class, maxId - 1).size() > 0);
    }

    @Test
    public void testFetchArticlesWithNullLabels() throws Exception {

    }

    @Test
    public void testExecuteQuery() throws Exception {

    }

    @Test
    public void testExecuteUpdateWithoutHibernate() throws Exception {

    }

    @Test
    public void testExecuteUpdate() throws Exception {

    }

    @Test
    public void testInsertCrimeEntities() throws Exception {

    }

    @Test
    public void testInsertCrimeEntityGroups() throws Exception {

    }

    @Test
    public void testFetchCrimeEntityGroups() throws Exception {

    }

    @Test
    public void testFetchCrimeEntityGroupsWithNullLabels() throws Exception {

    }

    @Test
    public void testFetchCrimeEntityGroupsWithNullOrUniqueLabels() throws Exception {

    }

    @Test
    public void testFetchCrimeEntityGroupsByIdRange() throws Exception {

    }

    @Test
    public void testInsertLocationDistrict() throws Exception {

    }

    @Test
    public void testFetchLocation() throws Exception {

    }

    @Test
    public void testInsertCrimePerson() throws Exception {

    }

    @Test
    public void testFetchCrimePeople() throws Exception {

    }

    @Test
    public void testInsertCrimeDetails() throws Exception {

    }

    @Test
    public void testGetRowCount() throws Exception {
        int count = DatabaseHandler.getRowCount(TrainingArticle.class);
        TestCase.assertTrue(count > 0);

    }

    @Test
    public void testGetRowCount1() throws Exception {

    }

    @Test
    public void testGetRowCount2() throws Exception {

    }

    @Test
    public void testGetRowCount3() throws Exception {

    }

    @Test
    public void testGetDistinctValueCount() throws Exception {

    }

    @Test
    public void testGetMaxIdOf() throws Exception {

    }

    @Test
    public void testGetLatestDate() throws Exception {

    }

    @Test
    public void testGetEarliestDateWithNullLabel() throws Exception {

    }

    @Test
    public void testGetLatestDateWithNullLabel() throws Exception {

    }

    @Test
    public void testFetchCrimeEntityGroup() throws Exception {

    }

    @Test
    public void testUpdateCrimeEntityGroup() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }

    @Test
    public void testDeleteAll() throws Exception {

    }

    @Test
    public void testCloseDatabase() throws Exception {

    }
}
