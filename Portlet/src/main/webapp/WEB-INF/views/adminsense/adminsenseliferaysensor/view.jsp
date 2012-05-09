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
<portlet:resourceURL var="getAnalyticsBigRangeURL" id="getAnalyticsBigRange" />

<script type="text/javascript">    
<%--//Is very important to load this at the very begining of the javascript--%>
google.load("visualization", "1");     

Liferay.on('portletReady', function(event) {            
    if('_' + event.portletId + '_' == '<portlet:namespace/>') {            
        
        function getTimeline(){
            jQuery("#<portlet:namespace/>containermask").mask('<fmt:message key="com.rcs.sense.general.mask.loading.text"/>');
            jQuery("#<portlet:namespace/>mytimelinecontainer").load("${getAnalyticsBigRangeURL}"
                ,{
                    "range" : jQuery('input:radio[name=<portlet:namespace/>range]:checked').val()
                }
                ,function() {            
                    jQuery("#<portlet:namespace/>containermask").unmask();
                }
            );
        }
        
        jQuery(function () {
            getTimeline();
            jQuery('input:radio[name=<portlet:namespace/>range]').on("change", function(){ getTimeline(); });
        });
    }
});
//function displayLocation(loc) {
//  var locDiv = document.getElementById("locationDiv");
//  locDiv.innerHTML = "lat: " + loc.coords.latitude + ", lon:" + loc.coords.longitude;
//}
//
//function getLocation() {
//    navigator.geolocation.getCurrentPosition(displayLocation);
//}
</script>

<div class="controls">
    <label class="radio">
        <input type="radio" name="<portlet:namespace/>range" value="1" checked>
        View last month report
    </label>
    <label class="radio">
        <input type="radio" name="<portlet:namespace/>range" value="2">
        View last week report
    </label>
    <label class="radio">
        <input type="radio" name="<portlet:namespace/>range" value="3">
        View last day report
    </label>
</div>

<p></p>
<div id="<portlet:namespace/>containermask" style="width: 650px;">
    <div id="<portlet:namespace/>mytimelinecontainer"></div>
    <div id="<portlet:namespace/>mynetworkcontainer"></div>
</div>
<!--<a href="#" onClick="getLocation()">Click here to display location</a><br>
<div id="locationDiv"></div>-->