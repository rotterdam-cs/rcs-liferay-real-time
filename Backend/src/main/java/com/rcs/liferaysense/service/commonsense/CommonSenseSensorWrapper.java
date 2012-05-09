package com.rcs.liferaysense.service.commonsense;

import java.io.Serializable;

/**
 *
 * @author juan
 */
class CommonSenseSensorWrapper implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Sensor sensor;

    public CommonSenseSensorWrapper() {
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
    
}
