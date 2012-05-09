package com.rcs.liferaysense.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Prj.M@x <pablo.rendon@rotterdam-cs.com>
 */
@Entity
@Table(name = "sense_configuration")
public class SenseConfiguration extends SenseEntity {
    public static final String SENSECONFIGURATIONPROPERTY = "property";
    private static final long serialVersionUID = 1L;
    
    private String property;
    
    private String propertyValue;

    /*
     Getters & Setters
    */
    
    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    } 
    
}