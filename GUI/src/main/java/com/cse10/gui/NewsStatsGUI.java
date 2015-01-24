package com.cse10.gui;

import com.cse10.gui.task.CeylonTodayCrawlTask;
import com.cse10.gui.task.DailyMirrorCrawlTask;
import com.cse10.gui.task.NewsFirstCrawlTask;
import com.cse10.gui.task.TheIslandCrawlTask;
import com.toedter.calendar.JDateChooser;
import de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

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
    private JPanel panelCrawlProgress;
    private JCheckBox ceylonTodayCrawlerCheckBox;
    private JCheckBox dailyMirrorCrawlerCheckBox;
    private JCheckBox newsFirstCrawlerCheckBox;
    private JCheckBox theIslandCrawlerCheckBox;
    private JDateChooser startCrawlDateChooser;
    private JDateChooser endCrawlDateChooser;
    private JButton startCrawlingButton;
    private JProgressBar ceylonTodayCrawlProgressBar;
    private JProgressBar dailyMirrorCrawlProgressBar;
    private JProgressBar newsFirstCrawlProgressBar;
    private JProgressBar theIslandCrawlProgressBar;
    private JProgressBar overallCrawlProgressBar;
    private JPanel panelStatusBar;
    private JLabel statusLabel;
    private JScrollPane scrollPaneClassifier;
    private JPanel panelClassifier;
    private JButton stopCrawlingButton;
    private JPanel panelClassifierProgress;
    private JPanel panelClassifierControl;
    private JPanel panelClassifierPapers;
    private JPanel panelClassifierTimePeriod;
    private JPanel panelCrawlResults;
    private ChartPanel chartPanelCrawler;
    private JPanel panelClassifierMOdel;
    private JCheckBox ceylonTodayClassifierCheckBox;
    private JCheckBox dailyMirrorClassifierCheckBox;
    private JCheckBox newsFirstClassifierCheckBox;
    private JCheckBox theIslandClassifierCheckBox;
    private JDateChooser startClassifyJDateChooser;
    private JDateChooser endClassifyJDateChooser;
    private JButton button1;
    private JButton startClassifyingButton;
    private JButton stopClassifyingButton;
    private JPanel panelClassifierResults;
    private JProgressBar ceylonTodayClassifyProgressBar;
    private JProgressBar dailyMirrorClassifyProgressBar;
    private JProgressBar newsFirstClassifyProgressBar;
    private JProgressBar theIslandClassifyProgressBar;
    private JProgressBar overallClassifyProgressBar;
    private ChartPanel chartPanelClassifier;

    private int ceylonTodayCrawlProgress;
    private int dailyMirrorCrawlProgress;
    private int newsFirstCrawlProgress;
    private int theIslandCrawlProgress;

    private int ceylonTodayClassifyProgress;
    private int dailyMirrorClassifyProgress;
    private int newsFirstClassifyProgress;
    private int theIslandClassifyProgress;

    public NewsStatsGUI() {

        startCrawlingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                statusLabel.setText("Crawling...");

                disableCrawlerUI();

                CeylonTodayCrawlTask ceylonTodayCrawlTask = new CeylonTodayCrawlTask();
                ceylonTodayCrawlTask.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if ("progress" == evt.getPropertyName()) {
                            int progress = (Integer) evt.getNewValue();
                            ceylonTodayCrawlProgress = progress;
                            ceylonTodayCrawlProgressBar.setValue(progress);
                            ceylonTodayCrawlProgressBar.setStringPainted(true);
                            setOverallCrawlProgress();
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
                            dailyMirrorCrawlProgress = progress;
                            dailyMirrorCrawlProgressBar.setValue(progress);
                            dailyMirrorCrawlProgressBar.setStringPainted(true);
                            setOverallCrawlProgress();
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
                            newsFirstCrawlProgress = progress;
                            newsFirstCrawlProgressBar.setValue(progress);
                            newsFirstCrawlProgressBar.setStringPainted(true);
                            setOverallCrawlProgress();
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
                            theIslandCrawlProgress = progress;
                            theIslandCrawlProgressBar.setValue(progress);
                            theIslandCrawlProgressBar.setStringPainted(true);
                            setOverallCrawlProgress();
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
                enableCrawlerUI();
                resetCrawlProgressBars();
            }
        });
        startClassifyingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        stopClassifyingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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

    private void createUIComponents() {
        // place custom component creation code here

        /* crawler chart */
        DefaultPieDataset pieDatasetCrawler = new DefaultPieDataset();
        pieDatasetCrawler.setValue("Ceylon Today", new Integer(10));
        pieDatasetCrawler.setValue("Daily Mirror", new Integer(20));
        pieDatasetCrawler.setValue("News First", new Integer(30));
        pieDatasetCrawler.setValue("The Island", new Integer(10));
        JFreeChart chartCrawler = ChartFactory.createPieChart3D("Crawled Articles", pieDatasetCrawler, true, true, true);
        chartPanelCrawler = new ChartPanel(chartCrawler);
        chartPanelCrawler.setVisible(false);

        /* classifier chart */
        DefaultPieDataset pieDatasetClassifer = new DefaultPieDataset();
        pieDatasetClassifer.setValue("Ceylon Today", new Integer(10));
        pieDatasetClassifer.setValue("Daily Mirror", new Integer(20));
        pieDatasetClassifer.setValue("News First", new Integer(30));
        pieDatasetClassifer.setValue("The Island", new Integer(10));
        JFreeChart chartClassifier = ChartFactory.createPieChart3D("Classified Articles", pieDatasetClassifer, true, true, true);
        chartPanelClassifier = new ChartPanel(chartClassifier);
        chartPanelClassifier.setVisible(false);
    }

    private void setOverallCrawlProgress() {

        int overallProgress = (ceylonTodayCrawlProgress + dailyMirrorCrawlProgress + newsFirstCrawlProgress + theIslandCrawlProgress) / 4;

        overallCrawlProgressBar.setValue(overallProgress);
        overallCrawlProgressBar.setStringPainted(true);

        if (overallProgress == 100) {
            statusLabel.setText("Ready");
            InfoDialog crawDialog = new InfoDialog();
            crawDialog.init(frame, "Crawling Completed Successfully!");

            enableCrawlerUI();
            resetCrawlProgressBars();
            chartPanelCrawler.setVisible(true);

        }
    }

    private void disableCrawlerUI() {
        ceylonTodayCrawlerCheckBox.setEnabled(false);
        dailyMirrorCrawlerCheckBox.setEnabled(false);
        newsFirstCrawlerCheckBox.setEnabled(false);
        theIslandCrawlerCheckBox.setEnabled(false);
        startCrawlDateChooser.setEnabled(false);
        endCrawlDateChooser.setEnabled(false);
        startCrawlingButton.setEnabled(false);

        stopCrawlingButton.setEnabled(true);
    }

    private void enableCrawlerUI() {
        ceylonTodayCrawlerCheckBox.setEnabled(true);
        dailyMirrorCrawlerCheckBox.setEnabled(true);
        newsFirstCrawlerCheckBox.setEnabled(true);
        theIslandCrawlerCheckBox.setEnabled(true);
        startCrawlDateChooser.setEnabled(true);
        endCrawlDateChooser.setEnabled(true);
        startCrawlingButton.setEnabled(true);

        stopCrawlingButton.setEnabled(false);
    }

    private void resetCrawlProgressBars() {
        ceylonTodayCrawlProgressBar.setValue(0);
        dailyMirrorCrawlProgressBar.setValue(0);
        newsFirstCrawlProgressBar.setValue(0);
        theIslandCrawlProgressBar.setValue(0);
        overallCrawlProgressBar.setValue(0);
    }

}