package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Bid;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.example.App.DIRECTORY_TO_WATCH;

public class QueueManager {

    private Map<String, Queue<Thread>> queueByBidType = new HashMap<>();
    private static final String SEPARATOR = FileSystems.getDefault().getSeparator();
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * parse Created/Modified file content into Bid Objects, wrap objects in  Threads and offer into Queue
     * and in the end log all bid details by running Threads
     *
     * @param fileName
     * @throws IOException
     */
    public void handleFileChange(String fileName) throws IOException {

        List<LinkedHashMap<String, Bid>> bids = parseFileContent(fileName);

        for (LinkedHashMap<String,Bid> bidEntry : bids) {

            Bid bid = bidEntry.get("bid");
            Queue<Thread> queue = queueByBidType.get(bid.getTy());
            if (queue == null) {
                queue = new LinkedList<>();
                queueByBidType.put(bid.getTy(),queue);
            }
            queue.offer(new Thread(new BidLogger(bid)));
        }

        logAllBids(queueByBidType);
    }

    /**
     * logs bids in queues
     * each logged Bid Thread is removed from queue
     * @param queueByBidType
     */
    private static void logAllBids(Map<String, Queue<Thread>> queueByBidType){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        for (Queue<Thread> queue : queueByBidType.values()) {
            Iterator<Thread> it = queue.iterator();
            while (it.hasNext()) {
                executor.submit(it.next());
                it.remove();
            }
        }
    }

    private List<LinkedHashMap<String, Bid>> parseFileContent(String fileName) throws IOException {

        Path path = FileSystems.getDefault().getPath(DIRECTORY_TO_WATCH+ SEPARATOR + fileName);
        String contents = Files.readString(path);

        List<LinkedHashMap<String,Bid>> objectList = objectMapper.readValue(contents, new TypeReference<List<LinkedHashMap<String,Bid>>>(){});
        return objectList;
    }


}
