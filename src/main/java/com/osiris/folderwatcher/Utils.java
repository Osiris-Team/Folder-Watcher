package com.osiris.folderwatcher;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Utils {

    public static void setSpacing(JPanel jPanel, int percent) {
        setSpacing(jPanel, percent, percent, percent, percent);
    }

    /**
     * Spacing in percent.
     *
     * @param top    spacing from top to bottom in percent.
     * @param bottom spacing from bottom to top in percent.
     * @param right  spacing from right to left in percent.
     * @param left   spacing from left to right in percent.
     */
    public static void setSpacing(JPanel panel, int top, int bottom, int right, int left) {
        int width = panel.getPreferredSize().width;
        int height = panel.getPreferredSize().height;
        System.out.println(width + " " + height);
        panel.setBorder(new EmptyBorder((height / 100) * top, (width / 100) * left, (height / 100) * bottom, (width / 100) * right));
    }

}
