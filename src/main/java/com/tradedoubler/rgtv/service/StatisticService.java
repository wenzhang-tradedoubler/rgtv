package com.tradedoubler.rgtv.service;

import com.tradedoubler.rgtv.dto.RgtvMessage;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by wen on 9/17/15.
 */
@Service
public class StatisticService {
    private final Logger LOGGER = getLogger(StatisticService.class);

    @Autowired
    @Qualifier("webSocketFlow.input")
    private MessageChannel messageChannel;

    private final AtomicLong numClicks = new AtomicLong();
    private final AtomicLong numTrackbacks = new AtomicLong();

    @PostConstruct
    public void initCounter() {
    }

    @Scheduled(fixedDelay = 1000)
    public void sendDataUpdates() {
        LOGGER.info("running statistic job");
        RgtvMessage rgtvMessage = new RgtvMessage();
        rgtvMessage.setType(1);
        rgtvMessage.setClick(numClicks.longValue());
        rgtvMessage.setTrackback(numTrackbacks.longValue());
        messageChannel.send(MessageBuilder.withPayload(rgtvMessage).build());
    }

    @Scheduled(fixedDelay = 5000)
    public void resetCounter() {
        LOGGER.info("resetting counters.");
        numClicks.getAndSet(0l);
        numTrackbacks.getAndSet(0l);
    }

    public void addClick() {
        numClicks.incrementAndGet();
    }

    public void addTrackBack() {
        numTrackbacks.incrementAndGet();
    }
}
