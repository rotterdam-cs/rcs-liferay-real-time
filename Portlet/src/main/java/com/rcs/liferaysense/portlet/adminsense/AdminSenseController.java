package com.rcs.liferaysense.portlet.adminsense;

import com.google.gson.Gson;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.User;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.rcs.common.service.ServiceActionResult;
import static com.rcs.liferaysense.common.Constants.*;
import com.rcs.liferaysense.common.ResourceBundleHelper;
import com.rcs.liferaysense.entities.SenseConfiguration;
import com.rcs.liferaysense.entities.SenseUser;
import com.rcs.liferaysense.entities.chap.graph.dtos.LiferaySensorDataDTO;
import com.rcs.liferaysense.entities.chap.graph.dtos.LiferaySensorsDataDTO;
import com.rcs.liferaysense.entities.dtos.LocalResponse;
import com.rcs.liferaysense.entities.dtos.PagesDto;
import com.rcs.liferaysense.entities.enums.TimelineRange;
import com.rcs.liferaysense.portlet.common.Utils;
import com.rcs.liferaysense.service.commonsense.*;
import com.rcs.liferaysense.service.local.SenseConfigurationService;
import com.rcs.liferaysense.service.local.SenseUserService;
import com.rcs.liferaysense.utils.HashUtils;
import java.util.*;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

/**
 * @author Prj.M@x <pablo.rendon@rotterdam-cs.com>
 */
@Controller
@RequestMapping("VIEW")
public class AdminSenseController {    
    private static Log log = LogFactoryUtil.getLog(AdminSenseController.class);  
    
    @Autowired
    private Utils utils;    
    @Autowired
    private CommonSenseService commonSenseService;    
    @Autowired
    private SenseUserService senseUserService;    
    @Autowired
    private SenseConfigurationService senseConfigurationService;
    
    /**
     * Main Method
     * @param request
     * @param response
     * @return
     * @throws PortalException
     * @throws SystemException 
     */
    @RenderMapping
    public ModelAndView resolveView(PortletRequest request, PortletResponse response) throws PortalException, SystemException {
        HashMap<String, Object> modelAttrs = new HashMap<String, Object>();       
        Long liferayUserId = utils.getUserId(request);        
        modelAttrs.put("isSenseAdmin", senseUserService.isSenseAdministratorByLiferayUserId(liferayUserId));
        return new ModelAndView("adminsense/view", modelAttrs); 
    }    

    /*
     ********************************************** ResourceMapping AJAX Methods
    */    
    /**
     * Sections Handler
     * @param request
     * @param response
     * @return
     * @throws Exception 
     */
    @ResourceMapping(value = "senseAdminSections")
    public ModelAndView senseAdminSectionsController(
             String section
            ,ResourceRequest request
            ,ResourceResponse response
    ) throws Exception {        
        HashMap<String, Object> modelAttrs = new HashMap<String, Object>();        
        Long liferayUserId = utils.getUserId(request);
        Locale locale = LocaleUtil.fromLanguageId(LanguageUtil.getLanguageId(request));
        long groupId = utils.getGroupId(request);
        long companyId = utils.getcompanyId(request);
        
        boolean isSenseAdmin = senseUserService.isSenseAdministratorByLiferayUserId(liferayUserId);          
        
        if (section.equals(ADMIN_SECTION_ACCOUNT)) {            
            //Account
            modelAttrs = admin_section_account(modelAttrs, liferayUserId, groupId, companyId);            
        } else if (section.equals(ADMIN_SECTION_ANALYTICS)) {            
            //Analytics            
            
        } else if (section.equals(ADMIN_SECTION_GLOBAL_SETTINGS)) {
            //if try to access the admin section and is not a Sense admin
            if (!isSenseAdmin) {
                String message = ResourceBundleHelper.getKeyLocalizedValue("com.rcs.sense.admin.error.not.authorized.section", locale);
                modelAttrs.put("errorMessage", message);
                return new ModelAndView("adminsense/" + ADMIN_SUBSECTION_TOP_MESSAGES, modelAttrs);
            } else {                
                //Global Settings
                modelAttrs = admin_section_global_settings(modelAttrs);
            }            
        }
        modelAttrs.put("isSenseAdmin", isSenseAdmin);
        return new ModelAndView("adminsense/" + section, modelAttrs);
    }
    
