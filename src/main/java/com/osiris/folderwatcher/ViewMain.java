package com.osiris.folderwatcher;

import com.osiris.dyml.watcher.DYWatcher;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ViewMain extends JPanel {
    private final List<File> watchingFiles = new CopyOnWriteArrayList<>();

    public ViewMain() {
        Utils.setSpacing(this, 10);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel lyControls = new JPanel();
        this.add(lyControls);
        // Layout
        TextField txtField = new TextField();
        lyControls.add(txtField);
        JCheckBox jCheckBox = new JCheckBox("Watch subdirectories");
        lyControls.add(jCheckBox);
        JButton jButton = new JButton("Watch");
        lyControls.add(jButton);

        Column cFileName = new Column("File", null);
        Column cEventType = new Column("Type", null);
        Column cDate = new Column("Date", null);
        Column cFullPath = new Column("Path", null);
        Table eventsTable = new Table(cFileName, cEventType, cDate, cFullPath);
        JScrollPane scrollPane = new JScrollPane(eventsTable);
        this.add(scrollPane);

        // Customize:
        jButton.setAlignmentX(CENTER_ALIGNMENT);
        txtField.setText(System.getProperty("user.dir"));
        /*
        new Thread(() -> {
            try{while (true){
                Thread.sleep(1000);
                cFileName.addRow(new JLabel("FileName"));
                cEventType.addRow(new JLabel("EVENT_KIND"));
                cDate.addRow(new JLabel(""+new Date(System.currentTimeMillis())));
                cFullPath.addRow(new JLabel("C:/Full/Path/FileName"));
            }} catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

         */

        jButton.addActionListener(event -> {
            try {
                File fileToWatch = new File(txtField.getText());
                DYWatcher watcher = DYWatcher.getForFile(fileToWatch, jCheckBox.isValid());

                for (File fileFromList :
                        watchingFiles) {
                    try {
                        DYWatcher.getForFile(fileFromList, jCheckBox.isValid()).close();
                        watchingFiles.remove(fileFromList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                watcher.addListeners(eventInDir -> {
                    String text = new Date(System.currentTimeMillis()) + " | " + eventInDir.getWatchEventContext() + " | " + eventInDir.getWatchEventKind().name();
                    cFileName.addRow(new JLabel("" + eventInDir.getWatchEventContext()));
                    cEventType.addRow(new JLabel(eventInDir.getWatchEventKind().name()));
                    cDate.addRow(new JLabel("" + new Date(System.currentTimeMillis())));
                    cFullPath.addRow(new JLabel("" + eventInDir.getFile()));
                    System.out.println(text);
                    updateUI();
                });

                watchingFiles.add(fileToWatch);
                lyControls.add(new JLabel("[INFO] Now listening to file events for " + fileToWatch.getAbsolutePath()));
                updateUI();
                watcher.printDetails();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        Arrays.toString(e.getStackTrace()),
                        e.getMessage(),
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 300);
    }

}
