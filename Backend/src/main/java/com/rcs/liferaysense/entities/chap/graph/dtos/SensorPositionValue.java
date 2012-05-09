package com.rcs.liferaysense.entities.chap.graph.dtos;

import java.io.Serializable;

/**
 * @author Prj.M@x <pablo.rendon@rotterdam-cs.com>
 * {"bearing":-1,"longitude":-68.84003619999999,"provider":"network","latitude":-32.8923816,"accuracy":64,"speed":-1,"altitude":-1}
 */
public class SensorPositionValue implements Serializable {    
    private static final long serialVersionUID = 1L;
    
    private String bearing;
    private double longitude;
    private String provider;
    private double latitude;
    private String accuracy;
    private String speed;
    private String altitude;

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getBearing() {
        return bearing;
    }

    public void setBearing(String bearing) {
        this.bearing = bearing;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }
    
    
    
}
