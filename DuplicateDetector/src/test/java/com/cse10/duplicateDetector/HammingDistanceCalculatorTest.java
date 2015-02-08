package com.cse10.duplicateDetector;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HammingDistanceCalculatorTest {

    HammingDistanceCalculator hammingDistanceCalculator;
    @Before
    public void setUp() throws Exception {
        hammingDistanceCalculator=new HammingDistanceCalculator();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetHammingDistance() throws Exception {
        long testValue1=213632207;
        long testValue2=134853934;
        TestCase.assertEquals(13, hammingDistanceCalculator.getHammingDistance(testValue1, testValue2));
    }

    @Test
    public void testGetHammingDistance1() throws Exception {
        int testValue1=12121;
        int testValue2=13442;
        TestCase.assertEquals(10,hammingDistanceCalculator.getHammingDistance(testValue1, testValue2));
    }
}