package com.cse10.classifier;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.SerializationHelper;
import java.util.Random;

/**
 * Wrapper class for LibSVM
 * Created by chamath on 12/20/2014
 */

public class SVMClassifierHandler {

    protected LibSVMExtended svm;

    public SVMClassifierHandler() {
        svm = new LibSVMExtended();
        int kernelTypeIndex = 2;
        SelectedTag st;
        st = new SelectedTag(kernelTypeIndex, LibSVM.TAGS_KERNELTYPE);
        svm.setKernelType(st);
    }

    /**
     * configure svm
     *
     * @param cost
     * @param gamma
     * @param weights
     * @param isNormalizeData
     */
    public void configure(double cost, double gamma, String weights, boolean isNormalizeData) {
        svm.setCost(cost);
        svm.setGamma(gamma);
        svm.setWeights(weights);
        svm.setNormalize(isNormalizeData);
    }

    /**
     * build classifier with given training data and save it
     *
     * @param filteredTrainingData
     * @param isSaving
     * @return
     */
    public void buildSVM(Instances filteredTrainingData, boolean isSaving) {
        try {
            svm.buildClassifier(filteredTrainingData);
            //save classifier
            if (isSaving) {
                SerializationHelper.write("Classifier\\src\\main\\resources\\models\\svm.model", svm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @return
     */
    public LibSVMExtended getSvm() {
        return svm;
    }

    /**
     * @param filteredTrainingData
     * @param numOfFolds
     */
    public void crossValidateClassifier(Instances filteredTrainingData, int numOfFolds) {

        //perform cross validation
        Evaluation evaluation = null;
        try {
            evaluation = new Evaluation(filteredTrainingData);
            evaluation.crossValidateModel(svm, filteredTrainingData, numOfFolds, new Random(1));
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
     *
     * @param filteredTestInstance
     * @return
     */
    public double classifyInstance(Instance filteredTestInstance) {
        double result = -1.0;
        try {
            result = svm.classifyInstance(filteredTestInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //setters


    public void setSvm(LibSVMExtended svm) {
        this.svm = svm;
    }
}
