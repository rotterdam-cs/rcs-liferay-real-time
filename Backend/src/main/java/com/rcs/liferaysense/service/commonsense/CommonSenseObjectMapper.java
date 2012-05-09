package com.rcs.liferaysense.service.commonsense;

import com.google.gson.Gson;
import com.rcs.liferaysense.entities.dtos.ResponseErrorMessage;
import edu.emory.mathcs.backport.java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Takes care of mapping JSON responses to OBJECTS
 * @author juan
 */
public class CommonSenseObjectMapper {
    
    private static final Logger logger = LoggerFactory.getLogger(CommonSenseObjectMapper.class);
    
    /**
     * Parse the Auth token response from JSON.
     * @param json
     * @return 
     */
    static String getAuthTokenResponse(String json) {
        Gson gson = new Gson();
        SessionId id = gson.fromJson(json, SessionId.class);
        return id.getSession_id();
    }
    
    /**
     * Map the sensors list from a json result.
     * @param result
     * @return 
     */
    static List<Sensor> mapSensorsList(String json) {
        Gson gson = new Gson();
        Sensors sensors = gson.fromJson(json, Sensors.class);
        return Arrays.asList(sensors.getSensors());
    }
    
    /**
     * Map the sensors data list from a json result.
     * @param json
     * @return 
     */
    static List<SensorValue> mapSensorData(String json) {

        SensorValues values = getCompleteSensorData(json);
        
        if (values.getTotal() == 0) {
            return Collections.emptyList();
        }
        
        return Arrays.asList(values.getData());
    }
    
    /**
     * Get the full sensor values object from a json String
     * @param json
     * @return 
     */
    static SensorValues getCompleteSensorData(String json) {
        Gson gson = new Gson();
        SensorValues values = gson.fromJson(json, SensorValues.class);
        return values;
    }
    
    /**
     * Get the full users list from a json String.
     * @param json
     * @return 
     */
    static List<CommonSenseUserData> mapUsersList(String json) {
        Gson gson = new Gson();
        SenseUsers users = gson.fromJson(json, SenseUsers.class);
        
        if (users.getUsers() == null) {
            return Collections.emptyList();
        }
        
        return Arrays.asList(users.getUsers());
    }
    
    /**
     * Get the full triggers list from a json String
     * @param testData
     * @return 
     */
    static List<Trigger> mapTriggersList(String json) {
        Gson gson = new Gson();
        Triggers triggers = gson.fromJson(json, Triggers.class);
        
        if (triggers.getTriggers() == null) {
            return Collections.emptyList();
        }
        
        return Arrays.asList(triggers.getTriggers());        
    }
    
    /**
     * Convert a single json sensor information to a sensor object.
     * @param body
     * @return 
     */
    static Sensor mapSingleSensor(String body) {
        Gson gson = new Gson();
        CommonSenseSensorWrapper sensor = gson.fromJson(body, CommonSenseSensorWrapper.class);
        return sensor.getSensor();
    }
    
    /**
     * Get the full user information from a json String.
     * @param json
     * @return 
     */
    public static CommonSenseUserData mapUser(String json) {
        Gson gson = new Gson();
        SenseUser user = gson.fromJson(json, SenseUser.class);
        
        return user.getUser();
    }
    
    /**
     * 
     * @param json
     * @return 
     */
    static ResponseErrorMessage mapMessage(String json){
        Gson gson = new Gson();
        ResponseErrorMessage error = gson.fromJson(json, ResponseErrorMessage.class);
        
        return error;
    }
    
}
