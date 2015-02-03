package com.cse10.analyzer;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class LRPredictorAlgorithmTest {

    @Test
    public void testPredict() throws Exception {
        HashMap<String,Integer> series = new HashMap<String,Integer>();
        series.put("2014 - 1", 10);
        series.put("2014 - 2", 10);
        series.put("2014 - 3", 10);
        series.put("2014 - 4", 10);
        PredictorAlgorithm lrPredictor = new LRPredictorAlgorithm();
        assertEquals(10, lrPredictor.predict(series, 5));

        series = new HashMap<String,Integer>();
        series.put("2014 - 1", 10);
        series.put("2014 - 2", 20);
        series.put("2014 - 3", 30);
        series.put("2014 - 4", 40);
        lrPredictor = new LRPredictorAlgorithm();
        assertEquals(60, lrPredictor.predict(series, 5));

        series = new HashMap<String,Integer>();
        series.put("2014 - 1", 10);
        series.put("2014 - 2", 20);
        series.put("2014 - 3", 10);
        series.put("2014 - 4", 20);
        series.put("2014 - 5", 10);
        series.put("2014 - 6", 20);
        lrPredictor = new LRPredictorAlgorithm();
        assertEquals(10, lrPredictor.predict(series, 6));
    }
}