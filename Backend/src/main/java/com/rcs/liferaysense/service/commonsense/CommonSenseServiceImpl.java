package com.rcs.liferaysense.service.commonsense;

import antlr.StringUtils;
import com.rcs.liferaysense.entities.dtos.ClientLocation;
import com.google.gson.Gson;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalService;
import com.rcs.common.service.ServiceActionResult;
import com.rcs.liferaysense.entities.SenseConfiguration;
import com.rcs.liferaysense.entities.chap.graph.dtos.LiferaySensorDataDTO;
import com.rcs.liferaysense.entities.dtos.LocalResponse;
import com.rcs.liferaysense.entities.dtos.PagesDto;
import com.rcs.liferaysense.entities.dtos.ResponseErrorMessage;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import static com.rcs.liferaysense.common.Constants.*;
import com.rcs.liferaysense.common.ResourceBundleHelper;
import com.rcs.liferaysense.service.local.SenseConfigurationService;

/**
 * Implementation of the common sense service. This uses the spring framework
 * REST client to communicate with the common sense API.
 * @author juan
 */
@Service
class CommonSenseServiceImpl implements CommonSenseService {
    private static Log log = LogFactoryUtil.getLog(CommonSenseServiceImpl.class);

    private static final Logger logger = LoggerFactory.getLogger(CommonSenseService.class);
    /**
     * This property is overriden on the init method..
     */
    private String SERVICE_URL = "http://api.sense-os.nl/"; //service url
    private String SERVICE_DEV_URL = "http://api.dev.sense-os.nl/";
    private String QUIT_NOTIFICATION_TEMPLATE = ""; //the notification template.
    private static final String LOGIN_ENDPOINT = "login?username={username}&password={password}";
    private static final String LOGOUT_ENDPOINT = "logout?&session_id={sessionId}";
    private static final String DATA_ENDPOINT = "sensors/{sensorId}/data?start_date={startSecs}&end_date={endSecs}&total=1&page={page}&session_id={sessionId}&per_page={per_page}";
    private static final String LAST_DATA_ENDPOINT = "sensors/{sensorId}/data.json?last=1&total=1&session_id={sessionId}";    
    private static final String CREATE_USER_ENDPOINT = "users.json?user[username]={username}&user[password]={password}&user[email]={email}&user[name]={name}&user[surname]={surname}&user[mobile]={mobile}";
    private static final String CURRENT_USERINFO_ENDPOINT = "users/current?session_id={sessionId}";
    private static final String LOG_LIFERAY_SENSOR_DATA_ENDPOINT = "sensors/{sensorId}/data?data[value]={value}&session_id={sessionId}";    
    private static final String CREATE_SENSOR_ENDPOINT = "sensors?sensor[name]={name}&sensor[display_name]={display_name}&sensor[data_type]={data_type}&sensor[device_type]={device_type}&sensor[type]={type}&sensor[data_structure]={data_structure}&session_id={sessionId}";
    private static final String USERS_FOR_SENSOR_ENDPOINT = "sensors/{sensorId}/users?session_id={sessionId}";
    private static final String TRIGGERS_ENDPOINT = "sensors/{sensorId}/triggers/{triggerId}?session_id={sessionId}";
    private static final String SENSORS_ENDPOINT = "sensors?page=0&details=full&session_id={sessionId}";
    private static final String SENSOR_ENDPOINT = "sensors/{sensorId}?session_id={sessionId}"; 
    private static final String TOTAL_DATA_ENDPOINT = "sensors/{sensorId}/data.json?page=0&per_page=1&total=1&session_id={sessionId}";
    
    private static final int RESULTS_PER_PAGE = 1000;
    private static final int MAXPAGES = 10;
        
    @Autowired
    private RestTemplate template;
    
    @Autowired
    private Validator validator;
    
    @Autowired
    private SenseConfigurationService senseConfigurationService;
    
