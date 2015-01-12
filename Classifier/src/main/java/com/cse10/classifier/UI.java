package com.cse10.classifier;

import com.cse10.article.*;
import com.cse10.database.DatabaseHandler;
import com.cse10.util.ArticleConverter;
import weka.core.Instances;
import weka.core.SerializationHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by chamath on 12/20/2014
 */
public class UI {

    private DataHandler dataHandler;
    private Instances trainingData;
    private Instances filteredTrainingData;
    private SVMClassifierHandler svmClassifierHandler;
    private GridSearch gridSearch;
    // no need if we use DataHandlerWithSampling data handler, it converts training data into feature vector
    private FeatureVectorTransformer featureVectorTransformer;

    public UI() {
        dataHandler = new GenericDataHandler();
        gridSearch = new GridSearch();
        svmClassifierHandler = new SVMClassifierHandler();
        featureVectorTransformer = new FeatureVectorTransformer();
    }

    /**
     * load training data from the database
     */
    public void loadTrainingData() {
        try {
            System.out.println("Start Data Handler--------------------------------------------");
            trainingData = dataHandler.loadTrainingData(featureVectorTransformer);
            System.out.println("--------------------------------------------------------------");
            System.out.println("Training Data Details-----------------------------------------");
            // System.out.println(trainingData);
            System.out.println("Number of Articles= " + trainingData.numInstances());
            int crimeCount = 0;
            int otherCount = 0;
            for (int i = 0; i < trainingData.numInstances(); i++) {
                if (trainingData.instance(i).classValue() == 0.0)
                    crimeCount++;
                else
                    otherCount++;
            }
            System.out.println("Number of Crime Articles= " + crimeCount);
            System.out.println("Number of Other Articles= " + otherCount);
            System.out.println("---------------------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * filter data, transform training articles to feature vectors
     * If data handler return filtered training data ( i.e. in DataHandlerWithSampling) then no need to
     * filter data.
     */
    public void filterData() {
        featureVectorTransformer.configure(1, 1,false);
        featureVectorTransformer.setInputFormat(trainingData);
        filteredTrainingData = featureVectorTransformer.getTransformedArticles(trainingData, dataHandler.getFileName());
    }

    /**
     * perform grid search to find best cost and gamma values
     */
    public void performGridSearch() {

        gridSearch.gridSearch(svmClassifierHandler.getSvm(), filteredTrainingData);
    }

    /**
     * cross validate the svm model using
     * if we use different weights, we need to normalize data
     */
    public void crossValidateModel() {

        // using parameters found during the grid search

        svmClassifierHandler.configure(8.0, 0.001953125, "10 1", true);
        svmClassifierHandler.crossValidateClassifier(filteredTrainingData, 10);

    }

    /**
     * build the model using training data and save model
     */
    public void buildModel() {
        svmClassifierHandler.buildSVM(filteredTrainingData, true);
    }

    /**
     * classify news articles
     * @param tableName
     */
    public void classifyNewsArticles(Class tableName) {

        Instances testData = dataHandler.loadTestData(tableName, "WHERE  `created_date` >  '2013-12-31'",true); //`created_date`<'2013-06-01'
        System.out.println("Size of test data= "+testData.numInstances());
        System.out.println("classfy-1");
        HashMap<Integer, Integer> articleIds = dataHandler.getArticleIds();

        Instances filteredTestData = featureVectorTransformer.getTransformedArticles(testData);
        List<Integer> crimeArticleIdList = new ArrayList<Integer>();
        System.out.println("classfy-2");
        for (int instNumber = 0; instNumber < filteredTestData.numInstances(); instNumber++) {

            double category = svmClassifierHandler.classifyInstance(filteredTestData.instance(instNumber));
            System.out.println(category);
            if (category == 0) { // if crime
                crimeArticleIdList.add(articleIds.get(instNumber));
            }
        }

        System.out.println("size of ID list = "+crimeArticleIdList.size());
        ListIterator iter=crimeArticleIdList.listIterator();
        while(iter.hasNext()){
            Integer id=(Integer)iter.next();
            System.out.println(id);
        }
        System.out.println("classfy-3");
        List<Article> articles = DatabaseHandler.fetchArticlesByIdList(tableName, crimeArticleIdList);

        System.out.println("size of article list="+articles.size());
        iter=articles.listIterator();
        while(iter.hasNext()){
            Article a=(Article)iter.next();
            System.out.println(a.getTitle());
        }
        System.out.println("classfy-4");

        // to prepare them as crime articles
        List<CrimeArticle> crimeArticles = ArticleConverter.convertToCrimeArticle(articles, tableName);
        System.out.println("classfy-5");

        System.out.println("size of crime article list="+crimeArticles.size());
        iter=crimeArticles.listIterator();
        while(iter.hasNext()){
            Article a=(Article)iter.next();
            System.out.println(a.getTitle());
        }
        System.out.println("classfy-6");
        DatabaseHandler.insertArticles(crimeArticles);
    }


    /**
     *
     * @return
     */
    public FeatureVectorTransformer getFeatureVectorTransformer() {
        return featureVectorTransformer;
    }

    /**
     * load training data, filter data and perform grid search, cross validate model,build and save
     * model
     */
    public void build() {
        loadTrainingData();
        //DataHandlerWithSampling apply filtering before returning training data, so no need to filter again
        filterData();
       // performGridSearch();
        crossValidateModel();
        buildModel();

    }

    /**
     * classify news articles
     * this method is only required if we use support vector based approach
     * retrieve saved svm model, then create filter with same training data and then classify unknown articles
     * @param tableName
     */
    public void classify(Class tableName){

        /*try {
            LibSVMExtended libSVMExtended = (LibSVMExtended) SerializationHelper.read("C:\\Users\\hp\\Desktop\\SVM implementation\\arffData1\\svm.model");
            svmClassifierHandler.setSvm(libSVMExtended);
            GenericDataHandler genericDataHandler=new GenericDataHandler();
            Instances instances=genericDataHandler.loadTrainingData(new FeatureVectorTransformer());
            System.out.println("configure");
            featureVectorTransformer.configure(1, 1, false);
            System.out.println("setInputFormat");
            featureVectorTransformer.setInputFormat(instances);
            System.out.println("filter data");
            filteredTrainingData = featureVectorTransformer.getTransformedArticles(instances, dataHandler.getFileName());
            //call after loading saved svm model and configuring the feature vector transformer
            System.out.println("classify");
            classifyNewsArticles(tableName);
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        classifyNewsArticles(tableName);
        // classifyNewsArticles(CeylonTodayArticle.class);
        // classifyNewsArticles(DailyMirrorArticle.class);

    }

    public static void main(String[] args) {

        UI UI = new UI();
        UI.build();
        UI.classify(DailyMirrorArticle.class);

    }

}
