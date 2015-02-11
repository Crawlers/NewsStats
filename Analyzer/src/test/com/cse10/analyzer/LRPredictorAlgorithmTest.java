package com.cse10.analyzer;

import com.cse10.database.DatabaseConstants;
import com.cse10.database.DatabaseHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class LRPredictorAlgorithmTest {

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
        PredictorAlgorithm lrPredictor = new LRPredictorAlgorithm();
        assertEquals(10, lrPredictor.predict(series));

        series = new HashMap<String,Integer>();
        series.put("2014 - 1", 10);
        series.put("2014 - 2", 20);
        series.put("2014 - 3", 30);
        series.put("2014 - 4", 40);
        lrPredictor = new LRPredictorAlgorithm();
        assertEquals(50, lrPredictor.predict(series));

        series = new HashMap<String,Integer>();
        series.put("2014 - 1", 10);
        series.put("2014 - 2", 20);
        series.put("2014 - 3", 10);
        series.put("2014 - 4", 20);
        series.put("2014 - 5", 10);
        series.put("2014 - 6", 20);
        lrPredictor = new LRPredictorAlgorithm();
        assertEquals(18, lrPredictor.predict(series));
    }
}