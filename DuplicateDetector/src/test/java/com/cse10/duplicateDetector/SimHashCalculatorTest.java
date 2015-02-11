package com.cse10.duplicateDetector;

import com.cse10.database.DatabaseConstants;
import junit.framework.TestCase;
import org.junit.*;

import static org.junit.Assert.*;

public class SimHashCalculatorTest {

    SimHashCalculator simHashCalculator;
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
        WordSegmenter wordSegmenter = new FullWordSegmenter();
        simHashCalculator = new SimHashCalculator(wordSegmenter);
    }

    @After
    public void tearDown() throws Exception {

    }


    @Test
    public void testSimhash64() throws Exception {
        String testArticleContent1 = "child sex crime 2012 11 08 Anuradhapura Anuradhapura Anuradhapura Anuradhapura";
        String testArticleContent2 = "illegal trade 2012 12 26 Anuradhapura Anuradhapura Anuradhapura Anuradhapura";
        long testValue1 = 212506208464060957L;
        long testValue2 = 1348539346806932285L;
        TestCase.assertEquals(testValue1, simHashCalculator.getSimhash64Value(testArticleContent1));
        TestCase.assertEquals(testValue2, simHashCalculator.getSimhash64Value(testArticleContent2));

    }

    @Test
    public void testSimhash32() throws Exception {
        String testArticleContent1 = "child sex crime 2012 11 08 Anuradhapura Anuradhapura Anuradhapura Anuradhapura";
        String testArticleContent2 = "illegal trade 2012 12 26 Anuradhapura Anuradhapura Anuradhapura Anuradhapura";
        long testValue1 = 270214823;
        long testValue2 = -1877269851;
        TestCase.assertEquals(testValue1, simHashCalculator.getSimhash32Value(testArticleContent1));
        TestCase.assertEquals(testValue2, simHashCalculator.getSimhash32Value(testArticleContent2));

    }
}