    @Autowired
    private UserLocalService userService;
    /**
     * Perform instance initialization, this sets up the restTemplate instance
     * and associate it with the custom error handler.
     */
    @PostConstruct
    private void init() {
        template.setErrorHandler(new CommonSenseErrorHandler());
    }
 
    /**
     * {@inheritDoc }
     * @param session
     * @param sensorId
     * @param dateFrom
     * @param dateTo
     * @return 
     */
    @Override
    public SensorValues getSensorData(CommonSenseSession session, String sensorIdStr, Date dateFrom, Date dateTo) {
        //build the parameters
        Map<String, String> parameters = buildSessionParameters(session);
        if (parameters == null)
            return new SensorValues();

        String startSecs = Long.toString(dateFrom.getTime() / 1000);
        String endSecs = Long.toString(dateTo.getTime() / 1000);
        //String sensorIdStr = Integer.toString(sensorId);

        // build the parameters map.
        parameters.put("startSecs", startSecs);
        parameters.put("endSecs", endSecs);
        parameters.put("sensorId", sensorIdStr);
        parameters.put("per_page", Integer.toString(RESULTS_PER_PAGE));
        
        int page = 0;
        
        List<SensorValues> valuesList = new LinkedList<SensorValues>();        
        String json = "";
        SensorValues values;
        
        do {
            parameters.put("page", Integer.toString(page));
            //logger.error("parameters2: " + parameters.toString());
            //call the service and get the response as JSON
            ResponseEntity<String> entity = template.getForEntity(SERVICE_URL + DATA_ENDPOINT, String.class, parameters);

            if (entity.getStatusCode() == HttpStatus.FORBIDDEN || entity.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.warn("Reading data for the given sensor is forbidden or not found, returning default empty value");
                SensorValues ret = new SensorValues();
                ret.setTotal(0);
                ret.setData(new SensorValue[0]);
                return ret;
            }
            json = entity.getBody();
            values = CommonSenseObjectMapper.getCompleteSensorData(json);            
            valuesList.add(values);
            page ++;
        } while (values.getData().length == RESULTS_PER_PAGE && page < MAXPAGES);
        SensorValues retvalues = buildSingleSensorValues(valuesList);
        retvalues.setJson(json);
        return retvalues;
    }
    
