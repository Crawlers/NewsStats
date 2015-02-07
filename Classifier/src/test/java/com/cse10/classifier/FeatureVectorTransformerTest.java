package com.cse10.classifier;

import junit.framework.TestCase;
import org.junit.*;
import org.junit.Test;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

//not depend on database
public class FeatureVectorTransformerTest {

    private FeatureVectorTransformer featureVectorTransformer;

    @Before
    public void setUp() throws Exception {
        featureVectorTransformer=new FeatureVectorTransformer();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testConfigure() throws Exception {
        featureVectorTransformer.configure(1,1,true);
        StringToWordVector stringToWordVector=featureVectorTransformer.getFilter();
        TestCase.assertEquals(true, stringToWordVector.getIDFTransform());
        TestCase.assertEquals(true,stringToWordVector.getLowerCaseTokens());
        TestCase.assertEquals(true,stringToWordVector.getTFTransform());
        TestCase.assertEquals(true,stringToWordVector.getLowerCaseTokens());
    }

    @Test
    public void testSetInputFormat() throws Exception {
        Instances data;
        BufferedReader reader = new BufferedReader(
                new FileReader("Classifier\\src\\main\\resources\\testData\\rawTestData"));
        data = new Instances(reader);
        reader.close();
        featureVectorTransformer.setInputFormat(data);
        Instances transformedData=featureVectorTransformer.getTransformedArticles(data);
        TestCase.assertTrue(Instances.class.isInstance(transformedData));

    }

    @Test
    public void testGetTransformedArticles() throws Exception {
        Instances data;
        BufferedReader reader = new BufferedReader(
                new FileReader("Classifier\\src\\main\\resources\\testData\\rawTestData"));
        data = new Instances(reader);
        reader.close();
        featureVectorTransformer.setInputFormat(data);
        Instances transformedData=featureVectorTransformer.getTransformedArticles(data);
        TestCase.assertTrue(Instances.class.isInstance(transformedData));
    }

    @Test
    public void testGetTransformedArticles1() throws Exception {
        Instances data;
        BufferedReader reader = new BufferedReader(
                new FileReader("Classifier\\src\\main\\resources\\testData\\rawTestData"));
        data = new Instances(reader);
        reader.close();
        featureVectorTransformer.setInputFormat(data);
        Instances transformedData=featureVectorTransformer.getTransformedArticles(data, "FVTTest");
        File file=new File("Classifier\\src\\main\\resources\\arffData\\FVTTest");
        TestCase.assertEquals(true,file.exists());
    }

    @Test
    public void testGetFilter() throws Exception {
         TestCase.assertTrue(StringToWordVector.class.isInstance(featureVectorTransformer.getFilter()));
    }
}