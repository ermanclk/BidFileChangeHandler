package org.example;

import org.example.service.FileWatchService;
import org.example.service.QueueManager;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchService;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class App {
    public static final String DIRECTORY_TO_WATCH = "temp";

    public static void main(String[] args) throws IOException, InterruptedException {
        startWatchingFileEvents();
    }

    /**
     * Runs FileWatchService services to listen file Create and Modify events on given directory
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private static void startWatchingFileEvents() throws IOException, InterruptedException {
        Path pathToWatch = Paths.get(DIRECTORY_TO_WATCH);
        if (pathToWatch == null) {
            throw new IllegalArgumentException("Directory not found");
        }
        QueueManager queueManager = new QueueManager();

        try (WatchService watcher = pathToWatch.getFileSystem().newWatchService()) {
            pathToWatch.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
            Thread th = new Thread(new FileWatchService(watcher, queueManager));
            th.start();
            th.join();
        }
    }
}
