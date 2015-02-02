package com.cse10.gate;

import junit.framework.TestCase;

public class DocumentContentFilterTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }

    public void testGetFilterdContent() throws Exception {

        DocumentContentFilter documentContentFilter=new DocumentContentFilter();
        String testContent="Brothers killed in Gandara. Two brothers have been killed after being assaulted with an axe in the Nawadunna area in \n" +
                "Gandara. Police said an unidentified group has carried out the alleged murder at around 8.30 on Friday night.\n" +
                "The individuals succumbed to their injuries after being admitted to the Matara Hospital.The deceased are aged \n" +
                "36 and 38 years The suspects are said to have fled the area. Police investigations have been launched to arrest the \n" +
                "suspects.";
        assertEquals("area brothers assaulted investigations axe area killed individuals succumbed injuries said unidentified murder ",documentContentFilter.getFilterdContent(testContent));
    }
}