package com.osiris.folderwatcher;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        javax.swing.SwingUtilities.invokeLater(() -> {
            try{
                JFrame frame = new JFrame("Folder-Watcher");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new ViewMain());
                frame.pack();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


}
