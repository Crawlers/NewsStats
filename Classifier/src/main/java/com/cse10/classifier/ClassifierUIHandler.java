package com.cse10.classifier;

import com.cse10.article.*;
import com.cse10.database.DatabaseConstants;
import com.cse10.database.DatabaseHandler;
import com.cse10.util.ArticleConverter;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;

/**
 * Created by chamath on 12/20/2014
 */
public class ClassifierUIHandler extends Observable {

    private DataHandler dataHandler;
    private Instances trainingData;
    private Instances filteredTrainingData;
    private SVMClassifierHandler svmClassifierHandler;
    private GridSearch gridSearch;
    private boolean isModelBuild;
    private static ClassifierUIHandler singleInstance;
    // no need if we use DataHandlerWithSampling data handler, it converts training data into feature vector
    private FeatureVectorTransformer featureVectorTransformer;

    private ClassifierUIHandler() {
        dataHandler = new GenericDataHandler();
        gridSearch = new GridSearch();
        svmClassifierHandler = new SVMClassifierHandler();
        featureVectorTransformer = new FeatureVectorTransformer();
        isModelBuild=false;
    }

    public synchronized static ClassifierUIHandler getInstance(){
        if(singleInstance==null){
            singleInstance=new ClassifierUIHandler();
        }
        return singleInstance;
    }

