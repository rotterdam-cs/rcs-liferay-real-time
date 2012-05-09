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
 
<div class="sense-logo">
    <img src="/${pageContext.request.contextPath}/img/SENSELogo.jpeg" width="100px">
</div>
<div style="clear: both;"></div>

<ul class="nav nav-list">                    
    <c:if test="${isSenseAdmin == true}" >
        <li class="divider"></li>
        <li>
            <a href="#<%=Constants.ADMIN_SECTION_ANALYTICS%>" data-toggle="tab" rel="sense-admin-menu-analytics">
                <i class="icon-th-list"></i><fmt:message key="com.rcs.sense.admin.analytics"/>                            
            </a>
        </li>
        <li>
            <a href="#<%=Constants.ADMIN_SECTION_GLOBAL_SETTINGS%>" data-toggle="tab" rel="sense-admin-menu-general-settings">
                <i class="icon-share"></i><fmt:message key="com.rcs.sense.admin.global.settings"/>                           
            </a>
        </li> 
    </c:if>
                   
</ul>