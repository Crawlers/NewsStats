package com.cse10.classifier;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.AdaBoostM1;
import weka.core.Instance;
import weka.core.Instances;

import java.util.Random;

/**
 * Wrapper class for AdoBoost method
 * Created by chamath on 12/20/2014.
 */
public class AdaBoostClassifierHandler {

    private AdaBoostM1 adaBoost;

    public AdaBoostClassifierHandler(AdaBoostM1 adaBoost) {
        this.adaBoost = adaBoost;
    }

    /**
     *
     * @param numOfIterations
     * @param classifier
     */
    public void configure(int numOfIterations,Classifier classifier){
         adaBoost.setNumIterations(numOfIterations);
         adaBoost.setClassifier(classifier);
    }

    /**
     *
     * @param trainingDataFiltered
     * @param numOfFolds
     */
    public void crossValidateClassifier(Instances trainingDataFiltered,int numOfFolds){

        //perform cross validation
        Evaluation evaluation = null;
        try {
            evaluation = new Evaluation(trainingDataFiltered);
            evaluation.crossValidateModel(adaBoost, trainingDataFiltered, numOfFolds, new Random(1));
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
     * classify the article
     * @param instance
     * @return
     */
    public double classifyInstance(Instance instance){
        double result=-1.0;
        try {
            result=adaBoost.classifyInstance(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
