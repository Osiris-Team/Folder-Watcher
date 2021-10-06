package com.osiris.folderwatcher;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Table extends JPanel {
    private final JPanel lyColumns = new JPanel();

    public Table(Column... columns) {
        this(Arrays.asList(columns));
    }

    public Table(List<Column> columns) {
        this.add(lyColumns);

        if (columns != null)
            for (Column col :
                    columns) {
                lyColumns.add(col);
            }
    }

    public void addColumn(Column column) {
        lyColumns.add(column);
        updateUI();
    }

    public void addRow(Column column, Component row) {
        column.addRow(row);
        updateUI();
    }
}