    /**
     * AJAX
     * Admin / Analytics / getAnalyticsBigRange
     * @param range
     * @param clientlocation
     * @param request
     * @param response
     * @return
     * @throws Exception 
     */
    @ResourceMapping(value = "getAnalyticsBigRange")
    public ModelAndView getAnalyticsBigRangeController (
             int range
            ,String clientlocation
            ,ResourceRequest request
            ,ResourceResponse response
    ) throws Exception {        
        ThemeDisplay themeDisplay= (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long groupId = utils.getGroupId(request);
        long companyId = utils.getcompanyId(request);   
        HashMap<String, Object> modelAttrs = new HashMap<String, Object>();        
        Locale locale = themeDisplay.getLocale();
        String contextPath = request.getContextPath();           
        List<PagesDto> pages = getPages(themeDisplay, locale);
           
        Date fromDate = TimelineRange.get(range);        
        Date toDate = new Date();
        
        List <LiferaySensorDataDTO> liferaySensorsData = new ArrayList<LiferaySensorDataDTO>();
        CommonSenseSession commonSenseSession = utils.getDefaultUserCommonSenseSession(groupId, companyId);
        ServiceActionResult<SenseConfiguration> serviceActionResult = senseConfigurationService.findByProperty(groupId, companyId, ADMIN_CONFIGURATION_DEFAULT_SENSE_LIFERAYSENSORDATA_ID);
        if (commonSenseSession != null && serviceActionResult.isSuccess()) {
            String liferaySensorId = serviceActionResult.getPayload().getPropertyValue();            
            liferaySensorsData = commonSenseService.getSensorDataDTO(commonSenseSession, liferaySensorId, fromDate, toDate, pages, groupId, companyId, locale, contextPath);
        }        
        modelAttrs.put("liferaySensorsData", liferaySensorsData);
        modelAttrs.put("fromDate", fromDate);
        modelAttrs.put("toDate", toDate);
        return new ModelAndView("adminsense/analyticstimelineview", modelAttrs);
    }
    
    /**
     * AJAX
     * Admin / Analytics / getAnalyticsBigRangeJSON
     * @param range
     * @param clientlocation
     * @param request
     * @param response
     * @return
     * @throws Exception 
     */
    @ResourceMapping(value = "getAnalyticsBigRangeJSON")
    public ModelAndView getAnalyticsBigRangeJSONController(
             int range
            ,String clientlocation
            ,ResourceRequest request
            ,ResourceResponse response
    ) throws Exception {        
        ThemeDisplay themeDisplay= (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long groupId = utils.getGroupId(request);
        long companyId = utils.getcompanyId(request);          
        Locale locale = themeDisplay.getLocale();
        String contextPath = request.getContextPath();           
        List<PagesDto> pages = getPages(themeDisplay, locale);
           
        Date fromDate = TimelineRange.get(range);        
        Date toDate = new Date();
        
        List <LiferaySensorDataDTO> liferaySensorsData = new ArrayList<LiferaySensorDataDTO>();
        CommonSenseSession commonSenseSession = utils.getDefaultUserCommonSenseSession(groupId, companyId);
        ServiceActionResult<SenseConfiguration> serviceActionResult = senseConfigurationService.findByProperty(groupId, companyId, ADMIN_CONFIGURATION_DEFAULT_SENSE_LIFERAYSENSORDATA_ID);
        if (commonSenseSession != null && serviceActionResult.isSuccess()) {
            String liferaySensorId = serviceActionResult.getPayload().getPropertyValue();            
            liferaySensorsData = commonSenseService.getSensorDataDTO(commonSenseSession, liferaySensorId, fromDate, toDate, pages, groupId, companyId, locale, contextPath);
        }
        
        LiferaySensorsDataDTO liferaySensorsDataDTO = new LiferaySensorsDataDTO();
        liferaySensorsDataDTO.setLiferaySensorsData(liferaySensorsData);
        
        Gson gson = new Gson();        
        String sensorJSON = gson.toJson(liferaySensorsDataDTO, LiferaySensorsDataDTO.class);        
        response.getWriter().write(sensorJSON);
        return null;
    }
    
    /**
     * AJAX
     * Admin / Analytics / getAnalyticsRange (Detailed Network View)
     * @param startTime
     * @param endTime
     * @param clientlocation
     * @param request
     * @param response
     * @return
     * @throws Exception 
     */
    @ResourceMapping(value = "getAnalyticsRange")
    public ModelAndView getAnalyticsRangeController(
             Long startTime
            ,Long endTime
            ,String clientlocation
            ,ResourceRequest request
            ,ResourceResponse response
    ) throws Exception {        
        ThemeDisplay themeDisplay= (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long groupId = utils.getGroupId(request);
        long companyId = utils.getcompanyId(request); 
        CommonSenseSession commonSenseSession = utils.getDefaultUserCommonSenseSession(groupId, companyId);
        
        Date fromDate = new Date(startTime);
        Date toDate = new Date(endTime);
        String contextPath = request.getContextPath();
        HashMap<String, Object> modelAttrs = getModelAttrs(fromDate, toDate, commonSenseSession, themeDisplay, groupId, companyId, contextPath);
        long stepSeconds = (endTime - startTime) / NETWORKMAP_ZOOM_ADJUST_FACTOR;
        modelAttrs.put("stepSeconds", stepSeconds);
        
        int autoReloadTime = DEFAULT_NETWORKMAP_AUTORELOAD_TIME;
        ServiceActionResult<SenseConfiguration> serviceActionResult = senseConfigurationService.findByProperty(groupId, companyId, ADMIN_CONFIGURATION_DEFAULT_NETWORKMAP_AUTORELOAD_TIME);
        if (commonSenseSession != null && serviceActionResult.isSuccess()) {
            try {
                autoReloadTime = Integer.parseInt(serviceActionResult.getPayload().getPropertyValue());
            } catch (NumberFormatException e) {
                log.error("Can not parse autoreload time " + e);
            }            
        }
        
        modelAttrs.put("autoReloadTime", autoReloadTime);
        return new ModelAndView("adminsense/analyticsnetworkview", modelAttrs);
    }
       
    /**
     * AJAX
     * Admin / Global Settings / senseAdminSaveGlobalSettings
     * @param request
     * @param response
     * @return
     * @throws Exception
     * To add more configuration options add the parameter in this method (if required) 
     * and add a "put" to the CONFIGURATION OPTIONS map with the name and value
     */
    @ResourceMapping(value = "senseAdminSaveGlobalSettings")
    public ModelAndView senseAdminSaveGlobalSettingsController(
             String auto_register
            ,String allow_change_account
            ,String default_sense_username
            ,String default_sense_pass  
            ,String time_to_keep_alive_page_navigation
            ,String network_autoreload_time
            ,ResourceRequest request
            ,ResourceResponse response
    ) throws Exception {
        HashMap<String, String> configurationOptions = new HashMap<String, String>(); 
        long groupId = utils.getGroupId(request);
        long companyId = utils.getcompanyId(request);        
        
        //If the password has not changed then use the same
        //This is because of the MD5
        String default_password = HashUtils.md5(default_sense_pass);
        ServiceActionResult<SenseConfiguration> serviceActionResult = senseConfigurationService.findByProperty(groupId, companyId, ADMIN_CONFIGURATION_DEFAULT_SENSE_PASSWORD);
        if (serviceActionResult.isSuccess()) {
            String oldPassword = serviceActionResult.getPayload().getPropertyValue();            
            if (default_password.equals(HashUtils.md5(oldPassword))) {
                default_password = oldPassword;
            }
        }  
        
        Locale locale = LocaleUtil.fromLanguageId(LanguageUtil.getLanguageId(request));
        LocalResponse result = new LocalResponse();
        String message = "";
        
        //Create a sense user if it doesn't exists
        CommonSenseSession commonAdminSenseSession = commonSenseService.login(default_sense_username, default_password);
        if (commonAdminSenseSession == null) {
            log.info("Creating new Sense Admin Account");
            utils.logOutSense(request);
            long liferayUserId = utils.getUserId(request);
            LocalResponse senseAdminUserResponse = senseAdminRegisterAccount(default_sense_username, default_password, liferayUserId);
            if (!senseAdminUserResponse.isSuccess()){
                result.setSuccess(false);
                result.setMessage(senseAdminUserResponse.getMessage());
                response.getWriter().write(utils.validationMessages(result));
                return null;
            } else {
                message += ResourceBundleHelper.getKeyLocalizedValue("com.rcs.sense.admin.global.settings.senseadminuser.created", locale);
            }
        } else {
            message += ResourceBundleHelper.getKeyLocalizedValue("com.rcs.sense.admin.global.settings.senseadminuser.verified", locale);
        }
        
        //Create LiferaySensorData Sensor if it doesn't exists
        List<Sensor> sensors = commonSenseService.listSensors(commonAdminSenseSession, LIFERAY_SENSOR_DEVICE_TYPE);
        if (sensors.isEmpty()) {
            String name = LIFERAY_SENSOR_NAME;
            String display_name = LIFERAY_SENSOR_DISPLAY_NAME;
            String data_type = LIFERAY_SENSOR_DATA_TYPE;
            String device_type = LIFERAY_SENSOR_DEVICE_TYPE;
            String type = LIFERAY_SENSOR_TYPE;
            String data_structure=LIFERAY_SENSOR_DATA_STRUCTURE;
            LocalResponse sensorCreationResonse = senseAdminCreateSensor(commonAdminSenseSession, name, display_name, data_type, device_type, type, data_structure);
            if (!sensorCreationResonse.isSuccess()) {
                result.setSuccess(false);
                result.setMessage(sensorCreationResonse.getMessage());
                response.getWriter().write(utils.validationMessages(result));
                return null;
            } else {
                sensors = commonSenseService.listSensors(commonAdminSenseSession, LIFERAY_SENSOR_DEVICE_TYPE);
                message += ResourceBundleHelper.getKeyLocalizedValue("com.rcs.sense.admin.global.settings.liferaysensordata.created", locale);
            }
        }
        String liferaySensorDataId = "";
        for (Sensor sensor : sensors) {
            liferaySensorDataId = String.valueOf(sensor.getId());
        }
        message += ResourceBundleHelper.getKeyLocalizedValue("com.rcs.sense.admin.global.settings.liferaysensordata.verified", locale).replace("{0}", liferaySensorDataId);
        
        //Create ClientLocation Sensor if it doesn't exists
        List<Sensor> sensorsClientLocation = commonSenseService.listSensors(commonAdminSenseSession, CLIENTLOCATION_SENSOR_DEVICE_TYPE);
        if (sensorsClientLocation.isEmpty()) {
            String name = CLIENTLOCATION_SENSOR_NAME;
            String display_name = CLIENTLOCATION_SENSOR_DISPLAY_NAME;
            String data_type = CLIENTLOCATION_SENSOR_DATA_TYPE;
            String device_type = CLIENTLOCATION_SENSOR_DEVICE_TYPE;
            String type = CLIENTLOCATION_SENSOR_TYPE;
            String data_structure=CLIENTLOCATION_SENSOR_DATA_STRUCTURE;
            LocalResponse sensorCreationResonse = senseAdminCreateSensor(commonAdminSenseSession, name, display_name, data_type, device_type, type, data_structure);
            if (!sensorCreationResonse.isSuccess()) {
                result.setSuccess(false);
                result.setMessage(sensorCreationResonse.getMessage());
                response.getWriter().write(utils.validationMessages(result));
                return null;
            } else {
                sensorsClientLocation = commonSenseService.listSensors(commonAdminSenseSession, CLIENTLOCATION_SENSOR_DEVICE_TYPE);
                message += ResourceBundleHelper.getKeyLocalizedValue("com.rcs.sense.admin.global.settings.clientlocationsensordata.created", locale);
            }
        }
        String clientLocationSensorId = "";
        for (Sensor sensor : sensorsClientLocation) {
            clientLocationSensorId = String.valueOf(sensor.getId());
        }
        message += ResourceBundleHelper.getKeyLocalizedValue("com.rcs.sense.admin.global.settings.clientlocationsensordata.verified", locale).replace("{0}", clientLocationSensorId);
         
        //CONFIGURATION OPTIONS
        configurationOptions.put(ADMIN_CONFIGURATION_ALLOW_AUTO_REGISTER, auto_register);
        configurationOptions.put(ADMIN_CONFIGURATION_ALLOW_CHANGE_SENSE_ACCOUNT, allow_change_account);
        configurationOptions.put(ADMIN_CONFIGURATION_DEFAULT_SENSE_USERNAME, default_sense_username);
        configurationOptions.put(ADMIN_CONFIGURATION_DEFAULT_SENSE_PASSWORD, default_password);
        configurationOptions.put(ADMIN_CONFIGURATION_DEFAULT_SENSE_LIFERAYSENSORDATA_ID, liferaySensorDataId);
        configurationOptions.put(ADMIN_CONFIGURATION_DEFAULT_SENSE_CLIENTLOCATIONSENSOR_ID, clientLocationSensorId);
        configurationOptions.put(ADMIN_CONFIGURATION_TIME_TO_KEEP_ALIVE_PAGE_NAVIGATION, time_to_keep_alive_page_navigation);
        configurationOptions.put(ADMIN_CONFIGURATION_DEFAULT_NETWORKMAP_AUTORELOAD_TIME, network_autoreload_time);
          
        //Save all configuration options
        for (Map.Entry<String, String> entry : configurationOptions.entrySet()) {           
            ServiceActionResult<SenseConfiguration> resultupdate = updateProperty(groupId, companyId, entry.getKey(), entry.getValue());
            if (!resultupdate.isSuccess()) {
                result.setSuccess(false);                
                List<String> validationKeys = resultupdate.getValidationKeys();
                for (String key : validationKeys) {                    
                    message += key + " ";
                    log.error("ERROR " + key);
                }
                result.setMessage(message);        
                response.getWriter().write(utils.validationMessages(result));
                return null;
            }
        }

        result.setSuccess(true);
        message += ResourceBundleHelper.getKeyLocalizedValue("com.rcs.sense.admin.global.settings.saved", locale);
        result.setMessage(message);        
        response.getWriter().write(utils.validationMessages(result));
        return null;
    }
        
    /**
     * AJAX
     * Admin / Analytics / getAnalyticsRangeJSON (Detailed Network View JSON)
     * @param startTime
     * @param endTime
     * @param clientlocation
     * @param request
     * @param response
     * @return
     * @throws Exception 
     */
    @ResourceMapping(value = "getAnalyticsRangeJSON")
    public ModelAndView getAnalyticsRangeJSONController(
             Long startTime
            ,Long endTime
            ,ResourceRequest request
            ,ResourceResponse response
    ) throws Exception {        
        ThemeDisplay themeDisplay= (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long groupId = utils.getGroupId(request);
        long companyId = utils.getcompanyId(request); 
        CommonSenseSession commonSenseSession = utils.getDefaultUserCommonSenseSession(groupId, companyId);
        
        Date fromDate = new Date(startTime);
        Date toDate = new Date(endTime);
        String contextPath = request.getContextPath();
        
        HashMap<String, Object> modelAttrs = getModelAttrs(fromDate, toDate, commonSenseSession, themeDisplay, groupId, companyId, contextPath);
        LiferaySensorsDataDTO liferaySensorsDataDTO = new LiferaySensorsDataDTO();
        List <LiferaySensorDataDTO> liferaySensorsData = (List <LiferaySensorDataDTO>) modelAttrs.get("liferaySensorsData");        
        liferaySensorsDataDTO.setLiferaySensorsData(liferaySensorsData);
        List<PagesDto> pages = (List <PagesDto>) modelAttrs.get("pages");        
        liferaySensorsDataDTO.setPages(pages);
        
        long stepSeconds = (endTime - startTime) / NETWORKMAP_ZOOM_ADJUST_FACTOR;
        liferaySensorsDataDTO.setStepSeconds(stepSeconds);
        
        Gson gson = new Gson();        
        String sensorJSON = gson.toJson(liferaySensorsDataDTO, LiferaySensorsDataDTO.class);        
        response.getWriter().write(sensorJSON);
        return null;
    }

    /*
     ********************************************************** Auxiliar Methods
    */    
    /**
     * Auxiliar Method
     * Section Account first AJAX call
     * @param modelAttrs
     * @param liferayUserId
     * @param locale
     * @param request
     * @param response
     * @return
     * @throws PortalException
     * @throws SystemException 
     */
    private HashMap admin_section_account(HashMap<String, Object> modelAttrs,Long liferayUserId, long groupId, long companyId) throws PortalException, SystemException {         
        modelAttrs = admin_section_global_settings(modelAttrs);        
        boolean senseUserRegistered = false;
        
        SenseUser senseUser = senseUserService.findByLiferayId(groupId, companyId, liferayUserId);
        
        //Sense User already created
        if (senseUser != null) {
            senseUserRegistered = true;
            modelAttrs.put("senseusername", senseUser.getUsername());            
        //Sense User not yet created
        } else {
            User liferayUser = UserLocalServiceUtil.getUserById(liferayUserId);            
            modelAttrs.put("senseusername", liferayUser.getEmailAddress());
            modelAttrs.put("sensepassword", liferayUser.getPassword().getBytes());
        }
        
        modelAttrs.put("senseUserRegistered", senseUserRegistered);        
        return modelAttrs;
    }
      
    /**
     * Auxiliar Method
     * Section Global Settings first AJAX call
     * @param modelAttrs
     * @param liferayUserId
     * @param locale
     * @param request
     * @param response
     * @return
     * @throws PortalException
     * @throws SystemException 
     */
    private HashMap admin_section_global_settings(HashMap<String, Object> modelAttrs) throws PortalException, SystemException {        
        List<SenseConfiguration> senseConfigurations = senseConfigurationService.findAll();
        for (SenseConfiguration senseConfiguration : senseConfigurations) {            
            if (senseConfiguration.getPropertyValue()!= null && senseConfiguration.getPropertyValue().equals(CHECKBOX_SELECTED_VALUE)){
                modelAttrs.put(senseConfiguration.getProperty(), true);
            } else if (senseConfiguration.getPropertyValue() == null ) {
                modelAttrs.put(senseConfiguration.getProperty(), false);
            } else {
                modelAttrs.put(senseConfiguration.getProperty(), senseConfiguration.getPropertyValue());
            }            
        }
        return modelAttrs;
    }
    
    /**
     * Auxiliar Method
     * @param commonSenseSession
     * @param themeDisplay
     * @return
     * @throws PortalException
     * @throws SystemException 
     */
    private HashMap<String, Object> getModelAttrs(Date fromDate, Date toDate, CommonSenseSession commonSenseSession, ThemeDisplay themeDisplay, long groupId, long companyId, String contextPath) throws PortalException, SystemException{
        HashMap<String, Object> modelAttrs = new HashMap<String, Object>();
        List <LiferaySensorDataDTO> liferaySensorsData = new ArrayList<LiferaySensorDataDTO>();
        Locale locale = themeDisplay.getLocale();
          
        List<PagesDto> pages = getPages(themeDisplay, locale);
                
        ServiceActionResult<SenseConfiguration> serviceActionResult = senseConfigurationService.findByProperty(groupId, companyId, ADMIN_CONFIGURATION_DEFAULT_SENSE_LIFERAYSENSORDATA_ID);
        if (commonSenseSession != null && serviceActionResult.isSuccess()) {
            String liferaySensorId = serviceActionResult.getPayload().getPropertyValue();
            liferaySensorsData = commonSenseService.getSensorDataDTO(commonSenseSession, liferaySensorId, fromDate, toDate, pages, groupId, companyId, locale, contextPath);
        }
        modelAttrs.put("pages", pages);
        modelAttrs.put("liferaySensorsData", liferaySensorsData);
        return modelAttrs;
    }
    
    /**
     * Auxiliar Method
     * @param layouts
     * @param locale
     * @return 
     */
    private List<PagesDto> getPages(ThemeDisplay themeDisplay, Locale locale) {
        List<PagesDto> pages = new ArrayList<PagesDto>();
        try {
            //if we need to get information from other groups we need to change this
            List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(themeDisplay.getScopeGroupId(), false);             
            for (Layout layout : layouts) {PagesDto page = new PagesDto();
                page.setId(layout.getLayoutId());
                page.setPage(layout.getName(locale));
                page.setUrl(layout.getFriendlyURL());
                page.setVisits(0);         
                pages.add(page);
            }            
        } catch (SystemException ex) {
            log.error("Error Getting Layouts (Pages) " + ex);
        }
        return pages;
    }
   
    /**
     * Auxiliar Method
     * @param groupId
     * @param companyId
     * @param propretyName
     * @param propertyValue
     * @return 
     */
    private ServiceActionResult<SenseConfiguration> updateProperty (long groupId, long companyId, String propretyName, String propertyValue) {
        ServiceActionResult<SenseConfiguration> serviceActionResult = senseConfigurationService.findByProperty(groupId, companyId, propretyName);
        SenseConfiguration senseConfiguration;
        if (!serviceActionResult.isSuccess()) {
            senseConfiguration = new SenseConfiguration();
            senseConfiguration.setGroupid(groupId);
            senseConfiguration.setCompanyid(companyId);
            senseConfiguration.setProperty(propretyName);
        } else {
            senseConfiguration = serviceActionResult.getPayload();
        }            
        senseConfiguration.setPropertyValue(propertyValue);
        ServiceActionResult<SenseConfiguration> resultupdate = senseConfigurationService.update(senseConfiguration);
        return resultupdate;
    }
    
    /**
     * Auxiliar Method
     * @param account_username
     * @param account_password
     * @param liferayUserId
     * @return
     * @throws Exception 
     */
    private LocalResponse senseAdminRegisterAccount(String account_username, String account_password, long liferayUserId) throws Exception {        
        User liferayUser = UserLocalServiceUtil.getUserById(liferayUserId);        
        LocalResponse result;        
        account_password = HashUtils.md5(account_password);
        String email = liferayUser.getEmailAddress();
        String firstname = liferayUser.getFirstName();
        String lastname = liferayUser.getLastName();
        String phone = liferayUser.getPhones().isEmpty()?"":liferayUser.getPhones().get(0).toString();        
        CommonSenseUserData data = new CommonSenseUserData(
            email,
            account_username, 
            firstname, 
            lastname, 
            phone, 
            account_password, 
            0, 
            "", 
            0);        
        result = commonSenseService.createUser(data);        
        return result;
    }
    
    /**
     * Auxiliar Method
     * @param commonSenseSession
     * @return
     * @throws Exception 
     * Examples   
     * LiferaySensorData
     * http://api.sense-os.nl/sensors?sensor[name]=Liferay User Data&sensor[display_name]=Liferay User Data&sensor[data_type]=json&sensor[device_type]=LIFERAYUD&sensor[type]=2&sensor[data_structure]={"ip":"string", "pageId":"long", "previous_pageId":"long", "page":"string", "previous_page":"string", "userAgent":"string", "liferayUserId":"long"}
     * Liferay ClientLocation (Position) Sensor
     * http://api.sense-os.nl/sensors?sensor[name]=position&sensor[display_name]=Liferay Client Location Data&sensor[data_type]=json&sensor[device_type]=LIFERAYCLD&sensor[type]=2&sensor[data_structure]={"latitude":"float","longitude":"float","ip":"string","address":{"city":"string","region":"string","country":"string","country_code":"string"}}    
     */
    private LocalResponse senseAdminCreateSensor(CommonSenseSession commonSenseSession, String name, String display_name, String data_type, String device_type, String type, String data_structure) throws Exception {        
        LocalResponse result;       
        CommonSenseSensorData commonSenseSensorData = new CommonSenseSensorData(
                 name
                ,display_name
                ,data_type
                ,device_type
                ,type
                ,data_structure
                ,0);        
        result = commonSenseService.createSensor(commonSenseSession, commonSenseSensorData);        
        return result;
    }
}