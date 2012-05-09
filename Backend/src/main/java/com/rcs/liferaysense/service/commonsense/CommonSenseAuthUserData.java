package com.rcs.liferaysense.service.commonsense;

/**
 * Provides authentication information for the common sense API. This class
 * wraps the common sense user data and adds the session id so it can be accessed
 * in a convenient way.
 * @author juan
 */
class CommonSenseAuthUserData extends CommonSenseUserData {
    private static final long serialVersionUID = 1L;
    
    /**
     * The common sense session id.
     */
    private String session_id;
    
    /**
     * No-arg constructor for serialization with gson.
     */
    public CommonSenseAuthUserData() {
    
    }
    
    /**
     * Create an instance of this object out of the given user data and a session id.
     * @param data
     * @param session_id 
     */
    public CommonSenseAuthUserData(CommonSenseUserData data, String session_id) {
        super(data.getEmail(), data.getUsername(), data.getName(), data.getSurname(), data.getMobile(), data.getPassword(), data.getId(), data.getUUID(), data.getOpenid());
        this.session_id = session_id;
    }
    
    /**
     * The session id.
     * @return 
     */
    public String getSession_id() {
        return session_id;
    }
    
    /**
     * The session id.
     * @param session_id 
     */
    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
}
