package com.cse10.gui;

import com.toedter.calendar.JDateChooser;
import de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by TharinduWijewardane on 2015-01-08.
 */
public class NewsStatsGUI {
    private JPanel panelMain;
    private JTabbedPane tabbedPane1;
    private JPanel panelWizard;
    private JScrollPane scrollPaneCrawl;
    private JPanel panelCrawl;
    private JPanel panelCrawlPapers;
    private JPanel panelCrawlTimePeriod;
    private JPanel panelCrawlControl;
    private JPanel panelCrawlResults;
    private JCheckBox ceylonTodayCheckBox;
    private JCheckBox dailyMirrorCheckBox;
    private JCheckBox newsFirstCheckBox;
    private JCheckBox theIslandCheckBox;
    private JDateChooser JDateChooser1;
    private JDateChooser JDateChooser2;
    private JButton startCrawlingButton;
    private JProgressBar progressBar1;
    private JProgressBar progressBar2;
    private JProgressBar progressBar3;
    private JProgressBar progressBar4;
    private JProgressBar progressBar5;

    public void init() {

        try {
//            UIManager.setLookAndFeel(new SyntheticaBlueIceLookAndFeel());
            UIManager.setLookAndFeel(new SyntheticaBlackStarLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("NewsStats");
        frame.setContentPane(new NewsStatsGUI().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack(); // packs the window according to components inside. this is not removed because its required to correct layouts

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(0, 0, screenSize.width / 2, screenSize.height / 2);

        frame.setVisible(true);

        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                int percent = 50;

                progressBar1.setString("Processing " + percent + "%");
                progressBar1.setValue(percent);
            }
        });

    }
}
