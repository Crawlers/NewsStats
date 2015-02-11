package com.cse10.extractor.gate;

import junit.framework.TestCase;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSONParserTest {

    private  JSONParser testJSONParser;

    @Before
    public void setUp() throws Exception {
        testJSONParser = new JSONParser();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetLocationInfo() throws Exception {
        // test for parsing of JSON object from Google Maps API
        TestCase.assertEquals("OK", testJSONParser.getLocationInfo("Poththapitiya").getString("status"));
        TestCase.assertEquals("ZERO_RESULTS", testJSONParser.getLocationInfo("ziii").getString("status"));
    }
}