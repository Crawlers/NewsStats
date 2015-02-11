package com.cse10.util;

import com.cse10.article.Article;
import com.cse10.article.CrimeArticle;
import com.cse10.database.DatabaseConstants;
import com.cse10.database.DatabaseHandler;
import com.cse10.entities.CrimeEntityGroup;
import com.cse10.entities.CrimePerson;
import com.cse10.entities.LocationDistrictMapper;
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

    @Test
    public void testUndoClassifications() throws Exception {

        boolean inserted = false;

        if (DatabaseHandler.getRowCount(CrimeArticle.class) == 0) { //if crime table is already empty

            inserted = true;

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
        TestCase.assertTrue(DatabaseHandler.getRowCount(Article.class, "label", "crime") == 0);
        TestCase.assertTrue(DatabaseHandler.getRowCount(Article.class, "label", "other") == 0);
    }

    @Test
    public void testUndoEntityExtraction() throws Exception {

        boolean inserted = false;
        int id = 999999;

        if (DatabaseHandler.getRowCount(CrimeEntityGroup.class) == 0 || DatabaseHandler.getRowCount(CrimePerson.class) == 0) {

            inserted = true;

            LocationDistrictMapper locationDistrictMapper = new LocationDistrictMapper();
            locationDistrictMapper.setLocation("Moratuwa");
            locationDistrictMapper.setDistrict("Colombo");

            CrimeEntityGroup crimeEntityGroup = new CrimeEntityGroup();
            crimeEntityGroup.setId(id);
            crimeEntityGroup.setCrimeType("robbery");
            crimeEntityGroup.setLocationDistrict(locationDistrictMapper);
            crimeEntityGroup.setCriminal("Wasantha");
            crimeEntityGroup.setPossession("drug");
            crimeEntityGroup.setCrimeDate(new Date());

            DatabaseHandler.insertCrimeEntities(crimeEntityGroup);
            logger.info("a crime entity group inserted");

            CrimePerson crimePerson = new CrimePerson();
            crimePerson.setCrimePersonId(id);
            crimePerson.setName("Wasantha");
            crimePerson.setEntityGroup(crimeEntityGroup);

            DatabaseHandler.insertCrimePerson(crimePerson);
            logger.info("a crime person inserted");

        }

        TableCleaner.undoEntityExtraction();

        TestCase.assertTrue(DatabaseHandler.getRowCount(CrimePerson.class) == 0);
        TestCase.assertTrue(DatabaseHandler.getRowCount(CrimeEntityGroup.class) == 0);

        if (inserted) {
            DatabaseHandler.executeUpdate("DELETE FROM " + DatabaseConstants.classToTableName.get(CrimePerson.class)
                    + " WHERE crime_person_id = " + id);
            DatabaseHandler.delete(CrimeEntityGroup.class, id);
        }
    }

    @Test
    public void testUndoDuplicateDetection() throws Exception {

        boolean inserted = false;
        int id = 999999;

        if (DatabaseHandler.getRowCount(CrimeEntityGroup.class, "label", "NULL") == 0) {

            inserted = true;

            LocationDistrictMapper locationDistrictMapper = new LocationDistrictMapper();
            locationDistrictMapper.setLocation("Moratuwa");
            locationDistrictMapper.setDistrict("Colombo");

            CrimeEntityGroup crimeEntityGroup = new CrimeEntityGroup();
            crimeEntityGroup.setId(id);
            crimeEntityGroup.setLabel("unique");
            crimeEntityGroup.setCrimeType("robbery");
            crimeEntityGroup.setLocationDistrict(locationDistrictMapper);
            crimeEntityGroup.setCriminal("Wasantha");
            crimeEntityGroup.setPossession("drug");
            crimeEntityGroup.setCrimeDate(new Date());

            DatabaseHandler.insertCrimeEntities(crimeEntityGroup);
            logger.info("a crime entity group inserted");
        }

        TableCleaner.undoDuplicateDetection();

        TestCase.assertTrue(DatabaseHandler.getRowCount(CrimeEntityGroup.class, "label", "NULL") == 0);

        if (inserted) {
            DatabaseHandler.delete(CrimeEntityGroup.class, id);
        }
    }
}
