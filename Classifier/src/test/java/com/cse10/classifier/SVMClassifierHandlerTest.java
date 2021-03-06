package com.cse10.classifier;

import com.cse10.database.DatabaseConstants;
import junit.framework.TestCase;
import org.junit.*;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;

import java.io.BufferedReader;
import java.io.FileReader;

//get data from file, no effect from db
public class SVMClassifierHandlerTest {

    SVMClassifierHandler svmClassifierHandler;
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
        svmClassifierHandler = new SVMClassifierHandler();

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
        SelectedTag st = new SelectedTag(2, LibSVM.TAGS_KERNELTYPE);
        //assertEquals(st.getSelectedTag(),svmClassifierHandler.getSvm().getSVMType().getSelectedTag());
        svmClassifierHandler.configure(1.0, 1.0, "1 10", false);
        TestCase.assertEquals(1.0, svmClassifierHandler.getSvm().getGamma());
        TestCase.assertEquals(1.0, svmClassifierHandler.getSvm().getCost());
        TestCase.assertEquals("1.0 10.0", svmClassifierHandler.getSvm().getWeights());
        TestCase.assertEquals(false, svmClassifierHandler.getSvm().getNormalize());
    }

    @Test
    public void testCrossValidateClassifier() throws Exception {
        Evaluation e = svmClassifierHandler.crossValidateClassifier(testTrainingData, 2);
        TestCase.assertEquals(0.5, e.weightedAreaUnderROC());
        double[][] confusionMatrix = e.confusionMatrix();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.print(confusionMatrix[i][j] + "  ");

            }
            System.out.println();
        }
        TestCase.assertEquals(0.0, (confusionMatrix[0][0] / (confusionMatrix[0][1] + confusionMatrix[0][0])) * 100);
        TestCase.assertEquals(100.0, (confusionMatrix[1][1] / (confusionMatrix[1][1] + confusionMatrix[1][0])) * 100);
    }

    @Test
    public void testBuildSVM() throws Exception {
        Instance i = testTrainingData.instance(0);
        svmClassifierHandler.buildSVM(testTrainingData, false);
        TestCase.assertEquals(1.0, svmClassifierHandler.classifyInstance(i));
    }


    @Test
    public void testGetSvm() throws Exception {
        TestCase.assertTrue(LibSVMExtended.class.isInstance(svmClassifierHandler.getSvm()));
    }


}