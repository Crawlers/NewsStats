package com.cse10.classifier;

import junit.framework.TestCase;
import org.junit.*;
import org.junit.Test;
import weka.core.Instances;

import static org.junit.Assert.*;

public class DataHandlerWithSamplingTest {

    private DataHandlerWithSampling dataHandlerWithSampling;
    @Before
    public void setUp() throws Exception {
        dataHandlerWithSampling=new DataHandlerWithSampling();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testLoadTrainingData() throws Exception {
        FeatureVectorTransformer featureVectorTransformer=new FeatureVectorTransformer();
        Instances trainingData=dataHandlerWithSampling.loadTrainingData(featureVectorTransformer);
        int crimeCount=0;
        int otherCount=0;
        for (int i = 0; i < trainingData.numInstances(); i++) {
            if (trainingData.instance(i).classValue() == 0.0)
                crimeCount++;
            else
                otherCount++;
        }
        //test number of crime articles and other articles
        TestCase.assertEquals(165,trainingData.numInstances());
        TestCase.assertEquals(82,crimeCount);
        TestCase.assertEquals(83,otherCount);
    }

    @Test
    public void testPrintDescription() throws Exception {
        String description="This data handler will load training data and use sampling method to generate training data.";
        TestCase.assertEquals(description, dataHandlerWithSampling.printDescription());
    }

    @Test
    public void testGetFileName() throws Exception {
        String fileName="dataWithSampling";
        TestCase.assertEquals(fileName,dataHandlerWithSampling.getFileName());
    }

    @Test
    public void testIsFeatureVectorTransformerRequired() throws Exception {
        TestCase.assertEquals(false,dataHandlerWithSampling.isFeatureVectorTransformerRequired());
    }
}