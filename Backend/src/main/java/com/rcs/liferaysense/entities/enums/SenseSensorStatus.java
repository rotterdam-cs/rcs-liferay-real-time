package com.rcs.liferaysense.entities.enums;

/**
 * @author Prj.M@x <pablo.rendon@rotterdam-cs.com>
 */
public enum SenseSensorStatus {

    PRIVATE("sensesensorstatus.private"),
    DISABLED("sensesensorstatus.disabled");
    
    private String key;

    private SenseSensorStatus(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
