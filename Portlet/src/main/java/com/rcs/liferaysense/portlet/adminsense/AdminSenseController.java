package com.rcs.liferaysense.portlet.adminsense;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.rcs.common.service.ServiceActionResult;
import static com.rcs.liferaysense.common.Constants.*;
import com.rcs.liferaysense.portlet.common.ResourceBundleHelper;
import com.rcs.liferaysense.portlet.common.Utils;
import com.rcs.liferaysense.entities.SenseConfiguration;
import com.rcs.liferaysense.entities.SenseUser;
import com.rcs.liferaysense.entities.dtos.ListResultsDTO;
import com.rcs.liferaysense.entities.dtos.LocalResponse;
import com.rcs.liferaysense.service.commonsense.CommonSenseObjectMapper;
import com.rcs.liferaysense.service.commonsense.CommonSenseService;
import com.rcs.liferaysense.service.commonsense.CommonSenseSession;
import com.rcs.liferaysense.service.commonsense.CommonSenseUserData;
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
    
    @RenderMapping
    public ModelAndView resolveView(PortletRequest request, PortletResponse response) throws PortalException, SystemException {
        HashMap<String, Object> modelAttrs = new HashMap<String, Object>();
        ThemeDisplay themeDisplay= (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long groupId = utils.getGroupId(request);
        long companyId = utils.getcompanyId(request);
        
        Long liferayUserId = utils.getUserId(request);
        boolean senseUserRegistered = false;
        
        log.error("is Sense Administrator: " + senseUserService.isSenseAdministratorByLiferayUserId(liferayUserId));
        modelAttrs.put("isSenseAdmin", senseUserService.isSenseAdministratorByLiferayUserId(liferayUserId));
                
        SenseUser senseUser = senseUserService.findByLiferayId(groupId, companyId, liferayUserId);
        if (senseUser != null) {
            senseUserRegistered = true;
            modelAttrs.put("senseusername", senseUser.getUsername());
        }
        modelAttrs.put("senseUserRegistered", senseUserRegistered);
        
        
        log.error("SENSE USER:" + senseUser);

        CommonSenseSession commonSenseSession = utils.getSenseSession(request);
        
        if (commonSenseSession != null) {
        
            CommonSenseUserData commonSenseCurrentUser = commonSenseService.getCurrentUserInfo(commonSenseSession);
            log.error("******************************* commonSenseCurrentUser.getEmail: " + commonSenseCurrentUser.getEmail());
            log.error("******************************* commonSenseCurrentUser.getName: " + commonSenseCurrentUser.getName());
            log.error("******************************* commonSenseCurrentUser.getId: " + commonSenseCurrentUser.getId());
            log.error("******************************* commonSenseCurrentUser.JSON: " + commonSenseCurrentUser.toString());

            modelAttrs.put("id", commonSenseCurrentUser.getId());
            modelAttrs.put("email", commonSenseCurrentUser.getEmail());
            modelAttrs.put("username", commonSenseCurrentUser.getUsername());
            modelAttrs.put("name", commonSenseCurrentUser.getName());
            modelAttrs.put("surname", commonSenseCurrentUser.getSurname());
            modelAttrs.put("mobile", commonSenseCurrentUser.getMobile());
            modelAttrs.put("UUID", commonSenseCurrentUser.getUUID());
            modelAttrs.put("openid", commonSenseCurrentUser.getOpenid());

            modelAttrs.put("CommonSenseUserData", commonSenseCurrentUser.toString());
        }
        
        return new ModelAndView("adminsense/view", modelAttrs); 
    }
//    

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
     * AJAX
     * Admin / Account / senseAdminRegisterAccount
     * @param request
     * @param response
     * @return
     * @throws Exception 
     */
    @ResourceMapping(value = "senseAdminRegisterAccount")
    public ModelAndView senseAdminRegisterAccountController(String account_username, String account_password, ResourceRequest request, ResourceResponse response) throws Exception {          
        //Log Out
        utils.logOutSense(request);
        
        Locale locale = LocaleUtil.fromLanguageId(LanguageUtil.getLanguageId(request)); 
        long liferayUserId = utils.getUserId(request);
        User liferayUser = UserLocalServiceUtil.getUserById(liferayUserId);        
        LocalResponse result = new LocalResponse();        
        
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
        //Create user in Sense
        result = commonSenseService.createUser(data);        
        log.error(result.getBody());
        
        if (result.isSuccess()) {        
            CommonSenseUserData commonSenseUserData = CommonSenseObjectMapper.mapUser(result.getBody());
            
            int senseUserId = commonSenseUserData.getId();
            long groupId = utils.getGroupId(request);
            long companyId = utils.getcompanyId(request);            
            
            //Create Sense User locally
            ServiceActionResult<SenseUser> resultAddSenseUser = senseUserService.addSenseUser(account_username, account_password, liferayUserId, senseUserId, companyId, groupId);
            if (resultAddSenseUser.isSuccess()) {
                String message = ResourceBundleHelper.getKeyLocalizedValue("com.rcs.sense.admin.success.created.user", locale);
                result.setMessage(message);
                
                //Login user
                utils.getSenseSession(request);
                
            } else {
                result.setSuccess(false);                
                List<String> validationKeys = resultAddSenseUser.getValidationKeys();
                for (String key : validationKeys) {                    
                    result.setMessage(key);
                    log.error("ERROR " + key);
                }
            }            
        }
        response.getWriter().write(utils.validationMessages(result));        
        return null;
    }
    
    /**
     * AJAX
     * Admin / Account / senseAdminSaveAccount
     * @param request
     * @param response
     * @return
     * @throws Exception 
     */
    @ResourceMapping(value = "senseAdminSaveAccount")
    public ModelAndView senseAdminSaveAccountController(String account_username, String account_password, ResourceRequest request, ResourceResponse response) throws Exception {
        Locale locale = LocaleUtil.fromLanguageId(LanguageUtil.getLanguageId(request)); 
        long liferayUserId = utils.getUserId(request);
        long groupId = utils.getGroupId(request);
        long companyId = utils.getcompanyId(request);
        LocalResponse result = new LocalResponse();
        account_password = HashUtils.md5(account_password);
        String message = "";
        
//        //To avoid save information if the autoregister is checked
//        SenseConfiguration senseConfiguration_auto_register = senseConfigurationService.findByProperty(groupId, companyId, ADMIN_CONFIGURATION_ALLOW_AUTO_REGISTER);
//        if (senseConfiguration_auto_register != null && senseConfiguration_auto_register.getPropertyValue().equals(CHECKBOX_SELECTED_VALUE)) {
//            message = ResourceBundleHelper.getKeyLocalizedValue("com.rcs.sense.admin.account.not.authorized", locale);
//            result.setSuccess(false);
//            result.setMessage(message);
//            response.getWriter().write(utils.validationMessages(result));
//            return null;
//        }
//        //To avoid save information if the allow user to change sense account is disabled
//        SenseConfiguration senseConfiguration_allow_change_account = senseConfigurationService.findByProperty(groupId, companyId, ADMIN_CONFIGURATION_ALLOW_CHANGE_SENSE_ACCOUNT);
//        if (senseConfiguration_allow_change_account != null && !senseConfiguration_allow_change_account.getPropertyValue().equals(CHECKBOX_SELECTED_VALUE)) {
//            message = ResourceBundleHelper.getKeyLocalizedValue("com.rcs.sense.admin.account.not.authorized", locale);
//            result.setSuccess(false);
//            result.setMessage(message);
//            response.getWriter().write(utils.validationMessages(result));
//            return null;
//        }        
        //Log Out
        utils.logOutSense(request);
        
        CommonSenseSession commonSenseSession = commonSenseService.login(account_username, account_password);
        if (commonSenseSession != null) {            
            
            result.setSuccess(true);
            CommonSenseUserData commonSenseUserData = commonSenseService.getCurrentUserInfo(commonSenseSession);
            int senseUserId = commonSenseUserData.getId();                        
            
            //Create Sense User locally
            ServiceActionResult<SenseUser> resultAddSenseUser = senseUserService.addSenseUser(account_username, account_password, liferayUserId, senseUserId, companyId, groupId);
            if (resultAddSenseUser.isSuccess()) {
                message = ResourceBundleHelper.getKeyLocalizedValue("com.rcs.sense.login.success", locale);

                //Login user
                utils.getSenseSession(request);
                
            } else {
                result.setSuccess(false);                
                List<String> validationKeys = resultAddSenseUser.getValidationKeys();
                for (String key : validationKeys) {                    
                    message = key;
                    log.error("ERROR " + key);
                }
            }            
        } else {
            message = ResourceBundleHelper.getKeyLocalizedValue("com.rcs.sense.login.error.invalid.credentials", locale); 
            result.setSuccess(false);
        }        
        result.setMessage(message);        
        response.getWriter().write(utils.validationMessages(result));
        return null;
    }
    
    /**
     * AJAX
     * Admin / Global Settings / senseAdminSaveGlobalSettings
     * @param request
     * @param response
     * @return
     * @throws Exception
     * To add more configuration options add the parameter in this method 
     * and add a "put" to the configurationOptions map with the name and value
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
        
        configurationOptions.put(ADMIN_CONFIGURATION_ALLOW_AUTO_REGISTER, auto_register);
        configurationOptions.put(ADMIN_CONFIGURATION_ALLOW_CHANGE_SENSE_ACCOUNT, allow_change_account);
        configurationOptions.put(ADMIN_CONFIGURATION_DEFAULT_SENSE_USERNAME, default_sense_username);
        configurationOptions.put(ADMIN_CONFIGURATION_DEFAULT_SENSE_PASSWORD, default_password);
        
        Locale locale = LocaleUtil.fromLanguageId(LanguageUtil.getLanguageId(request));
        LocalResponse result = new LocalResponse();
        String message = "";
        
        for (Map.Entry<String, String> entry : configurationOptions.entrySet()) {           
            ServiceActionResult<SenseConfiguration> resultupdate = updateProperty(groupId, companyId, entry.getKey(), entry.getValue());
            if (!resultupdate.isSuccess()) {
                result.setSuccess(false);                
                List<String> validationKeys = resultupdate.getValidationKeys();
                for (String key : validationKeys) {                    
                    message = key;
                    log.error("ERROR " + key);
                }
                result.setMessage(message);        
                response.getWriter().write(utils.validationMessages(result));
                return null;
            }
        }

        result.setSuccess(true);
        message = ResourceBundleHelper.getKeyLocalizedValue("com.rcs.sense.admin.global.settings.saved", locale);
        result.setMessage(message);        
        response.getWriter().write(utils.validationMessages(result));
        return null;
    }

    /**
     * 
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
     * This method updates the logged in box
     * @param request
     * @param response
     * @return
     * @throws Exception 
     */
    @ResourceMapping(value = "checkSenseLoggedIn")
    public ModelAndView checkSenseLoggedInController(ResourceRequest request, ResourceResponse response) throws Exception {         
        HashMap<String, Object> modelAttrs = new HashMap<String, Object>();
        Locale locale = LocaleUtil.fromLanguageId(LanguageUtil.getLanguageId(request)); 
        String message;
        
        CommonSenseSession commonSenseSession = utils.getSenseSession(request);
        if (commonSenseSession != null) {
            CommonSenseUserData commonSenseCurrentUser = commonSenseService.getCurrentUserInfo(commonSenseSession);
            modelAttrs.put("logged", true);
            message = ResourceBundleHelper.getKeyLocalizedValue("com.rcs.sense.logged.welcome.message", locale);
            message = message.replace("{0}", commonSenseCurrentUser.getUsername());
        } else {
            modelAttrs.put("logged", false);
            message = ResourceBundleHelper.getKeyLocalizedValue("com.rcs.sense.logged.not.logged", locale);
        }        
        modelAttrs.put("senseLoggedMessage", message);

        return new ModelAndView("adminsense/logged", modelAttrs);
    }    
    
}