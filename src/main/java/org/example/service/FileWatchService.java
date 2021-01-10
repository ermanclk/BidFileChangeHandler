package org.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class FileWatchService implements Runnable {

    private WatchService watchService;
    private QueueManager queueManager;

    private static Logger log = LoggerFactory.getLogger(FileWatchService.class);

    public FileWatchService(WatchService myWatcher, QueueManager queueManager) {
        this.watchService = myWatcher;
        this.queueManager= queueManager;
    }

    /**
     *  listen events on file changes, and trigger QueueManager to handle change.
     */
    @Override
    public void run() {
        try {

            WatchKey key = watchService.take();
            while (key != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    log.info("Received {} event for file {}",event.kind(),event.context());
                    queueManager.handleFileChange(event.context().toString());
                }
                key.reset();
                key = watchService.take();
            }

        } catch (InterruptedException | IOException e) {
            Thread.currentThread().interrupt();
            log.error("{} thread interrupted..", Thread.currentThread().getName());
        }

        log.info("stopping file watch service..");
    }
}