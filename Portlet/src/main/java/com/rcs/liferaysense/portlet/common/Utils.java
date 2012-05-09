package com.rcs.liferaysense.portlet.common;

import com.google.gson.Gson;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.theme.ThemeDisplay;
import com.rcs.liferaysense.entities.SenseUser;
import com.rcs.liferaysense.entities.dtos.LocalResponse;
import com.rcs.liferaysense.service.commonsense.CommonSenseService;
import com.rcs.liferaysense.service.commonsense.CommonSenseSession;
import com.rcs.liferaysense.service.local.SenseUserService;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author ggenovese
 */
@Component
public class Utils {

    private static final Logger log = Logger.getLogger(Utils.class);
    @Autowired
    private UserLocalService userService;
    @Autowired
    private SenseUserService senseUserService;
    @Autowired
    private CommonSenseService commonSenseService;
    @Autowired
    private WebParameters webParams;


    /**
     * Gets the logged in Liferay's user id attribute
     * @param request the Request
     * @return the user's id or 0 if not logged-in
     */
    public long getUserId(PortletRequest request) {
        long userID = (Long) request.getAttribute(WebKeys.USER_ID);
        return userID;
    }
    
    /**
     * 
     * @param error
     * @return String Json 
     */
    public String validationMessages(LocalResponse localResponse) {
        Gson gson = new Gson();
        String result = gson.toJson(localResponse);
        log.error("validationMessage: " + result);
        return result;

    }

    /**
     * Gets the current display theme
     * @param request the request
     * @return 
     */
    public ThemeDisplay getThemeDisplay(PortletRequest request) {
        return (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
    }

    /**
     * Get the group id
     * @param request the request
     * @return the group id
     */
    public long getGroupId(PortletRequest request) {
        ThemeDisplay themeDisplay = getThemeDisplay(request);
        return themeDisplay.getScopeGroupId();
    }
    
    /**
     * 
     * @param request
     * @return 
     */
    public long getcompanyId(PortletRequest request) {
        ThemeDisplay themeDisplay = getThemeDisplay(request);
        return themeDisplay.getCompanyId();
    }
    

    /**
     * Gets the User object associated with the user ID
     * @param userId the user id
     * @return the User object or null if not logged in
     */
    public User findUser(long userId) {
        try {
            User user = userService.getUser(userId);
            return user;
        } catch (PortalException ex) {
            return null;
        } catch (SystemException ex) {
            return null;
        }
    }

    /**
     * 
     * @param date
     * @param format
     * @param isFrom
     * @return
     * @throws java.text.ParseException 
     */
    public Date filterDateFromString(String date, String format, boolean isFrom) throws java.text.ParseException {
        DateFormat dateFormat = new SimpleDateFormat(format);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFormat.parse(date));

        if (isFrom) {

            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

        } else {

            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);

        }


        return calendar.getTime();

    }

    /**
     * It returns the sense session stored in a session variable. 
     * If null, it creates a new one and stores it
     * The sense session will persist while senseportlet session exists
     * @param request
     * @return 
     */
    public CommonSenseSession getSenseSession(PortletRequest request) throws PortalException, SystemException {

        log.warn("request: " + request);

        //getting the session
        HttpSession httpSession = ((LiferayPortletRequest) request).getHttpServletRequest().getSession();

        //getting the variable from the session
        CommonSenseSession senseSession = (CommonSenseSession) httpSession.getAttribute(webParams.getSenseSessionVariableName());

        log.warn("senseSession: " + senseSession);

        if (senseSession != null) {
            //returning the already created one
            return senseSession;

        } else {
            log.warn("else { senseSession: " + senseSession);
            //creating and storing a new sense session

            long liferayUserId = getUserId(request);            
            long groupId = getGroupId(request);
            long companyId = getcompanyId(request);
            
            SenseUser senseUser = senseUserService.findByLiferayId(groupId, companyId, liferayUserId);
            if (senseUser != null) {
                CommonSenseSession commonSenseSession = commonSenseService.login(senseUser.getUsername(), senseUser.getPassword());            
                httpSession.setAttribute(webParams.getSenseSessionVariableName(), commonSenseSession);                
                return commonSenseSession;
            } else {
                return null;
            }
        }
    }
    
    public CommonSenseSession getSenseSession(HttpServletRequest request) throws PortalException, SystemException {

        log.warn("request: " + request);

        //getting the session
        //HttpSession httpSession = ((LiferayPortletRequest) request).getHttpServletRequest().getSession();
        HttpSession httpSession = request.getSession();

        //getting the variable from the session
        CommonSenseSession senseSession = (CommonSenseSession) httpSession.getAttribute(webParams.getSenseSessionVariableName());

        log.warn("senseSession: " + senseSession);

        if (senseSession != null) {
            //returning the already created one
            return senseSession;

        } else {
            
            log.warn("else { senseSession: " + senseSession);
            //creating and storing a new sense session
            long liferayUserId = (Long) request.getSession().getAttribute(WebKeys.USER_ID);                        
            SenseUser senseUser = senseUserService.findByLiferayId(liferayUserId);
            if (senseUser != null) {
                CommonSenseSession commonSenseSession = commonSenseService.login(senseUser.getUsername(), senseUser.getPassword());            
                httpSession.setAttribute(webParams.getSenseSessionVariableName(), commonSenseSession);                
                return commonSenseSession;
            } else {
                log.warn("senseUser null");
                return null;
            }
        }
    }
    
    /**
     * 
     * @param request
     * @throws PortalException
     * @throws SystemException 
     */
    public void logOutSense(PortletRequest request) throws PortalException, SystemException {        
        //getting the session
        HttpSession httpSession = ((LiferayPortletRequest) request).getHttpServletRequest().getSession();
        CommonSenseSession senseSession = (CommonSenseSession) httpSession.getAttribute(webParams.getSenseSessionVariableName());
        if (senseSession != null) {
            commonSenseService.logout(getSenseSession(request));
            httpSession.setAttribute(webParams.getSenseSessionVariableName(), null);
        }
    }

    
}
