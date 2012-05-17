package com.rcs.liferaysense.portlet.senseliferaysensor;

import com.google.gson.Gson;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.rcs.common.service.ServiceActionResult;
import com.rcs.liferaysense.portlet.common.Utils;
import com.rcs.liferaysense.service.commonsense.*;
import java.util.*;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import static com.rcs.liferaysense.common.Constants.*;
import com.rcs.liferaysense.entities.SenseConfiguration;
import com.rcs.liferaysense.entities.dtos.ClientLocation;
import com.rcs.liferaysense.service.local.SenseConfigurationService;

/**
 *
 * @author Prj.M@x <pablo.rendon@rotterdam-cs.com>
 */
@Controller
@RequestMapping("VIEW")
public class SenseLiferayController {    
    private static Log log = LogFactoryUtil.getLog(SenseLiferayController.class);      
   
    @Autowired
    private Utils utils;    
    @Autowired
    private CommonSenseService commonSenseService; 
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
        return new ModelAndView("senseliferay/view", modelAttrs); 
    }
    
    /*
     ********************************************** ResourceMapping AJAX Methods
    */    
    /**
     * Store User Data and Location in Sense
     * @param clientlocation
     * @param request
     * @param response
     * @return
     * @throws Exception 
     */
    @ResourceMapping(value = "storeLiferayData")
    public ModelAndView storeLiferayDataController(
            String clientlocation
            ,ResourceRequest request
            ,ResourceResponse response
    ) throws Exception {         
        ThemeDisplay themeDisplay= (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long groupId = utils.getGroupId(request);
        long companyId = utils.getcompanyId(request);        
        String pageTitle = themeDisplay.getLayout().getName(themeDisplay.getLocale()); 
        long pageId = themeDisplay.getLayout().getLayoutId();        
        HttpSession httpSession = ((LiferayPortletRequest) request).getHttpServletRequest().getSession();        
        
        String previousPageTitle = (String) httpSession.getAttribute("pageTitle");     
        long previousPageId = 0;
        if (httpSession.getAttribute("pageId") != null) {
            previousPageId = (Long) httpSession.getAttribute("pageId");    
        }        
        httpSession.setAttribute("pageTitle", pageTitle);
        httpSession.setAttribute("pageId", pageId);
       
        String userAgent = ((LiferayPortletRequest) request).getHttpServletRequest().getHeader("User-Agent");
        String userIP = ((LiferayPortletRequest) request).getHttpServletRequest().getRemoteAddr();
        CommonSenseSession commonSenseSession = null;
        try {            
            commonSenseSession = utils.getDefaultUserCommonSenseSession(groupId, companyId);
            
            if (commonSenseSession != null) {
                long liferayUserId = 0;
                if (themeDisplay.isSignedIn()) {
                    liferayUserId = utils.getUserId(request);
                }

                //Store LiferaySensor Data
                LiferaySensorData liferaySensorData = new LiferaySensorData();
                liferaySensorData.setIp(userIP);
                liferaySensorData.setPageId(pageId);
                liferaySensorData.setPrevious_pageId(previousPageId);
                liferaySensorData.setPage(pageTitle);
                liferaySensorData.setPrevious_page(previousPageTitle);
                liferaySensorData.setUserAgent(userAgent);
                liferaySensorData.setLiferayUserId(liferayUserId);
                ServiceActionResult<SenseConfiguration> serviceActionResultLiferaySensorData = senseConfigurationService.findByProperty(groupId, companyId, ADMIN_CONFIGURATION_DEFAULT_SENSE_LIFERAYSENSORDATA_ID);
                if (serviceActionResultLiferaySensorData.isSuccess()) {
                    String liferaySensorId = serviceActionResultLiferaySensorData.getPayload().getPropertyValue(); 
                    commonSenseService.addLiferaySensorData(commonSenseSession, liferaySensorId, liferaySensorData);
                }
                
                //Store ClientLocation Data
                Gson gson = new Gson();
                ClientLocation clientLocation = gson.fromJson(clientlocation, ClientLocation.class);
                clientLocation.setIp(userIP);                
                ServiceActionResult<SenseConfiguration> serviceActionResultClientlocationSensor = senseConfigurationService.findByProperty(groupId, companyId, ADMIN_CONFIGURATION_DEFAULT_SENSE_CLIENTLOCATIONSENSOR_ID);
                if (serviceActionResultClientlocationSensor.isSuccess()) {
                    String clientLocationSensorId = serviceActionResultClientlocationSensor.getPayload().getPropertyValue();  
                    commonSenseService.addClientLocationData(commonSenseSession, clientLocationSensorId, clientLocation);
                }               

            } else {
                log.error("commonSense Session null");
            }            
        } catch(PortalException e) {
            log.error("No CommonSense Session: " + e);
        } catch(SystemException e) {
            log.error("No CommonSense Session: " + e);
        }
        return null;
    }    
}