package com.cse10.classifier;

import junit.framework.TestCase;

public class StanfordCoreNLPLemmatizerTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }

    public void testStem() throws Exception {
        StanfordCoreNLPLemmatizer stanfordCoreNLPLemmatizer=new StanfordCoreNLPLemmatizer();
        assertEquals("talk",stanfordCoreNLPLemmatizer.stem("talking"));
    }


    public void testGetOptions() throws Exception {
        StanfordCoreNLPLemmatizer stanfordCoreNLPLemmatizer=new StanfordCoreNLPLemmatizer();
        String[] options=stanfordCoreNLPLemmatizer.getOptions();
        assertEquals("-S",options[0]);
        assertEquals("Stanford Core NLP",options[1]);
    }
}