package com.cse10.duplicateDetector;

import com.cse10.database.DatabaseConstants;
import junit.framework.TestCase;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

public class BinoryWordSegmenterTest {

    BinoryWordSegmenter binoryWordSegmenter;
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
        binoryWordSegmenter = new BinoryWordSegmenter();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetWords() throws Exception {
        String testContent = "he bought a book";
        List<String> biGrams = binoryWordSegmenter.getWords(testContent);
        /*        for(String biGram:biGrams){
            System.out.println(biGram);
        }*/
        TestCase.assertEquals("he", biGrams.get(0));
        TestCase.assertEquals("e ", biGrams.get(1));
        TestCase.assertEquals(" b", biGrams.get(2));
        TestCase.assertEquals("bo", biGrams.get(3));
        TestCase.assertEquals("ou", biGrams.get(4));
        TestCase.assertEquals("ug", biGrams.get(5));
        TestCase.assertEquals("gh", biGrams.get(6));
        TestCase.assertEquals("ht", biGrams.get(7));
    }
}