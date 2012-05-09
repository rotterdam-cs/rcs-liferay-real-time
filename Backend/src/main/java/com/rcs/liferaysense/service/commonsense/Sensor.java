package com.rcs.liferaysense.service.commonsense;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Represents the data of a sensor.
 * @author juan
 */
public class Sensor implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String name;
    
    @SerializedName("display_name")
    private String displayName;
    
    private String averageId;
    
    private CommonSenseUserData owner;
    
    private boolean multipleValues;
    
    private String device_type;

    public Sensor() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAverageId() {
        return averageId;
    }

    public void setAverageId(String averageId) {
        this.averageId = averageId;
    }

    public CommonSenseUserData getOwner() {
        return owner;
    }

    public void setOwner(CommonSenseUserData owner) {
        this.owner = owner;
    }

    public boolean isMultipleValues() {
        return multipleValues;
    }

    public void setMultipleValues(boolean multipleValues) {
        this.multipleValues = multipleValues;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Sensor other = (Sensor) obj;
        if (this.id != other.id) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.displayName == null) ? (other.displayName != null) : !this.displayName.equals(other.displayName)) {
            return false;
        }

        if (this.owner != other.owner && (this.owner == null || !this.owner.equals(other.owner))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + this.id;
        hash = 73 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 73 * hash + (this.displayName != null ? this.displayName.hashCode() : 0);
        hash = 73 * hash + (this.owner != null ? this.owner.hashCode() : 0);
        return hash;
    }
    


}
