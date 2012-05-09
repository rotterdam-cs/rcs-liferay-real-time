package com.rcs.liferaysense.service.commonsense;

import java.io.Serializable;
import org.apache.log4j.Logger;

/**
 * Holds the logic for handling the outages of a service access token
 * for the common sense service.
 * @author Juan
 */
class ServiceAccessToken implements CommonSenseSession, Serializable {

    private static final long serialVersionUID = 1L;
    private String accessToken;
    private long lastAccessTimestamp;
    private final long expiration = 3600000; //we assume its one hour
    private CommonSenseServiceImpl service;
    private final String username;
    private final String password;
    private static final Logger log = Logger.getLogger(ServiceAccessToken.class);

    /**
     * Build a new instance of the service access token, this requires
     * to have access to the common sense service to doLogin on-demand.
     * @param service 
     * @param username used to login when the session expires.
     * @param password used to login when the session expires
     */
    ServiceAccessToken(CommonSenseServiceImpl service, String username, String password) {
        lastAccessTimestamp = 0;
        accessToken = null;
        this.service = service;
        this.username = username;
        this.password = password;
    }

    /**
     * Build a new instance of the service access token, this requires
     * to have access to the common sense service to doLogin on-demand.
     * @param service 
     * @param username used to login when the session expires.
     * @param password used to login when the session expires.
     * @param existingToken this is an existing session id.
     */
    ServiceAccessToken(String existingToken, CommonSenseServiceImpl service, String username, String password) {
        this(service, username, password);
        lastAccessTimestamp = System.currentTimeMillis();
        accessToken = existingToken;
    }

    /**
     * Check if the current access token can be used to query the service 
     * or not.
     * @return 
     */
    @Override
    public synchronized boolean isLoggedIn() {
        long currentTimestamp = System.currentTimeMillis();
        
        boolean notExpired = currentTimestamp < lastAccessTimestamp + expiration;
        boolean hasToken = accessToken != null;
        
        return notExpired && hasToken;
    }

    /**
     * Get the access token, try to doLogin if the token has expired. This 
     * method will throw a runtime exception if the service is down.
     * @return 
     */
    synchronized String getAccessToken() {
        if (!isLoggedIn()) {
            accessToken = service.doLogin(username, password);
        }
        lastAccessTimestamp = System.currentTimeMillis();
        return accessToken;
    }
}
