package com.cse10.classifier;

import libsvm.svm_model;
import weka.classifiers.functions.LibSVM;

/**
 * LibSVM extension to access underlying model in order to analyze support vectors
 * Created by chamath on 12/21/2014.
 */
public class LibSVMExtended extends LibSVM{

    /**
     * access underlying model
     *
     * @return
     */
    public svm_model getSVMModel() {
        return (svm_model) m_Model;
    }

}
