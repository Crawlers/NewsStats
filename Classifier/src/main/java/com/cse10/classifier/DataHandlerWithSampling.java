package com.cse10.classifier;

import com.cse10.database.DatabaseConstants;
import libsvm.svm_model;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.DatabaseLoader;
import weka.filters.Filter;
import weka.filters.supervised.instance.SMOTE;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

/**
 * Created by chamath on 12/20/2014.
 */
public class DataHandlerWithSampling extends DataHandler {

    public DataHandlerWithSampling() {
        fileName = "dataWithSampling";
    }

    @Override
    protected void printDescription() {
        System.out.println("This data handler will load training data and use sampling method to generate training data.");
    }

    /**
     * fetch training data no need to use FeatureVectorTransformer again
     *
     * @param featureVectorTransformer
     * @return Instances
     * @throws Exception
     */
    public Instances loadTrainingData(FeatureVectorTransformer featureVectorTransformer) {
        printDescription();
        FastVector attributeList = new FastVector(2);

        Attribute content = new Attribute("text", (FastVector) null);

        FastVector classVal = new FastVector();
        classVal.addElement("crime");
        classVal.addElement("other");
        Attribute classValue = new Attribute("@@class@@", classVal);

        //add class attribute and news text
        attributeList.addElement(content);
        attributeList.addElement(classValue);
        Instances trainingData = new Instances("TrainingNews", attributeList, 0);
        if (trainingData.classIndex() == -1) {
            trainingData.setClassIndex(trainingData.numAttributes() - 1);
        }
        DatabaseLoader databaseLoader = null;
        try {
            databaseLoader = new DatabaseLoader();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (databaseLoader != null) {
            //I have added this one, Because in weka 3.6.11 version we can not set url from databaseutils.props file
            //It generates an error with that
            databaseLoader.setUrl(DatabaseConstants.DB_URL);
            databaseLoader.setUser(DatabaseConstants.DB_USERNAME);
            databaseLoader.setPassword(DatabaseConstants.DB_PASSWORD);
        }

        ArrayList<String> queries = new ArrayList<String>();
        queries.add("SELECT content, label FROM article_ceylon_today_2013 where `label` IS NOT NULL");
        queries.add("SELECT content, label FROM article_daily_mirror_2012 where `label` IS NOT NULL");
        queries.add("SELECT content, label FROM article_daily_mirror_2013 where `label` IS NOT NULL");
        queries.add("SELECT content, label FROM article_the_island_2012 where `label` IS NOT NULL");
        queries.add("SELECT content, label FROM article_the_island_2013 where `label` IS NOT NULL");

        ListIterator queryIterator = queries.listIterator();
        ArrayList<Instances> instancesList = new ArrayList<Instances>();
        while (queryIterator.hasNext()) {
            databaseLoader.setQuery((String) queryIterator.next());
            Instances ins = null;
            try {
                ins = databaseLoader.getDataSet();
            } catch (IOException e) {

                e.printStackTrace();
            }
            instancesList.add(ins);
        }

        ListIterator instancesListIterator = instancesList.listIterator();
        while (instancesListIterator.hasNext()) {
            Instances ins = (Instances) instancesListIterator.next();
            //for each instance
            for (int i = 0; i < ins.numInstances(); i++) {
                String news = ins.instance(i).stringValue(0);
                String label = ins.instance(i).stringValue(1);
                Instance inst = new Instance(trainingData.numAttributes());
                inst.setValue(content, news);
                inst.setValue(classValue, label);
                inst.setDataset(trainingData);
                trainingData.add(inst);
            }
        }
        trainingData.setClassIndex(trainingData.numAttributes() - 1);

        featureVectorTransformer.configure(1, 1, false);
        featureVectorTransformer.setInputFormat(trainingData);
        Instances filteredData = featureVectorTransformer.getTransformedArticles(trainingData, "hybrid1");

        SVMClassifierHandler svm = new SVMClassifierHandler();
        svm.configure(8.0, 0.001953125, "10 1", true);
        svm.buildSVM(filteredData, false);

        svm_model svmModel = svm.getSvm().getSVMModel();
        int n[] = svmModel.sv_indices;


        System.out.println("Number of support vectors=" + n.length);

        double otherCount = 0;
        double crimeCount = 0;
        Instances otherClassSupportVectors = new Instances(filteredData); //other class support vectors
        otherClassSupportVectors.delete();

        Instances crimeClassSupportVectors = new Instances(filteredData); //other class support vectors
        crimeClassSupportVectors.delete();

        for (int k = 0; k < n.length; k++) {
            Instance i = filteredData.instance(n[k] - 1);
            System.out.println(n[k] - 1 + " " + i.classValue());
            if (i.classValue() == 0.0) {
                crimeCount++;
                crimeClassSupportVectors.add(i);
            } else if (i.classValue() == 1.0) {
                otherCount++;
                otherClassSupportVectors.add(i);
            }

        }
        System.out.println("Crime Count " + crimeCount);
        System.out.println("Other Count " + otherCount);

        ArffSaver saver = new ArffSaver();
        saver.setInstances(otherClassSupportVectors);
        try {
            saver.setFile(new File("C:\\Users\\hp\\Desktop\\SVM implementation\\arffData1\\otherClassSupportVectors1.arff"));
            saver.writeBatch();
        } catch (IOException e) {
            e.printStackTrace();
        }

        saver.setInstances(crimeClassSupportVectors);
        try {
            saver.setFile(new File("C:\\Users\\hp\\Desktop\\SVM implementation\\arffData1\\crimeClassSupportVectors2.arff"));
            saver.writeBatch();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int j = 0; j < crimeClassSupportVectors.numInstances(); j++) {
            otherClassSupportVectors.add(crimeClassSupportVectors.instance(j));
        }

        filteredData = otherClassSupportVectors;
        Random r = new Random();
        filteredData.randomize(r);
        saver.setInstances(filteredData);
        try {
            saver.setFile(new File("C:\\Users\\hp\\Desktop\\SVM implementation\\arffData1\\balancedTrainingDataHybrid7.arff"));
            saver.writeBatch();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SMOTE s = new SMOTE();
        try {
            s.setInputFormat(filteredData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Specifies percentage of SMOTE instances to create.
        double percentage = ((otherCount/crimeCount)-1)*100;
        s.setPercentage(Math.round(percentage));
        Instances dataBalanced = null;
        try {
            dataBalanced = Filter.useFilter(filteredData, s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataBalanced.randomize(r);
        saver.setInstances(dataBalanced);
        try {
            saver.setFile(new File("C:\\Users\\hp\\Desktop\\SVM implementation\\arffData1\\balancedTrainingDataHybrid8.arff"));
            saver.writeBatch();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataBalanced;
    }
}
