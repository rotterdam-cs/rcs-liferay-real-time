package com.rcs.liferaysense.service.commonsense;

import java.io.Serializable;

/**
 * Holds the array of sensor values read from the CommonSense API service.
 * @author juan
 */
public class SensorValues implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private SensorValue[] data;
    private String json;
    private int total;
    
    public SensorValues() {
    }

    public SensorValue[] getData() {
        return data;
    }

    public void setData(SensorValue[] data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
    
}