    /**
     * load training data from the database
     */
    private void loadTrainingData() {
        try {
            System.out.println(Thread.currentThread().getName()+"\n Classifier UI Handler -> Start Data Loading");
            trainingData = dataHandler.loadTrainingData(featureVectorTransformer);
            System.out.println(Thread.currentThread().getName()+"\n Classifier UI Handler -> Training Data Details");
            System.out.println(Thread.currentThread().getName()+"  Number of Articles= " + trainingData.numInstances());
            int crimeCount = 0;
            int otherCount = 0;
            for (int i = 0; i < trainingData.numInstances(); i++) {
                if (trainingData.instance(i).classValue() == 0.0)
                    crimeCount++;
                else
                    otherCount++;
            }
            System.out.println(Thread.currentThread().getName()+"  Number of Crime Articles= " + crimeCount);
            System.out.println(Thread.currentThread().getName()+"  Number of Other Articles= " + otherCount);
            System.out.println(Thread.currentThread().getName()+"\n Classifier UI Handler -> End of Data Loading");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * filter data, transform training articles to feature vectors
     * If data handler return filtered training data ( i.e. in DataHandlerWithSampling) then no need to
     * filter data.
     */
    private void filterData() {
        System.out.println(Thread.currentThread().getName()+"\n Classifier UI Handler -> Start Data Filtering");
        featureVectorTransformer.configure(1, 1, false);
        featureVectorTransformer.setInputFormat(trainingData);
        filteredTrainingData = featureVectorTransformer.getTransformedArticles(trainingData, dataHandler.getFileName());
        System.out.println(Thread.currentThread().getName()+"\n Classifier UI Handler -> End of Data Filtering");
    }

    /**
     * perform grid search to find best cost and gamma values
     */
    private void performGridSearch() {
        System.out.println(Thread.currentThread().getName()+"\n Classifier UI Handler -> Start Grid Search");
        gridSearch.gridSearch(svmClassifierHandler.getSvm(), filteredTrainingData);
        System.out.println(Thread.currentThread().getName()+"\n Classifier UI Handler -> End of Grid Search");
    }

    /**
     * cross validate the svm model using
     * if we use different weights, we need to normalize data
     */
    private void crossValidateModel() {

        System.out.println(Thread.currentThread().getName()+"\n Classifier UI Handler -> Start cross validation");
        // using parameters found during the grid search
        svmClassifierHandler.configure(8.0, 0.001953125, "10 1", true);
        svmClassifierHandler.crossValidateClassifier(filteredTrainingData, 10);
        System.out.println(Thread.currentThread().getName()+"\n Classifier UI Handler -> End of cross validation");

    }

    /**
     * buildClassifier the model using training data and save model
     */
    private void buildModel() {
        System.out.println(Thread.currentThread().getName()+"\n Classifier UI Handler -> Start building model");
        svmClassifierHandler.buildSVM(filteredTrainingData, true);
        System.out.println(Thread.currentThread().getName()+"\n Classifier UI Handler -> End of building model");
    }


    /**
     * load training data, filter data and perform grid search, cross validate model,buildClassifier and save
     * model, this function is used by GUI
     */
    public synchronized void buildClassifier() {
        System.out.println("\n--------------------------------------------------------------");
        int progress = 0;
        if (!isModelBuild) {
            System.out.println(Thread.currentThread().getName()+" Classifier UI Handler -> Building Classifier");

            loadTrainingData();
            progress = 20;
            setChanged();
            notifyObservers(progress);

            //DataHandlerWithSampling apply filtering before returning training data, so no need to filter again
            filterData();
            progress = 40;
            setChanged();
            notifyObservers(progress);

            //only perform this if we need to change  cost and gamma values
            //performGridSearch();

            crossValidateModel();
            progress = 60;
            setChanged();
            notifyObservers(progress);

            buildModel();
            progress = 80;
            setChanged();
            notifyObservers(progress);
            isModelBuild=true;
            System.out.println(Thread.currentThread().getName()+" Classifier UI Handler ->  End of Building Classifier");
        }else{
            System.out.println(Thread.currentThread().getName()+" Classifier UI Handler ->  Classifier is Already Existing");
        }
        System.out.println("---------------------------------------------------------------------------");
    }


    /**
     * classify news articles
     *
     * @param tableName
     */
    public void classifyNewsArticles1(Class tableName) {

        System.out.println("\n-------------------------------------------------------------------------");
        System.out.println(Thread.currentThread().getName()+ " Classifier UI Handler -> Start Classifying Articles");
        System.out.println(Thread.currentThread().getName()+" Classifier UI Handler -> Start Loading Test Data");

        //get the last id of that news paper's classified articles
        DatabaseConstants databaseConstants = new DatabaseConstants();
        String articleName = (String) databaseConstants.classToTableName.get(tableName);
        int maxId = DatabaseHandler.getMaxIdOf(CrimeArticle.class, articleName);
        System.out.println(Thread.currentThread().getName()+"  Classifier UI Handler -> MaxId =" + maxId);
        //add that id to below condition. then get only the unclassified data

        //get only unclassified data using weka loading
        Instances testData = dataHandler.loadTestData(tableName, "WHERE id > " + Integer.toString(maxId), true); //`created_date`<'2013-06-01'

        System.out.println(Thread.currentThread().getName()+" Classifier UI Handler -> Finish Loading Test Data");
        System.out.println(Thread.currentThread().getName()+" Classifier UI Handler -> Size of Test Data= " + testData.numInstances());

        if (testData.numInstances() != 0) {
            HashMap<Integer, Integer> articleIds = dataHandler.getArticleIds();
            Instances filteredTestData = featureVectorTransformer.getTransformedArticles(testData);
            List<Integer> crimeArticleIdList = new ArrayList<Integer>();

            for (int instNumber = 0; instNumber < filteredTestData.numInstances(); instNumber++) {
                double category = svmClassifierHandler.classifyInstance(filteredTestData.instance(instNumber));
                if (category == 0) { // if crime
                    crimeArticleIdList.add(articleIds.get(instNumber));
                }
            }


            System.out.println(Thread.currentThread().getName()+" Classifier UI Handler -> size of ID list = " + crimeArticleIdList.size());
            ListIterator iter;
            List<Article> articles = DatabaseHandler.fetchArticlesByIdList(tableName, crimeArticleIdList);

            System.out.println(Thread.currentThread().getName()+" Classifier UI Handler -> size of article list= " + articles.size());
            System.out.println(Thread.currentThread().getName()+" Classifier UI Handler -> Article Titles");
            System.out.println("  {");
            iter = articles.listIterator();
            while (iter.hasNext()) {
                Article a = (Article) iter.next();
                System.out.println("    " + a.getTitle());
            }
            System.out.println("  }");

            // to prepare them as crime articles
            List<CrimeArticle> crimeArticles = ArticleConverter.convertToCrimeArticle(articles, tableName);

            iter = crimeArticles.listIterator();
            System.out.println(Thread.currentThread().getName()+" Classifier UI Handler -> size of crime article list= " + crimeArticles.size());
            System.out.println(Thread.currentThread().getName()+" Classifier UI Handler -> Crime Article Titles");
            System.out.println("  {");
            while (iter.hasNext()) {
                Article a = (Article) iter.next();
                System.out.println("     " + a.getTitle());
            }
            System.out.println("  }");

            DatabaseHandler.insertArticles(crimeArticles);

        } else {
            System.out.println(Thread.currentThread().getName()+" No New Articles to Classify");
        }

        System.out.println(Thread.currentThread().getName()+" Classifier UI Handler -> End of Classifying Articles");
        int progress = 100;
        setChanged();
        notifyObservers(progress);

        //to finish hybernate session and close database. other wise JVM will run continuously
        //DatabaseHandler.closeDatabase();

        System.out.println("---------------------------------------------------------------------------------");
    }

    /**
     * classify news articles
     *
     * @param tableName
     */
    public void classifyNewsArticles(Class tableName) {

        System.out.println(Thread.currentThread().getName()+"\n-------------------------------------------------------------------------");
        System.out.println(Thread.currentThread().getName()+" Classifier UI Handler -> Start Classifying Articles");
        System.out.println(Thread.currentThread().getName()+" Classifier UI Handler -> Start Loading Test Data");


        //get only unclassified data using weka loading
        Instances testData = dataHandler.loadTestData(tableName, "WHERE  label IS NULL", true); //`created_date`<'2013-06-01'

        System.out.println(Thread.currentThread().getName()+" Classifier UI Handler -> Finish Loading Test Data");
        System.out.println(Thread.currentThread().getName()+" Classifier UI Handler -> Size of Test Data= " + testData.numInstances());

        if (testData.numInstances() != 0) {
            List<Article> testDataArticles = DatabaseHandler.fetchArticlesWithNullLabels(tableName);
            HashMap<Integer, Integer> articleIds = dataHandler.getArticleIds();
            Instances filteredTestData = featureVectorTransformer.getTransformedArticles(testData);
            List<Integer> crimeArticleIdList = new ArrayList<Integer>();

            for (int instNumber = 0; instNumber < filteredTestData.numInstances(); instNumber++) {
                double category = svmClassifierHandler.classifyInstance(filteredTestData.instance(instNumber));
                if (category == 0) { // if crime
                    crimeArticleIdList.add(articleIds.get(instNumber));
                }
            }

           /* for (Article article : testDataArticles) {
                if (crimeArticleIdList.contains(article.getId())) {
                    article.setLabel("crime");
                } else {
                    article.setLabel("other");
                }
                DatabaseHandler.updateArticle(article);
            }*/

            System.out.println(Thread.currentThread().getName()+" Classifier UI Handler -> size of ID list = " + crimeArticleIdList.size());
            ListIterator iter;
            List<Article> articles = DatabaseHandler.fetchArticlesByIdList(tableName, crimeArticleIdList);

            System.out.println(Thread.currentThread().getName()+" Classifier UI Handler -> size of article list= " + articles.size());
            System.out.println(Thread.currentThread().getName()+" Classifier UI Handler -> Article Titles");
            System.out.println("  {");
            iter = articles.listIterator();
            while (iter.hasNext()) {
                Article a = (Article) iter.next();
                System.out.println("    " + a.getTitle());
            }
            System.out.println("  }");

            // to prepare them as crime articles
            List<CrimeArticle> crimeArticles = ArticleConverter.convertToCrimeArticle(articles, tableName);

            iter = crimeArticles.listIterator();
            System.out.println(Thread.currentThread().getName()+" Classifier UI Handler -> size of crime article list= " + crimeArticles.size());
            System.out.println(Thread.currentThread().getName()+" Classifier UI Handler -> Crime Article Titles");
            System.out.println("  {");
            while (iter.hasNext()) {
                Article a = (Article) iter.next();
                System.out.println("     " + a.getTitle());
            }
            System.out.println("  }");

            //below line should be replace by a transaction
            //DatabaseHandler.insertArticles(crimeArticles);

            //transaction
            System.out.println(Thread.currentThread().getName()+"   Classifier UI Handler -> Transaction");
        /*    for(CrimeArticle crimeArticle:crimeArticles){
                for(Article article:testDataArticles){
                    if(article.getId()<crimeArticle.getNewspaperId() && article.getLabel()==null){
                        article.setLabel("other");
                        DatabaseHandler.updateArticle(article);
                    }else if(article.getId()==crimeArticle.getNewspaperId() && article.getLabel()==null){
                        article.setLabel("crime");
                        //transaction
                        *//*DatabaseHandler.insertArticle(crimeArticle);
                        DatabaseHandler.updateArticle(article);*//*
                        DatabaseHandler.insertCrimeArticleAndUpdatePprArticle(crimeArticle,article);
                        //end of transaction
                        break;
                    }
                }
            }*/

             for (Article article : testDataArticles) {
                if (crimeArticleIdList.contains(article.getId())) {
                    article.setLabel("crime");
                    for(CrimeArticle crimeArticle:crimeArticles){
                        if(article.getId()==crimeArticle.getNewspaperId()){
                            DatabaseHandler.insertCrimeArticleAndUpdatePprArticle(crimeArticle,article);
                        }
                    }
                } else {
                    article.setLabel("other");
                    DatabaseHandler.updateArticle(article);
                }

            }


        } else {
            System.out.println(Thread.currentThread().getName()+"  Classifier UI Handler -> No New Articles to Classify");
        }

        System.out.println(Thread.currentThread().getName()+" Classifier UI Handler -> End of Classifying Articles");
        int progress = 100;
        setChanged();
        notifyObservers(progress);

        //to finish hybernate session and close database. other wise JVM will run continuously
        DatabaseHandler.closeDatabase();

        System.out.println(Thread.currentThread().getName()+"---------------------------------------------------------------------------------");
    }

    /**
     * use to test hybernate and weka db connection
     */
    private void testHybernate() {
        //to test weka db connection
        loadTrainingData();
        //to test hybernate connection
        Instances testData = dataHandler.loadTestData(DailyMirrorArticle.class, "WHERE  `created_date` <  '2012-01-06'", false);
        System.out.println(testData.numInstances());
    }

    public void print(int number){

    }

    public static void main(String[] args) {

        ClassifierUIHandler classifierUIHandler = ClassifierUIHandler.getInstance();
        classifierUIHandler.buildClassifier();
        classifierUIHandler.classifyNewsArticles(DailyMirrorArticle.class);
        classifierUIHandler.buildClassifier();
        classifierUIHandler.classifyNewsArticles(CeylonTodayArticle.class);
        classifierUIHandler.buildClassifier();
        classifierUIHandler.classifyNewsArticles(TheIslandArticle.class);
        classifierUIHandler.buildClassifier();
        classifierUIHandler.classifyNewsArticles(DailyMirrorArticle.class);



    }

}
