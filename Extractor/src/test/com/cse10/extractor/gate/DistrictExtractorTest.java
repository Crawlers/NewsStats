package com.cse10.extractor.gate;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DistrictExtractorTest {
    private DistrictExtractor testDistrictExtractor;

    @Before
    public void setUp() throws Exception {
        testDistrictExtractor = new DistrictExtractor();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetDistrict() throws Exception {
        TestCase.assertEquals("Kandy", testDistrictExtractor.getDistrict("Poththapitiya"));
        TestCase.assertEquals("NULL", testDistrictExtractor.getDistrict("ziii"));
    }
}