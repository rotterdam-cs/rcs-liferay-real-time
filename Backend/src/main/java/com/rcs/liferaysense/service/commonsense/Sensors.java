package com.rcs.liferaysense.service.commonsense;

import java.io.Serializable;

/**
 * Convenience class for Sensor Json Deserialization
 * @author juan
 */
public class Sensors implements Serializable {
    private static final long serialVersionUID = 1L;
    private Sensor[] sensors;

    public Sensors() {
    }

    public Sensor[] getSensors() {
        return sensors;
    }

    public void setSensors(Sensor[] sensors) {
        this.sensors = sensors;
    }
    
}
