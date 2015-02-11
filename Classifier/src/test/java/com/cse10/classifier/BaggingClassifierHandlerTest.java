package com.cse10.classifier;

import com.cse10.database.DatabaseConstants;
import junit.framework.TestCase;
import org.junit.*;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.core.Instance;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;

//get data from file, no effect from db
public class BaggingClassifierHandlerTest {
    BaggingClassifierHandler baggingClassifierHandler;
    Instances testTrainingData;
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
        baggingClassifierHandler = new BaggingClassifierHandler();

        BufferedReader reader = new BufferedReader(
                new FileReader("Classifier\\src\\main\\resources\\testData\\arffTestData"));
        testTrainingData = new Instances(reader);
        reader.close();
        testTrainingData.setClassIndex(0);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testConfigure() throws Exception {
        baggingClassifierHandler.configure(2, new LibSVM());
        TestCase.assertEquals(2, baggingClassifierHandler.getModel().getNumIterations());
        TestCase.assertTrue(LibSVM.class.isInstance(baggingClassifierHandler.getModel().getClassifier()));
    }

    @Test
    public void testCrossValidateClassifier() throws Exception {
        Evaluation e = baggingClassifierHandler.crossValidateClassifier(testTrainingData, 2);
        TestCase.assertEquals(0.7187074829931973, e.weightedAreaUnderROC());
        double[][] confusionMatrix = e.confusionMatrix();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.print(confusionMatrix[i][j] + "  ");

            }
            System.out.println();
        }
        TestCase.assertEquals(14.285714285714285, (confusionMatrix[0][0] / (confusionMatrix[0][1] + confusionMatrix[0][0])) * 100);
        TestCase.assertEquals(97.14285714285714, (confusionMatrix[1][1] / (confusionMatrix[1][1] + confusionMatrix[1][0])) * 100);
    }

    /**
     * test both build and classify methods
     *
     * @throws Exception
     */
    @Test
    public void testBuildEnsemble() throws Exception {
        Instance i = testTrainingData.instance(0);
        baggingClassifierHandler.buildEnsemble(testTrainingData, false);
        TestCase.assertEquals(0.0, baggingClassifierHandler.classifyInstance(i));
    }

}