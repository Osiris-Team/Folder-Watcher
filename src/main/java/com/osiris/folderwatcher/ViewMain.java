package com.osiris.folderwatcher;

import com.osiris.dyml.watcher.DYRegisteredFile;
import com.osiris.dyml.watcher.DYWatcher;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ViewMain extends JPanel {
    private List<File> watchingFiles = new CopyOnWriteArrayList<>();

    public ViewMain() {
        Utils.setSpacing(this, 10);

        // Layout
        TextField txtField = new TextField();
        add(txtField);
        JCheckBox jCheckBox = new JCheckBox("Watch subdirectories");
        add(jCheckBox);
        JButton jButton = new JButton("Watch");
        add(jButton);
        JLabel lEvents = new JLabel("Events:");
        add(lEvents);

        // Customize:
        jButton.setAlignmentX(CENTER_ALIGNMENT);
        lEvents.setAlignmentX(CENTER_ALIGNMENT);
        txtField.setText(System.getProperty("user.dir"));
        jButton.addActionListener(event ->{
            try {
                File fileToWatch = new File(txtField.getText());
                DYWatcher watcher = DYWatcher.getForFile(fileToWatch, jCheckBox.isValid());

                for (File fileFromList :
                        watchingFiles) {
                    try{
                        DYWatcher.getForFile(fileFromList, jCheckBox.isValid()).close();
                        watchingFiles.remove(fileFromList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                watcher.addListeners(eventInDir -> {
                    String text = new Date(System.currentTimeMillis())+" | "+eventInDir.getWatchEventContext()+" | "+eventInDir.getWatchEventKind().name();
                    System.out.println(text);
                    add(new JLabel(text));
                    updateUI();
                });

                watchingFiles.add(fileToWatch);
                add(new JLabel("[INFO] Now listening to file events for "+fileToWatch.getAbsolutePath()));
                lEvents.setText("Events:");
                updateUI();
                watcher.printDetails();
            } catch (IOException e) {
                lEvents.setText(e.getMessage());
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 300);
    }

}
