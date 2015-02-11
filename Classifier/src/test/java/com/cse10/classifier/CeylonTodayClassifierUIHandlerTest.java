package com.cse10.classifier;

import com.cse10.database.DatabaseConstants;
import junit.framework.TestCase;
import org.junit.*;
import org.junit.Test;

import java.util.Date;


public class CeylonTodayClassifierUIHandlerTest {

    private CeylonTodayClassifierUIHandler ceylonTodayClassifierUIHandler;

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
        ceylonTodayClassifierUIHandler = new CeylonTodayClassifierUIHandler();
        ceylonTodayClassifierUIHandler.setEndDate(new Date());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetClassifierConfigurator() throws Exception {
        TestCase.assertTrue(ClassifierConfigurator.class.isInstance(ceylonTodayClassifierUIHandler.getClassifierConfigurator()));
    }

    @Test
    public void testSetEndDate() throws Exception {
        ceylonTodayClassifierUIHandler.setEndDate(new Date());
        TestCase.assertEquals(new Date(), ceylonTodayClassifierUIHandler.getEndDate());
    }

    @Test
    public void testGetEndDate() throws Exception {
        TestCase.assertTrue(Date.class.isInstance(ceylonTodayClassifierUIHandler.getEndDate()));
    }

    //test the functionality of the thread
    @Test
    public void testConfigurator() {
        //not tested, tested in other articles
    }
}