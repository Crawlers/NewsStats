package com.cse10.extractor.gate;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GateTestTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDoTest() throws Exception {
        TestCase.assertEquals(true, GateTest.doTest());
    }
}