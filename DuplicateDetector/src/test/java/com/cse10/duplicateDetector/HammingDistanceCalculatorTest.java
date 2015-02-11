package com.cse10.duplicateDetector;

import com.cse10.database.DatabaseConstants;
import junit.framework.TestCase;
import org.junit.*;

public class HammingDistanceCalculatorTest {

    HammingDistanceCalculator hammingDistanceCalculator;
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
        hammingDistanceCalculator = new HammingDistanceCalculator();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetHammingDistance() throws Exception {
        long testValue1 = 213632207;
        long testValue2 = 134853934;
        TestCase.assertEquals(13, hammingDistanceCalculator.getHammingDistance(testValue1, testValue2));
    }

    @Test
    public void testGetHammingDistance1() throws Exception {
        int testValue1 = 12121;
        int testValue2 = 13442;
        TestCase.assertEquals(10, hammingDistanceCalculator.getHammingDistance(testValue1, testValue2));
    }
}