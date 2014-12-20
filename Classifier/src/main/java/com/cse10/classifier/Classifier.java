package com.cse10.classifier;

import com.cse10.article.Article;
import com.cse10.article.ArticleConverter;
import com.cse10.article.CeylonTodayArticle;
import com.cse10.article.CrimeArticle;
import com.cse10.database.DatabaseHandler;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by TharinduWijewardane on 2014-11-14.
 */
public class Classifier {

    private DataHandler dataHandler;
    private Instances trainingData;
    private Instances filteredTrainingData;
    private SVMClassifier svmClassifier;
    private GridSearch gridSearch;
    private FeatureVectorTransformer featureVectorTransformer;

    public Classifier() {
        dataHandler = new GenericDataHandler();
        gridSearch = new GridSearch();
        svmClassifier = new SVMClassifier();
        featureVectorTransformer = new FeatureVectorTransformer();
    }

    /**
     * load training data from the database
     */
    public void loadTrainingData() {
        try {
            trainingData = dataHandler.loadTrainingData();
            System.out.println("--------------Training Data Details-------------------------");
            System.out.println(trainingData);
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
     */
    public void filterData() {
        featureVectorTransformer.configure(1, 1, true);
        featureVectorTransformer.setInputFormat(trainingData);
        filteredTrainingData = featureVectorTransformer.getTransformedArticles(trainingData,dataHandler.getFileName());
    }

    /**
     * perform grid search to find best cost and gamma values
     */
    public void performGridSearch() {

        gridSearch.gridSearch(svmClassifier.getSvm(), filteredTrainingData);
    }

    /**
     * cross validate the svm model using
     */
    public void crossValidateModel() {
        System.out.println("-----------------------Start LibSVM---------------------------");
        //using parameters found during the grid search
        svmClassifier.configure(8.0, 0.001953125, "10 1", true);
        svmClassifier.crossValidateClassifier(filteredTrainingData, 10);
        System.out.println("---------------------------------------------------------------");
    }

    /**
     * build the model using training data
     */
    public void buildModel() {
        svmClassifier.buildSVM(filteredTrainingData);
    }

    /**
     * classify news articles
     */
    public void classifyNewsArticles() {
        Instances testData = dataHandler.loadTestData(CeylonTodayArticle.class, "WHERE 1");
        HashMap<Integer, Integer> articleIds = dataHandler.getArticleIds();
        Instances filteredTestData = featureVectorTransformer.getTransformedArticles(testData);
        List<Integer> crimeArticleIdList = new ArrayList<Integer>();

        for (int instNumber = 0; instNumber < filteredTestData.numInstances(); instNumber++) {

            double category = svmClassifier.classifyInstance(filteredTestData.instance(instNumber));
            if (category == 0) { // if crime
                crimeArticleIdList.add(articleIds.get(instNumber));
            }
        }

        List<Article> articles = DatabaseHandler.fetchArticlesByIdList(CeylonTodayArticle.class, crimeArticleIdList);

        // to prepare them as crime articles
        List<CrimeArticle> crimeArticles = ArticleConverter.convertToCrimeArticle(articles, CeylonTodayArticle.class);

        DatabaseHandler.insertArticles(crimeArticles);

    }

    public static void main(String[] args) {

        Classifier classifier = new Classifier();
        classifier.loadTrainingData();
        classifier.filterData();
        //classifier.performGridSearch();
        classifier.crossValidateModel();
        classifier.buildModel();
        classifier.classifyNewsArticles();

    }

}
