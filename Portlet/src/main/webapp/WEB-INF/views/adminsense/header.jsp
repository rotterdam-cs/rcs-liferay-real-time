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

<div class="taglib-header "> 
    <span class="header-back-to"> 
        <a data-toggle="modal" href="#<portlet:namespace/>helpWindow"><i class="icon-question-sign"></i></a> 
    </span> 
    <h1 class="header-title">
        <span>            
            <span class="hidden" id="<portlet:namespace/>sense-admin-menu-analytics"><i class="icon-th-list"></i> <fmt:message key="com.rcs.sense.admin.analytics"/></span>
            <span class="hidden" id="<portlet:namespace/>sense-admin-menu-general-settings"><i class="icon-share"></i> <fmt:message key="com.rcs.sense.admin.global.settings"/></span>
        </span>
    </h1> 
</div>