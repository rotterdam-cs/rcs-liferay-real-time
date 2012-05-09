package com.rcs.liferaysense.service.commonsense;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 *
 * @author juan
 */
public class Trigger implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String name;
    private boolean active;
    
    @SerializedName("last_triggered")
    private double lastTriggered;
    
    @SerializedName("times_triggered")
    private int timesTriggered;
    
    @SerializedName("last_sensor_data_value")
    private String lastSensorDataValue;
    
    @SerializedName("last_sensor_data_date")
    private double lastSensorDataDate;

    public Trigger() {
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLastSensorDataDate() {
        return lastSensorDataDate;
    }

    public void setLastSensorDataDate(double lastSensorDataDate) {
        this.lastSensorDataDate = lastSensorDataDate;
    }

    public String getLastSensorDataValue() {
        return lastSensorDataValue;
    }

    public void setLastSensorDataValue(String lastSensorDataValue) {
        this.lastSensorDataValue = lastSensorDataValue;
    }

    public double getLastTriggered() {
        return lastTriggered;
    }

    public void setLastTriggered(double lastTriggered) {
        this.lastTriggered = lastTriggered;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimesTriggered() {
        return timesTriggered;
    }

    public void setTimesTriggered(int timesTriggered) {
        this.timesTriggered = timesTriggered;
    }
}
