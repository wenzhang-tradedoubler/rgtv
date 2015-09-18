package com.tradedoubler.rgtv;

/**
 * Created by wen on 9/18/15.
 */
public enum EnumEndPoint {
    GEO_LOCATION_ENDPOINT("/topic/geolocation"),
    NUMBER_STATISTICS_ENDPOINT("/topic/numberstatistics");

    private String path;
    EnumEndPoint(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
