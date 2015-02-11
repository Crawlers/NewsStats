package com.cse10.duplicateDetector;

import com.cse10.database.DatabaseConstants;
import junit.framework.TestCase;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

public class FullWordSegmenterTest {

    FullWordSegmenter fullWordSegmenter;
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
        fullWordSegmenter = new FullWordSegmenter();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetWords() throws Exception {
        String testContent = "he went to Matara TODAY";
        List<String> words = fullWordSegmenter.getWords(testContent);
        TestCase.assertEquals("he", words.get(0));
        TestCase.assertEquals("went", words.get(1));
        TestCase.assertEquals("to", words.get(2));
        //check conversion to lower case
        TestCase.assertEquals("matara", words.get(3));
        TestCase.assertEquals("today", words.get(4));
    }
}