    /**
     * @param session
     * @param sensorIdStr
     * @param dateFrom
     * @param dateTo
     * @param pages
     * @return 
     */
    @Override
    public List <LiferaySensorDataDTO> getSensorDataDTO(CommonSenseSession session, String sensorIdStr, Date dateFrom, Date dateTo, List<PagesDto> pages, long groupId, long companyId, Locale locale, String contextPath) {
        List <LiferaySensorDataDTO> liferaySensorsData = new ArrayList<LiferaySensorDataDTO>();
        SensorValues sensorValues = getSensorData(session, sensorIdStr, dateFrom, dateTo);        
        Gson gson = new Gson();
        SensorValue[] gavalues = sensorValues.getData();        
        
        //get time to keep alive navigation
        Calendar gcToRemove = GregorianCalendar.getInstance(); 
        gcToRemove.setTime(new Date());
        int timeAlive = DEFAULT_TIME_TO_KEEP_ALIVE_PAGE_NAVIGATION;
        ServiceActionResult<SenseConfiguration> serviceActionResultTimeConf = senseConfigurationService.findByProperty(groupId, companyId, ADMIN_CONFIGURATION_TIME_TO_KEEP_ALIVE_PAGE_NAVIGATION);
        if (serviceActionResultTimeConf.isSuccess()) {
            timeAlive = Integer.parseInt(serviceActionResultTimeConf.getPayload().getPropertyValue());
        }
        gcToRemove.add(Calendar.MINUTE, -timeAlive);
        Long timeToRemove = gcToRemove.getTimeInMillis();
        
        for (SensorValue sensorValue : gavalues) {
            LiferaySensorData liferaySensorData = gson.fromJson(sensorValue.getValue(), LiferaySensorData.class);
            LiferaySensorDataDTO liferaySensorDataDTO = new LiferaySensorDataDTO();            
            liferaySensorDataDTO.setTimestamp(sensorValue.getAsDate().getTime());
            liferaySensorDataDTO.setBrowser(liferaySensorData.getUserAgent());
            liferaySensorDataDTO.setPage(liferaySensorData.getPage());
            liferaySensorDataDTO.setPageId(liferaySensorData.getPageId());
            liferaySensorDataDTO.setPrevious_page(liferaySensorData.getPrevious_page());
            liferaySensorDataDTO.setPrevious_pageId(liferaySensorData.getPrevious_pageId());
            liferaySensorDataDTO.setLiferayUserId(liferaySensorData.getLiferayUserId());
            liferaySensorDataDTO.setIp(liferaySensorData.getIp());            
            
            String userInformation = ResourceBundleHelper.getKeyLocalizedValue("com.rcs.sense.admin.analytics.details.visitorinformation", locale);
            userInformation = userInformation.replaceAll("\\{ip\\}", liferaySensorDataDTO.getIp());
            userInformation = userInformation.replaceAll("\\{img\\}", "<img src=\"" + contextPath + "/img/" + liferaySensorDataDTO.getBrowser() + ".png\">");

            Long liferayUserId = liferaySensorDataDTO.getLiferayUserId();
            if (liferayUserId != 0) {
                try {
                    User liferayUser = userService.getUserById(liferayUserId);
                    if (liferayUser != null) {
                        userInformation = ResourceBundleHelper.getKeyLocalizedValue("com.rcs.sense.admin.analytics.details.userinformation", locale);
                        userInformation = userInformation.replaceAll("\\{name\\}", liferayUser.getFullName());                        
                        userInformation = userInformation.replaceAll("\\{email\\}", liferayUser.getEmailAddress());                        
                    } 
                } catch (PortalException e) {
                    log.error("PortalException" + e);
                } catch (SystemException e) {
                    log.error("SystemException" + e);
                }

                userInformation = userInformation.replaceAll("\\{ip\\}", liferaySensorDataDTO.getIp());
                userInformation = userInformation.replaceAll("\\{img\\}", "<img src=\"" + contextPath + "/img/" + liferaySensorDataDTO.getBrowser() + ".png\">");

                liferaySensorDataDTO.setLiferayUserInformation(userInformation);
            } else {
                liferayUserId = Long.parseLong(liferaySensorDataDTO.getIp().replaceAll("[^\\d]", ""));
            }
            
            for (PagesDto pagesDto : pages) {
                if (pagesDto.getId() == liferaySensorDataDTO.getPageId()) {
                    pagesDto.setVisits(pagesDto.getVisits() + 1);
                    log.error("adding visits to (" + pagesDto.getId() + ")" + pagesDto.getPage() + " = " + pagesDto.getVisits());
                    liferaySensorDataDTO.setPageCounter(pagesDto.getVisits());
                    for (PagesDto pagesDtoInt : pages) {
                        if (pagesDtoInt.isUserInPage(liferayUserId)){
                            pagesDtoInt.removeUsersInPage(liferayUserId);
                        }
                    }                        
                    gcToRemove.setTime(new Date(liferaySensorDataDTO.getTimestamp()));
                    //Add user to page only if he has entered to the page between the last n minutes                                               
                    if (liferaySensorDataDTO.getTimestamp() > timeToRemove) {
                        pagesDto.addUsersInPage(liferayUserId, userInformation);
                    }
                }
            }
            for (PagesDto pagesDto : pages) {
                HashMap<Long, String> usersInPage = pagesDto.getUsersInPage();
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<Long,String> entry : usersInPage.entrySet()) {
                    sb.append(entry.getValue());
                    sb.append("<br />");
                }
                pagesDto.setUsersInPageInfo(sb.toString());
            }
            
            //Only add the movement with exiting pages
            if (isValidMovement(pages, liferaySensorDataDTO.getPageId(), liferaySensorDataDTO.getPrevious_pageId())) {
                liferaySensorsData.add(liferaySensorDataDTO);
            }
        }
        return liferaySensorsData;
    }
    
    private boolean isValidMovement(List<PagesDto> pages, long pageFrom, long pageTo) {
        boolean result = false;
        for (PagesDto pagesDto : pages) {            
            if (pagesDto.getId() == pageFrom){
                result = true;
            }
        }
        if (result) {
            result = false;
            for (PagesDto pagesDto : pages) {            
                if (pagesDto.getId() == pageTo){
                    result = true;
                }
            }   
        }
        return result;
    }
    
    /**
     * {@inheritDoc }
     * @param session
     * @param sensorId
     * @return 
     */
    @Override
    public SensorValue getLastValue(CommonSenseSession session, int sensorId) {
        //build the parameter map.
        Map<String, String> parameters = buildSessionParameters(session);

        //stringify values.
        String sensorIdStr = Integer.toString(sensorId);

        //set the values to the map.
        parameters.put("sensorId", sensorIdStr);

        //call the service and get the response as json.
        String json = template.getForObject(SERVICE_URL + LAST_DATA_ENDPOINT, String.class, parameters);

        //convert the json response to java objects.
        SensorValues values = CommonSenseObjectMapper.getCompleteSensorData(json);
        if (values.getTotal() == 0) {
            return null;
        } else {
            return values.getData()[0];
        }
    }

    /**
     * Login into the service.
     * @return 
     */
    String doLogin(String username, String hashedPassword) {

        // build the parameter map.
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("username", username);
        parameters.put("password", hashedPassword);

        // get the response entity, useful for checking the http status of the response.
        ResponseEntity<String> entity = template.postForEntity(SERVICE_URL + LOGIN_ENDPOINT, null, String.class, parameters);
        
        //if the status is 403, then answer null.
        if (entity.getStatusCode() == HttpStatus.FORBIDDEN) {
            return null;
        }

        //get the access token that is on the body of the result.
        String result = entity.getBody();
        //parse the access token.
        return CommonSenseObjectMapper.getAuthTokenResponse(result);
    }

    /**
     * {@inheritDoc }
     * @param session 
     */
    @Override
    public void logout(CommonSenseSession session) {
        Map<String, String> parameters = buildSessionParameters(session);
        String result = template.getForObject(SERVICE_URL + LOGOUT_ENDPOINT, String.class, parameters);
    }

    /**
     * Build a parameters map with the session access token inside.
     * @return 
     */
    private Map<String, String> buildSessionParameters(CommonSenseSession session) {
        if (session != null) {
            ServiceAccessToken token = (ServiceAccessToken) session;
            Map<String, String> ret = new HashMap<String, String>();
            ret.put("sessionId", token.getAccessToken());
            return ret;
        }
        return null;
    }

    
    /**
     * {@inheritDoc }
     * @param username
     * @param password
     * @return 
     */
    @Override
    public CommonSenseSession login(String username, String password) {        
        String token = doLogin(username, password);        
        if (token == null) {
            return null;
        }
        return new ServiceAccessToken(token, this, username, password);
    }
       

    /**
     * {@inheritDoc }     
     * @param data
     * @return 
     */        
    @Override
    public LocalResponse createUser(CommonSenseUserData data) {
        Set<ConstraintViolation<CommonSenseUserData>> violations = validator.validate(data);
        
        LocalResponse result = new LocalResponse();
        
        if (!violations.isEmpty()) {
            return result;
        }

        Map<String, String> callParams = new HashMap<String, String>();
        callParams.put("username", data.getUsername());
        callParams.put("password", data.getPassword());
        callParams.put("email", data.getEmail());
        callParams.put("name", data.getName());
        callParams.put("surname", data.getSurname());
        callParams.put("mobile", data.getMobile());
        try {
            ResponseEntity<String> entity = template.postForEntity(SERVICE_URL + CREATE_USER_ENDPOINT, null, String.class, callParams);            
            HttpStatus code = entity.getStatusCode();
            result.setResponseCode(code.value());
            result.setBody(entity.getBody());
            
            if (code != HttpStatus.CREATED) {                
                ResponseErrorMessage responseErrorMessage = CommonSenseObjectMapper.mapMessage(entity.getBody());
                result.setMessage(responseErrorMessage.getError());
                logger.warn("Could not create user on the common sense service");
            } else {
                result.setSuccess(true);
            }
        
        //todo-remove this when the header issue is fixed.
        } catch (IllegalArgumentException ex) {
            logger.error("Illegal argument while calling the web service", ex);
        }
        return result;
    }

    /**
     * 
     * @param commonSenseSensorData
     * @return 
     */
    @Override
    public LocalResponse createSensor(CommonSenseSession session, CommonSenseSensorData commonSenseSensorData) {
        Set<ConstraintViolation<CommonSenseSensorData>> violations = validator.validate(commonSenseSensorData);
        
        LocalResponse result = new LocalResponse();
        
        if (!violations.isEmpty()) {
            return result;
        }
        
        Map<String, String> callParams = buildSessionParameters(session);
        callParams.put("name", commonSenseSensorData.getName());
        callParams.put("display_name", commonSenseSensorData.getDisplay_name());
        callParams.put("data_type", commonSenseSensorData.getData_type());
        callParams.put("device_type", commonSenseSensorData.getDevice_type());
        callParams.put("type", commonSenseSensorData.getType());
        callParams.put("data_structure", commonSenseSensorData.getData_structure());
        try {
            ResponseEntity<String> entity = template.postForEntity(SERVICE_URL + CREATE_SENSOR_ENDPOINT, null, String.class, callParams);            
            HttpStatus code = entity.getStatusCode();
            result.setResponseCode(code.value());
            result.setBody(entity.getBody());
            
            if (code != HttpStatus.CREATED) {                
                ResponseErrorMessage responseErrorMessage = CommonSenseObjectMapper.mapMessage(entity.getBody());
                result.setMessage(responseErrorMessage.getError());
                logger.warn("Could not create sensor on the common sense service");
            } else {
                result.setSuccess(true);
            }
        
        //todo-remove this when the header issue is fixed.
        } catch (IllegalArgumentException ex) {
            logger.error("Illegal argument while calling the web service", ex);
        }
        return result;
    }
    
    /**
     * 
     * @param session
     * @return 
     */
    @Override
    public List<Sensor> listSensors(CommonSenseSession session) {
        //build the parameters
        Map<String, String> parameters = buildSessionParameters(session);
        //query the service
        String result = template.getForObject(SERVICE_URL + SENSORS_ENDPOINT, String.class, parameters);

        //return the list of sensors.
        List<Sensor> sensorsFromServerForUser = CommonSenseObjectMapper.mapSensorsList(result);
        
        return sensorsFromServerForUser;
    }
    
    /**
     * 
     * @param session
     * @param device_type
     * @return 
     */
    @Override
    public List<Sensor> listSensors(CommonSenseSession session, String device_type) {        
        List<Sensor> allsensors = listSensors(session);
        List<Sensor> sensorsFromServerForUser = new LinkedList<Sensor>();
        for (Sensor sensor : allsensors) {
            if (device_type.equals(sensor.getDevice_type())) {
                sensorsFromServerForUser.add(sensor);
            }
        }        
        return sensorsFromServerForUser;
    }
    
    /**
     * 
     * @param session
     * @return 
     */
    @Override
    public CommonSenseUserData getCurrentUserInfo(CommonSenseSession session) {

        Map<String, String> callParams = buildSessionParameters(session);
        
        ResponseEntity<String> entity = template.getForEntity(SERVICE_URL + CURRENT_USERINFO_ENDPOINT, String.class, callParams);
        if (entity.getStatusCode() != HttpStatus.OK) {
            logger.warn("Got Code " + entity.getStatusCode().toString() + " while trying to get user information ");
            return null;
        }
        //convert json response to object and return.
        return CommonSenseObjectMapper.mapUser(entity.getBody());
        
    }
    
    private SensorValues buildSingleSensorValues(List<SensorValues> valuesList) {
        SensorValues ret = new SensorValues();
        ArrayList<SensorValue> values = new ArrayList<SensorValue>();
        for (SensorValues v: valuesList){
            Collections.addAll(values, v.getData());
        }
        ret.setTotal(values.size());
        ret.setData(values.toArray(new SensorValue[0]));
        return ret;
    }

    
    /**
     * 
     * @param session
     * @param sensorId
     * @param value
     * @return 
     */
    @Override
    public LocalResponse addLiferaySensorData(CommonSenseSession session, int sensorId, LiferaySensorData liferaySensorData) {        
        Set<ConstraintViolation<LiferaySensorData>> violations = validator.validate(liferaySensorData);       
        LocalResponse result = new LocalResponse();
        if (!violations.isEmpty()) {
            return result;
        }
        String sensorIdStr = Integer.toString(sensorId);
        Map<String, String> parameters = buildSessionParameters(session);        
        if (parameters == null){
            logger.error("parameteres NULL");
            return null;
        }
        Gson gson = new Gson();
        parameters.put("sensorId", sensorIdStr);
        String jsonValue = gson.toJson(liferaySensorData, LiferaySensorData.class);
        parameters.put("value", jsonValue);
        try {
            ResponseEntity<String> entity = template.postForEntity(SERVICE_URL + LOG_LIFERAY_SENSOR_DATA_ENDPOINT, null, String.class, parameters);            
            HttpStatus code = entity.getStatusCode();
            result.setResponseCode(code.value());
            result.setBody(entity.getBody());
            
            if (code != HttpStatus.CREATED) {                
                ResponseErrorMessage responseErrorMessage = CommonSenseObjectMapper.mapMessage(entity.getBody());
                result.setMessage(responseErrorMessage.getError());
                logger.error("code: " +code);
                logger.error("entity.getBody(): " + entity.getBody());
                logger.warn("Could not create datapoint on the common sense service");
            } else {
                result.setSuccess(true);
            }
        
        //todo-remove this when the header issue is fixed.
        } catch (IllegalArgumentException ex) {
            logger.error("Illegal argument while calling the web service", ex);
        }
        return result;
    }
    
    
    /**
     * 
     * @param session
     * @param sensorId
     * @param clientLocationData
     * @return 
     */
    @Override
    public LocalResponse addClientLocationData(CommonSenseSession session, int sensorId, ClientLocation clientLocationData) {        
        Set<ConstraintViolation<ClientLocation>> violations = validator.validate(clientLocationData);       
        LocalResponse result = new LocalResponse();
        if (!violations.isEmpty()) {
            return result;
        }
        String sensorIdStr = Integer.toString(sensorId);
        Map<String, String> parameters = buildSessionParameters(session);        
        if (parameters == null){
            logger.error("parameteres NULL");
            return null;
        }
        Gson gson = new Gson();
        parameters.put("sensorId", sensorIdStr);
        String jsonValue = gson.toJson(clientLocationData, ClientLocation.class);
        parameters.put("value", jsonValue);
        try {
            ResponseEntity<String> entity = template.postForEntity(SERVICE_URL + LOG_LIFERAY_SENSOR_DATA_ENDPOINT, null, String.class, parameters);            
            HttpStatus code = entity.getStatusCode();
            result.setResponseCode(code.value());
            result.setBody(entity.getBody());
            
            if (code != HttpStatus.CREATED) {                
                ResponseErrorMessage responseErrorMessage = CommonSenseObjectMapper.mapMessage(entity.getBody());
                result.setMessage(responseErrorMessage.getError());
                logger.error("code: " +code);
                logger.error("entity.getBody(): " + entity.getBody());
                logger.warn("Could not create datapoint on the common sense service");
            } else {
                result.setSuccess(true);
            }
        
        //todo-remove this when the header issue is fixed.
        } catch (IllegalArgumentException ex) {
            logger.error("Illegal argument while calling the web service", ex);
        }
        return result;
    }

}
