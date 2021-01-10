package org.example.service;

import org.example.model.Bid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BidLogger implements Runnable{

    private Bid bid;
    private static Logger log = LoggerFactory.getLogger(BidLogger.class);

    public BidLogger(Bid bid) {
        this.bid=bid;
    }

    @Override
    public void run() {
        log.info(bid.toString());
    }
}
