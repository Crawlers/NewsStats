package com.cse10.gui;

import com.cse10.article.*;
import com.cse10.database.DatabaseConstants;
import com.cse10.database.DatabaseHandler;
import com.cse10.entities.CrimeEntityGroup;
import com.cse10.entities.CrimePerson;
import com.cse10.entities.LocationDistrictMapper;
import com.cse10.gui.task.analyze.AnalyzeTask;
import com.cse10.gui.task.analyze.PredictTask;
import com.cse10.gui.task.analyze.UploadDataTask;
import com.cse10.gui.task.classify.CeylonTodayClassifyTask;
import com.cse10.gui.task.classify.DailyMirrorClassifyTask;
import com.cse10.gui.task.classify.NewsFirstClassifyTask;
import com.cse10.gui.task.classify.TheIslandClassifyTask;
import com.cse10.gui.task.crawl.CeylonTodayCrawlTask;
import com.cse10.gui.task.crawl.DailyMirrorCrawlTask;
import com.cse10.gui.task.crawl.NewsFirstCrawlTask;
import com.cse10.gui.task.crawl.TheIslandCrawlTask;
import com.cse10.gui.task.duplicateDetect.DuplicateDetectorTask;
import com.cse10.gui.task.extract.ExtractorTask;
import com.cse10.util.TableCleaner;
import com.toedter.calendar.JDateChooser;
import de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TharinduWijewardane on 2015-01-08.
 */
public class NewsStatsGUI {

    private static JFrame frame;

    private JPanel panelMain;
    private JTabbedPane tabbedPane1;
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
    private JButton extractorButton;
    private JProgressBar extractorProgressBar;
    private ChartPanel chartPanelExtractorPie;
    private JScrollPane scrollPaneDuplicateDetector;
    private JPanel panelDuplicateDetector;
    private JButton duplicateDetectionButton;
    private JProgressBar duplicateDetectorProgressBar;
    private ChartPanel chartPanelDuplicateDetector;
    private JScrollPane scrollPaneAnalyzer;
    private JPanel panelAnalyzer;
    private JButton analyzeButton;
    private JProgressBar analyzerProgressBar;
    private JButton predictButton;
    private JButton uploadDataButton;
    private JProgressBar predictorProgressBar;
    private JProgressBar uploaderProgressBar;
    private ChartPanel chartPanelExtractorLine;
    private JScrollPane scrollPaneUtil;
    private JPanel panelUtil;
    private JLabel databaseLabel;
    private JButton undoClassificationButton;
    private JButton undoEntityExtractionButton;
    private JButton undoDuplicateDetectionButton;
    private ChartPanel chartPanelTables;

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

    private Date ctClassifyStartDate;
    private Date dmClassifyStartDate;
    private Date nfClassifyStartDate;
    private Date tiClassifyStartDate;

    private Date ceylonTodayCrawlStartDate;
    private Date dailyMirrorCrawlStartDate;
    private Date newsFirstCrawlStartDate;
    private Date theIslandCrawlStartDate;

    private CeylonTodayCrawlTask ceylonTodayCrawlTask;
    private DailyMirrorCrawlTask dailyMirrorCrawlTask;
    private NewsFirstCrawlTask newsFirstCrawlTask;
    private TheIslandCrawlTask theIslandCrawlTask;

    private CeylonTodayClassifyTask ceylonTodayClassifyTask;
    private DailyMirrorClassifyTask dailyMirrorClassifyTask;
    private NewsFirstClassifyTask newsFirstClassifyTask;
    private TheIslandClassifyTask theIslandClassifyTask;

    private ExtractorTask extractorTask;
    private DuplicateDetectorTask duplicateDetectorTask;

    private boolean extractButtonState = true;
    private boolean duplicateDetectButtonState = true;
    private boolean analyzeButtonState = true;
    private boolean predictButtonState = true;
    private boolean uploadDataButtonState = true;

