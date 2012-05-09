package com.rcs.liferaysense.service.commonsense;

import java.io.Serializable;

/**
 *
 * @author juan
 */
public class Triggers implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Trigger[] triggers;

    public Triggers() {
    }

    public Trigger[] getTriggers() {
        return triggers;
    }

    public void setTriggers(Trigger[] triggers) {
        this.triggers = triggers;
    }
    
}
