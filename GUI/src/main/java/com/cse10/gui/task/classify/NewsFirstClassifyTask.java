package com.cse10.gui.task.classify;

import com.cse10.classifier.ClassifierUIHandler;
import com.cse10.classifier.NewsFirstClassifierUIHandler;

import java.util.Date;
import java.util.Observable;

/**
 * Created by TharinduWijewardane on 2015-01-10.
 */
public class NewsFirstClassifyTask extends ClassifyTask {

    public NewsFirstClassifyTask() {
    }

    public NewsFirstClassifyTask(Date startDate, Date endDate) {
        super(startDate, endDate);
    }

    @Override
    protected ClassifierUIHandler getClassifierUIHandler() {
        return new NewsFirstClassifierUIHandler();
    }

    @Override
    protected String getPaperName() {
        return "News First";
    }

    @Override
    public void update(Observable o, Object arg) {
        String message = (String) arg;
        String[] m = message.split(" ");
        if (m[0].equals("article_news_first"))
            setProgress(Integer.parseInt(m[1]));
    }
}
