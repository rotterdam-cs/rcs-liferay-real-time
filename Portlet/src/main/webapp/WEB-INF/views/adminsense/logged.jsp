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
<span class="label" id="<portlet:namespace/>loggedinfo"><c:out value="${senseLoggedMessage}" escapeXml="false" /></span>

<script type="text/javascript">
    jQuery(function() {
        <c:if test="${logged == true}" >
            jQuery("#<portlet:namespace/>loggedinfo").addClass("label-success");
        </c:if>
        <c:if test="${logged == false}" >
            jQuery("#<portlet:namespace/>loggedinfo").addClass("label-important");
        </c:if>
    });
</script>