package com.rcs.liferaysense.entities.dtos;

import java.io.Serializable;

/**
 *
 * @author pablo
 */
public class ClientLocation implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String latitude;
    private String longitude;
    private String ip;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
   
}
