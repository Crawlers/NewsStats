package com.cse10.classifier;

import com.cse10.article.TrainingArticle;
import com.cse10.database.DatabaseHandler;
import libsvm.svm_model;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.supervised.instance.SMOTE;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * load training data using sampling technique
 * Created by Chamath on 12/20/2014.
 */
public class DataHandlerWithSampling extends DataHandler {

    public DataHandlerWithSampling() {
        isFeatureVectorTransformerRequired=false;
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

        //load training data using database handler
        List<TrainingArticle> trainingArticles= DatabaseHandler.fetchTrainingArticles();
        for(TrainingArticle trainingArticle:trainingArticles){
            Instance inst = new Instance(trainingData.numAttributes());
            inst.setValue(content, trainingArticle.getContent());
            inst.setValue(classValue, trainingArticle.getLabel());
            inst.setDataset(trainingData);
            trainingData.add(inst);
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
            saver.setFile(new File("Classifier\\src\\main\\resources\\arffData\\otherClassSupportVectors.arff"));
            saver.writeBatch();
        } catch (IOException e) {
            e.printStackTrace();
        }

        saver.setInstances(crimeClassSupportVectors);
        try {
            saver.setFile(new File("Classifier\\src\\main\\resources\\arffData\\crimeClassSupportVectors.arff"));
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
            saver.setFile(new File("Classifier\\src\\main\\resources\\arffData\\balancedTrainingDataHybrid.arff"));
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
            saver.setFile(new File("Classifier\\src\\main\\resources\\arffData\\balancedTrainingDataHybridRandomized.arff"));
            saver.writeBatch();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataBalanced;
    }
}
