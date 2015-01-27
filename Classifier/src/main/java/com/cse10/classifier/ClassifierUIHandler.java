package com.cse10.classifier;

import java.util.Observer;

/**
 * Created by chamath on 1/27/2015.
 */
public abstract class ClassifierUIHandler extends Thread {

    protected ClassifierConfigurator classifierConfigurator;

    public ClassifierUIHandler(){
        classifierConfigurator=ClassifierConfigurator.getInstance();
    }

    public ClassifierConfigurator getClassifierConfigurator(){
        return classifierConfigurator;
    }

    @Override
    public void run() {

    }

}
