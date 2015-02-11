package com.cse10.classifier;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.SerializationHelper;

import java.util.Random;

import org.apache.log4j.Logger;

/**
 * Wrapper class for LibSVM
 * Created by Chamath on 12/20/2014
 */

public class SVMClassifierHandler extends ClassifierHandler {

    protected LibSVMExtended svm;
    Logger log;

    public SVMClassifierHandler() {
        log = Logger.getLogger(this.getClass());
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
     * @param isNormalizeData check whether data normalization is required
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
     * @param isSaving             check whether model need to be save into file
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
     * access svm model
     *
     * @return
     */
    public LibSVMExtended getSvm() {
        return svm;
    }

    /**
     * return Evaluation object for testing purposes
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
            evaluation.crossValidateModel(svm, filteredTrainingData, numOfFolds, new Random(1));
            log.info(evaluation.toSummaryString());
            log.info(evaluation.weightedAreaUnderROC() + "\n");
            double[][] confusionMatrix = evaluation.confusionMatrix();
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    log.info(confusionMatrix[i][j] + "  \n");

                }
                log.info("\n");
            }
            log.info("accuracy for crime class= " + (confusionMatrix[0][0] / (confusionMatrix[0][1] + confusionMatrix[0][0])) * 100 + "% \n");
            log.info("accuracy for other class= " + (confusionMatrix[1][1] / (confusionMatrix[1][1] + confusionMatrix[1][0])) * 100 + "% \n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return evaluation;
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

}
