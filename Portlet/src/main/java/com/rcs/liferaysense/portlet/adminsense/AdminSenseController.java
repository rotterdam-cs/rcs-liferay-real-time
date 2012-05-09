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
import com.rcs.liferaysense.entities.SenseConfiguration;
import com.rcs.liferaysense.entities.SenseUser;
import com.rcs.liferaysense.entities.chap.graph.dtos.LiferaySensorDataDTO;
import com.rcs.liferaysense.entities.dtos.LocalResponse;
import com.rcs.liferaysense.entities.dtos.PagesDto;
import com.rcs.liferaysense.portlet.common.ResourceBundleHelper;
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
 *
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
        ThemeDisplay themeDisplay= (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);       
        Long liferayUserId = utils.getUserId(request);        
        modelAttrs.put("isSenseAdmin", senseUserService.isSenseAdministratorByLiferayUserId(liferayUserId));
        return new ModelAndView("adminsense/view", modelAttrs); 
    }    

    
    
    /*
     ************************************************************** AJAX Methods
    */
    
    /**
     * Sections Handler
     * @param request
     * @param response
     * @return
     * @throws Exception 
     */
    @ResourceMapping(value = "senseAdminSections")
    public ModelAndView senseAdminSectionsController(String section, ResourceRequest request, ResourceResponse response) throws Exception {
        
        HashMap<String, Object> modelAttrs = new HashMap<String, Object>();        
        Long liferayUserId = utils.getUserId(request);
        Locale locale = LocaleUtil.fromLanguageId(LanguageUtil.getLanguageId(request));
        
        boolean isSenseAdmin = senseUserService.isSenseAdministratorByLiferayUserId(liferayUserId);          
        
        if (section.equals(ADMIN_SECTION_ACCOUNT)) {            
            //Account
            modelAttrs = admin_section_account(modelAttrs, liferayUserId, locale, request, response);            
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
                modelAttrs = admin_section_global_settings(modelAttrs, liferayUserId, locale, request, response);
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
        HashMap<String, Object> modelAttrs = new HashMap<String, Object>();        
        
        Locale locale = themeDisplay.getLocale();        
        List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(themeDisplay.getScopeGroupId(), false);      
        List<PagesDto> pages = getPages(layouts, locale);
                
        List <LiferaySensorDataDTO> liferaySensorsData = new ArrayList<LiferaySensorDataDTO>();
        
        CommonSenseSession commonSenseSession = utils.getSenseSession(request);
        Calendar gc = GregorianCalendar.getInstance();            
        gc.setTime(new Date());
        switch(range) {
            case 1: gc.add(Calendar.MONTH, -1);
                break;
            case 2: gc.add(Calendar.DAY_OF_YEAR, -7);
                break;
            case 3: gc.add(Calendar.HOUR, -24);
                break;
            default:
                gc.add(Calendar.MONTH, -1);
                break;
        }        
        Date fromDate = gc.getTime();            
        Date toDate = new Date();
        
        if (commonSenseSession != null) {
            SensorValues sensorValues = commonSenseService.getSensorData(commonSenseSession, LIFERAY_SENSOR_ID, fromDate, toDate);            
            
            Gson gson = new Gson();
            SensorValue[] gavalues = sensorValues.getData();            
            
            for (SensorValue sensorValue : gavalues) {
                LiferaySensorData liferaySensorData = gson.fromJson(sensorValue.getValue(), LiferaySensorData.class);
                LiferaySensorDataDTO liferaySensorDataDTO = new LiferaySensorDataDTO();
                for (PagesDto pagesDto : pages) {
                    if (pagesDto.getId() == liferaySensorData.getPageId()) {
                        pagesDto.setVisits(pagesDto.getVisits() + 1);
                        liferaySensorDataDTO.setTimestamp(sensorValue.getAsDate().getTime());
                        liferaySensorDataDTO.setBrowser(liferaySensorData.getUserAgent());
                        liferaySensorDataDTO.setPage(liferaySensorData.getPage());
                        liferaySensorDataDTO.setPageId(liferaySensorData.getPageId());
                        liferaySensorDataDTO.setPrevious_page(liferaySensorData.getPrevious_page());
                        liferaySensorDataDTO.setPrevious_pageId(liferaySensorData.getPrevious_pageId());
                        liferaySensorDataDTO.setPageCounter(pagesDto.getVisits());
                        liferaySensorsData.add(liferaySensorDataDTO);
                    }                    
                }                
            }
        }        
        modelAttrs.put("liferaySensorsData", liferaySensorsData);
        modelAttrs.put("fromDate", fromDate);
        modelAttrs.put("toDate", toDate);
        return new ModelAndView("adminsense/analyticstimelineview", modelAttrs);
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
                
        //CONFIGURATION OPTIONS
        configurationOptions.put(ADMIN_CONFIGURATION_ALLOW_AUTO_REGISTER, auto_register);
        configurationOptions.put(ADMIN_CONFIGURATION_ALLOW_CHANGE_SENSE_ACCOUNT, allow_change_account);
        configurationOptions.put(ADMIN_CONFIGURATION_DEFAULT_SENSE_USERNAME, default_sense_username);
        configurationOptions.put(ADMIN_CONFIGURATION_DEFAULT_SENSE_PASSWORD, default_password);
        configurationOptions.put(ADMIN_CONFIGURATION_DEFAULT_SENSE_LIFERAYSENSORDATA_ID, liferaySensorDataId);
        
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
    private HashMap admin_section_account(HashMap<String, Object> modelAttrs,Long liferayUserId, Locale locale, ResourceRequest request, ResourceResponse response) throws PortalException, SystemException {         
        modelAttrs = admin_section_global_settings(modelAttrs, liferayUserId, locale, request, response);        
        boolean senseUserRegistered = false;
        long groupId = utils.getGroupId(request);
        long companyId = utils.getcompanyId(request);
        
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
    private HashMap admin_section_global_settings(HashMap<String, Object> modelAttrs,Long liferayUserId, Locale locale, ResourceRequest request, ResourceResponse response) throws PortalException, SystemException {        
        //Send all configurations to the view
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
     * @param layouts
     * @param locale
     * @return 
     */
    private List<PagesDto> getPages(List<Layout> layouts, Locale locale){
        List<PagesDto> pages = new ArrayList<PagesDto>();
        for (Layout layout : layouts) {PagesDto page = new PagesDto();
            page.setId(layout.getLayoutId());
            page.setPage(layout.getName(locale));
            page.setUrl(layout.getFriendlyURL());
            page.setVisits(0);         
            pages.add(page);
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
     * @param groupId
     * @param companyId
     * @return 
     */
    private CommonSenseSession getDefaultUserCommonSenseSession (long groupId, long companyId) {
        CommonSenseSession commonAdminSenseSession = null;
        ServiceActionResult<SenseConfiguration> serviceActionResultPassword = senseConfigurationService.findByProperty(groupId, companyId, ADMIN_CONFIGURATION_DEFAULT_SENSE_PASSWORD);
        if (serviceActionResultPassword.isSuccess()) {
            ServiceActionResult<SenseConfiguration> serviceActionResultUsername = senseConfigurationService.findByProperty(groupId, companyId, ADMIN_CONFIGURATION_DEFAULT_SENSE_USERNAME);
            if (serviceActionResultUsername.isSuccess()) {
                commonAdminSenseSession = commonSenseService.login(serviceActionResultUsername.getPayload().getPropertyValue(), serviceActionResultPassword.getPayload().getPropertyValue());
            }
        }        
        return commonAdminSenseSession;
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
     * http://api.sense-os.nl/sensors?sensor[name]=position&sensor[display_name]=Liferay ClientLocation 1&sensor[data_type]=json&sensor[device_type]=LIFERAY&sensor[type]=2&sensor[data_structure]={"latitude":"float","longitude":"float","ip":"string","address":{"city":"string","region":"string","country":"string","country_code":"string"}}    
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