package com.cse10.classifier;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.core.*;
import java.util.Random;

/**
 * Wrapper class for LibSVM
 * Created by Tharindu on 2014-11-11.
 */

public class SVMHandler {

    protected LibSVM svm;

    public SVMHandler(){
        svm = new LibSVM();
        int kernelTypeIndex = 2;
        SelectedTag st;
        st = new SelectedTag(kernelTypeIndex , LibSVM.TAGS_KERNELTYPE);
        svm.setKernelType(st);
    }

    /**
     * configure svm
     * @param cost
     * @param gamma
     * @param weights
     * @param isNormalizeData
     */
    public void configure(double cost,double gamma,String weights,boolean isNormalizeData){
        svm.setCost(cost);
        svm.setGamma(gamma);
        svm.setWeights(weights);
        svm.setNormalize(isNormalizeData);
    }

    /**
     * build classifier with given training data
     * @param trainingDataFiltered
     * @return
     */
    public LibSVM buildSVM(Instances trainingDataFiltered) {
        try {
            svm.buildClassifier(trainingDataFiltered);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return svm;
    }

    /**
     *
     * @return
     */
    public LibSVM getSvm() {
        return svm;
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
            evaluation.crossValidateModel(svm, trainingDataFiltered, numOfFolds, new Random(1));
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
     * Test the classification (just print the results)
     *
     * @param articleClass which type of articles that need to be classified (ex:- CeylonTodayArticle.class)
     * @param constrain
     * @throws Exception

    public void testClassifier(Class articleClass, String constrain) throws Exception {
        DataHandler dataHandler=new DataHandler();
        //get test instances and perform predictions
        Instances testData = dataHandler.loadTestData(articleClass, constrain);
        Instances testDataFiltered = weka.filters.Filter.useFilter(testData, filter);

        for (int i = 0; i < testDataFiltered.numInstances(); i++) {

            System.out.println(testData.instance(i));
            System.out.println(svm.classifyInstance(testDataFiltered.instance(i)));
            System.out.println();

        }
    }
     **/

    /**
     * classify the article
     * @param instance
     * @return
     */
     public double classifyInstance(Instance instance){
         double result=-1.0;
         try {
             result=svm.classifyInstance(instance);
         } catch (Exception e) {
             e.printStackTrace();
         }
         return result;
     }


}
