<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8" 
    import="com.rcs.liferaysense.common.Constants"
    import="com.rcs.liferaysense.entities.enums.TimelineRange"
%>
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
<portlet:resourceURL var="getAnalyticsBigRangeURL" id="getAnalyticsBigRange" /> 

<div class="modal hide fade" id="<portlet:namespace/>helpWindow">
    <div class="modal-header">
        <a class="close" data-dismiss="modal">x</a>
        <h3><i class="icon-question-sign"></i> <fmt:message key="com.rcs.sense.admin.help.center"/> - <fmt:message key="com.rcs.sense.admin.global.settings"/> <span style="float: right;"><img src="${pageContext.request.contextPath}/img/SENSELogo.jpeg" width="100px"></span></h3>
    </div>
    <div class="modal-body">
        <p><fmt:message key="com.rcs.sense.admin.help.analytics.settings1"/></p>
        <p><fmt:message key="com.rcs.sense.admin.help.analytics.settings2"/></p>
    </div>
    <div class="modal-footer">
        <a href="#" class="btn" data-dismiss="modal"><fmt:message key="com.rcs.sense.general.close"/></a>
    </div>
</div>
<div class="controls">
    <div class="sense-admin-range">
        <label class="radio">
            <input type="radio" name="<portlet:namespace/>range" value="<%=TimelineRange.LAST_HOUR.getId()%>" checked>
            <fmt:message key="com.rcs.sense.admin.analytics.report.range.last.hour"/>
        </label>
        <label class="radio">
            <input type="radio" name="<portlet:namespace/>range" value="<%=TimelineRange.LAST_DAY.getId()%>">
            <fmt:message key="com.rcs.sense.admin.analytics.report.range.last.day"/>
        </label>
    </div>
    <div class="sense-admin-range">
        <label class="radio">
            <input type="radio" name="<portlet:namespace/>range" value="<%=TimelineRange.LAST_WEEK.getId()%>">
            <fmt:message key="com.rcs.sense.admin.analytics.report.range.last.week"/>
        </label>
        <label class="radio">
            <input type="radio" name="<portlet:namespace/>range" value="<%=TimelineRange.LAST_MONTH.getId()%>">
            <fmt:message key="com.rcs.sense.admin.analytics.report.range.last.month"/>
        </label>
    </div>
    <div style="clear: both;"></div>
</div>

<p></p>
<div id="<portlet:namespace/>containermask" class="sense-graphic-container">
    <div id="<portlet:namespace/>mytimelinecontainer"></div>
    <div id="<portlet:namespace/>mynetworkcontainer"></div>
</div>

<script type="text/javascript">    
    function getTimeline() {
        jQuery("#<portlet:namespace/>administration-container-mask").mask('<fmt:message key="com.rcs.sense.general.mask.loading.text"/>');
        jQuery("#<portlet:namespace/>mytimelinecontainer").load("${getAnalyticsBigRangeURL}"
            ,{
                "range" : jQuery('input:radio[name=<portlet:namespace/>range]:checked').val()
            }
            ,function() {            
                jQuery("#<portlet:namespace/>administration-container-mask").unmask();
            }
        );
    }
    jQuery(function () {
        setTimeout(getTimeline, 1000);
        jQuery('input:radio[name=<portlet:namespace/>range]').on("change", function(){ getTimeline(); });
    });
</script>