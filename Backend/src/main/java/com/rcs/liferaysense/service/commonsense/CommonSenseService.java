package com.rcs.liferaysense.service.commonsense;

import com.rcs.liferaysense.entities.chap.graph.dtos.LiferaySensorDataDTO;
import com.rcs.liferaysense.entities.dtos.ClientLocation;
import com.rcs.liferaysense.entities.dtos.LocalResponse;
import com.rcs.liferaysense.entities.dtos.PagesDto;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.springframework.scheduling.annotation.Async;

/**
 * Implements common sense API calls.
 * @author juan
 */
public interface CommonSenseService {
       
    /**
     * Start a new common sense session.
     * @param username
     * @param password the password will be hashed using the MD5 algorithm.
     * @return a common sense session or null if the login fails.
     */
    CommonSenseSession login(String username, String password);
    
    
    /**
     * Logout from the common sense session.
     * @param session 
     */
    void logout(CommonSenseSession session);
    
    
    /**
     * Get the data for a given sensor on the given period. This method will
     * result on calling the /sensors/{id}/data API call.
     * The returned result will contain a list with the relevant data and 
     * the order will be the same as the service returns.
     * @param sensorId
     * @param dateFrom
     * @param dateTo
     * @return 
     */
    SensorValues getSensorData(CommonSenseSession session, String sensorId, Date dateFrom, Date dateTo);
    
    /**
     * 
     * @param session
     * @param sensorId
     * @param dateFrom
     * @param dateTo
     * @param pages
     * @return 
     */
    List <LiferaySensorDataDTO> getSensorDataDTO(CommonSenseSession session, String sensorId, Date dateFrom, Date dateTo, List<PagesDto> pages, long groupId, long companyId, Locale locale, String contextPath);
    

    /**
     * Get the last value for a given sensor.
     * @param session
     * @param sensorId
     * @return the latest record or null if there are no records
     */
    SensorValue getLastValue(CommonSenseSession session, int sensorId);

    
    /**
     * Create a new user on the common sense web service.
     * create a user.
     * @param data the user data. This data will pass through a validation process.
     * according to the documentation.
     * @return true or false wether the user has been created or not.
     */    
    LocalResponse createUser(CommonSenseUserData data);

    
    /**
     * 
     * @param commonSenseSensorData
     * @return 
     */
    LocalResponse createSensor(CommonSenseSession session, CommonSenseSensorData commonSenseSensorData);

    
    /**
     * Get a list of sensors by calling the /sensors API call.
     * The returned result will contain a list with the relevant data and 
     * the order will be the same as the service returns.
     * @return 
     */
    List<Sensor> listSensors(CommonSenseSession session);
    
    /**
     * Filter the common sense sensors by participant email. Useful when you want
     * to get only the sensors for a single participant.
     * @param session
     * @param participantEmail
     * @return 
     */
    List<Sensor> listSensors(CommonSenseSession session, String device_type);
    
    
    
    /**
     * Get the users registered for a given sensor. This performs a lookup on
     * the common sense REST API.
     * @param sensorId
     * @param session 
     * @return A list of users or an empty list if none.
     */
    CommonSenseUserData getCurrentUserInfo(CommonSenseSession session);

    
    /**
     * 
     * @param session
     * @param sensorId
     * @param value
     * @return 
     */
    //LocalResponse addLiferaySensorData(CommonSenseSession session, String sensorId, LiferaySensorData value);
    @Async
    void addLiferaySensorData(CommonSenseSession session, String sensorId, LiferaySensorData value);
    @Async
    void addLiferaySensorData(String username, String password, String sensorIdStr, LiferaySensorData liferaySensorData);
    
    /**
     * 
     * @param session
     * @param sensorId
     * @param value
     * @return 
     */
    LocalResponse addClientLocationData(CommonSenseSession session, String sensorId, ClientLocation value);
}
