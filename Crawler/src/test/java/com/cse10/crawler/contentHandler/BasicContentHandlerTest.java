package com.cse10.crawler.contentHandler;

import edu.uci.ics.crawler4j.crawler.Page;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class BasicContentHandlerTest {

    BasicContentHandler basicContentHandler;

    @Before
    public void setUp() throws Exception {
        basicContentHandler = new BasicContentHandler() {
            @Override
            public List extractArticles(Page page) {
                return null;
            }
        };
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testFilterArticles() throws Exception {

        TestCase.assertEquals(true, basicContentHandler.filterArticles("12345678901"));
    }
}
