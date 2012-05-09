package com.rcs.liferaysense.service.commonsense;

import java.io.Serializable;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Holds the data for a common sense user.
 * @author juan
 */
public class CommonSenseSensorData implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    @NotBlank
    private String name;
    @NotBlank
    private String display_name;
    private String data_type;
    private String device_type;
    private String type;
    private String data_structure;

    public CommonSenseSensorData() {
    }

    public CommonSenseSensorData(String name, String display_name, String data_type, String device_type, String type, String data_structure, int id) {
        this.name = name;
        this.display_name = display_name;
        this.data_type = data_type;
        this.device_type = device_type;
        this.id = id;
        this.type = type;
        this.data_structure = data_structure;
    }

    public String getData_structure() {
        return data_structure;
    }

    public void setData_structure(String data_structure) {
        this.data_structure = data_structure;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}