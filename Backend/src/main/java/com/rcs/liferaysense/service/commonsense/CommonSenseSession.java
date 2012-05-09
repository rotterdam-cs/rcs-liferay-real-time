package com.rcs.liferaysense.service.commonsense;

import java.io.Serializable;

/**
 * Represents a session on the common sense, there's no useful data for the
 * user on instances of this but the instance itself, it should be kept and used
 * to ask for data on the common sense service.
 * @author juan
 */
public interface CommonSenseSession extends Serializable {
    
    /**
     * check if the current session is logged in (or has expired).
     * @return 
     */
    boolean isLoggedIn();
}
