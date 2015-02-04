package com.cse10.extractor.gate;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EntityExtractorTaskTest {
    private  EntityExtractorTask testEntityExtractorTask;
    private  Thread testThread;

    @Before
    public void setUp() throws Exception {
        testEntityExtractorTask = new EntityExtractorTask();
        testThread = new Thread(testEntityExtractorTask);
        testThread.start();
    }

    @After
    public void tearDown() throws Exception {
        testThread.interrupt();
    }

    @Test
    public void testGetEntityExtrator() throws Exception {
        TestCase.assertEquals("com.cse10.extractor.gate.EntityExtractor", testEntityExtractorTask.getEntityExtrator().getClass().getName());
    }

    @Test
    public void testRun() throws Exception {
        TestCase.assertEquals(Thread.State.RUNNABLE, testThread.getState());
    }
}