    private AnalyzeTask analyzeTask;
    private PredictTask predictTask;
    private UploadDataTask uploadDataTask;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                NewsStatsGUI.init();
            }
        });
    }

    public NewsStatsGUI() {

        initComponentLists(); // initialize list containing UI components
        enableCrawlerUI();
        enableClassifierUI();
        enableExtractorUI();
        enableDuplicateDetectorUI();
        setUpUtilUi();

        startCrawlingButton.addActionListener(new ActionListener() { //when crawler button is clicked
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!(ceylonTodayCrawlerCheckBox.isSelected() || dailyMirrorCrawlerCheckBox.isSelected() || newsFirstCrawlerCheckBox.isSelected() || theIslandCrawlerCheckBox.isSelected())) {
                    return; // no paper is selected
                }

                statusLabel.setText("Crawling...");

                disableCrawlerUI();
                uiComponentsActive.setNewCrawlerUI();

                if (ceylonTodayCrawlerCheckBox.isSelected()) {
                    ceylonTodayCrawlTask = new CeylonTodayCrawlTask(ceylonTodayCrawlStartDate, ceylonTodayCrawlerEndDateChooser.getDate());
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
                    dailyMirrorCrawlTask = new DailyMirrorCrawlTask(dailyMirrorCrawlStartDate, dailyMirrorCrawlerEndDateChooser.getDate());
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
                    newsFirstCrawlTask = new NewsFirstCrawlTask(newsFirstCrawlStartDate, newsFirstCrawlerEndDateChooser.getDate());
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
                    theIslandCrawlTask = new TheIslandCrawlTask(theIslandCrawlStartDate, theIslandCrawlerEndDateChooser.getDate());
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
//                enableCrawlerUI();
                resetCrawlProgressBars();
                refreshUI();
            }
        });
        startClassifyingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!(ceylonTodayClassifierCheckBox.isSelected() || dailyMirrorClassifierCheckBox.isSelected() || newsFirstClassifierCheckBox.isSelected() || theIslandClassifierCheckBox.isSelected())) {
                    return; // no paper is selected
                }

                statusLabel.setText("Classifying...");

                disableClassifierUI();
                uiComponentsActive.setNewClassifierUI();

                if (ceylonTodayClassifierCheckBox.isSelected()) {
                    ceylonTodayClassifyTask = new CeylonTodayClassifyTask(ctClassifyStartDate, ceylonTodayClassifierEndDateChooser.getDate());
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
                    dailyMirrorClassifyTask = new DailyMirrorClassifyTask(dmClassifyStartDate, dailyMirrorClassifierEndDateChooser.getDate());
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
                    newsFirstClassifyTask = new NewsFirstClassifyTask(nfClassifyStartDate, newsFirstClassifierEndDateChooser.getDate());
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
                    theIslandClassifyTask = new TheIslandClassifyTask(tiClassifyStartDate, theIslandClassifierEndDateChooser.getDate());
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
//                enableClassifierUI();
                resetClassifyProgressBars();
                refreshUI();
            }
        });
        extractorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (extractButtonState) {
                    extractButtonState = false;
                    disableExtractorUI();

                    extractorTask = new ExtractorTask();
                    extractorTask.addPropertyChangeListener(new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            if ("progress" == evt.getPropertyName()) {
                                int progress = (Integer) evt.getNewValue();
                                extractorProgressBar.setValue(progress);
                                extractorProgressBar.setStringPainted(true);
                                if (progress == 100) {
                                    extractButtonState = true;
//                                    enableExtractorUI();
                                    drawExtractorChart();
                                    refreshUI();

                                    statusLabel.setText("Ready");
                                    InfoDialog infoDialog = new InfoDialog();
                                    infoDialog.init(frame, "Entity Extraction Completed Successfully!");
                                }
                            }
                        }
                    });
                    extractorTask.execute();

                } else {
                    extractButtonState = true;
//                    enableExtractorUI();
                    drawExtractorChart();
                    refreshUI();

                    if (extractorTask != null) {
                        extractorTask.stopExtract();
                        extractorTask.cancel(true);
                    }

                }
            }
        });
        duplicateDetectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (duplicateDetectButtonState) {
                    duplicateDetectButtonState = false;
                    disableDuplicateDetectorUI();

                    duplicateDetectorTask = new DuplicateDetectorTask();
                    duplicateDetectorTask.addPropertyChangeListener(new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            if ("progress" == evt.getPropertyName()) {
                                int progress = (Integer) evt.getNewValue();
                                duplicateDetectorProgressBar.setValue(progress);
                                duplicateDetectorProgressBar.setStringPainted(true);
                                if (progress == 100) {
                                    duplicateDetectButtonState = true;
//                                    enableDuplicateDetectorUI();
                                    drawDuplicateDetectorChart();
                                    refreshUI();

                                    statusLabel.setText("Ready");
                                    InfoDialog infoDialog = new InfoDialog();
                                    infoDialog.init(frame, "Duplicate Detection Completed Successfully!");
                                }
                            }
                        }
                    });
                    duplicateDetectorTask.execute();

                } else {
                    duplicateDetectButtonState = true;
//                    enableDuplicateDetectorUI();
                    drawDuplicateDetectorChart();
                    refreshUI();

                    if (duplicateDetectorTask != null) {
                        duplicateDetectorTask.stop();
                        duplicateDetectorTask.cancel(true);
                    }

                }
            }
        });
        analyzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (analyzeButtonState) {
                    analyzeButtonState = false;

                    analyzeButton.setText("cancel");

                    analyzeTask = new AnalyzeTask();
                    analyzeTask.addPropertyChangeListener(new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            if ("progress" == evt.getPropertyName()) {
                                int progress = (Integer) evt.getNewValue();
                                analyzerProgressBar.setValue(progress);
                                analyzerProgressBar.setStringPainted(true);
                                if (progress == 100) {
                                    statusLabel.setText("Ready");
                                    InfoDialog infoDialog = new InfoDialog();
                                    infoDialog.init(frame, "Analyze Completed Successfully!");

                                    analyzeButton.setText("Analyze");
                                    analyzerProgressBar.setValue(0);
                                }
                            }
                        }
                    });
                    analyzeTask.execute();

                } else {
                    analyzeButtonState = true;
                    analyzeButton.setText("Analyze");
                    analyzerProgressBar.setValue(0);

                    if (analyzeTask != null) {
                        analyzeTask.stop();
                        analyzeTask.cancel(true);
                    }

                }
            }
        });
        predictButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (predictButtonState) {
                    predictButtonState = false;

                    predictButton.setText("cancel");

                    predictTask = new PredictTask();
                    predictTask.addPropertyChangeListener(new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            if ("progress" == evt.getPropertyName()) {
                                int progress = (Integer) evt.getNewValue();
                                predictorProgressBar.setValue(progress);
                                predictorProgressBar.setStringPainted(true);
                                if (progress == 100) {
                                    statusLabel.setText("Ready");
                                    InfoDialog infoDialog = new InfoDialog();
                                    infoDialog.init(frame, "Prediction Completed Successfully!");

                                    predictButton.setText("Predict");
                                    predictorProgressBar.setValue(0);
                                }
                            }
                        }
                    });
                    predictTask.execute();

                } else {
                    predictButtonState = true;
                    predictButton.setText("Predict");
                    predictorProgressBar.setValue(0);

                    if (predictTask != null) {
                        predictTask.stop();
                        predictTask.cancel(true);
                    }

                }
            }
        });
        uploadDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (uploadDataButtonState) {
                    uploadDataButtonState = false;

                    uploadDataButton.setText("cancel");

                    uploadDataTask = new UploadDataTask();
                    uploadDataTask.addPropertyChangeListener(new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            if ("progress" == evt.getPropertyName()) {
                                int progress = (Integer) evt.getNewValue();
                                uploaderProgressBar.setValue(progress);
                                uploaderProgressBar.setStringPainted(true);
                                if (progress == 100) {
                                    statusLabel.setText("Ready");
                                    InfoDialog infoDialog = new InfoDialog();
                                    infoDialog.init(frame, "Upload Completed Successfully!");

                                    uploadDataButton.setText("Upload Data");
                                    uploaderProgressBar.setValue(0);
                                }
                            }
                        }
                    });
                    uploadDataTask.execute();

                } else {
                    uploadDataButtonState = true;
                    uploadDataButton.setText("Upload Data");
                    uploaderProgressBar.setValue(0);

                    if (uploadDataTask != null) {
                        uploadDataTask.stop();
                        uploadDataTask.cancel(true);
                    }

                }
            }
        });
        undoClassificationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        TableCleaner.undoClassifications(true);

                        refreshUI();
                    }
                });
            }
        });
        undoEntityExtractionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("clicked");
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        System.out.println("invoking");
                        TableCleaner.undoEntityExtraction();

                        refreshUI();
                    }
                });
            }
        });
        undoDuplicateDetectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        TableCleaner.undoDuplicateDetection();

                        refreshUI();
                    }
                });
            }
        });
    }

    public static void init() {

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
        drawCrawlerChart();

        /* classifier chart */
        drawClassifierChart();

        /* extractor chart */
        drawExtractorChart();

        /* duplicate detector chart */
        drawDuplicateDetectorChart();

        /* tables chart in Util tab */
        drawTablesChart();

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

//            enableCrawlerUI();
            resetCrawlProgressBars();
            drawCrawlerChart();
            refreshUI();

            statusLabel.setText("Ready");
            InfoDialog infoDialog = new InfoDialog();
            infoDialog.init(frame, "Crawling Completed Successfully!");
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
        if (chartPanelCrawler == null) {
            chartPanelCrawler = new ChartPanel(chart);
        } else {
            chartPanelCrawler.setChart(chart);
        }
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

        ceylonTodayCrawlerCheckBox.setEnabled(true);
        dailyMirrorCrawlerCheckBox.setEnabled(true);
        newsFirstCrawlerCheckBox.setEnabled(true);
        theIslandCrawlerCheckBox.setEnabled(true);
        startCrawlingButton.setEnabled(true);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            ceylonTodayCrawlStartDate = DatabaseHandler.getLatestDate(CeylonTodayArticle.class);
        } catch (NullPointerException e) {
            try {
                ceylonTodayCrawlStartDate = sdf.parse("2014-01-01"); // if table is empty start crawling from this date
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        try {
            dailyMirrorCrawlStartDate = DatabaseHandler.getLatestDate(DailyMirrorArticle.class);
        } catch (NullPointerException e) {
            try {
                dailyMirrorCrawlStartDate = sdf.parse("2014-01-01"); // if table is empty start crawling from this date
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        try {
            newsFirstCrawlStartDate = DatabaseHandler.getLatestDate(NewsFirstArticle.class);
        } catch (NullPointerException e) {
            try {
                newsFirstCrawlStartDate = sdf.parse("2014-01-01"); // if table is empty start crawling from this date
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        try {
            theIslandCrawlStartDate = DatabaseHandler.getLatestDate(TheIslandArticle.class);
        } catch (NullPointerException e) {
            try {
                theIslandCrawlStartDate = sdf.parse("2014-01-01"); // if table is empty start crawling from this date
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }

        ceylonTodayCrawlerStartDateLabel.setText(sdf.format(ceylonTodayCrawlStartDate));
        dailyMirrorCrawlerStartDateLabel.setText(sdf.format(dailyMirrorCrawlStartDate));
        newsFirstCrawlerStartDateLabel.setText(sdf.format(newsFirstCrawlStartDate));
        theIslandCrawlerStartDateLabel.setText(sdf.format(theIslandCrawlStartDate));

        ceylonTodayCrawlerEndDateChooser.setDate(new Date());
        dailyMirrorCrawlerEndDateChooser.setDate(new Date());
        newsFirstCrawlerEndDateChooser.setDate(new Date());
        theIslandCrawlerEndDateChooser.setDate(new Date());

        stopCrawlingButton.setEnabled(false);
    }

    private void resetCrawlProgressBars() {

        ceylonTodayCrawlProgress = 0;
        dailyMirrorCrawlProgress = 0;
        newsFirstCrawlProgress = 0;
        theIslandCrawlProgress = 0;

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

//            enableClassifierUI();
            resetClassifyProgressBars();
            drawClassifierChart();
            refreshUI();

            statusLabel.setText("Ready");
            InfoDialog infoDialog = new InfoDialog();
            infoDialog.init(frame, "Classifying Completed Successfully!");
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
        if (chartPanelClassifier == null) {
            chartPanelClassifier = new ChartPanel(chart);
        } else {
            chartPanelClassifier.setChart(chart);
        }
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

        ceylonTodayClassifierCheckBox.setEnabled(true);
        dailyMirrorClassifierCheckBox.setEnabled(true);
        newsFirstClassifierCheckBox.setEnabled(true);
        theIslandClassifierCheckBox.setEnabled(true);
        startClassifyingButton.setEnabled(true);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            ctClassifyStartDate = DatabaseHandler.getEarliestDateWithNullLabel(CeylonTodayArticle.class);
            ceylonTodayClassifierStartDateLabel.setText(sdf.format(ctClassifyStartDate));
            ceylonTodayClassifierEndDateChooser.setDate(DatabaseHandler.getLatestDateWithNullLabel(CeylonTodayArticle.class));
        } catch (NullPointerException e) {
            ceylonTodayClassifierCheckBox.setSelected(false);
            ceylonTodayClassifierCheckBox.setEnabled(false);
        }
        try {
            dmClassifyStartDate = DatabaseHandler.getEarliestDateWithNullLabel(DailyMirrorArticle.class);
            dailyMirrorClassifierStartDateLabel.setText(sdf.format(dmClassifyStartDate));
            dailyMirrorClassifierEndDateChooser.setDate(DatabaseHandler.getLatestDateWithNullLabel(DailyMirrorArticle.class));
        } catch (NullPointerException e) {
            dailyMirrorClassifierCheckBox.setSelected(false);
            dailyMirrorClassifierCheckBox.setEnabled(false);
        }
        try {
            nfClassifyStartDate = DatabaseHandler.getEarliestDateWithNullLabel(NewsFirstArticle.class);
            newsFirstClassifierStartDateLabel.setText(sdf.format(nfClassifyStartDate));
            newsFirstClassifierEndDateChooser.setDate(DatabaseHandler.getLatestDateWithNullLabel(NewsFirstArticle.class));
        } catch (NullPointerException e) {
            newsFirstClassifierCheckBox.setSelected(false);
            newsFirstClassifierCheckBox.setEnabled(false);
        }
        try {
            tiClassifyStartDate = DatabaseHandler.getEarliestDateWithNullLabel(TheIslandArticle.class);
            theIslandClassifierStartDateLabel.setText(sdf.format(tiClassifyStartDate));
            theIslandClassifierEndDateChooser.setDate(DatabaseHandler.getLatestDateWithNullLabel(TheIslandArticle.class));
        } catch (NullPointerException e) {
            theIslandClassifierCheckBox.setSelected(false);
            theIslandClassifierCheckBox.setEnabled(false);
        }


        stopClassifyingButton.setEnabled(false);
    }

    private void resetClassifyProgressBars() {

        ceylonTodayClassifyProgress = 0;
        dailyMirrorClassifyProgress = 0;
        newsFirstClassifyProgress = 0;
        theIslandClassifyProgress = 0;

        ceylonTodayClassifyProgressBar.setValue(0);
        dailyMirrorClassifyProgressBar.setValue(0);
        newsFirstClassifyProgressBar.setValue(0);
        theIslandClassifyProgressBar.setValue(0);
        overallClassifyProgressBar.setValue(0);

        DatabaseHandler.closeDatabase(); //to close hibernate and let jvm stop
    }

    /* EXTRACTOR TAB */

    private void disableExtractorUI() {

        extractorButton.setText("Cancel Operation");
    }

    private void enableExtractorUI() {

        extractorButton.setText("Start");
        extractorProgressBar.setValue(0);
    }

    private void drawExtractorChart() {

        int locationCount = DatabaseHandler.getDistinctValueCount(LocationDistrictMapper.class, "location");
        int policeCount = DatabaseHandler.getDistinctValueCount(CrimeEntityGroup.class, "police");
        int courtCount = DatabaseHandler.getDistinctValueCount(CrimeEntityGroup.class, "court");
        int criminalCount = DatabaseHandler.getDistinctValueCount(CrimePerson.class, "name");

        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Locations", locationCount);
        dataset.setValue("Police Stations", policeCount);
        dataset.setValue("Courts", courtCount);
        dataset.setValue("Criminals/Suspects", criminalCount);

        final JFreeChart chart = ChartFactory.createPieChart(
                "Extracted Entities",   // chart title
                dataset,                // data
                true,                   // include legend
                true,                   // tool tips
                false                   // generate URLs
        );
        if (chartPanelExtractorPie == null) {
            chartPanelExtractorPie = new ChartPanel(chart);
        } else {
            chartPanelExtractorPie.setChart(chart);
        }
        chartPanelExtractorPie.setVisible(true);

        /* CHART TWO */

        // row keys...
        final String[] series = new String[11];
        series[0] = "Violent Crimes";
        series[1] = "Theft";
        series[2] = "Abduction";
        series[3] = "Terrorism";
        series[4] = "Sex Crime";
        series[5] = "Clash";
        series[6] = "Illegal Trading";
        series[7] = "Treasure Hunting";
        series[8] = "Drug Offenses";
        series[9] = "Unsuitable Consumer Goods";
        series[10] = "Other";

        // column keys...
        final String category1 = "";

        // create the dataset...
        final DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();

        // data
        int num = 0;
        for (int i = 0; i < series.length; i++) {
            num = DatabaseHandler.getRowCount(CrimeEntityGroup.class, "crimeType", series[i]);
            dataset2.addValue(num, series[i], category1);
        }

        // create the chart...
        final JFreeChart chart2 = ChartFactory.createBarChart(
                "Crime Types",              // chart title
                "Type",                     // domain axis label
                "Frequency",                // range axis label
                dataset2,                    // data
                PlotOrientation.VERTICAL,   // orientation
                true,                       // include legend
                true,                       // tooltips?
                false                       // URLs?
        );

        if (chartPanelExtractorLine == null) {
            chartPanelExtractorLine = new ChartPanel(chart2);
        } else {
            chartPanelExtractorLine.setChart(chart2);
        }
        chartPanelExtractorLine.setVisible(true);
    }

    /* DUPLICATE DETECTOR TAB */

    private void disableDuplicateDetectorUI() {

        duplicateDetectionButton.setText("Cancel Operation");
    }

    private void enableDuplicateDetectorUI() {

        duplicateDetectionButton.setText("Start");
        duplicateDetectorProgressBar.setValue(0);
    }

    private void drawDuplicateDetectorChart() {

        int originalCount = DatabaseHandler.getRowCount(CrimeEntityGroup.class, "label", "unique");
        int duplicateCount = DatabaseHandler.getRowCount(CrimeEntityGroup.class, "label", "duplicate");

        // row keys...
        final String series1 = "Original";
        final String series2 = "Duplicates";

        // column keys...
        final String category1 = "";

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(originalCount, series1, category1);
        dataset.addValue(duplicateCount, series2, category1);

        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
                "Detected Duplicates",         // chart title
                "Type",               // domain axis label
                "Frequency",                  // range axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
        );
        if (chartPanelDuplicateDetector == null) {
            chartPanelDuplicateDetector = new ChartPanel(chart);
        } else {
            chartPanelDuplicateDetector.setChart(chart);
        }
        chartPanelDuplicateDetector.setVisible(true);
    }

    /* UTIL TAB */

    private void setUpUtilUi() {

        String dbUrl = DatabaseConstants.DB_URL;

        databaseLabel.setText(dbUrl);

        //allowing to delete entries only in test and demo databases
        if (dbUrl.contains("test") || dbUrl.contains("demo")) {
            undoClassificationButton.setEnabled(true);
            undoEntityExtractionButton.setEnabled(true);
            undoDuplicateDetectionButton.setEnabled(true);
        } else {
            undoClassificationButton.setEnabled(false);
            undoEntityExtractionButton.setEnabled(false);
            undoDuplicateDetectionButton.setEnabled(false);
        }
    }

    private void drawTablesChart() {

        // row keys...
        String[] series = new String[9];
        series[0] = "Crawled Ceylon Today";
        series[1] = "Crawled Daily Mirror";
        series[2] = "Crawled News First";
        series[3] = "Crawled The Island";
//        series[4] = "Training Articles";
        series[5] = "Classified Crime Articles";
        series[6] = "Crime Persons";
        series[7] = "Crime Entity Groups";
        series[8] = "Crime Locations";

        // column keys...
        final String category1 = "";

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // data
        int[] counts = new int[series.length];
        counts[0] = DatabaseHandler.getRowCount(CeylonTodayArticle.class);
        counts[1] = DatabaseHandler.getRowCount(DailyMirrorArticle.class);
        counts[2] = DatabaseHandler.getRowCount(NewsFirstArticle.class);
        counts[3] = DatabaseHandler.getRowCount(TheIslandArticle.class);
//        counts[4] = DatabaseHandler.getRowCount(TrainingArticle.class);
        counts[5] = DatabaseHandler.getRowCount(CrimeArticle.class);
        counts[6] = DatabaseHandler.getRowCount(CrimePerson.class);
        counts[7] = DatabaseHandler.getRowCount(CrimeEntityGroup.class);
        counts[8] = DatabaseHandler.getRowCount(LocationDistrictMapper.class);

        for (int i = 0; i < series.length; i++) {
            if (i == 4) {
                continue; // skipping training articles for now
            }
            dataset.addValue(counts[i], series[i], category1);
        }

        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
                "Tables",         // chart title
                "Type",               // domain axis label
                "Frequency",                  // range axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
        );
        if (chartPanelTables == null) {
            chartPanelTables = new ChartPanel(chart);
        } else {
            chartPanelTables.setChart(chart);
        }
        chartPanelTables.setVisible(true);
    }

    private void refreshUI() {

        enableCrawlerUI();
        drawCrawlerChart();

        enableClassifierUI();
        drawClassifierChart();

        enableExtractorUI();
        drawExtractorChart();

        enableDuplicateDetectorUI();
        drawDuplicateDetectorChart();

        drawTablesChart();
    }
}
