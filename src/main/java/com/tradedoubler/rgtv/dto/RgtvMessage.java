package com.tradedoubler.rgtv.dto;

/**
 * Created by wen on 9/17/15.
 */
public class RgtvMessage {
    private Integer type;
    private float lat;
    private float lng;
    private Integer event;
    private Long click;
    private Long trackback;


    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getEvent() {
        return event;
    }

    public void setEvent(Integer event) {
        this.event = event;
    }

    public Long getClick() {
        return click;
    }

    public void setClick(Long click) {
        this.click = click;
    }

    public Long getTrackback() {
        return trackback;
    }

    public void setTrackback(Long trackback) {
        this.trackback = trackback;
    }
}