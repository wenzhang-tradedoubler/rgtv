package com.tradedoubler.rgtv.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tradedoubler.rgtv.dto.LocationGet;
import org.apache.log4j.spi.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by wen on 9/17/15.
 */
@Service
public class IPResolveService {

    private final Logger LOGGER = getLogger(IPResolveService.class);


    @Autowired
    private RestTemplate restTemplate;

    private final static String IP_SERVICE_BASE_URL = "http://www.telize.com/geoip/";

    @Cacheable(value="ipLocationCache", key="#ip")
    public LocationGet getLocationByIP(String ip) {
        try {
            String url = IP_SERVICE_BASE_URL.concat(ip);
            String response = restTemplate.getForObject(url, String.class);
            Gson gson = new GsonBuilder().create();
            LocationGet locationGet = gson.fromJson(response, LocationGet.class);
            LOGGER.info("request");
            return locationGet;
        } catch (HttpClientErrorException ex) {

        }
        return null;
    }
}
