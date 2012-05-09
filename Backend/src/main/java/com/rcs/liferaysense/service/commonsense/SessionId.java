package com.rcs.liferaysense.service.commonsense;

import java.io.Serializable;

/**
 * Represents the sessionID on the sense API
 * @author juan
 */
public class SessionId implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String session_id;

    public SessionId() {
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
    
}
