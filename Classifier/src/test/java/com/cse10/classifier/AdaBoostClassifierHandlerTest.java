package com.cse10.classifier;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.core.Instance;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;

public class AdaBoostClassifierHandlerTest {

    AdaBoostClassifierHandler adaBoostClassifierHandler;
    Instances testTrainingData;

    @Before
    public void setUp() throws Exception {
        adaBoostClassifierHandler=new AdaBoostClassifierHandler();

        BufferedReader reader = new BufferedReader(
                new FileReader("C:\\Users\\hp\\IdeaProjects\\NewsStats6\\Classifier\\src\\main\\resources\\arffData\\testData"));
        testTrainingData = new Instances(reader);
        reader.close();
        testTrainingData.setClassIndex(0);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testConfigure() throws Exception {
        adaBoostClassifierHandler.configure(2,new LibSVM());
        TestCase.assertEquals(2, adaBoostClassifierHandler.getModel().getNumIterations());
        TestCase.assertTrue(LibSVM.class.isInstance(adaBoostClassifierHandler.getModel().getClassifier()));
    }

    @Test
    public void testCrossValidateClassifier() throws Exception {
        Evaluation e= adaBoostClassifierHandler.crossValidateClassifier(testTrainingData,2);
        TestCase.assertEquals(0.719047619047619, e.weightedAreaUnderROC());
        double[][] confusionMatrix = e.confusionMatrix();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.print(confusionMatrix[i][j] + "  ");

            }
            System.out.println();
        }
        TestCase.assertEquals(28.57142857142857, (confusionMatrix[0][0] / (confusionMatrix[0][1] + confusionMatrix[0][0])) * 100);
        TestCase.assertEquals(97.14285714285714, (confusionMatrix[1][1] / (confusionMatrix[1][1] + confusionMatrix[1][0])) * 100);
    }

    /**
     * test both build and classify methods
     * @throws Exception
     */
    @Test
    public void testBuildEnsemble() throws Exception {
        Instance i = testTrainingData.instance(0);
        adaBoostClassifierHandler.buildEnsemble(testTrainingData,false);
        TestCase.assertEquals(0.0, adaBoostClassifierHandler.classifyInstance(i));
    }

}