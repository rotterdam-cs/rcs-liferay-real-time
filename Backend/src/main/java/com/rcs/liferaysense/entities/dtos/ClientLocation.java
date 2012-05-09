package com.rcs.liferaysense.entities.dtos;

import com.rcs.liferaysense.entities.dtos.Address;
import java.io.Serializable;

/**
 *
 * @author pablo
 */
public class ClientLocation implements Serializable {
    private static final long serialVersionUID = 1L;
    
//    private int id;
    private String latitude;
    private String longitude;
    private String ip;
    private Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

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

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
    
    
    
    
}
