package com.tradedoubler.rgtv.dto;

/**
 * Created by wen on 9/17/15.
 */
public class RgtvMessage {
    private Integer type;
    private Float lat;
    private Float lng;
    private Integer event;
    private Long click;
    private Long trackback;


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

    public Integer getType() {
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
