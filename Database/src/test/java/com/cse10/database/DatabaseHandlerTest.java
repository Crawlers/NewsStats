package com.cse10.database;

import com.cse10.entities.CrimeEntityGroup;
import org.junit.Test;

public class DatabaseHandlerTest {

    @Test
    public void testFetchLocation() throws Exception {

    }

    @Test
    public void testFetchCrimeEntityGroup() throws Exception {
        CrimeEntityGroup crimeEntityGroup = DatabaseHandler.fetchCrimeEntityGroup(2);
        System.out.println(crimeEntityGroup.getCrimeType());
    }

    @Test
    public void testUpdateCrimeEntityGroup() throws Exception {

    }
}