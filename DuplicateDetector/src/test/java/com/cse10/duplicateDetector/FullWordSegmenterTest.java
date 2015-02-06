package com.cse10.duplicateDetector;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FullWordSegmenterTest {

    FullWordSegmenter fullWordSegmenter;
    @Before
    public void setUp() throws Exception {
        fullWordSegmenter=new FullWordSegmenter();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetWords() throws Exception {
        String testContent="he went to Matara TODAY";
        List<String> words=fullWordSegmenter.getWords(testContent);
        TestCase.assertEquals("he",words.get(0));
        TestCase.assertEquals("went",words.get(1));
        TestCase.assertEquals("to",words.get(2));
        TestCase.assertEquals("matara",words.get(3));
        TestCase.assertEquals("today",words.get(4));
    }
}