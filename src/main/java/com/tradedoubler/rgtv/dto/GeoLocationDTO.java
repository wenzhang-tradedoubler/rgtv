package com.tradedoubler.rgtv.dto;

/**
 * Created by wen on 9/18/15.
 */
public class GeoLocationDTO {

    private Float lat;
    private Float lng;
    private Integer event;

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }

    public Integer getEvent() {
        return event;
    }

    public void setEvent(Integer event) {
        this.event = event;
    }
}
