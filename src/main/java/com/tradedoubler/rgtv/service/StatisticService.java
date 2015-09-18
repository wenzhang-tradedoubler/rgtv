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


    private final static String CLICK_KEY = "clicks";
    private final static String TRACK_BACK_KEY = "trackbacks";

    private ConcurrentMap<String, AtomicLong> statistics;

    @PostConstruct
    public void initCounter() {
        statistics = new ConcurrentHashMap<>();
        statistics.put(CLICK_KEY, new AtomicLong(0l));
        statistics.put(TRACK_BACK_KEY, new AtomicLong(0l));
    }

    @Scheduled(fixedDelay = 1000)
    public void sendDataUpdates() {
        LOGGER.info("running statistic job");
        RgtvMessage rgtvMessage = new RgtvMessage();
        rgtvMessage.setType(1);
        rgtvMessage.setClick(statistics.get(CLICK_KEY).longValue());
        rgtvMessage.setTrackback(statistics.get(TRACK_BACK_KEY).longValue());
        messageChannel.send(MessageBuilder.withPayload(rgtvMessage).build());
    }

    @Scheduled(fixedDelay = 60000)
    public void resetCounter() {
        LOGGER.info("resetting counters.");
        AtomicLong clicks = statistics.get(CLICK_KEY);
        clicks.getAndSet(0l);
        AtomicLong trackbacks = statistics.get(TRACK_BACK_KEY);
        trackbacks.getAndSet(0l);
    }

    public void addClick() {
        AtomicLong clicks = statistics.get(CLICK_KEY);
        clicks.incrementAndGet();
    }

    public void addTrackBack() {
        AtomicLong trackbacks = statistics.get(TRACK_BACK_KEY);
        trackbacks.incrementAndGet();
    }

}
