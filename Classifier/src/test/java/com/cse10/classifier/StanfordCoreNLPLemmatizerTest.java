package com.cse10.classifier;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Enumeration;

public class StanfordCoreNLPLemmatizerTest {

    StanfordCoreNLPLemmatizer stanfordCoreNLPLemmatizer;

    @Before
    public void setUp() throws Exception {
        stanfordCoreNLPLemmatizer=new StanfordCoreNLPLemmatizer();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testStem() throws Exception {
        TestCase.assertEquals("talk", stanfordCoreNLPLemmatizer.stem("talking"));
    }

    @Test
    public void testGetOptions() throws Exception {
        String[] options=stanfordCoreNLPLemmatizer.getOptions();
        TestCase.assertEquals("-S", options[0]);
        TestCase.assertEquals("Stanford Core NLP", options[1]);
    }

    @Test
    public void testGetRevision() throws Exception {
        TestCase.assertEquals("1.0", stanfordCoreNLPLemmatizer.getRevision());
    }

    @Test
    public void testListOptions() throws Exception {
        Enumeration<String>e=stanfordCoreNLPLemmatizer.listOptions();
        TestCase.assertEquals("-S", e.nextElement());
        TestCase.assertEquals("Stanford Core NLP", e.nextElement());
    }

    @Test
    public void testSetOptions() throws Exception {
        String[] options=new String[2];
        options[0]="aa";
        stanfordCoreNLPLemmatizer.setOptions(options);

        Enumeration<String>e=stanfordCoreNLPLemmatizer.listOptions();
        TestCase.assertEquals("aa", e.nextElement());

    }
}