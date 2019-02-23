package br.com.jugpe.ic.thread;

import br.com.jugpe.ic.Application;
import br.com.jugpe.ic.storage.FileStorage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IntegrityThread {

    private Timer timer;
    private TimerTask timerTask;

    private Properties properties;

    private File source;

    private Logger logger = Application.getLogger();
    private FileStorage fileStorage = Application.getFileStorage();

    public IntegrityThread(Properties properties) {
        this.timer = new Timer("IntegrityChecker");
        this.properties = properties;
        this.source = new File(properties.getProperty("source.checking"));
    }

    public void start() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!source.exists()) {
                    logger.log(Level.SEVERE, "This source not exists.");
                }

                if (!source.isDirectory()) {
                    logger.log(Level.SEVERE, "This source is a not directory, finishing the application...");
                }

                File[] files = source.listFiles();
                Base64.Encoder encoder = Base64.getEncoder();
                Base64.Decoder decoder = Base64.getDecoder();

                try {
                    if (files.length >= 1) {
                        if (fileStorage.size() == 0) {
                            Map<String, String> map = new LinkedHashMap<>();

                            for (File file : files) {
                                StringBuilder sb = new StringBuilder();

                                Files.lines(Paths.get(file.toURI())).forEach(sb::append);

                                map.put(file.getName(), encoder.encodeToString(sb.toString().getBytes()));

                                logger.log(Level.INFO, "Saving " + file.getName() + " in file storage.");
                            }

                            fileStorage.add(map);
                        } else {
                            for (File file : files) {
                                StringBuilder sb = new StringBuilder();

                                Files.lines(Paths.get(file.toURI())).forEach(sb::append);

                                if (!fileStorage.get(file.getName()).equals(encoder.encodeToString(sb.toString().getBytes()))) {
                                    Files.write(Paths.get(file.toURI()),
                                            new String(decoder.decode(fileStorage.get(file.getName()))).getBytes());

                                    logger.log(Level.SEVERE, "File " + file.getName() + " has changed, restoring...");
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        timer.scheduleAtFixedRate(timerTask, 0, Long.parseLong(properties.getProperty("time.checking")));
    }

}