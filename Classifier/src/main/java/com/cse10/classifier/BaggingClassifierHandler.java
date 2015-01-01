package com.cse10.classifier;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.Bagging;
import weka.core.Instance;
import weka.core.Instances;

import java.util.Random;

/**
 * wrapper class for bagging
 * Created by chamath on 12/20/2014.
 */
public class BaggingClassifierHandler {
    private Bagging bagging;

    public BaggingClassifierHandler(Bagging bagging) {
        this.bagging = new Bagging();
    }


    /**
     *
     * @param numOfIterations
     * @param classifier
     */
    public void configure(int numOfIterations,Classifier classifier){
        bagging.setNumIterations(numOfIterations);
        bagging.setClassifier(classifier);
    }

    /**
     *
     * @param filteredTrainingData Instances
     * @param numOfFolds int
     */
    public void crossValidateClassifier(Instances filteredTrainingData,int numOfFolds){

        //perform cross validation
        Evaluation evaluation = null;
        try {
            evaluation = new Evaluation(filteredTrainingData);
            evaluation.crossValidateModel(bagging, filteredTrainingData, numOfFolds, new Random(1));
            System.out.println(evaluation.toSummaryString());
            System.out.println(evaluation.weightedAreaUnderROC());
            double[][] confusionMatrix = evaluation.confusionMatrix();
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    System.out.print(confusionMatrix[i][j] + "  ");

                }
                System.out.println();
            }
            System.out.println("accuracy for crime class= " + (confusionMatrix[0][0] / (confusionMatrix[0][1] + confusionMatrix[0][0])) * 100 + "%");
            System.out.println("accuracy for other class= " + (confusionMatrix[1][1] / (confusionMatrix[1][1] + confusionMatrix[1][0])) * 100 + "%");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * classify the given news article
     * @param filteredTestInstance
     * @return double
     */
    public double classifyInstance(Instance filteredTestInstance){
        double result=-1.0;
        try {
            result= bagging.classifyInstance(filteredTestInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
