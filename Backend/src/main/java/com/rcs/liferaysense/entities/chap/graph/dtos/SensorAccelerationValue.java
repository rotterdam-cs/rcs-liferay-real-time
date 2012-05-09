package com.rcs.liferaysense.entities.chap.graph.dtos;

import java.io.Serializable;

/**
 * @author Prj.M@x <pablo.rendon@rotterdam-cs.com>
 */
public class SensorAccelerationValue implements Serializable {    
    private static final long serialVersionUID = 1L;
    
    private double x_axis;
    
    private double y_axis;
    
    private double z_axis;

    public double getX_axis() {
        return x_axis;
    }

    public void setX_axis(double x_axis) {
        this.x_axis = x_axis;
    }

    public double getY_axis() {
        return y_axis;
    }

    public void setY_axis(double y_axis) {
        this.y_axis = y_axis;
    }

    public double getZ_axis() {
        return z_axis;
    }

    public void setZ_axis(double z_axis) {
        this.z_axis = z_axis;
    }
     
}