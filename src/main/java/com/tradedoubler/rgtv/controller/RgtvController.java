package com.tradedoubler.rgtv.controller;

import com.tradedoubler.cdt.IUserJourneyListener;
import com.tradedoubler.cdt.UserJourneyReader;
import com.tradedoubler.rgtv.EnumEndPoint;
import com.tradedoubler.rgtv.dto.GeoLocationDTO;
import com.tradedoubler.rgtv.dto.LocationGet;
import com.tradedoubler.rgtv.service.IPResolveService;
import com.tradedoubler.rgtv.service.StatisticService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.websocket.OnClose;
import java.util.Random;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by wen on 9/17/15.
 */
@RestController
@RequestMapping("/hercules")
public class RgtvController implements IUserJourneyListener {

    private final Logger LOGGER = getLogger(RgtvController.class);

    @Autowired
    private IPResolveService ipResolveService;

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private SimpMessagingTemplate template;

    private final Random r = new Random();

    @RequestMapping("/click")
    @ResponseStatus(HttpStatus.OK)
    public void receiveClick(@RequestParam("ip") String ip) {
        LocationGet locationGet = ipResolveService.getLocationByIP(ip);
        if (locationGet == null) return;
        GeoLocationDTO geoLocationDTO = new GeoLocationDTO();
        geoLocationDTO.setLat(locationGet.getLatitude() + r.nextFloat());
        geoLocationDTO.setLng(locationGet.getLongitude() + r.nextFloat());
        geoLocationDTO.setEvent(0);
        statisticService.addClick();
        template.convertAndSend(EnumEndPoint.GEO_LOCATION_ENDPOINT.getPath(), geoLocationDTO);
        LOGGER.info("Send click to client succeed" + " lat=" + geoLocationDTO.getLat() + " lng=" + geoLocationDTO.getLng());
    }

    @RequestMapping("/trackback")
    @ResponseStatus(HttpStatus.OK)
    public void receiveTrackback(@RequestParam("ip") String ip) {
        LocationGet locationGet = ipResolveService.getLocationByIP(ip);
        if (locationGet == null) return;
        GeoLocationDTO geoLocationDTO = new GeoLocationDTO();
        geoLocationDTO.setLat(locationGet.getLatitude() + r.nextFloat());
        geoLocationDTO.setLng(locationGet.getLongitude() + r.nextFloat());
        geoLocationDTO.setEvent(1);
        statisticService.addTrackBack();
        template.convertAndSend(EnumEndPoint.GEO_LOCATION_ENDPOINT.getPath(), geoLocationDTO);
        LOGGER.info("Send trackback to client succeed" + " lat=" + geoLocationDTO.getLat() + " lng=" + geoLocationDTO.getLng());
    }

    @PostConstruct
    public void init(){
        UserJourneyReader reader = new UserJourneyReader(this);
        reader.start();
    }

    @Override
    public void onNumDevices(long numDevices) {
        statisticService.setCDTDevicesNum(numDevices);
    }
}
