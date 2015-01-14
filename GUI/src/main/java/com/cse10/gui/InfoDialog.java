package com.cse10.gui;

import de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InfoDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel labelInfo;
    //    private JButton buttonCancel;

    public InfoDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

//        buttonCancel.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                onCancel();
//            }
//        });
//
//// call onCancel() when cross is clicked
//        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
//        addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                onCancel();
//            }
//        });
//
//// call onCancel() on ESCAPE
//        contentPane.registerKeyboardAction(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                onCancel();
//            }
//        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
// add your code here
        dispose();
    }

//    private void onCancel() {
//// add your code here if necessary
//        dispose();
//    }

    public void init(JFrame frame, String msg) { // frame is used to set relative location

        try {
//            UIManager.setLookAndFeel(new SyntheticaBlueIceLookAndFeel());
            UIManager.setLookAndFeel(new SyntheticaBlackStarLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        InfoDialog dialog = new InfoDialog();
        dialog.setTitle("Info");
        dialog.labelInfo.setText(msg);
        dialog.setLocationRelativeTo(frame);
        dialog.pack();
        dialog.setVisible(true);

    }
}
