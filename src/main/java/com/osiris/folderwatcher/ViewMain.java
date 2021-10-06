package com.osiris.folderwatcher;

import com.osiris.dyml.watcher.DYWatcher;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        Column cSizeMb = new Column("Size (Mb)", null);
        Column cEventType = new Column("Type", null);
        Column cDate = new Column("Date", null);
        Column cFullPath = new Column("Path", null);
        Table eventsTable = new Table(cFileName, cSizeMb, cEventType, cDate, cFullPath);
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
                    JLabel jbl;
                    WatchEvent.Kind<?> eventKind = eventInDir.getWatchEventKind();
                    if (eventKind.equals(StandardWatchEventKinds.ENTRY_CREATE)){
                        jbl = new JLabel(eventInDir.getWatchEventKind().name());
                        jbl.setForeground(Color.GREEN);
                    }
                    else if(eventKind.equals(StandardWatchEventKinds.ENTRY_MODIFY)){
                        jbl = new JLabel(eventInDir.getWatchEventKind().name());
                        jbl.setForeground(Color.YELLOW);
                    }
                    else if (eventKind.equals(StandardWatchEventKinds.ENTRY_DELETE)){
                        jbl = new JLabel(eventInDir.getWatchEventKind().name());
                        jbl.setForeground(Color.RED);
                    }
                    else {
                        jbl = new JLabel(eventInDir.getWatchEventKind().name());
                        jbl.setForeground(Color.MAGENTA);
                    }

                    cEventType.addRow(jbl);
                    cSizeMb.addRow(new JLabel(""+(eventInDir.getFile().length() / 1048576))); // / 1mb in bytes
                    cDate.addRow(new JLabel("" + DateTimeFormatter.ISO_INSTANT.format(Instant.now())));
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
