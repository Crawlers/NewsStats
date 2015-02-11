package com.cse10.classifier;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.filters.AllFilter;

import org.apache.log4j.Logger;

/**
 * Created by Chamath on 12/20/2014
 */

/**
 * Perform grid search to find best values for cost and gamma
 */
public class GridSearch {

    private Logger log;

    public GridSearch() {
        log = Logger.getLogger(this.getClass());
    }

    /**
     * Perform grid search to find the best values for cost and gamma
     *
     * @param classifier
     * @param dataFiltered
     */
    public void gridSearch(Classifier classifier, Instances dataFiltered) {
        weka.classifiers.meta.GridSearch gs = new weka.classifiers.meta.GridSearch();
        gs.setClassifier(classifier);
        gs.setFilter(new AllFilter());

        gs.setXProperty("classifier.cost");
        gs.setXMin(3);
        gs.setXMax(11);
        gs.setXStep(2);
        gs.setXBase(2);
        gs.setXExpression("pow(BASE,I)");

        gs.setYProperty("classifier.gamma");
        gs.setYMin(-10);
        gs.setYMax(-8);
        gs.setYStep(0.5);
        gs.setYBase(2);
        gs.setYExpression("pow(BASE,I)");

        int evaluationCriteriaIndex = 6;
        SelectedTag st;
        st = new SelectedTag(evaluationCriteriaIndex, weka.classifiers.meta.GridSearch.TAGS_EVALUATION);
        gs.setEvaluation(st);
        gs.setGridIsExtendable(true);
        gs.setDebug(true);
        try {
            gs.buildClassifier(dataFiltered);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Criteria: " + gs.getEvaluation().getSelectedTag().getID());
        log.info("Results: " + gs.getValues());
    }

}
