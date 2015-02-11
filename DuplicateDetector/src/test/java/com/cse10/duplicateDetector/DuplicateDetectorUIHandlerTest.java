package com.cse10.duplicateDetector;

import com.cse10.database.DatabaseConstants;
import com.cse10.database.DatabaseHandler;
import junit.framework.TestCase;
import org.junit.*;

import static org.junit.Assert.*;

public class DuplicateDetectorUIHandlerTest {

    private DuplicateDetectorUIHandler duplicateDetectorUIHandler;
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
        duplicateDetectorUIHandler = new DuplicateDetectorUIHandler();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testStartDuplicateDetection() throws Exception {
        DatabaseHandler.executeUpdate("UPDATE `crime_entity_group` SET label = NULL");
        Thread thread = new Thread(duplicateDetectorUIHandler);
        thread.run();
        int size = DatabaseHandler.fetchCrimeEntityGroupsWithNullLabels().size();
        TestCase.assertEquals(0, size);
        DatabaseHandler.executeUpdate("UPDATE `crime_entity_group` SET label = NULL");

    }
}