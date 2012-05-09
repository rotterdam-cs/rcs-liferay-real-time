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
<portlet:resourceURL var="senseAdminSaveAccountURL" id="senseAdminSaveAccount" />
<portlet:resourceURL var="senseAdminRegisterAccountURL" id="senseAdminRegisterAccount" />

<div class="modal hide fade" id="<portlet:namespace/>helpWindow">
    <div class="modal-header">
        <a class="close" data-dismiss="modal">x</a>
        <h3><i class="icon-question-sign"></i> <fmt:message key="com.rcs.sense.admin.help.center"/> - <fmt:message key="com.rcs.sense.admin.account"/> <span style="float: right;"><img src="/${pageContext.request.contextPath}/img/SENSELogo.jpeg" width="100px"></span></h3>
    </div>
    <div class="modal-body">
        <p><fmt:message key="com.rcs.sense.admin.help.account1"/></p>
        <p><fmt:message key="com.rcs.sense.admin.help.account2"/></p>        
        <p><fmt:message key="com.rcs.sense.admin.help.account3"/></p>
    </div>
    <div class="modal-footer">
        <a href="#" class="btn" data-dismiss="modal"><fmt:message key="com.rcs.sense.general.close"/></a>
    </div>
</div>

<c:if test="${auto_register == true || (auto_register != true && allow_change_account != true && senseUserRegistered == true)}" >    
    <div class="alert fade in">
        <h4 class="alert-heading"><fmt:message key="com.rcs.sense.admin.account.username"/> <c:if test="${senseUserRegistered == true}" >${senseusername}</c:if></h4>
        <fmt:message key="com.rcs.sense.admin.account.not.authorized"/>
    </div>
</c:if>

<c:if test="${auto_register != true && (allow_change_account == true || senseUserRegistered == false)}" >
    <form class="well" id="<portlet:namespace/>senseaccountform">   
        <p>
            <label for="<portlet:namespace/>account_username"><fmt:message key="com.rcs.sense.general.username"/>:</label>
            <input type="text" name="<portlet:namespace/>account_username" class="required span3" id="<portlet:namespace/>account_username" value="${senseusername}" />
        </p>
        <p>
            <label for="<portlet:namespace/>account_password"><fmt:message key="com.rcs.sense.general.password"/>:</label>
            <input type="password" placeholder="*****" name="<portlet:namespace/>account_password" class="required span3" id="<portlet:namespace/>account_password" value="<c:if test="${senseUserRegistered == false}" >${sensepassword}</c:if>" />
        </p>
        <c:if test="${allow_change_account == true}" >
            <p>
                <label for="<portlet:namespace/>account-user-created-account" class="checkbox"></label>
                <input type="checkbox" name="<portlet:namespace/>account-user-created-account" <c:if test="${senseUserRegistered == true}" >checked="checked"</c:if> id="<portlet:namespace/>account-user-created-account"> <fmt:message key="com.rcs.sense.admin.use.already.created.account"/>
            </p>
        </c:if>
        <p>
            <c:if test="${allow_change_account == true}" >
                <button type="button" class="btn <c:if test="${senseUserRegistered == false}" >hidden</c:if>" <c:if test="${senseUserRegistered == true}" >disabled="true"</c:if> id="<portlet:namespace/>account-button-save"><fmt:message key="com.rcs.sense.general.save"/></button>
            </c:if>
            <button type="button" class="btn <c:if test="${senseUserRegistered == true}" >hidden</c:if>" id="<portlet:namespace/>account-button-register"><fmt:message key="com.rcs.sense.general.register"/></button>
        </p>
    </form>
</c:if>


