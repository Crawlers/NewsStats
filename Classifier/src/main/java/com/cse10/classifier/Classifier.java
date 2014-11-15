package com.cse10.classifier;

import com.cse10.article.*;

/**
 * Created by TharinduWijewardane on 2014-11-14.
 */
public class Classifier {

    public static void main(String[] args) {

        SVMHandler svmHandler = new SVMHandler();
        try {
//            svmHandler.buildSVM();
//            svmHandler.crossValidateClassifier();
//            svmHandler.testClassifier(CeylonTodayArticle.class, "WHERE id >= 1103 AND id < 1120");
            svmHandler.classifyNews(NewsFirstArticle.class, "WHERE id < 100");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
