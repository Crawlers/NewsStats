package com.cse10.classifier;

import junit.framework.TestCase;
import org.junit.*;
import org.junit.Test;
import weka.core.Instances;

import static org.junit.Assert.*;

public class DataHandlerWithGateTest {

    private DataHandlerWithGate dataHandlerWithGate;
    @Before
    public void setUp() throws Exception {
        dataHandlerWithGate=new DataHandlerWithGate();
    }

    @After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void testLoadTrainingData() throws Exception {
        Instances trainingData=dataHandlerWithGate.loadTrainingData(new FeatureVectorTransformer());
        //test number of articles
        TestCase.assertEquals(151,trainingData.numInstances());
        int crimeCount=0;
        int otherCount=0;
        for (int i = 0; i < trainingData.numInstances(); i++) {
            if (trainingData.instance(i).classValue() == 0.0)
                crimeCount++;
            else
                otherCount++;
        }
        //test number of crime articles and other articles
        TestCase.assertEquals(39,crimeCount);
        TestCase.assertEquals(112,otherCount);
        //functionality of gate component used in this data handler is tested in gate
    }

    @Test
    public void testPrintDescription() throws Exception {
        String description="This data handler will load training data and filter nouns,adjectives,verbs and adverbs from article content";
        TestCase.assertEquals(description, dataHandlerWithGate.printDescription());
    }

    @Test
    public void testGetFileName() throws Exception {
        String fileName="dataWithGate";
        TestCase.assertEquals(fileName,dataHandlerWithGate.getFileName());
    }

    @Test
    public void testIsFeatureVectorTransformerRequired() throws Exception {
        TestCase.assertEquals(true,dataHandlerWithGate.isFeatureVectorTransformerRequired());
    }
}