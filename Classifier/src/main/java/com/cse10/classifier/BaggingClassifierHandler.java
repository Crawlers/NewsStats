package com.cse10.classifier;


import weka.classifiers.meta.Bagging;

/**
 * wrapper class for bagging
 * Created by Chamath on 12/20/2014.
 */
public class BaggingClassifierHandler extends EnsembleClassifierHandler {


    public BaggingClassifierHandler() {
        super();
        this.randomizableIteratedSingleClassifierEnhancer = new Bagging();
    }

}
