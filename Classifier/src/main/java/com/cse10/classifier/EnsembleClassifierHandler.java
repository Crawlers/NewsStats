package com.cse10.classifier;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.RandomizableIteratedSingleClassifierEnhancer;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

import java.util.Random;

/**
 * super class for ensemble classifiers
 * Created by Chamath on 2/3/2015.
 */

public abstract class EnsembleClassifierHandler extends ClassifierHandler {

    protected RandomizableIteratedSingleClassifierEnhancer randomizableIteratedSingleClassifierEnhancer;

    public EnsembleClassifierHandler() {

    }


    /**
     * @param numOfIterations
     * @param classifier
     */
    public void configure(int numOfIterations, Classifier classifier) {
        randomizableIteratedSingleClassifierEnhancer.setNumIterations(numOfIterations);
        randomizableIteratedSingleClassifierEnhancer.setClassifier(classifier);
    }

    /**
     *
     * @param filteredTrainingData
     * @param numOfFolds
     * @return
     */
    public Evaluation crossValidateClassifier(Instances filteredTrainingData, int numOfFolds) {

        //perform cross validation
        Evaluation evaluation = null;
        try {
            evaluation = new Evaluation(filteredTrainingData);
            evaluation.crossValidateModel(randomizableIteratedSingleClassifierEnhancer, filteredTrainingData, numOfFolds, new Random(1));
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
        return evaluation;
    }

    /**
     * build ensemble classifier with given training data and save it
     *
     * @param filteredTrainingData
     * @param isSaving
     * @return
     */
    public void buildEnsemble(Instances filteredTrainingData, boolean isSaving) {
        try {
            randomizableIteratedSingleClassifierEnhancer.buildClassifier(filteredTrainingData);
            //save classifier
            if (isSaving) {
                SerializationHelper.write("Classifier\\src\\main\\resources\\models\\adaBoost.model", randomizableIteratedSingleClassifierEnhancer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * classify the given news article
     *
     * @param filteredTestInstance
     * @return double
     */
    public double classifyInstance(Instance filteredTestInstance) {
        double result = -1.0;
        try {
            result = randomizableIteratedSingleClassifierEnhancer.classifyInstance(filteredTestInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public RandomizableIteratedSingleClassifierEnhancer getModel(){
        return randomizableIteratedSingleClassifierEnhancer;
    }

}
