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
<portlet:resourceURL var="senseAdminSectionsURL" id="senseAdminSections" />
<portlet:resourceURL var="checkSenseLoggedInURL" id="checkSenseLoggedIn" />

<%@include file="header.jsp" %>

<div id="<portlet:namespace/>logged-container" class="logged-container"></div>

<div class="container-fluid">
    <div class="row-fluid admin-mask" id="<portlet:namespace/>administration-container-mask">
        <div class="span7 sense-admin-left-container">
            <%@include file="top_messages.jsp" %>
            <div id="<portlet:namespace/>administration-container" ></div>
        </div>
        <div class="span5 sense-admin-right-menu">
            <%@include file="right_menu.jsp" %>
        </div>
    </div>
</div>
     
<script type="text/javascript">
    
    function UpdateSenseLoginStatus (){
        jQuery("#<portlet:namespace/>logged-container").load("${checkSenseLoggedInURL}");
    }
            
    Liferay.on('portletReady', function(event) {            
        if('_' + event.portletId + '_' == '<portlet:namespace/>') {            
            
            <%--//Load the first section (Account)--%>
            jQuery(function () {
                jQuery('a[data-toggle="tab"]:first').tab('show');
                UpdateSenseLoginStatus();
            });
            
            <%--//Actions to perform when change section --%>
            jQuery('a[data-toggle="tab"]').on('shown', function () {
                jQuery("#<portlet:namespace/>administration-container-mask").mask('<fmt:message key="com.rcs.sense.general.mask.loading.text"/>');
                jQuery(".alert").hide();
                var link = jQuery(this).attr("href").replace("#","");                
                jQuery(".toHide").addClass("hidden");
                jQuery("#<portlet:namespace/>" + jQuery(this).attr("rel")).addClass("toHide").removeClass("hidden");                              
                
                jQuery("#<portlet:namespace/>administration-container").load("${senseAdminSectionsURL}"
                    ,{
                        "section" : link
                    }
                    ,function() {
                        jQuery("#<portlet:namespace/>administration-container-mask").unmask();
                    }
                );
            });  
            
            
        }
    });
</script>