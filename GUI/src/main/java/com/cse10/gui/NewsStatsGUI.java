package com.cse10.gui;

import com.cse10.gui.task.CeylonTodayCrawlTask;
import com.cse10.gui.task.DailyMirrorCrawlTask;
import com.cse10.gui.task.NewsFirstCrawlTask;
import com.cse10.gui.task.TheIslandCrawlTask;
import com.toedter.calendar.JDateChooser;
import de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by TharinduWijewardane on 2015-01-08.
 */
public class NewsStatsGUI {

    private JFrame frame;

    private JPanel panelMain;
    private JTabbedPane tabbedPane1;
    private JPanel panelWizard;
    private JScrollPane scrollPaneCrawler;
    private JPanel panelCrawler;
    private JPanel panelCrawlPapers;
    private JPanel panelCrawlTimePeriod;
    private JPanel panelCrawlControl;
    private JPanel panelCrawlResults;
    private JCheckBox ceylonTodayCheckBox;
    private JCheckBox dailyMirrorCheckBox;
    private JCheckBox newsFirstCheckBox;
    private JCheckBox theIslandCheckBox;
    private JDateChooser startCrawlDateChooser;
    private JDateChooser endCrawlDateChooser;
    private JButton startCrawlingButton;
    private JProgressBar ceylonTodayProgressBar;
    private JProgressBar dailyMirrorProgressBar;
    private JProgressBar newsFirstProgressBar;
    private JProgressBar theIslandProgressBar;
    private JProgressBar overallProgressBar;
    private JPanel panelStatusBar;
    private JLabel statusLabel;
    private JScrollPane scrollPaneClassifier;
    private JPanel panelClassifier;
    private JButton stopCrawlingButton;
    private JPanel panelClassifierResults;
    private JPanel panelClassifierControl;
    private JPanel panelClassifierPapers;
    private JPanel panelClassifierTimePeriod;

    private int ceylonTodayProgress;
    private int dailyMirrorProgress;
    private int newsFirstProgress;
    private int theIslandProgress;

    public NewsStatsGUI() {

        startCrawlingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                statusLabel.setText("Crawling...");

                disableUI();

                CeylonTodayCrawlTask ceylonTodayCrawlTask = new CeylonTodayCrawlTask();
                ceylonTodayCrawlTask.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if ("progress" == evt.getPropertyName()) {
                            int progress = (Integer) evt.getNewValue();
                            ceylonTodayProgress = progress;
                            ceylonTodayProgressBar.setValue(progress);
                            ceylonTodayProgressBar.setStringPainted(true);
                            setOverallProgress();
                        }
                    }
                });
                ceylonTodayCrawlTask.execute();

                DailyMirrorCrawlTask dailyMirrorCrawlTask = new DailyMirrorCrawlTask();
                dailyMirrorCrawlTask.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if ("progress" == evt.getPropertyName()) {
                            int progress = (Integer) evt.getNewValue();
                            dailyMirrorProgress = progress;
                            dailyMirrorProgressBar.setValue(progress);
                            dailyMirrorProgressBar.setStringPainted(true);
                            setOverallProgress();
                        }
                    }
                });
                dailyMirrorCrawlTask.execute();

                NewsFirstCrawlTask newsFirstCrawlTask = new NewsFirstCrawlTask();
                newsFirstCrawlTask.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if ("progress" == evt.getPropertyName()) {
                            int progress = (Integer) evt.getNewValue();
                            newsFirstProgress = progress;
                            newsFirstProgressBar.setValue(progress);
                            newsFirstProgressBar.setStringPainted(true);
                            setOverallProgress();
                        }
                    }
                });
                newsFirstCrawlTask.execute();

                final TheIslandCrawlTask theIslandCrawlTask = new TheIslandCrawlTask();
                theIslandCrawlTask.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if ("progress" == evt.getPropertyName()) {
                            int progress = (Integer) evt.getNewValue();
                            theIslandProgress = progress;
                            theIslandProgressBar.setValue(progress);
                            theIslandProgressBar.setStringPainted(true);
                            setOverallProgress();
                        }
                    }
                });
                theIslandCrawlTask.execute();

            }
        });
        stopCrawlingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO stop swing worker tasks
                enableUI();
                resetProgressBars();
            }
        });
    }

    public void init() {

        try {
//            UIManager.setLookAndFeel(new SyntheticaBlueIceLookAndFeel());
            UIManager.setLookAndFeel(new SyntheticaBlackStarLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("NewsStats");
        frame.setContentPane(new NewsStatsGUI().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack(); // packs the window according to components inside. this is not removed because its required to correct layouts

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(0, 0, screenSize.width / 2, screenSize.height / 2);

        frame.setVisible(true);

    }

    private void setOverallProgress() {

        int overallProgress = (ceylonTodayProgress + dailyMirrorProgress + newsFirstProgress + theIslandProgress) / 4;

        overallProgressBar.setValue(overallProgress);
        overallProgressBar.setStringPainted(true);

        if (overallProgress == 100) {
            statusLabel.setText("Ready");
            CrawlEndDialog crawDialog = new CrawlEndDialog();
            crawDialog.init(frame);

            enableUI();
            resetProgressBars();
        }
    }

    private void disableUI() {
        ceylonTodayCheckBox.setEnabled(false);
        dailyMirrorCheckBox.setEnabled(false);
        newsFirstCheckBox.setEnabled(false);
        theIslandCheckBox.setEnabled(false);
        startCrawlDateChooser.setEnabled(false);
        endCrawlDateChooser.setEnabled(false);
        startCrawlingButton.setEnabled(false);

        stopCrawlingButton.setEnabled(true);
    }

    private void enableUI() {
        ceylonTodayCheckBox.setEnabled(true);
        dailyMirrorCheckBox.setEnabled(true);
        newsFirstCheckBox.setEnabled(true);
        theIslandCheckBox.setEnabled(true);
        startCrawlDateChooser.setEnabled(true);
        endCrawlDateChooser.setEnabled(true);
        startCrawlingButton.setEnabled(true);

        stopCrawlingButton.setEnabled(false);
    }

    private void resetProgressBars() {
        ceylonTodayProgressBar.setValue(0);
        dailyMirrorProgressBar.setValue(0);
        newsFirstProgressBar.setValue(0);
        theIslandProgressBar.setValue(0);
        overallProgressBar.setValue(0);
    }

}
