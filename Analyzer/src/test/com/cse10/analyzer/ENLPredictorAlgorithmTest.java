package com.cse10.analyzer;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class ENLPredictorAlgorithmTest {

    @Test
    public void testPredict() throws Exception {
        HashMap<String,Integer> series = new HashMap<String,Integer>();
        series.put("2014 - 1", 10);
        series.put("2014 - 2", 10);
        series.put("2014 - 3", 10);
        series.put("2014 - 4", 10);
        PredictorAlgorithm enlPredictor = new ENLPredictorAlgorithm();
        assertEquals(10, enlPredictor.predict(series, 5));

        series = new HashMap<String,Integer>();
        series.put("2014 - 1", 10);
        series.put("2014 - 2", 20);
        series.put("2014 - 3", 30);
        series.put("2014 - 4", 40);
        enlPredictor = new ENLPredictorAlgorithm();
        assertEquals(60, enlPredictor.predict(series, 5));

        series = new HashMap<String,Integer>();
        series.put("2014 - 1", 10);
        series.put("2014 - 2", 20);
        series.put("2014 - 3", 10);
        series.put("2014 - 4", 20);
        series.put("2015 - 1", 10);
        series.put("2015 - 2", 20);
        enlPredictor = new ENLPredictorAlgorithm();
        assertEquals(10, enlPredictor.predict(series, 6));
    }
}