package com.cse10.gui;

import javax.swing.*;

/**
 * Created by TharinduWijewardane on 2015-01-07.
 */
public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                NewsStatsGUI.init();
            }
        });

    }

}
