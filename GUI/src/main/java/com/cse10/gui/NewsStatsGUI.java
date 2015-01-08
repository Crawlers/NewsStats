package com.cse10.gui;

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

    public static void main(String[] args) {

        try {
//            UIManager.setLookAndFeel(new SyntheticaBlueIceLookAndFeel());
            UIManager.setLookAndFeel(new SyntheticaBlackStarLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Form2");
        frame.setContentPane(new NewsStatsGUI().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack(); // packs the window according to components inside. this is not removed because its required to correct layouts

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(0, 0, screenSize.width / 2, screenSize.height / 2);

        frame.setVisible(true);
    }
}