<script type="text/javascript">
    jQuery(function() {
        
        <%--//Handle REGISTER Response--%>
        function registerHandleResponse(responseText, statusText, xhr, form) {  
            var response = getResponseTextInfo(responseText);                       
            if (!response[0]) {
                jQuery("#<portlet:namespace/>alert-content").html(response[1]);
                jQuery(".alert-info").hide();
                jQuery(".alert-error").fadeIn();                
            } else {
                jQuery(".alert-error").hide();
                jQuery("#<portlet:namespace/>info-content").html(response[1]);
                jQuery(".alert-info").fadeIn();
                <c:if test="${allow_change_account == true}" >
                    jQuery("#<portlet:namespace/>account-user-created-account").click();
                </c:if>
                <c:if test="${allow_change_account != true}" >
                    jQuery("#<portlet:namespace/>account-button-register").hide();
                    jQuery("#<portlet:namespace/>account_username").attr("disabled", true);
                    jQuery("#<portlet:namespace/>account_password").attr("disabled", true);
                </c:if>
                jQuery(".sense-admin-right-menu li.disabled").removeClass("disabled");
                jQuery("#<portlet:namespace/>account-button-save").attr("disabled", true);
            }
            jQuery("#<portlet:namespace/>administration-container-mask").unmask();
            UpdateSenseLoginStatus();
        }
        
        <%--//Handle SAVE Response--%>
        function saveHandleResponse(responseText, statusText, xhr, form) {  
            var response = getResponseTextInfo(responseText);
            if (!response[0]) {
                jQuery("#<portlet:namespace/>alert-content").html(response[1]);
                jQuery(".alert-info").hide();
                jQuery(".alert-error").fadeIn();                
            } else {
                jQuery(".alert-error").hide();
                jQuery("#<portlet:namespace/>info-content").html(response[1]);
                jQuery(".alert-info").fadeIn();
                jQuery(".sense-admin-right-menu li.disabled").removeClass("disabled");
                jQuery("#<portlet:namespace/>account-button-save").attr("disabled", true);
            }
            jQuery("#<portlet:namespace/>administration-container-mask").unmask();
            UpdateSenseLoginStatus();
        }
        
        <c:if test="${allow_change_account == true}" >     
            <%--//Toogle the send button between register and save--%>
            jQuery(document).on("change", "#<portlet:namespace/>account-user-created-account", function() {
                if(jQuery(this).is(":checked")){
                    jQuery("#<portlet:namespace/>account-button-register").addClass("hidden");
                    jQuery("#<portlet:namespace/>account-button-save").removeClass("hidden");
                } else {
                    jQuery("#<portlet:namespace/>account-button-register").removeClass("hidden");
                    jQuery("#<portlet:namespace/>account-button-save").addClass("hidden");
                }
            });    
            <%--//Enable save when onchange--%>
            jQuery(document).on("keypress", "#<portlet:namespace/>account_username", function() {
                jQuery("#<portlet:namespace/>account-button-save").attr("disabled", false);            
            });
            jQuery(document).on("keypress", "#<portlet:namespace/>account_password", function() {
                jQuery("#<portlet:namespace/>account-button-save").attr("disabled", false);            
            });
            <%--//Listener for Save Button --%>
            var optionsSave = {
                url : '${senseAdminSaveAccountURL}'
                ,type : 'POST'             
                ,success : saveHandleResponse
            };
            jQuery("#<portlet:namespace/>account-button-save").click(function() {
                if(jQuery('#<portlet:namespace/>senseaccountform').valid()) {
                    jQuery("#<portlet:namespace/>administration-container-mask").mask('<fmt:message key="com.rcs.sense.general.mask.loading.text"/>');
                    jQuery('#<portlet:namespace/>senseaccountform').ajaxSubmit(optionsSave);
                }
            });
        </c:if>
    
        <%--//Listener for Register Button --%>
        var optionsRegister = {
            url : '${senseAdminRegisterAccountURL}'
            ,type : 'POST'             
            ,success : registerHandleResponse
        };
        jQuery("#<portlet:namespace/>account-button-register").click(function() {
            if(jQuery('#<portlet:namespace/>senseaccountform').valid()) {
                jQuery("#<portlet:namespace/>administration-container-mask").mask('<fmt:message key="com.rcs.sense.general.mask.loading.text"/>');
                jQuery('#<portlet:namespace/>senseaccountform').ajaxSubmit(optionsRegister);
            }
        });
        
    
    });  
</script>