package com.rcs.liferaysense.entities.dtos;

import java.io.Serializable;

/**
 *
 * @author pablo
 */
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String city;
    private String region;
    private String country;
    private String country_code;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
    
    
    
}
