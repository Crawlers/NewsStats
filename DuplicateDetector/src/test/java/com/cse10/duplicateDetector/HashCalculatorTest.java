package com.cse10.duplicateDetector;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HashCalculatorTest {

    HashCalculator hashCalculator;
    @Before
    public void setUp() throws Exception {
        hashCalculator=new HashCalculator();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetHash32Value() throws Exception {
        int testValue1 = -1161307140;
        long testValue2 =-2020209788;
        TestCase.assertEquals(testValue1,(hashCalculator.getHash32Value("apple")));
        TestCase.assertEquals(testValue2,(hashCalculator.getHash32Value("colombo")));

    }

    @Test
    public void testGetHash64Value() throws Exception {
        long testValue1=-5532993126846898198L;
        long testValue2=-3187130264557305215L;
        TestCase.assertEquals(testValue1, (hashCalculator.getHash64Value("apple")));
        TestCase.assertEquals(testValue2, (hashCalculator.getHash64Value("colombo")));
    }
}