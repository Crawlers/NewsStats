package com.cse10.gui.task.classify;


import com.cse10.classifier.ClassifierUIHandler;
import com.cse10.classifier.TheIslandClassifierUIHandler;

import java.util.Date;
import java.util.Observable;


/**
 * Created by TharinduWijewardane on 2015-01-10.
 */
public class TheIslandClassifyTask extends ClassifyTask {

    public TheIslandClassifyTask() {
    }

    public TheIslandClassifyTask(Date startDate, Date endDate) {
        super(startDate, endDate);
    }

    @Override
    protected ClassifierUIHandler getClassifierUIHandler() {
        return new TheIslandClassifierUIHandler();
    }

    @Override
    protected String getPaperName() {
        return "The Island";
    }

    @Override
    public void update(Observable o, Object arg) {
        String message = (String) arg;
        String[] m = message.split(" ");
        if (m[0].equals("article_the_island"))
            setProgress(Integer.parseInt(m[1]));
    }
}
