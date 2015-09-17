package com.tradedoubler.rgtv.controller;

import com.tradedoubler.rgtv.dto.LocationGet;
import com.tradedoubler.rgtv.dto.RgtvMessage;
import com.tradedoubler.rgtv.service.IPResolveService;
import com.tradedoubler.rgtv.service.StatisticService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by wen on 9/17/15.
 */
@RestController
@RequestMapping("/hercules")
public class RgtvController {

    private final Logger LOGGER = getLogger(RgtvController.class);

    @Autowired
    @Qualifier("webSocketFlow.input")
    private MessageChannel messageChannel;

    @Autowired
    private IPResolveService ipResolveService;

    @Autowired
    private StatisticService statisticService;

    @RequestMapping("/click")
    public void receiveClick(@RequestParam("ip") String ip) {
        LocationGet locationGet = ipResolveService.getLocationByIP(ip);
        RgtvMessage rgtvMessage = new RgtvMessage();
        rgtvMessage.setLat(locationGet.getLatitude());
        rgtvMessage.setLng(locationGet.getLatitude());
        rgtvMessage.setType(0);
        rgtvMessage.setEvent(0);
        statisticService.addClick();
        boolean sent = messageChannel.send(MessageBuilder.withPayload(rgtvMessage).build());
        LOGGER.info("Send click to client "+ (sent ? "succeed" : "failed")+" lat="+rgtvMessage.getLat()+" lng="+rgtvMessage.getLng());
    }

    @RequestMapping("/trackback")
    public void receiveTrackback(@RequestParam("ip") String ip) {
        LocationGet locationGet = ipResolveService.getLocationByIP(ip);
        RgtvMessage rgtvMessage = new RgtvMessage();
        rgtvMessage.setLat(locationGet.getLatitude());
        rgtvMessage.setLng(locationGet.getLatitude());
        rgtvMessage.setType(0);
        rgtvMessage.setEvent(1);
        statisticService.addTrackBack();
        boolean sent = messageChannel.send(MessageBuilder.withPayload(rgtvMessage).build());
        LOGGER.info("Send trackback to client " + (sent ? "succeed" : "failed") + " lat=" + rgtvMessage.getLat() + " lng=" + rgtvMessage.getLng());
    }
}
