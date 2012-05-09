<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8" import="com.rcs.liferaysense.common.Constants"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@taglib prefix="aui" uri="http://liferay.com/tld/aui" %>
<%@taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui" %>
<%@taglib prefix="theme" uri="http://liferay.com/tld/theme" %>
<%@taglib prefix="liferay-util" uri="http://liferay.com/tld/util" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<fmt:setBundle basename="Language"/>
<portlet:defineObjects /> 
<portlet:resourceURL var="senseAdminSaveGlobalSettingsURL" id="senseAdminSaveGlobalSettings" />

<div class="modal hide fade" id="<portlet:namespace/>helpWindow">
    <div class="modal-header">
        <a class="close" data-dismiss="modal">x</a>
        <h3><i class="icon-question-sign"></i> <fmt:message key="com.rcs.sense.admin.help.center"/> - <fmt:message key="com.rcs.sense.admin.global.settings"/> <span style="float: right;"><img src="/${pageContext.request.contextPath}/img/SENSELogo.jpeg" width="100px"></span></h3>
    </div>
    <div class="modal-body">
        <p><fmt:message key="com.rcs.sense.admin.help.global.settings1"/></p>
        <p><fmt:message key="com.rcs.sense.admin.help.global.settings2"/></p>        
        <p><fmt:message key="com.rcs.sense.admin.help.global.settings3"/></p>
    </div>
    <div class="modal-footer">
        <a href="#" class="btn" data-dismiss="modal"><fmt:message key="com.rcs.sense.general.close"/></a>
    </div>
</div>

<form class="well" id="<portlet:namespace/>senseglobalsettingsform">    
    <p>
        <label for="<portlet:namespace/><%=Constants.ADMIN_CONFIGURATION_ALLOW_AUTO_REGISTER%>" class="checkbox"></label>
        <input type="checkbox" name="<portlet:namespace/><%=Constants.ADMIN_CONFIGURATION_ALLOW_AUTO_REGISTER%>" <c:if test="${auto_register == true}" >checked="checked"</c:if> id="<portlet:namespace/><%=Constants.ADMIN_CONFIGURATION_ALLOW_AUTO_REGISTER%>"> <fmt:message key="com.rcs.sense.admin.global.settings.auto.register"/>
    </p>    
    <p>
        <label for="<portlet:namespace/><%=Constants.ADMIN_CONFIGURATION_ALLOW_CHANGE_SENSE_ACCOUNT%>" class="checkbox"></label>
        <input type="checkbox" name="<portlet:namespace/><%=Constants.ADMIN_CONFIGURATION_ALLOW_CHANGE_SENSE_ACCOUNT%>" <c:if test="${allow_change_account == true}" >checked="checked"</c:if> <c:if test="${auto_register == true}" >disabled="disabled"</c:if> id="<portlet:namespace/><%=Constants.ADMIN_CONFIGURATION_ALLOW_CHANGE_SENSE_ACCOUNT%>"> <fmt:message key="com.rcs.sense.admin.global.settings.allow.change.user"/>
    </p>
    
    <p>
        <label for="<portlet:namespace/><%=Constants.ADMIN_CONFIGURATION_DEFAULT_SENSE_USERNAME%>"><fmt:message key="com.rcs.sense.general.default.username"/>:</label>
        <input type="text" name="<portlet:namespace/><%=Constants.ADMIN_CONFIGURATION_DEFAULT_SENSE_USERNAME%>" class="required span3" id="<portlet:namespace/><%=Constants.ADMIN_CONFIGURATION_DEFAULT_SENSE_USERNAME%>" value="${default_sense_username}" />
    </p>    
    
    <p>
        <label for="<portlet:namespace/><%=Constants.ADMIN_CONFIGURATION_DEFAULT_SENSE_PASSWORD%>"><fmt:message key="com.rcs.sense.general.default.password"/>:</label>
        <input type="password" name="<portlet:namespace/><%=Constants.ADMIN_CONFIGURATION_DEFAULT_SENSE_PASSWORD%>" class="required span3" id="<portlet:namespace/><%=Constants.ADMIN_CONFIGURATION_DEFAULT_SENSE_PASSWORD%>" value="${default_sense_pass}" />
    </p>
    
    <p>
        <button type="button" class="btn" id="<portlet:namespace/>global-settings-button-save"><fmt:message key="com.rcs.sense.general.save"/></button>        
    </p>
</form>

<script type="text/javascript">
    jQuery(function() { 

        <%--//Handle SAVE Response--%>
        function saveGlobalSettingsHandleResponse(responseText, statusText, xhr, form) {            
            var response = getResponseTextInfo(responseText);
            if (!response[0]) {
                jQuery("#<portlet:namespace/>alert-content").html(response[1]);
                jQuery(".alert-info").hide();
                jQuery(".alert-error").fadeIn();                
            } else {
                jQuery(".alert-error").hide();
                jQuery("#<portlet:namespace/>info-content").html(response[1]);
                jQuery(".alert-info").fadeIn();                
            }
            jQuery("#<portlet:namespace/>administration-container-mask").unmask();
        }

        jQuery(document).on("change", "#<portlet:namespace/><%=Constants.ADMIN_CONFIGURATION_ALLOW_AUTO_REGISTER%>", function() {
            if(jQuery(this).is(":checked")){
                jQuery("#<portlet:namespace/><%=Constants.ADMIN_CONFIGURATION_ALLOW_CHANGE_SENSE_ACCOUNT%>").attr("checked", false);
                jQuery("#<portlet:namespace/><%=Constants.ADMIN_CONFIGURATION_ALLOW_CHANGE_SENSE_ACCOUNT%>").attr("disabled", true); 
            } else {
                jQuery("#<portlet:namespace/><%=Constants.ADMIN_CONFIGURATION_ALLOW_CHANGE_SENSE_ACCOUNT%>").attr("disabled", false); 
            }
        });
        
        <%--//Listener for Register Button --%>
        var optionsSaveGlobalSettings = {
            url : '${senseAdminSaveGlobalSettingsURL}'
            ,type : 'POST'             
            ,success : saveGlobalSettingsHandleResponse
        };
        jQuery("#<portlet:namespace/>global-settings-button-save").click(function() {
            if(jQuery('#<portlet:namespace/>senseglobalsettingsform').valid()) {
                jQuery("#<portlet:namespace/>administration-container-mask").mask('<fmt:message key="com.rcs.sense.general.mask.loading.text"/>');
                jQuery('#<portlet:namespace/>senseglobalsettingsform').ajaxSubmit(optionsSaveGlobalSettings);
            }
        });
    
    });  
</script>