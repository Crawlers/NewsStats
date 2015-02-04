package com.cse10.duplicateDetector;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimHashCalculatorTest {

    SimHashCalculator simHashCalculator;

    @Before
    public void setUp() throws Exception {
        WordSegmenter wordSegmenter=new FullWordSegmenter();
        simHashCalculator=new SimHashCalculator(wordSegmenter);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testHammingDistance() throws Exception {

        //TestCase.assertEquals(Long.toBinaryString(new Long(213632246078292797)),simHashCalculator.simhash64("child sex crime 2012-11-08 Anuradhapura Anuradhapura Anuradhapura Anuradhapura"));
    }

    @Test
    public void testHammingDistance1() throws Exception {

    }

    @Test
    public void testSimhash64() throws Exception {

    }

    @Test
    public void testSimhash32() throws Exception {

    }
}