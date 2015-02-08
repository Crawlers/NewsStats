package com.cse10.duplicateDetector;

import com.cse10.article.Article;
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
        List<String>articles=dataHandler.readArticlesFromFile();
        String testContent="Investigations launched into Rs.3.4m robbery in Mount Lavinia sum of Rs.3.4 million  belonging to a private educational institute in Mount Lavinia was stolen on Monday. According to the police, the money which was being transported to  be deposited at a bank, was robbed by two persons who arrived in a three-wheeler. The suspects are yet to be identified. Police has however launched investigations into the theft.";
        TestCase.assertEquals(testContent,articles.get(0));
        TestCase.assertEquals(30,articles.size());
    }

    @Test
    public void testReadArticlesFromDB() throws Exception {
        HashMap<Integer,String>data=dataHandler.readArticlesFromDB();
        Iterator<Integer>ids=data.keySet().iterator();
        TestCase.assertEquals("Unsuitable Consumer Goods 20130101 Welisara Gampaha",data.get(ids.next()));
        TestCase.assertEquals(100,data.keySet().size());
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
        String originalCrimeType=crimeEntityGroup.getCrimeType();

        crimeEntityGroup.setCrimeType("Illegal Trading");
        dataHandler.updateCrimeEntityGroup(crimeEntityGroup);

        crimeEntityGroup=dataHandler.fetchCrimeEntityGroup(4);
        TestCase.assertEquals("Illegal Trading",crimeEntityGroup.getCrimeType());

        //restore to original state
        crimeEntityGroup.setCrimeType(originalCrimeType);
        dataHandler.updateCrimeEntityGroup(crimeEntityGroup);
    }

}