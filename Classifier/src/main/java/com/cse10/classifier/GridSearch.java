package com.cse10.classifier;

import weka.classifiers.functions.LibSVM;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.Tag;
import weka.filters.AllFilter;

/**
 * Created by TharinduWijewardane on 2014-11-14.
 */
public class GridSearch {

    public static void main(String[] args) {
        SVMHandler svmHandler = new SVMHandler();
        try {
            svmHandler.buildSVM();
            gridSearch(svmHandler.svm, svmHandler.dataFiltered);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void gridSearch(LibSVM svm, Instances dataFiltered) throws Exception {
        weka.classifiers.meta.GridSearch gs = new weka.classifiers.meta.GridSearch();
        gs.setClassifier(svm);
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
        //-y-property classifier.kernel.gamma -y-min -5.0 -y-max 2.0 -y-step 1.0 -y-base 10.0 -y-expression pow(BASE,I) -filter weka.filters.AllFilter -x-property classifier.nu -x-min 0.01 -x-max 1.0 -x-step 10.0 -x-base 10.0 -x-expression I -sample-size 100.0 -traversal COLUMN-WISE -log-file "C:\Program Files\Weka-3-6" -S 1 -W weka.classifiers.functions.LibSVM -- -S 2 -K 2 -D 3 -G 0.0 -R 0.0 -N 0.5 -M 40.0 -C 1.0 -E 0.0010 -P 0.1

        int EVALUATION_CC = 0;
        int EVALUATION_RMSE = 1;
        int EVALUATION_RRSE = 2;
        int EVALUATION_MAE = 3;
        int EVALUATION_RAE = 4;
        int EVALUATION_COMBINED = 5;
        int EVALUATION_ACC = 6;
        int EVALUATION_KAPPA = 7;
        Tag[] TAGS_EVALUATION = {
                new Tag(EVALUATION_CC, "CC", "Correlation coefficient"),
                new Tag(EVALUATION_RMSE, "RMSE", "Root mean squared error"),
                new Tag(EVALUATION_RRSE, "RRSE", "Root relative squared error"),
                new Tag(EVALUATION_MAE, "MAE", "Mean absolute error"),
                new Tag(EVALUATION_RAE, "RAE", "Root absolute error"),
                new Tag(EVALUATION_COMBINED, "COMB", "Combined = (1-abs(CC)) + RRSE + RAE"),
                new Tag(EVALUATION_ACC, "ACC", "Accuracy"),
                new Tag(EVALUATION_KAPPA, "KAP", "Kappa")
        };
        SelectedTag st = new SelectedTag(EVALUATION_ACC, TAGS_EVALUATION);
        System.out.println(st.getTags());
        gs.setEvaluation(st);

        gs.setDebug(true);

        gs.buildClassifier(dataFiltered);
        System.out.println("Criteria " + gs.getEvaluation().getSelectedTag().getID());
        System.out.println("&&&&&&&&&&&&" + gs.getValues());
    }

}
