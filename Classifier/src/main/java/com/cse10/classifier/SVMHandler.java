package com.cse10.classifier;

import com.cse10.article.Article;
import com.cse10.article.ArticleConverter;
import com.cse10.article.CrimeArticle;
import com.cse10.database.DatabaseConstants;
import com.cse10.database.DatabaseHandler;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.core.*;
import weka.core.converters.DatabaseLoader;
import weka.core.stemmers.SnowballStemmer;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Tharindu on 2014-11-11.
 */
public class SVMHandler {

    protected LibSVM svm;
    protected StringToWordVector filter; //same filter should be used for training, evaluating, and classifying
    protected Instances dataRaw;
    protected Instances dataFiltered;

    /**
     * prepare the filter, filter the training data and build the SVM
     *
     * @return
     * @throws Exception
     */
    public LibSVM buildSVM() throws Exception {

        //set tokenizer - we can specify n-grams for classification
        NGramTokenizer tokenizer = new NGramTokenizer();
        tokenizer.setNGramMinSize(1);
        tokenizer.setNGramMaxSize(1);
        tokenizer.setDelimiters("\\W");

        //set stemmer - set english stemmer
        SnowballStemmer stemmer = new SnowballStemmer();
        stemmer.setStemmer("english");

        StanfordCoreNLPLemmatizer scnlpl = new StanfordCoreNLPLemmatizer();

        //create new filter for vector transformation
        filter = new StringToWordVector();
        filter.setLowerCaseTokens(true);
        filter.setOutputWordCounts(true);
        filter.setTFTransform(true);
        filter.setIDFTransform(true);
        filter.setStopwords(new File("E:\\PROJECT-workspace\\SVM-classification\\data\\StopWordsR1.txt"));
        filter.setTokenizer(tokenizer);
//        filter.setStemmer(stemmer);
        filter.setStemmer(scnlpl);
        System.out.println("Stemmer Name- " + filter.getStemmer());

        dataRaw = loadTrainingData();

        // apply the StringToWordVector filter
        filter.setInputFormat(dataRaw);
        dataFiltered = Filter.useFilter(dataRaw, filter);
        System.out.println("Number of Attributes after stop words removal- " + dataFiltered.numAttributes());
        System.out.println("\n\nFiltered data:\n\n" + dataFiltered);

        //initialize the model and set SVM type and kernal type
        LibSVM svm = new LibSVM();
        //-S 1 -K 3 -D 3 -G 0.0 -R 0.0 -N 0.5 -M 40.0 -C 1.0 -E 0.0010 -P 0.1 Best G 0.001953125 for C=32578
        String svmOptions = "-S 0 -K 2 -C 8 -G 0.001953125"; //c=32578, g=0.0038498
        svm.setOptions(weka.core.Utils.splitOptions(svmOptions));
        System.out.println("&&&&&&&&" + svm.getSVMType() + svm.getKernelType());//1,3 best result 81%
        svm.buildClassifier(dataFiltered);

        return svm;
    }

    /**
     * Cross validate the classifier
     *
     * @throws Exception
     */
    public void crossValidateClassifier() throws Exception {

        if (svm == null) {
            svm = buildSVM();
        }

//        //create a filtered classifier
//        FilteredClassifier fc = new FilteredClassifier();
//        // set classifier and filter for filtered classifier
//        fc.setClassifier(svm);
//        fc.setFilter(filter);


        //    perform cross vlaidation
        Evaluation evaluation = new Evaluation(dataFiltered);
        evaluation.crossValidateModel(svm, dataFiltered, 4, new Random(1));
        System.out.println(evaluation.toSummaryString());
        System.out.println(evaluation.weightedAreaUnderROC());

    }

    /**
     * Test the classification (just print the results)
     *
     * @param articleClass which type of articles that need to be classified (ex:- CeylonTodayArticle.class)
     * @param constrain
     * @throws Exception
     */
    public void testClassifier(Class articleClass, String constrain) throws Exception {

        if (svm == null) {
            svm = buildSVM();
        }

        //get test instances and perform predictions
        Instances testData = loadTestData(articleClass, constrain);
        Instances testDataFiltered = Filter.useFilter(testData, filter);

        for (int i = 0; i < testDataFiltered.numInstances(); i++) {

            System.out.println(testData.instance(i));
            System.out.println(svm.classifyInstance(testDataFiltered.instance(i)));
            System.out.println();

        }
    }

