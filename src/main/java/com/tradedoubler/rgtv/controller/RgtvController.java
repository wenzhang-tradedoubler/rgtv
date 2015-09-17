package com.tradedoubler.rgtv.controller;

import com.tradedoubler.rgtv.dto.LocationGet;
import com.tradedoubler.rgtv.dto.RgtvMessage;
import com.tradedoubler.rgtv.service.IPResolveService;
import com.tradedoubler.rgtv.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wen on 9/17/15.
 */
@RestController
@RequestMapping("/hercules")
public class RgtvController {

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
        statisticService.addClick();

    }

    @RequestMapping("/trackback")
    public void receiveTrackback(@RequestParam("ip") String ip) {
        LocationGet locationGet = ipResolveService.getLocationByIP(ip);
        RgtvMessage rgtvMessage = new RgtvMessage();
        rgtvMessage.setLat(locationGet.getLatitude());
        rgtvMessage.setLng(locationGet.getLatitude());
        statisticService.addTrackBack();
        messageChannel.send(MessageBuilder.withPayload(rgtvMessage).build());
    }
}
