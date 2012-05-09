package com.rcs.liferaysense.portlet.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 *
 * @author ggenovese
 */
@Component
public class WebParameters {

    @Value(value="${ajaxRequestsTimeOut}")
    private String ajaxRequestsTimeOut;

    @Value(value="${fromDateDashboardInterval}")
    private String fromDateDashboardInterval;
    
    @Value(value="${senseSessionVariableName}")
    private  String senseSessionVariableName;
    
       
    public String getAjaxRequestsTimeOut() {
        return ajaxRequestsTimeOut;
    }

    public void setAjaxRequestsTimeOut(String ajaxRequestsTimeOut) {
        this.ajaxRequestsTimeOut = ajaxRequestsTimeOut;
    }

    public String getFromDateDashboardInterval() {
        return fromDateDashboardInterval;
    }

    public void setFromDateDashboardInterval(String fromDateDashboardInterval) {
        this.fromDateDashboardInterval = fromDateDashboardInterval;
    }
    
    public String getSenseSessionVariableName() {
        return senseSessionVariableName;
    }

    public void setSenseSessionVariableName(String senseSessionVariableName) {
        this.senseSessionVariableName = senseSessionVariableName;
    }
}