package com.cse10.classifier;

import com.cse10.article.*;
import com.cse10.database.DatabaseConstants;
import com.cse10.util.ArticleConverter;
import weka.core.Instances;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * Combine all of the functionality, used by ui handlers
 * Created by Chamath on 12/20/2014
 */
public class ClassifierConfigurator extends Observable {

    private DataHandler dataHandler;
    private Instances trainingData;
    private Instances filteredTrainingData;
    private SVMClassifierHandler svmClassifierHandler;
    private GridSearch gridSearch;
    private boolean isModelBuild;
    // no need if we use DataHandlerWithSampling data handler, it converts training data into feature vector
    private FeatureVectorTransformer featureVectorTransformer;
    //singleton
    private static ClassifierConfigurator classifierConfigurator;
    private Logger log;

    private ClassifierConfigurator() {
        dataHandler = new GenericDataHandler();
        gridSearch = new GridSearch();
        svmClassifierHandler = new SVMClassifierHandler();
        featureVectorTransformer = new FeatureVectorTransformer();
        isModelBuild = false;
        log = Logger.getLogger(this.getClass());
    }

    /**
     * to get singleton instance
     *
     * @return
     */
    public synchronized static ClassifierConfigurator getInstance() {
        if (classifierConfigurator == null) {
            classifierConfigurator = new ClassifierConfigurator();
        }
        return classifierConfigurator;
    }

