package com.tradedoubler.rgtv.service;

import com.tradedoubler.rgtv.EnumEndPoint;
import com.tradedoubler.rgtv.dto.NumberStatisticsDTO;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicLong;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by wen on 9/17/15.
 */
@Service
public class StatisticService {
    private final Logger LOGGER = getLogger(StatisticService.class);

    @Autowired
    private SimpMessagingTemplate template;

    private final AtomicLong numClicks = new AtomicLong();
    private final AtomicLong numTrackbacks = new AtomicLong();
    private final AtomicLong numCDTDevices = new AtomicLong();

    @PostConstruct
    public void initCounter() {
    }

    @Scheduled(fixedDelay = 1000)
    public void sendDataUpdates() {
        LOGGER.info("running statistic job");
        long clickInOneSecond = numClicks.longValue();
        long trackbackInOneSecond = numTrackbacks.longValue();
        numClicks.getAndSet(0l);
        numTrackbacks.getAndSet(0l);
        NumberStatisticsDTO numberStatisticsDTO = new NumberStatisticsDTO();
        numberStatisticsDTO.setClick(clickInOneSecond);
        numberStatisticsDTO.setTrackback(trackbackInOneSecond);
        numberStatisticsDTO.setCdtDevices(numCDTDevices.get());
        template.convertAndSend(EnumEndPoint.NUMBER_STATISTICS_ENDPOINT.getPath(), numberStatisticsDTO);
    }

    public void addClick() {
        numClicks.incrementAndGet();
    }

    public void addTrackBack() {
        numTrackbacks.incrementAndGet();
    }

    public void setCDTDevicesNum(long num) {
        numCDTDevices.set(num);
    }
}
