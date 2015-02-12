package com.cse10.analyzer;

import com.cse10.database.DatabaseConstants;
import com.cse10.database.DatabaseHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class ENLPredictorAlgorithmTest {


    private static String previousDB;

    @BeforeClass
    public static void setUp() throws Exception {
        previousDB = DatabaseConstants.DB_URL;
        DatabaseConstants.DB_URL = "jdbc:mysql://localhost:3306/newsstats";
    }

    @AfterClass
    public static void tearDown() throws Exception {
        DatabaseConstants.DB_URL = previousDB;
        DatabaseHandler.closeDatabase();
    }

    @Test
    public void testPredict() throws Exception {
        HashMap<String,Integer> series = new HashMap<String,Integer>();
        series.put("2014 - 1", 10);
        series.put("2014 - 2", 10);
        series.put("2014 - 3", 10);
        series.put("2014 - 4", 10);
        PredictorAlgorithm enlPredictor = new ENLPredictorAlgorithm();
        assertEquals(10, enlPredictor.predict(series));

        series = new HashMap<String,Integer>();
        series.put("2014 - 1", 10);
        series.put("2014 - 2", 20);
        series.put("2014 - 3", 30);
        series.put("2014 - 4", 40);
        enlPredictor = new ENLPredictorAlgorithm();
        assertEquals(50, enlPredictor.predict(series));

        series = new HashMap<String,Integer>();
        series.put("2014 - 1", 10);
        series.put("2014 - 2", 20);
        series.put("2014 - 3", 10);
        series.put("2014 - 4", 20);
        series.put("2015 - 1", 10);
        series.put("2015 - 2", 20);
        enlPredictor = new ENLPredictorAlgorithm();
        assertEquals(18, enlPredictor.predict(series));
    }
}