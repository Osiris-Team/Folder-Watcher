package com.osiris.folderwatcher;

import javax.swing.*;
import java.awt.*;

public class Column extends JPanel {

    public Column(String name, Component... rows) {
        this.add(new JLabel(name));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        if (rows != null)
            for (Component row :
                    rows) {
                add(row);
            }
        updateUI();
    }

    public void addRow(Component row) {
        this.add(row);
        updateUI();
    }
}