    /**
     * classify the articles and store crime articles in article_crime table
     *
     * @param articleClass which type of articles that need to be classified (ex:- CeylonTodayArticle.class)
     * @param constrain
     * @throws Exception
     */
    public void classifyNews(Class articleClass, String constrain) throws Exception {

        if (svm == null) {
            svm = buildSVM();
        }

        FastVector attributeList = new FastVector(2);
        Attribute a1 = new Attribute("text", (FastVector) null);

        FastVector classVal = new FastVector();
        classVal.addElement("crime");
        classVal.addElement("other");
        Attribute c = new Attribute("@@class@@", classVal);

        //add class attribute and news text
        attributeList.addElement(a1);
        attributeList.addElement(c);


        Instances classifyData = new Instances("TestNews", attributeList, 0);
        if (classifyData.classIndex() == -1) {
            classifyData.setClassIndex(classifyData.numAttributes() - 1);
        }

        HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();

        String tableName = new DatabaseConstants().classToTableName.get(articleClass);
        String q = "SELECT id, content FROM " + tableName + " " + constrain;
        ResultSet rs = DatabaseHandler.executeQuery(q);

        int instNumber = 0;
        while (rs.next()) {

            int id = rs.getInt("id");
            String news = rs.getString("content");

            Instance inst = new Instance(classifyData.numAttributes());
            inst.setValue(a1, news);

            inst.setDataset(classifyData);
            inst.setClassMissing();

//            System.out.println(inst);
//            System.out.println();
            classifyData.add(inst);

            hashMap.put(instNumber, id); //in order to keep track of ID
            instNumber++;

        }

        Instances classifyDataFiltered = Filter.useFilter(classifyData, filter);

        List<Integer> crimeArticleIdList = new ArrayList<Integer>();

        for (instNumber = 0; instNumber < classifyDataFiltered.numInstances(); instNumber++) {

            System.out.println(classifyData.instance(instNumber));
            System.out.println(classifyDataFiltered.instance(instNumber));

            if(svm == null){
                System.out.println("nullllllll");
            }

            double category = svm.classifyInstance(classifyDataFiltered.instance(instNumber));

            System.out.println(category);
            System.out.println();

            if (category == 0) { // if crime
                crimeArticleIdList.add(hashMap.get(instNumber));
            }

        }

        List<Article> articles = DatabaseHandler.fetchArticlesByIdList(articleClass, crimeArticleIdList);

        // to prepare them as crime articles
        List<CrimeArticle> crimeArticles = ArticleConverter.convertToCrimeArticle(articles, articleClass);

        DatabaseHandler.insertArticles(crimeArticles);

    }

    /**
     * fetch data from article_training table
     *
     * @return Instances
     * @throws Exception
     */
    private Instances loadTrainingData() throws Exception {
        FastVector attributeList = new FastVector(2);
        Attribute a1 = new Attribute("text", (FastVector) null);

        FastVector classVal = new FastVector();
        classVal.addElement("crime");
        classVal.addElement("other");
        Attribute c = new Attribute("@@class@@", classVal);

        //add class attribute and news text
        attributeList.addElement(a1);
        attributeList.addElement(c);


        Instances trainingData = new Instances("TrainingNews", attributeList, 0);
        if (trainingData.classIndex() == -1) {
            trainingData.setClassIndex(trainingData.numAttributes() - 1);
        }

        DatabaseLoader databaseLoader = new DatabaseLoader();

        String q = "SELECT content, label FROM article_training"; //WHERE label != 'accident'

        databaseLoader.setUser(DatabaseConstants.DB_USERNAME);
        databaseLoader.setPassword(DatabaseConstants.DB_PASSWORD);
        databaseLoader.setQuery(q);

        Instances dataRaw0 = databaseLoader.getDataSet();

        //for each test instance

        for (int i = 0; i < dataRaw0.numInstances(); i++) {

//            System.out.println(dataRaw0.instance(i));

            String news = dataRaw0.instance(i).stringValue(0);
            String label = dataRaw0.instance(i).stringValue(1);
            if (label.equalsIgnoreCase("accident")) {
                label = "other"; //accidents are not considered as crimes
            }

            Instance inst = new Instance(trainingData.numAttributes());
            inst.setValue(a1, news);
            inst.setValue(c, label);

            inst.setDataset(trainingData);

//            System.out.println(inst);
//            System.out.println();
            trainingData.add(inst);
        }

        trainingData.setClassIndex(trainingData.numAttributes() - 1);

        return trainingData;
    }

    /**
     * fetch test data under given conditions
     *
     * @param articleClass which type of articles that need to be classified (ex:- CeylonTodayArticle.class)
     * @param constrain    specify WHERE clause including 'where '
     * @return
     * @throws Exception
     */
    private Instances loadTestData(Class articleClass, String constrain) throws Exception {

        FastVector attributeList = new FastVector(2);
        Attribute a1 = new Attribute("text", (FastVector) null);

        FastVector classVal = new FastVector();
        classVal.addElement("crime");
        classVal.addElement("other");
        Attribute c = new Attribute("@@class@@", classVal);

        //add class attribute and news text
        attributeList.addElement(a1);
        attributeList.addElement(c);


        Instances testData = new Instances("TestNews", attributeList, 0);
        if (testData.classIndex() == -1) {
            testData.setClassIndex(testData.numAttributes() - 1);
        }

        DatabaseLoader databaseLoader = new DatabaseLoader();

        String tableName = new DatabaseConstants().classToTableName.get(articleClass);
        String q = "SELECT content FROM " + tableName + " " + constrain;

        databaseLoader.setUser(DatabaseConstants.DB_USERNAME);
        databaseLoader.setPassword(DatabaseConstants.DB_PASSWORD);
        databaseLoader.setQuery(q);

        Instances dataRaw0 = databaseLoader.getDataSet();

        //for each test instance

        for (int i = 0; i < dataRaw0.numInstances(); i++) {

//            System.out.println(dataRaw0.instance(i));

            String news = dataRaw0.instance(i).toString();

            Instance inst = new Instance(testData.numAttributes());
            inst.setValue(a1, news);

            inst.setDataset(testData);
            inst.setClassMissing();

//            System.out.println(inst);
//            System.out.println();
            testData.add(inst);
        }

        return testData;
    }

}
