package com.cse10.duplicateDetector;

import com.cse10.database.DatabaseHandler;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DuplicateDetectorUIHandlerTest {

    private DuplicateDetectorUIHandler duplicateDetectorUIHandler;
    @Before
    public void setUp() throws Exception {
        duplicateDetectorUIHandler=new DuplicateDetectorUIHandler();
    }

    @After
    public void tearDown() throws Exception {

    }
    @Test
    public void testStartDuplicateDetection() throws Exception {
        DatabaseHandler.executeUpdate("UPDATE `crime_entity_group` SET label = NULL");
        Thread thread=new Thread(duplicateDetectorUIHandler);
        thread.run();
        int size=DatabaseHandler.fetchCrimeEntityGroupsWithNullLabels().size();
        TestCase.assertEquals(0,size);
        DatabaseHandler.executeUpdate("UPDATE `crime_entity_group` SET label = NULL");

    }
}