package com.cse10.classifier;

import weka.classifiers.meta.AdaBoostM1;


/**
 * Wrapper class for AdoBoost method
 * Created by chamath on 12/20/2014.
 */
public class AdaBoostClassifierHandler extends EnsembleClassifierHandler {


    public AdaBoostClassifierHandler() {
        super();
        this.randomizableIteratedSingleClassifierEnhancer = new AdaBoostM1();
    }

}
