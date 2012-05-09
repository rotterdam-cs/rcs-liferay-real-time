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
<portlet:resourceURL var="getAnalyticsRangeURL" id="getAnalyticsRange" />

<script type="text/javascript">    

var startTime = "${fromDate}";
var endTime = "${toDate}";
var loading = false;
// Called when the Visualization API is loaded.
function drawVisualizationimeline<portlet:namespace/>() {
    // Create and populate a data table.
    var data = new google.visualization.DataTable();
    data.addColumn('datetime', 'start');
    data.addColumn('datetime', 'end');
    data.addColumn('string', 'content');
    data.addColumn('string', 'group');

    data.addRows([
        <c:forEach items="${liferaySensorsData}" var="row">
            [new Date(${row.timestamp}), ,'${row.pageCounter}', '${row.page}'],
        </c:forEach>
        [new Date(), ,undefined, undefined],
    ]);

    // specify options
    var options = {
        "width":  "650px" 
        //,"height": "300px"
        ,"style": "box"
    };

    // Instantiate our timeline object.
    timeline = new links.Timeline(document.getElementById('<portlet:namespace/>mytimeline'));

    // Attach event listeners
    google.visualization.events.addListener(timeline, 'select', onselect);
    google.visualization.events.addListener(timeline, 'rangechange', onrangechange);  
    google.visualization.events.addListener(timeline, 'rangechanged', onrangechanged);      

    // Draw our timeline with the created data and options 
    timeline.draw(data, options);

    // Set the scale by hand. Autoscaling will be disabled.
    // Note: you can achieve the same by specifying scale and step in the
    // options for the timeline.
    timeline.setScale(links.Timeline.StepDate.SCALE.DAY, 1);
}

function onrangechange() {
    // adjust the values of startDate and endDate
    var range = timeline.getVisibleChartRange();
    jQuery('#<portlet:namespace/>startDate').html("Start Time: <b>" + dateFormat(range.start) + " </b> ");
    jQuery('#<portlet:namespace/>endDate').html(" &nbsp; - &nbsp; End Time: <b>" + dateFormat(range.end) + "</b>");
    startTime = range.start;
    endTime = range.end;
}
function onrangechangeddelayed() {   
   var newStartDate = new Date(startTime);
   var newEndDate   = new Date(endTime);
//   jQuery("#<portlet:namespace/>mynetworkcontainer").load("${getAnalyticsRangeURL}"
//        ,{
//            "startTime" : newStartDate.getTime()
//            ,"endTime" : newEndDate.getTime()
//        }
//        ,function() {            
//            jQuery("#<portlet:namespace/>mynetworkcontainer").unmask();
//            loading = false;
//        }
//    );
}
function onrangechanged() {    
    if (!loading) {
        jQuery("#<portlet:namespace/>mynetworkcontainer").mask('<fmt:message key="com.rcs.sense.general.mask.loading.text"/>');
        loading = true;
        setTimeout(onrangechangeddelayed, 1000);
    }
}

// Format given date as "yyyy-mm-dd hh:ii:ss"
// @param datetime   A Date object.
function dateFormat(date) {
    datetime =   date.getFullYear() + "-" + 
    ((date.getMonth()   <  9) ? "0" : "") + (date.getMonth() + 1) + "-" +
    ((date.getDate()    < 10) ? "0" : "") +  date.getDate() + " " +
    ((date.getHours()   < 10) ? "0" : "") +  date.getHours() + ":" + 
    ((date.getMinutes() < 10) ? "0" : "") +  date.getMinutes() + ":" + 
    ((date.getSeconds() < 10) ? "0" : "") +  date.getSeconds()
    return datetime;
}


          
jQuery(function () {
    jQuery("#<portlet:namespace/>mynetworkcontainer").mask('<fmt:message key="com.rcs.sense.general.mask.loading.text"/>');
    drawVisualizationimeline<portlet:namespace/>();
    onrangechangeddelayed();         
});
    
</script>

<div>
    <strong>General report: </strong>    
    <span id="<portlet:namespace/>endDate" style="float: right;"></span>
    <span id="<portlet:namespace/>startDate" style="float: right;"></span>
</div>
<div id="<portlet:namespace/>mytimeline" style="background-color: #FFFFFF; width: 650px;"></div>
