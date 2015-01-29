package com.cse10.gui;

import com.cse10.article.CeylonTodayArticle;
import com.cse10.article.DailyMirrorArticle;
import com.cse10.article.NewsFirstArticle;
import com.cse10.article.TheIslandArticle;
import com.cse10.database.DatabaseHandler;
import com.cse10.gui.task.classify.CeylonTodayClassifyTask;
import com.cse10.gui.task.classify.DailyMirrorClassifyTask;
import com.cse10.gui.task.classify.NewsFirstClassifyTask;
import com.cse10.gui.task.classify.TheIslandClassifyTask;
import com.cse10.gui.task.crawl.CeylonTodayCrawlTask;
import com.cse10.gui.task.crawl.DailyMirrorCrawlTask;
import com.cse10.gui.task.crawl.NewsFirstCrawlTask;
import com.cse10.gui.task.crawl.TheIslandCrawlTask;
import com.cse10.gui.task.extract.ExtractorTask;
import com.toedter.calendar.JDateChooser;
import de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private JPanel panelCrawlControl;
    private JPanel panelCrawlProgress;
    private JCheckBox ceylonTodayCrawlerCheckBox;
    private JCheckBox dailyMirrorCrawlerCheckBox;
    private JCheckBox newsFirstCrawlerCheckBox;
    private JCheckBox theIslandCrawlerCheckBox;
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
    private JPanel panelCrawlResults;
    private ChartPanel chartPanelCrawler;
    private JPanel panelClassifierModel;
    private JCheckBox ceylonTodayClassifierCheckBox;
    private JCheckBox dailyMirrorClassifierCheckBox;
    private JCheckBox newsFirstClassifierCheckBox;
    private JCheckBox theIslandClassifierCheckBox;
    private JButton startClassifyingButton;
    private JButton stopClassifyingButton;
    private JPanel panelClassifierResults;
    private JProgressBar ceylonTodayClassifyProgressBar;
    private JProgressBar dailyMirrorClassifyProgressBar;
    private JProgressBar newsFirstClassifyProgressBar;
    private JProgressBar theIslandClassifyProgressBar;
    private JProgressBar overallClassifyProgressBar;
    private ChartPanel chartPanelClassifier;
    private JLabel ceylonTodayCrawlerStartDateLabel;
    private JLabel dailyMirrorCrawlerStartDateLabel;
    private JLabel newsFirstCrawlerStartDateLabel;
    private JLabel theIslandCrawlerStartDateLabel;
    private JDateChooser ceylonTodayCrawlerEndDateChooser;
    private JDateChooser dailyMirrorCrawlerEndDateChooser;
    private JDateChooser newsFirstCrawlerEndDateChooser;
    private JDateChooser theIslandCrawlerEndDateChooser;
    private JDateChooser ceylonTodayClassifierEndDateChooser;
    private JDateChooser dailyMirrorClassifierEndDateChooser;
    private JDateChooser newsFirstClassifierEndDateChooser;
    private JDateChooser theIslandClassifierEndDateChooser;
    private JLabel ceylonTodayClassifierStartDateLabel;
    private JLabel dailyMirrorClassifierStartDateLabel;
    private JLabel newsFirstClassifierStartDateLabel;
    private JLabel theIslandClassifierStartDateLabel;
    private JScrollPane scrollPaneExtractor;
    private JPanel panelExtractor;
    private JButton startExtractorButton;
    private JProgressBar extractorProgressBar;
    private ChartPanel chartPanelExtractor;

    private UIComponents uiComponentsAll;
    private UIComponents uiComponentsActive;

    private int ceylonTodayCrawlProgress;
    private int dailyMirrorCrawlProgress;
    private int newsFirstCrawlProgress;
    private int theIslandCrawlProgress;

    private int ceylonTodayClassifyProgress;
    private int dailyMirrorClassifyProgress;
    private int newsFirstClassifyProgress;
    private int theIslandClassifyProgress;

    private Date ctStartDate;
    private Date dmStartDate;
    private Date nfStartDate;
    private Date tiStartDate;

    private CeylonTodayCrawlTask ceylonTodayCrawlTask;
    private DailyMirrorCrawlTask dailyMirrorCrawlTask;
    private NewsFirstCrawlTask newsFirstCrawlTask;
    private TheIslandCrawlTask theIslandCrawlTask;

    private CeylonTodayClassifyTask ceylonTodayClassifyTask;
    private DailyMirrorClassifyTask dailyMirrorClassifyTask;
    private NewsFirstClassifyTask newsFirstClassifyTask;
    private TheIslandClassifyTask theIslandClassifyTask;

    private ExtractorTask extractorTask;

    private boolean extract = true;

    public NewsStatsGUI() {

        initComponentLists(); // initialize list containing UI components
        enableCrawlerUI();
        enableClassifierUI();

        startCrawlingButton.addActionListener(new ActionListener() { //when crawler button is clicked
            @Override
            public void actionPerformed(ActionEvent e) {

                statusLabel.setText("Crawling...");

                disableCrawlerUI();
                uiComponentsActive.setNewCrawlerUI();

                if (ceylonTodayCrawlerCheckBox.isSelected()) {
                    ceylonTodayCrawlTask = new CeylonTodayCrawlTask();
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
                    uiComponentsActive.addCheckBoxes(UIComponents.CRAWLER, ceylonTodayCrawlerCheckBox);
                    uiComponentsActive.addProgressBars(UIComponents.CRAWLER, ceylonTodayCrawlProgressBar);
                    ceylonTodayCrawlTask.execute();
                }

                if (dailyMirrorCrawlerCheckBox.isSelected()) {
                    dailyMirrorCrawlTask = new DailyMirrorCrawlTask();
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
                    uiComponentsActive.addCheckBoxes(UIComponents.CRAWLER, dailyMirrorCrawlerCheckBox);
                    uiComponentsActive.addProgressBars(UIComponents.CRAWLER, dailyMirrorCrawlProgressBar);
                    dailyMirrorCrawlTask.execute();
                }

                if (newsFirstCrawlerCheckBox.isSelected()) {
                    newsFirstCrawlTask = new NewsFirstCrawlTask();
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
                    uiComponentsActive.addCheckBoxes(UIComponents.CRAWLER, newsFirstCrawlerCheckBox);
                    uiComponentsActive.addProgressBars(UIComponents.CRAWLER, newsFirstCrawlProgressBar);
                    newsFirstCrawlTask.execute();
                }

                if (theIslandCrawlerCheckBox.isSelected()) {
                    theIslandCrawlTask = new TheIslandCrawlTask();
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
                    uiComponentsActive.addCheckBoxes(UIComponents.CRAWLER, theIslandCrawlerCheckBox);
                    uiComponentsActive.addProgressBars(UIComponents.CRAWLER, theIslandCrawlProgressBar);
                    theIslandCrawlTask.execute();
                }

            }
        });
        stopCrawlingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (ceylonTodayCrawlTask != null) {
                    ceylonTodayCrawlTask.stopCrawling();
                    ceylonTodayCrawlTask.cancel(true);
                }
                if (dailyMirrorCrawlTask != null) {
                    dailyMirrorCrawlTask.stopCrawling();
                    dailyMirrorCrawlTask.cancel(true);
                }
                if (newsFirstCrawlTask != null) {
                    newsFirstCrawlTask.stopCrawling();
                    newsFirstCrawlTask.cancel(true);
                }
                if (theIslandCrawlTask != null) {
                    theIslandCrawlTask.stopCrawling();
                    theIslandCrawlTask.cancel(true);
                }

                statusLabel.setText("Ready");
                enableCrawlerUI();
                resetCrawlProgressBars();
            }
        });
        startClassifyingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText("Classifying...");

                disableClassifierUI();
                uiComponentsActive.setNewClassifierUI();

                if (ceylonTodayClassifierCheckBox.isSelected()) {
                    ceylonTodayClassifyTask = new CeylonTodayClassifyTask(ctStartDate, ceylonTodayClassifierEndDateChooser.getDate());
                    ceylonTodayClassifyTask.addPropertyChangeListener(new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            if ("progress" == evt.getPropertyName()) {
                                int progress = (Integer) evt.getNewValue();
                                ceylonTodayClassifyProgress = progress;
                                ceylonTodayClassifyProgressBar.setValue(progress);
                                ceylonTodayClassifyProgressBar.setStringPainted(true);
                                setOverallClassifyProgress();
                            }
                        }
                    });
                    uiComponentsActive.addCheckBoxes(UIComponents.CLASSIFIER, ceylonTodayCrawlerCheckBox);
                    uiComponentsActive.addProgressBars(UIComponents.CLASSIFIER, ceylonTodayCrawlProgressBar);
                    ceylonTodayClassifyTask.execute();
                }

                if (dailyMirrorClassifierCheckBox.isSelected()) {
                    dailyMirrorClassifyTask = new DailyMirrorClassifyTask(dmStartDate, dailyMirrorClassifierEndDateChooser.getDate());
                    dailyMirrorClassifyTask.addPropertyChangeListener(new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            if ("progress" == evt.getPropertyName()) {
                                int progress = (Integer) evt.getNewValue();
                                dailyMirrorClassifyProgress = progress;
                                dailyMirrorClassifyProgressBar.setValue(progress);
                                dailyMirrorClassifyProgressBar.setStringPainted(true);
                                setOverallClassifyProgress();
                            }
                        }
                    });
                    uiComponentsActive.addCheckBoxes(UIComponents.CLASSIFIER, dailyMirrorCrawlerCheckBox);
                    uiComponentsActive.addProgressBars(UIComponents.CLASSIFIER, dailyMirrorCrawlProgressBar);
                    dailyMirrorClassifyTask.execute();
                }

                if (newsFirstClassifierCheckBox.isSelected()) {
                    newsFirstClassifyTask = new NewsFirstClassifyTask(nfStartDate, newsFirstClassifierEndDateChooser.getDate());
                    newsFirstClassifyTask.addPropertyChangeListener(new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            if ("progress" == evt.getPropertyName()) {
                                int progress = (Integer) evt.getNewValue();
                                newsFirstClassifyProgress = progress;
                                newsFirstClassifyProgressBar.setValue(progress);
                                newsFirstClassifyProgressBar.setStringPainted(true);
                                setOverallClassifyProgress();
                            }
                        }
                    });
                    uiComponentsActive.addCheckBoxes(UIComponents.CLASSIFIER, newsFirstCrawlerCheckBox);
                    uiComponentsActive.addProgressBars(UIComponents.CLASSIFIER, newsFirstCrawlProgressBar);
                    newsFirstClassifyTask.execute();
                }

                if (theIslandClassifierCheckBox.isSelected()) {
                    theIslandClassifyTask = new TheIslandClassifyTask(tiStartDate, theIslandClassifierEndDateChooser.getDate());
                    theIslandClassifyTask.addPropertyChangeListener(new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            if ("progress" == evt.getPropertyName()) {
                                int progress = (Integer) evt.getNewValue();
                                theIslandClassifyProgress = progress;
                                theIslandClassifyProgressBar.setValue(progress);
                                theIslandClassifyProgressBar.setStringPainted(true);
                                setOverallClassifyProgress();
                            }
                        }
                    });
                    uiComponentsActive.addCheckBoxes(UIComponents.CLASSIFIER, theIslandCrawlerCheckBox);
                    uiComponentsActive.addProgressBars(UIComponents.CLASSIFIER, theIslandCrawlProgressBar);
                    theIslandClassifyTask.execute();
                }
            }
        });
        stopClassifyingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (ceylonTodayClassifyTask != null) {
                    ceylonTodayClassifyTask.stopClassification();
                    ceylonTodayClassifyTask.cancel(true);
                }
                if (dailyMirrorClassifyTask != null) {
                    dailyMirrorClassifyTask.stopClassification();
                    dailyMirrorClassifyTask.cancel(true);
                }
                if (newsFirstClassifyTask != null) {
                    newsFirstClassifyTask.stopClassification();
                    newsFirstClassifyTask.cancel(true);
                }
                if (theIslandClassifyTask != null) {
                    theIslandClassifyTask.stopClassification();
                    theIslandClassifyTask.cancel(true);
                }

                statusLabel.setText("Ready");
                enableClassifierUI();
                resetClassifyProgressBars();
            }
        });
        startExtractorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (extract) {
                    extract = false;
                    disableExtractorUI();

                    extractorTask = new ExtractorTask();
                    extractorTask.execute();

                } else {
                    extract = true;
                    enableExtractorUI();

                    if (extractorTask != null) {
                        extractorTask.stopExtract();
                        extractorTask.cancel(true);
                    }

                }
            }
        });
    }

    public void init() {

        try {
            UIManager.setLookAndFeel(new SyntheticaBlackStarLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("NewsStats");
        frame.setContentPane(new NewsStatsGUI().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack(); // packs the window according to components inside. this is not removed because its required to correct layouts

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(0, 0, screenSize.width * 2 / 3, screenSize.height * 2 / 3);

        frame.setVisible(true);
    }

    private void createUIComponents() {
        // place custom component creation code here

        panelClassifierModel = new JPanel();

        /* crawler chart */
        final JFreeChart chartCrawler = ChartFactory.createBarChart(
                "Crawled Articles",         // chart title
                "Type",               // domain axis label
                "Frequency",                  // range axis label
                null,                  // data
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
        );
        chartPanelCrawler = new ChartPanel(chartCrawler);
        chartPanelCrawler.setVisible(true);

        /* classifier chart */
        final JFreeChart chartClassifier = ChartFactory.createBarChart(
                "Classified Articles",         // chart title
                "Type",               // domain axis label
                "Frequency",                  // range axis label
                null,                  // data
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
        );
        chartPanelClassifier = new ChartPanel(chartClassifier);
        chartPanelClassifier.setVisible(true);

        /* extractor chart */
        final JFreeChart chartExtractor = ChartFactory.createBarChart(
                "Extracted Entities",         // chart title
                "Type",               // domain axis label
                "Frequency",                  // range axis label
                null,                  // data
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
        );
        chartPanelExtractor = new ChartPanel(chartExtractor);
        chartPanelExtractor.setVisible(true);

    }

    /**
     * lists of UI components. for extendability
     */
    private void initComponentLists() {

        uiComponentsAll = new UIComponents();
        uiComponentsActive = new UIComponents();

        //uiComponentsAll.addCheckBoxes(UIComponents.CRAWLER, ceylonTodayCrawlerCheckBox, dailyMirrorCrawlerCheckBox, newsFirstCrawlerCheckBox, theIslandCrawlerCheckBox);
        //uiComponentsAll.addCheckBoxes(UIComponents.CLASSIFIER, ceylonTodayClassifierCheckBox, dailyMirrorClassifierCheckBox, newsFirstClassifierCheckBox, theIslandClassifierCheckBox);

    }

    /* CRAWLER TAB */

    private void setOverallCrawlProgress() {

        int numOfSelectedPapers = uiComponentsActive.getCheckBoxes(UIComponents.CRAWLER).size();

        int overallProgress = (ceylonTodayCrawlProgress + dailyMirrorCrawlProgress + newsFirstCrawlProgress + theIslandCrawlProgress) / numOfSelectedPapers;

        overallCrawlProgressBar.setValue(overallProgress);
        overallCrawlProgressBar.setStringPainted(true);

        if (overallProgress == 100) {
            statusLabel.setText("Ready");
            InfoDialog infoDialog = new InfoDialog();
            infoDialog.init(frame, "Crawling Completed Successfully!");

            enableCrawlerUI();
            resetCrawlProgressBars();
            drawCrawlerChart();

        }
    }

    private void drawCrawlerChart() {

        int ctArticles = DatabaseHandler.getRowCount(CeylonTodayArticle.class);
        int dmArticles = DatabaseHandler.getRowCount(DailyMirrorArticle.class);
        int nfArticles = DatabaseHandler.getRowCount(NewsFirstArticle.class);
        int tiArticles = DatabaseHandler.getRowCount(TheIslandArticle.class);

        // row keys...
        final String series1 = "Ceylon Today";
        final String series2 = "Daily Mirror";
        final String series3 = "News First";
        final String series4 = "The Island";

        // column keys...
        final String category1 = "";

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(ctArticles, series1, category1);
        dataset.addValue(dmArticles, series2, category1);
        dataset.addValue(nfArticles, series3, category1);
        dataset.addValue(tiArticles, series4, category1);

        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
                "Crawled Articles",         // chart title
                "Type",               // domain axis label
                "Frequency",                  // range axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
        );
        chartPanelCrawler.setChart(chart);
        chartPanelCrawler.setVisible(true);
    }

    private void disableCrawlerUI() {

        ceylonTodayCrawlerCheckBox.setEnabled(false);
        dailyMirrorCrawlerCheckBox.setEnabled(false);
        newsFirstCrawlerCheckBox.setEnabled(false);
        theIslandCrawlerCheckBox.setEnabled(false);
        startCrawlingButton.setEnabled(false);

        stopCrawlingButton.setEnabled(true);
    }

    private void enableCrawlerUI() {

        ceylonTodayCrawlProgress = 0;
        dailyMirrorCrawlProgress = 0;
        newsFirstCrawlProgress = 0;
        theIslandCrawlProgress = 0;

        ceylonTodayCrawlerCheckBox.setEnabled(true);
        dailyMirrorCrawlerCheckBox.setEnabled(true);
        newsFirstCrawlerCheckBox.setEnabled(true);
        theIslandCrawlerCheckBox.setEnabled(true);
        startCrawlingButton.setEnabled(true);

        ceylonTodayCrawlerStartDateLabel.setText(DatabaseHandler.getLatestDateString(CeylonTodayArticle.class));
        dailyMirrorCrawlerStartDateLabel.setText(DatabaseHandler.getLatestDateString(DailyMirrorArticle.class));
        newsFirstCrawlerStartDateLabel.setText(DatabaseHandler.getLatestDateString(NewsFirstArticle.class));
        theIslandCrawlerStartDateLabel.setText(DatabaseHandler.getLatestDateString(TheIslandArticle.class));

        ceylonTodayCrawlerEndDateChooser.setDate(new Date());
        dailyMirrorCrawlerEndDateChooser.setDate(new Date());
        newsFirstCrawlerEndDateChooser.setDate(new Date());
        theIslandCrawlerEndDateChooser.setDate(new Date());

        stopCrawlingButton.setEnabled(false);
    }

    private void resetCrawlProgressBars() {

        ceylonTodayCrawlProgressBar.setValue(0);
        dailyMirrorCrawlProgressBar.setValue(0);
        newsFirstCrawlProgressBar.setValue(0);
        theIslandCrawlProgressBar.setValue(0);
        overallCrawlProgressBar.setValue(0);

        DatabaseHandler.closeDatabase(); //to close hibernate and let jvm stop
    }

    /* CLASSIFIER TAB */

    private void setOverallClassifyProgress() {

        int numOfSelectedPapers = uiComponentsActive.getCheckBoxes(UIComponents.CLASSIFIER).size();

        int overallProgress = (ceylonTodayClassifyProgress + dailyMirrorClassifyProgress + newsFirstClassifyProgress + theIslandClassifyProgress) / numOfSelectedPapers;

        overallClassifyProgressBar.setValue(overallProgress);
        overallClassifyProgressBar.setStringPainted(true);

        if (overallProgress == 100) {
            statusLabel.setText("Ready");
            InfoDialog infoDialog = new InfoDialog();
            infoDialog.init(frame, "Classifying Completed Successfully!");

            enableClassifierUI();
            resetClassifyProgressBars();
            drawClassifierChart();

        }
    }

    private void drawClassifierChart() {

        int ctArticlesCrime = DatabaseHandler.getRowCount(CeylonTodayArticle.class, "label", "crime");
        int ctArticlesNonCrime = DatabaseHandler.getRowCount(CeylonTodayArticle.class, "label", "other");

        int dmArticlesCrime = DatabaseHandler.getRowCount(DailyMirrorArticle.class, "label", "crime");
        int dmArticlesNonCrime = DatabaseHandler.getRowCount(DailyMirrorArticle.class, "label", "other");

        int nfArticlesCrime = DatabaseHandler.getRowCount(NewsFirstArticle.class, "label", "crime");
        int nfArticlesNonCrime = DatabaseHandler.getRowCount(NewsFirstArticle.class, "label", "other");

        int tiArticlesCrime = DatabaseHandler.getRowCount(TheIslandArticle.class, "label", "crime");
        int tiArticlesNonCrime = DatabaseHandler.getRowCount(TheIslandArticle.class, "label", "other");

        int crime = ctArticlesCrime + dmArticlesCrime + nfArticlesCrime + tiArticlesCrime;
        int nonCrime = ctArticlesNonCrime + dmArticlesNonCrime + nfArticlesNonCrime + tiArticlesNonCrime;

        // row keys...
        final String series1 = "Crime";
        final String series2 = "Non Crime";

        // column keys...
        final String category1 = "";

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(crime, series1, category1);
        dataset.addValue(nonCrime, series2, category1);

        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
                "Classified Articles",         // chart title
                "Type",               // domain axis label
                "Frequency",                  // range axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
        );
        chartPanelClassifier.setChart(chart);
        chartPanelClassifier.setVisible(true);
    }


    private void disableClassifierUI() {

        ceylonTodayClassifierCheckBox.setEnabled(false);
        dailyMirrorClassifierCheckBox.setEnabled(false);
        newsFirstClassifierCheckBox.setEnabled(false);
        theIslandClassifierCheckBox.setEnabled(false);
        startClassifyingButton.setEnabled(false);

        stopClassifyingButton.setEnabled(true);
    }

    private void enableClassifierUI() {

        ceylonTodayClassifyProgress = 0;
        dailyMirrorClassifyProgress = 0;
        newsFirstClassifyProgress = 0;
        theIslandClassifyProgress = 0;

        ceylonTodayClassifierCheckBox.setEnabled(true);
        dailyMirrorClassifierCheckBox.setEnabled(true);
        newsFirstClassifierCheckBox.setEnabled(true);
        theIslandClassifierCheckBox.setEnabled(true);
        startClassifyingButton.setEnabled(true);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            ctStartDate = DatabaseHandler.getEarliestDateWithNullLabel(CeylonTodayArticle.class);
            ceylonTodayClassifierStartDateLabel.setText(sdf.format(ctStartDate));
            ceylonTodayClassifierEndDateChooser.setDate(DatabaseHandler.getLatestDateWithNullLabel(CeylonTodayArticle.class));
        } catch (NullPointerException e) {
            ceylonTodayClassifierCheckBox.setSelected(false);
            ceylonTodayClassifierCheckBox.setEnabled(false);
        }
        try {
            dmStartDate = DatabaseHandler.getEarliestDateWithNullLabel(DailyMirrorArticle.class);
            dailyMirrorClassifierStartDateLabel.setText(sdf.format(dmStartDate));
            dailyMirrorClassifierEndDateChooser.setDate(DatabaseHandler.getLatestDateWithNullLabel(DailyMirrorArticle.class));
        } catch (NullPointerException e) {
            dailyMirrorClassifierCheckBox.setSelected(false);
            dailyMirrorClassifierCheckBox.setEnabled(false);
        }
        try {
            nfStartDate = DatabaseHandler.getEarliestDateWithNullLabel(NewsFirstArticle.class);
            newsFirstClassifierStartDateLabel.setText(sdf.format(nfStartDate));
            newsFirstClassifierEndDateChooser.setDate(DatabaseHandler.getLatestDateWithNullLabel(NewsFirstArticle.class));
        } catch (NullPointerException e) {
            newsFirstClassifierCheckBox.setSelected(false);
            newsFirstClassifierCheckBox.setEnabled(false);
        }
        try {
            tiStartDate = DatabaseHandler.getEarliestDateWithNullLabel(TheIslandArticle.class);
            theIslandClassifierStartDateLabel.setText(sdf.format(tiStartDate));
            theIslandClassifierEndDateChooser.setDate(DatabaseHandler.getLatestDateWithNullLabel(TheIslandArticle.class));
        } catch (NullPointerException e) {
            theIslandClassifierCheckBox.setSelected(false);
            theIslandClassifierCheckBox.setEnabled(false);
        }


        stopClassifyingButton.setEnabled(false);
    }

    private void resetClassifyProgressBars() {

        ceylonTodayClassifyProgressBar.setValue(0);
        dailyMirrorClassifyProgressBar.setValue(0);
        newsFirstClassifyProgressBar.setValue(0);
        theIslandClassifyProgressBar.setValue(0);
        overallClassifyProgressBar.setValue(0);

        DatabaseHandler.closeDatabase(); //to close hibernate and let jvm stop
    }

    /* EXTRACTOR TAB */

    private void disableExtractorUI() {

        startExtractorButton.setText("Cancel Extracting");
    }

    private void enableExtractorUI() {

        startExtractorButton.setText("Start Extracting");
    }
}
