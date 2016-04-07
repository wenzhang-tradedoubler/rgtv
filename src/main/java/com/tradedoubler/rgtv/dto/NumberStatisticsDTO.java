package com.tradedoubler.rgtv.dto;

/**
 * Created by wen on 9/18/15.
 */
public class NumberStatisticsDTO {

    private Long click;
    private Long trackback;
    private Long cdtDevices;

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

    public void setCdtDevices(Long cdtDevices){
        this.cdtDevices = cdtDevices;
    }

    public Long getCdtDevices() {
        return cdtDevices;
    }
}
