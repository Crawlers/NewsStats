package com.cse10.gui.task.classify;

import com.cse10.classifier.ClassifierUIHandler;
import com.cse10.classifier.DailyMirrorClassifierUIHandler;

import java.util.Date;
import java.util.Observable;

/**
 * Created by TharinduWijewardane on 2015-01-10.
 */
public class DailyMirrorClassifyTask extends ClassifyTask {

    public DailyMirrorClassifyTask() {
    }

    public DailyMirrorClassifyTask(Date startDate, Date endDate) {
        super(startDate, endDate);
    }

    @Override
    protected ClassifierUIHandler getClassifierUIHandler() {
        return new DailyMirrorClassifierUIHandler();
    }

    @Override
    protected String getPaperName() {
        return "Daily Mirror";
    }

    @Override
    public void update(Observable o, Object arg) {
        String message = (String) arg;
        String[] m = message.split(" ");
        if (m[0].equals("article_daily_mirror"))
            setProgress(Integer.parseInt(m[1]));
    }
}
