package com.cse10.gui.task.classify;

import com.cse10.classifier.CeylonTodayClassifierUIHandler;
import com.cse10.classifier.ClassifierUIHandler;

import java.util.Date;
import java.util.Observer;

/**
 * Created by TharinduWijewardane on 2015-01-10.
 */
public class CeylonTodayClassifyTask extends ClassifyTask implements Observer {

    public CeylonTodayClassifyTask() {
    }

    public CeylonTodayClassifyTask(Date startDate, Date endDate) {
        super(startDate, endDate);
    }

    @Override
    protected ClassifierUIHandler getClassifierUIHandler() {
        return new CeylonTodayClassifierUIHandler();
    }

    @Override
    protected String getPaperName() {
        return "Ceylon Today";
    }

    @Override
    public void update(java.util.Observable o, Object arg) {
        String message = (String) arg;
        String[] m = message.split(" ");
        if (m[0].equals("article_ceylon_today"))
            setProgress(Integer.parseInt(m[1]));
    }
}
