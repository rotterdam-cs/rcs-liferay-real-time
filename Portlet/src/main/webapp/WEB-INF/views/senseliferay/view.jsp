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
<portlet:resourceURL var="storeLiferayDataURL" id="storeLiferayData" />

<script type="text/javascript">
    <%--
    var coords;
    function displayLocation(loc) {
        coords = loc.coords;
    }
    function getLocation() {
        navigator.geolocation.getCurrentPosition(displayLocation);
        return coords;
    }
    --%>
    Liferay.on('portletReady', function(event) {            
        if('_' + event.portletId + '_' == '<portlet:namespace/>') {
            jQuery(function () {
                <%--jQuery("#<portlet:namespace/>senseliferaystatus").html("Storing data in Sense...");--%>
                jQuery.get("${storeLiferayDataURL}"
                    ,{
                        "clientlocation" : jQuery.toJSON(google.loader.ClientLocation)
                    }
                    ,function(returned_data) {
                        <%--jQuery("#<portlet:namespace/>senseliferaystatus").removeClass("label-important").addClass("label-success");
                        jQuery("#<portlet:namespace/>senseliferaystatus").html("");--%>
                    }
                );

            });            
        }
    });
</script>
<%--
<div id="<portlet:namespace/>senseliferaystatus" style="text-align: left; width: 150px;"></div>
--%>