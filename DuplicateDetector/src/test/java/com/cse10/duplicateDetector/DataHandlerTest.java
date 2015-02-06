package com.cse10.duplicateDetector;

import com.cse10.entities.CrimeEntityGroup;
import com.cse10.entities.LocationDistrictMapper;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class DataHandlerTest {

    DataHandler dataHandler;
    @Before
    public void setUp() throws Exception {
        dataHandler=new DataHandler();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testReadArticlesFromFile() throws Exception {
            dataHandler.readArticlesFromFile();
    }

    @Test
    public void testReadArticlesFromDB() throws Exception {
        HashMap<Integer,String>data=dataHandler.readArticlesFromDB();
        Iterator<Integer>ids=data.keySet().iterator();
        TestCase.assertEquals("Violent Crimes 2013 07 12 Gampola Kandy ",data.get(ids.next()));
    }

    @Test
    public void testFetchCrimeEntityGroup() throws Exception {
        CrimeEntityGroup crimeEntityGroup=dataHandler.fetchCrimeEntityGroup(4);
        TestCase.assertEquals(5,crimeEntityGroup.getCrimeArticleId());
        TestCase.assertEquals("Violent Crimes",crimeEntityGroup.getCrimeType());
        TestCase.assertEquals(Date.valueOf("2013-02-20"),crimeEntityGroup.getCrimeDate());
        LocationDistrictMapper locationDistrictMapper = crimeEntityGroup.getLocationDistrict();
        if (locationDistrictMapper != null) {
            String location = locationDistrictMapper.getLocation();
            TestCase.assertEquals("Dompe",location);
            String district = crimeEntityGroup.getDistrict();
            TestCase.assertEquals("Gampaha",district);
        }
    }

    @Test
    public void testUpdateCrimeEntityGroup() throws Exception {
        CrimeEntityGroup crimeEntityGroup=dataHandler.fetchCrimeEntityGroup(4);
        TestCase.assertEquals(5,crimeEntityGroup.getCrimeArticleId());
        TestCase.assertEquals("Violent Crimes",crimeEntityGroup.getCrimeType());
        TestCase.assertEquals(Date.valueOf("2013-02-20"),crimeEntityGroup.getCrimeDate());
        LocationDistrictMapper locationDistrictMapper = crimeEntityGroup.getLocationDistrict();
        if (locationDistrictMapper != null) {
            String location = locationDistrictMapper.getLocation();
            TestCase.assertEquals("Dompe",location);
            String district = crimeEntityGroup.getDistrict();
            TestCase.assertEquals("Gampaha",district);
        }
        crimeEntityGroup.setCrimeType("Illegal Trading");
        dataHandler.updateCrimeEntityGroup(crimeEntityGroup);

        crimeEntityGroup=dataHandler.fetchCrimeEntityGroup(4);
        TestCase.assertEquals(5,crimeEntityGroup.getCrimeArticleId());
        TestCase.assertEquals("Illegal Trading",crimeEntityGroup.getCrimeType());

        crimeEntityGroup.setCrimeType("Violent Crimes");
        dataHandler.updateCrimeEntityGroup(crimeEntityGroup);
    }

}