    /**
     * load training data from the database
     */
    private void loadTrainingData() throws InterruptedException {

        //check if interrupted
        checkInterruption();
        try {
            log.info(Thread.currentThread().getName() + " Classifier UI Handler -> Start Data Loading");
            trainingData = dataHandler.loadTrainingData(featureVectorTransformer);
            log.info(Thread.currentThread().getName() + " Classifier UI Handler -> Training Data Details");
            log.info(Thread.currentThread().getName() + "  Number of Articles= " + trainingData.numInstances());
            int crimeCount = 0;
            int otherCount = 0;
            for (int i = 0; i < trainingData.numInstances(); i++) {
                if (trainingData.instance(i).classValue() == 0.0)
                    crimeCount++;
                else
                    otherCount++;
            }
            log.info(Thread.currentThread().getName() + "  Number of Crime Articles= " + crimeCount);
            log.info(Thread.currentThread().getName() + "  Number of Other Articles= " + otherCount);
            log.info(Thread.currentThread().getName() + " Classifier UI Handler -> End of Data Loading");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * filter data, transform training articles to feature vectors
     * If data handler return filtered training data ( i.e. in DataHandlerWithSampling) then no need to
     * filter data.
     */
    private void filterData() throws InterruptedException {
        //check if interrupted
        checkInterruption();
        log.info(Thread.currentThread().getName() + " Classifier UI Handler -> Start Data Filtering");
        featureVectorTransformer.configure(1, 1, true);
        featureVectorTransformer.setInputFormat(trainingData);
        filteredTrainingData = featureVectorTransformer.getTransformedArticles(trainingData, dataHandler.getFileName());
        log.info(Thread.currentThread().getName() + " Classifier UI Handler -> End of Data Filtering");
    }

    /**
     * perform grid search to find best cost and gamma values
     */
    private void performGridSearch() throws InterruptedException {
        //check if interrupted
        checkInterruption();
        log.info(Thread.currentThread().getName() + " Classifier UI Handler -> Start Grid Search");
        gridSearch.gridSearch(svmClassifierHandler.getSvm(), filteredTrainingData);
        log.info(Thread.currentThread().getName() + " Classifier UI Handler -> End of Grid Search");
    }

    /**
     * cross validate the svm model using
     * if we use different weights, we need to normalize data
     */
    private void crossValidateModel() throws InterruptedException {
        //check if interrupted
        checkInterruption();
        log.info(Thread.currentThread().getName() + " Classifier UI Handler -> Start cross validation");
        // using parameters found during the grid search
        svmClassifierHandler.configure(8.0, 0.001953125, "10 1", true);
        svmClassifierHandler.crossValidateClassifier(filteredTrainingData, 10);
        log.info(Thread.currentThread().getName() + " Classifier UI Handler -> End of cross validation");

    }

    /**
     * buildClassifier the model using training data and save model
     */
    private void buildModel() throws InterruptedException {
        //check if interrupted
        checkInterruption();
        log.info(Thread.currentThread().getName() + " Classifier UI Handler -> Start building model");
        svmClassifierHandler.buildSVM(filteredTrainingData, true);
        log.info(Thread.currentThread().getName() + " Classifier UI Handler -> End of building model");
    }


    /**
     * load training data, filter data and perform grid search, cross validate model,buildClassifier and save
     * model, this function is used by GUI
     */
    private synchronized void buildClassifier(Class tableName) throws InterruptedException {
        log.info("\n--------------------------------------------------------------");
        int progress = 0;

        //if interrupted
        checkInterruption();


        if (!isModelBuild) {
            log.info(Thread.currentThread().getName() + " Classifier UI Handler -> Building Classifier");

            loadTrainingData();
            progress = 20;
            notify(new DatabaseConstants().classToTableName.get(tableName), progress);
            //check if interrupted
            checkInterruption();

            //check whether feature vector transform is required
            if (dataHandler.isFeatureVectorTransformerRequired()) {
                filterData();
            } else {
                filteredTrainingData = trainingData;
            }
            progress = 40;
            notify(new DatabaseConstants().classToTableName.get(tableName), progress);
            //check if interrupted
            checkInterruption();

            //only perform this if we need to change  cost and gamma values
            //performGridSearch();

            crossValidateModel();
            progress = 60;
            notify(new DatabaseConstants().classToTableName.get(tableName), progress);
            //check if interrupted
            checkInterruption();


            buildModel();
            progress = 80;
            notify(new DatabaseConstants().classToTableName.get(tableName), progress);
            //check if interrupted
            checkInterruption();

            isModelBuild = true;

            //check if interrupted
            checkInterruption();
            log.info(Thread.currentThread().getName() + " Classifier UI Handler ->  End of Building Classifier");
        } else {

            progress = 80;
            notify(new DatabaseConstants().classToTableName.get(tableName), progress);
            log.info(Thread.currentThread().getName() + " Classifier UI Handler ->  Classifier is Already Existing");
        }
        log.info("---------------------------------------------------------------------------");
    }

    /**
     * classify news articles
     *
     * @param tableName
     */
    private synchronized void classifyNewsArticles(Class tableName, Date endDate) throws InterruptedException {

        //check if interrupted
        checkInterruption();

        log.info(Thread.currentThread().getName() + "------------------------------------------------------------------------");
        log.info(Thread.currentThread().getName() + " Classifier UI Handler -> Start Classifying Articles");
        log.info(Thread.currentThread().getName() + " Classifier UI Handler -> Start Loading Test Data");

        //convert util.Date to sql.Date
        log.info(Thread.currentThread().getName() + " Classifier UI Handler ->" + endDate);
        java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());


        //get only unclassified data using weka loading
        log.info(Thread.currentThread().getName() + " Classifier UI Handler ->" + sqlEndDate);
        Instances testData = dataHandler.loadTestData(tableName, "WHERE  label IS NULL and `created_date` <= '" + sqlEndDate + "'", true); //`created_date`<'2013-06-01'

        log.info(Thread.currentThread().getName() + " Classifier UI Handler -> Finish Loading Test Data");
        log.info(Thread.currentThread().getName() + " Classifier UI Handler -> Size of Test Data= " + testData.numInstances());

        //check if interrupted
        checkInterruption();

        if (testData.numInstances() != 0) {
            List<Article> testDataArticles = dataHandler.fetchArticlesWithNullLabels(tableName, endDate);
            HashMap<Integer, Integer> articleIds = dataHandler.getArticleIds();
            Instances filteredTestData = featureVectorTransformer.getTransformedArticles(testData);
            List<Integer> crimeArticleIdList = new ArrayList<Integer>();

            for (int instNumber = 0; instNumber < filteredTestData.numInstances(); instNumber++) {
                double category = svmClassifierHandler.classifyInstance(filteredTestData.instance(instNumber));
                if (category == 0) { // if crime
                    crimeArticleIdList.add(articleIds.get(instNumber));
                }
            }

            //check if interrupted
            checkInterruption();

            log.info(Thread.currentThread().getName() + " Classifier UI Handler -> size of ID list = " + crimeArticleIdList.size());
            ListIterator iter;
            List<Article> articles = dataHandler.fetchArticlesByIdList(tableName, crimeArticleIdList);

            log.info(Thread.currentThread().getName() + " Classifier UI Handler -> size of article list= " + articles.size());
            log.info(Thread.currentThread().getName() + " Classifier UI Handler -> Article Titles");
            log.info("  {");
            iter = articles.listIterator();
            while (iter.hasNext()) {
                Article a = (Article) iter.next();
                log.info("    " + a.getTitle());
            }
            log.info("  }");

            //check if interrupted
            checkInterruption();

            // to prepare them as crime articles
            List<CrimeArticle> crimeArticles = ArticleConverter.convertToCrimeArticle(articles, tableName);

            iter = crimeArticles.listIterator();
            log.info(Thread.currentThread().getName() + " Classifier UI Handler -> size of crime article list= " + crimeArticles.size());
            log.info(Thread.currentThread().getName() + " Classifier UI Handler -> Crime Article Titles");
            log.info("  {");
            while (iter.hasNext()) {
                Article a = (Article) iter.next();
                log.info("     " + a.getTitle());
            }
            log.info("  }");

            checkInterruption();
            //transaction
            log.info(Thread.currentThread().getName() + "   Classifier UI Handler -> Transaction");

            for (Article article : testDataArticles) {
                if (crimeArticleIdList.contains(article.getId())) {
                    article.setLabel("crime");
                    for (CrimeArticle crimeArticle : crimeArticles) {
                        if (article.getId() == crimeArticle.getNewspaperId()) {
                            dataHandler.insertCrimeArticleAndUpdatePprArticle(crimeArticle, article);
                        }
                    }

                    checkInterruption();

                } else {
                    article.setLabel("other");
                    dataHandler.updateArticle(article);

                    checkInterruption();
                }


            }

        } else {
            log.info(Thread.currentThread().getName() + "  Classifier UI Handler -> No New Articles to Classify");
        }

        log.info(Thread.currentThread().getName() + " Classifier UI Handler -> End of Classifying Articles");

        int progress = 100;
        notify(new DatabaseConstants().classToTableName.get(tableName), progress);

        //to finish hybernate session and close database. other wise JVM will run continuously
        dataHandler.closeDatabase();
        log.info(Thread.currentThread().getName() + "---------------------------------------------------------------------------------");
    }

    /**
     * start classification process
     */
    public void startClassification(Class tableName, Date endDate) {
        log.info(Thread.currentThread().getName() + "Classifier UI Handler -> Start Classification");
        try {
            buildClassifier(tableName);
            classifyNewsArticles(tableName, endDate);
        } catch (InterruptedException e) {
            System.out.println("#############");
        }

    }

    /**
     * stop classification process
     */
    public void stopClassification() {
        log.info(Thread.currentThread().getName() + "Classifier UI Handler -> Stop Classification");

    }

    /**
     * helper function to handle interruption
     *
     * @return
     */
    private void checkInterruption() throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            log.info(Thread.currentThread().getName() + " Classifier UI Handler -> Interrupted  ");
            dataHandler.closeDatabase();
            throw new InterruptedException();
        }
    }

    /**
     * helper function to notify observers
     *
     * @param name
     * @param progress
     */
    private void notify(String name, int progress) throws InterruptedException {
        checkInterruption();
        setChanged();
        notifyObservers(name + " " + Integer.toString(progress));

    }


}
