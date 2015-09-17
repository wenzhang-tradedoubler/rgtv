package com.tradedoubler.rgtv.dto;

/**
 * Created by wen on 9/17/15.
 */
public class RgtvMessage {
    private float lat;
    private float lng;
    private int type;

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
}
