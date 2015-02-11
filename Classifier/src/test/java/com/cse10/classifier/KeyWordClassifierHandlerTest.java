package com.cse10.classifier;

import com.cse10.database.DatabaseConstants;
import junit.framework.TestCase;
import org.junit.*;
import org.junit.Test;
import weka.core.Instances;
import weka.core.tokenizers.NGramTokenizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

//get data from file
public class KeyWordClassifierHandlerTest {

    private KeyWordClassifierHandler keyWordClassifierHandler;
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
        keyWordClassifierHandler = new KeyWordClassifierHandler();
    }

    @After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void testConfigure() throws Exception {
        keyWordClassifierHandler.configure(1, 1, "\\W");
        NGramTokenizer tokenizer = keyWordClassifierHandler.getTokenizer();
        TestCase.assertEquals(1, tokenizer.getNGramMinSize());
        TestCase.assertEquals(1, tokenizer.getNGramMaxSize());
        TestCase.assertEquals("\\W", tokenizer.getDelimiters());
    }

    @Test
    public void testCrossValidateClassifier() throws Exception {
        Instances testTrainingData;
        BufferedReader reader = new BufferedReader(
                new FileReader("Classifier\\src\\main\\resources\\testData\\rawTestData"));
        testTrainingData = new Instances(reader);
        reader.close();
        List<Double> accuracyValues = keyWordClassifierHandler.crossValidateClassifier(testTrainingData);
        TestCase.assertEquals(100.0, accuracyValues.get(0));
        TestCase.assertEquals(63.63636363636363, accuracyValues.get(1));
    }

    @Test
    public void testClassifyInstance() throws Exception {
        //already tested in other classes
    }
}