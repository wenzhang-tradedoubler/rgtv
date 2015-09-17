package com.tradedoubler.rgtv.service;

import com.tradedoubler.rgtv.dto.RgtvMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by wen on 9/17/15.
 */
@Service
public class StatisticService {

    @Autowired
    @Qualifier("webSocketFlow.input")
    private MessageChannel messageChannel;


    private final static String CLICK_KEY = "clicks";
    private final static String TRACK_BACK_KEY = "trackbacks";

    private ConcurrentMap<String, Long> statistics;

    public StatisticService() {
        statistics = new ConcurrentHashMap<>();
        statistics.put(CLICK_KEY, 0l);
        statistics.put("trackbacks", 0l);
    }

    @Scheduled(fixedDelay = 1000)
    public void sendDataUpdates() {
        RgtvMessage rgtvMessage = new RgtvMessage();
        rgtvMessage.setType(2);
        rgtvMessage.setClick(statistics.get(CLICK_KEY));
        rgtvMessage.setTrackback(statistics.get(TRACK_BACK_KEY));
        messageChannel.send(MessageBuilder.withPayload(rgtvMessage).build());

    }

    public void addClick() {
        Long clicks = statistics.get(CLICK_KEY);
        statistics.put(CLICK_KEY, clicks + 1);
    }

    public void addTrackBack() {
        Long trackbacks = statistics.get(TRACK_BACK_KEY);
        statistics.put(TRACK_BACK_KEY, trackbacks);
    }

}
