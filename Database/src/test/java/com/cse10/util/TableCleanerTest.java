package com.cse10.util;

import com.cse10.article.Article;
import com.cse10.article.CrimeArticle;
import com.cse10.database.DatabaseConstants;
import com.cse10.database.DatabaseHandler;
import com.cse10.entities.CrimeEntityGroup;
import com.cse10.entities.CrimePerson;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

public class TableCleanerTest {

    private static Logger logger = Logger.getLogger(TableCleanerTest.class);
    private static String previousDB;

    @BeforeClass
    public static void setUp() throws Exception {
        previousDB = DatabaseConstants.DB_URL;
        DatabaseConstants.DB_URL = "jdbc:mysql://localhost:3306/newsstats_test";
        logger.info("Database changed into test db");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        DatabaseConstants.DB_URL = previousDB;
        logger.info("Database changed back");
        DatabaseHandler.closeDatabase();
    }

    @Test
    public void testUndoClassifications() throws Exception {
        if(DatabaseHandler.getRowCount(CrimeArticle.class) == 0){ //if crime table is already empty
            CrimeArticle crimeArticle = new CrimeArticle();
            crimeArticle.setId(1);
            crimeArticle.setTitle("article title goes here");
            crimeArticle.setContent("article content goes here");
            crimeArticle.setAuthor("author's name goes here");
            crimeArticle.setCreatedDate(new Date());

            DatabaseHandler.insertArticle(crimeArticle);
            logger.info("a crime article inserted");
        }

        TableCleaner.undoClassifications(true);

        TestCase.assertTrue(DatabaseHandler.getRowCount(CrimeArticle.class) == 0);
        TestCase.assertTrue(DatabaseHandler.getRowCount(Article.class, "label", "NULL") == 0);
    }

    @Test
    public void testUndoEntityExtraction() throws Exception {

        if (DatabaseHandler.getRowCount(CrimeEntityGroup.class) == 0){
            CrimeEntityGroup crimeEntityGroup = new CrimeEntityGroup();
            
        }

        TableCleaner.undoEntityExtraction();

        TestCase.assertTrue(DatabaseHandler.getRowCount(CrimePerson.class) == 0);
        TestCase.assertTrue(DatabaseHandler.getRowCount(CrimeEntityGroup.class) == 0);
    }

    @Test
    public void testUndoDuplicateDetection() throws Exception {

    }
}