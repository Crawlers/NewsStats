package com.cse10.gui;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by TharinduWijewardane on 2015-01-20.
 */
public class UIComponents {

    public static final String CRAWLER = "crawler";
    public static final String CLASSIFIER = "classifier";
    public static final String CHECKBOXES = "checkboxes";
    public static final String PROGRESS_BARS = "progress_bars";

    public HashMap<String, ArrayList> crawlerUI;
    public HashMap<String, ArrayList> classifierUI;
    public ArrayList<JCheckBox> checkBoxesCrawler;
    public ArrayList<JProgressBar> progressBarsCrawler;
    public ArrayList<JCheckBox> checkBoxesClassifier;
    public ArrayList<JProgressBar> progressBarsClassifier;

    public UIComponents() {

        setNewCrawlerUI();
        setNewClassifierUI();
    }

    public void setNewClassifierUI() {
        classifierUI = new HashMap<>();

        checkBoxesClassifier = new ArrayList();
        progressBarsClassifier = new ArrayList();

        classifierUI.put(CHECKBOXES, checkBoxesClassifier);
        classifierUI.put(PROGRESS_BARS, progressBarsClassifier);
    }

    public void setNewCrawlerUI() {
        crawlerUI = new HashMap<>();

        checkBoxesCrawler = new ArrayList();
        progressBarsCrawler = new ArrayList();

        crawlerUI.put(CHECKBOXES, checkBoxesCrawler);
        crawlerUI.put(PROGRESS_BARS, progressBarsCrawler);
    }

    public void addCheckBoxes(String tab, JCheckBox... jCheckBoxes) {
        switch (tab) {
            case CRAWLER:
                for (JCheckBox jCheckBox : jCheckBoxes) {
                    checkBoxesCrawler.add(jCheckBox);
                }
                break;
            case CLASSIFIER:
                for (JCheckBox jCheckBox : jCheckBoxes) {
                    checkBoxesClassifier.add(jCheckBox);
                }
                break;
        }
    }

    public void addProgressBars(String tab, JProgressBar... jProgressBars) {
        switch (tab) {
            case CRAWLER:
                for (JProgressBar jProgressBar : jProgressBars) {
                    progressBarsCrawler.add(jProgressBar);
                }
                break;
            case CLASSIFIER:
                for (JProgressBar jProgressBar : jProgressBars) {
                    progressBarsClassifier.add(jProgressBar);
                }
                break;
        }
    }

    public ArrayList<JCheckBox> getCheckBoxes(String tab) {

        switch (tab) {
            case CRAWLER:
                return checkBoxesCrawler;
            case CLASSIFIER:
                return checkBoxesClassifier;
            default:
                return new ArrayList<>();
        }
    }

    public ArrayList<JProgressBar> getProgressBars(String tab) {

        switch (tab) {
            case CRAWLER:
                return progressBarsCrawler;
            case CLASSIFIER:
                return progressBarsClassifier;
            default:
                return new ArrayList<>();
        }
    }

}
