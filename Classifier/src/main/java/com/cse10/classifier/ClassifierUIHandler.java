package com.cse10.classifier;

import java.util.Date;

/**
 * super class for all the ui handlers
 * Created by Chamath on 1/27/2015.
 */
public abstract class ClassifierUIHandler extends Thread {

    protected ClassifierConfigurator classifierConfigurator;
    protected Date endDate;

    public ClassifierUIHandler() {
        classifierConfigurator = ClassifierConfigurator.getInstance();
    }

    public ClassifierConfigurator getClassifierConfigurator() {
        return classifierConfigurator;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    //for functional testing
    public Date getEndDate() {
        return endDate;
    }

    @Override
    public void run() {

    }

}
