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
<portlet:resourceURL var="getAnalyticsRangeJSONURL" id="getAnalyticsRangeJSON" />

<script type="text/javascript">
    var nodesTable = null;
    var linksTable = null;
    var network = null;
    var second;
//    var selectedPackages = [];

    function drawVisualization<portlet:namespace/>(pages, sensordata, stepSeconds, tmpSliderValue) {
        nodesTable = new google.visualization.DataTable();
        nodesTable.addColumn('number', 'id');
        nodesTable.addColumn('string', 'text');
        nodesTable.addColumn('string', 'style');
        nodesTable.addColumn('string', 'group');
        nodesTable.addColumn('string', 'title');
        nodesTable.addColumn('string', 'action');
        nodesTable.addColumn('datetime', 'timestamp');

        nodesTable.addRow([
             0
            ,''
            ,'text'
            ,undefined
            ,'<fmt:message key="com.rcs.sense.admin.analytics.external.site"/>'
            ,undefined
            ,undefined
        ]);
        
        if (pages == null) {
            <c:forEach items="${pages}" var="row">
                nodesTable.addRow([
                    ${row.id}
                    ,'${row.page} [ ${row.visits} ] <c:if test="${row.currentvisitors > 0}" > \n <fmt:message key="com.rcs.sense.admin.analytics.current"/>: ${row.currentvisitors}</c:if>'
                    ,'<c:if test="${row.id == 1}" >circle</c:if><c:if test="${row.id != 1}" >rect</c:if>'
                    ,<c:if test="${row.current == true}" >'current'</c:if><c:if test="${row.current != true}" >'common'</c:if>
                    ,'<a class="various" data-fancybox-type="iframe" href="${row.url}" ><fmt:message key="com.rcs.sense.admin.analytics.page.preview"/></a><br /><fmt:message key="com.rcs.sense.admin.analytics.total.views"/>: ${row.visits}<br />${row.usersInPageInfo}'
                    ,undefined
                    ,undefined
                ]);
            </c:forEach>
        } else {
            jQuery.each(pages, function(index, row) {
                var id = row.id;
                var text = (row.currentvisitors > 0) ? row.page + ' [ ' + row.visits + ' ]\n <fmt:message key="com.rcs.sense.admin.analytics.current"/>: ' + row.currentvisitors : row.page + ' [ ' + row.visits + ' ]';
                var style = (row.id == 1) ? "circle" : "rect";
                var group = (row.current == true) ? "current" : (row.current != true) ? "common" : "";
                var title = '<a class="various" data-fancybox-type="iframe" href="' + row.url + '" ><fmt:message key="com.rcs.sense.admin.analytics.page.preview"/></a><br /><fmt:message key="com.rcs.sense.admin.analytics.total.views"/>: ' + row.visits + '<br />' + row.UsersInPageInfo + '';               
                nodesTable.addRow([
                     id
                    ,text
                    ,style
                    ,group
                    ,title
                    ,undefined
                    ,undefined
                ]);                
            });
        }

        linksTable = new google.visualization.DataTable();
        linksTable.addColumn('number', 'id');
        linksTable.addColumn('number', 'from');
        linksTable.addColumn('number', 'to');
        linksTable.addColumn('string', 'style');
        linksTable.addColumn('number', 'width');
        linksTable.addColumn('number', 'length');        
        linksTable.addColumn('string', 'action');
        linksTable.addColumn('datetime', 'timestamp');

        var total = 0;
        var linksAdded = new Array();
        if (sensordata == null) {
            <c:forEach items="${liferaySensorsData}" var="row">
                var packageFrom = ${row.previous_pageId};
                var packageTo = ${row.pageId};
                if (packageFrom == packageTo) { packageFrom = 0; }
                if (linksAdded[packageFrom] == null) { linksAdded[packageFrom] =  new Array(); }
                if (linksAdded[packageFrom][packageTo] !== true){
                    linksAdded[packageFrom][packageTo] = true;
                    linksTable.addRow([
                        total
                        ,packageFrom
                        ,packageTo
                        ,undefined
                        ,1
                        ,150
                        ,undefined
                        ,undefined
                    ]);        
                }
                total ++;
            </c:forEach> 
        } else {
            jQuery.each(sensordata, function(index, row) {                
                var packageFrom = row.previous_pageId;
                var packageTo = row.pageId;
                if (packageFrom == packageTo) { packageFrom = 0;}
                if (linksAdded[packageFrom] == null) { linksAdded[packageFrom] =  new Array();}
                if (linksAdded[packageFrom][packageTo] != true) {
                    linksAdded[packageFrom][packageTo] = true;
                    linksTable.addRow([
                        total
                        ,packageFrom
                        ,packageTo
                        ,undefined
                        ,1
                        ,150
                        ,undefined
                        ,undefined
                    ]);        
                }
                total ++;
            });
        }
        packagesTable = new google.visualization.DataTable();
        packagesTable.addColumn('number', 'id');
        packagesTable.addColumn('number', 'from');
        packagesTable.addColumn('number', 'to');
        packagesTable.addColumn('string', 'title');
        packagesTable.addColumn('number', 'progress');
        packagesTable.addColumn('string', 'action');
        packagesTable.addColumn('datetime', 'timestamp'); 
        packagesTable.addColumn('number', 'duration');
        packagesTable.addColumn('string', 'style');
        packagesTable.addColumn('string', 'image');
        packagesTable.addColumn('number', 'uid');       
        
        var n = 0;
        var action = 'create';
        var color = undefined;
        var stepCount = 20;        
        if (sensordata == null) {
            second = ${stepSeconds};
            <c:forEach items="${liferaySensorsData}" var="row">
                var recorederedTime = new Date(${row.timestamp});
                var t = new Date(${row.timestamp});
                var duration = undefined;
                var packageFrom = ${row.previous_pageId};
                var packageTo = ${row.pageId};
                if (packageFrom == packageTo) { packageFrom = 0; }
                var c = 0;
                var uid = ${row.liferayUserId};
                while (c < stepCount + 1) {
                    var progress = c / stepCount;
                    var action = 'create';
                    var title = '${row.liferayUserInformation} <br /> <span style="color:gray;">(' + recorederedTime + ')</span>';
                    packagesTable.addRow([
                         n
                        ,packageFrom
                        ,packageTo
                        ,title
                        ,progress
                        ,action
                        ,new Date(t.getTime())
                        ,duration
                        ,'image'
                        ,'${pageContext.request.contextPath}/img/${row.browser}.png'
                        ,uid
                    ]);                
                    c += 1;
                    t = new Date(t.getTime() + second);
                }
                packagesTable.addRow([n, packageFrom, packageTo, undefined, progress, 'delete', new Date(t), duration,'image',undefined,uid]);                
                n++;
            </c:forEach>
        } else {
            second = stepSeconds;
            jQuery.each(sensordata, function(index, row) {
                var recorederedTime = new Date(row.timestamp);
                var t = new Date(row.timestamp);
                var duration = undefined;
                var packageFrom = row.previous_pageId;
                var packageTo = row.pageId;
                if (packageFrom == packageTo) { packageFrom = 0; }
                var c = 0;
                var uid = row.liferayUserId;                
                while (c < stepCount + 1) {
                    //var isselected = (selectedPackages.indexOf(uid) != -1) ? true : false;
                    var progress = c / stepCount;
                    var action = 'create';                    
                    var title = row.liferayUserInformation + ' <br /> <span style="color:gray;">(' + recorederedTime + ')</span>';
                    var browser = row.browser;
                    if (n < 1 && c == 0) { browser = 'empty'; }                    
                    packagesTable.addRow([
                         n
                        ,packageFrom
                        ,packageTo
                        ,title
                        ,progress
                        ,action
                        ,new Date(t)
                        ,duration
                        ,'image'
                        ,'${pageContext.request.contextPath}/img/' + browser + '.png'
                        ,uid
                    ]);                
                    c += 1;
                    t = new Date(t.getTime() + second);
                }
                packagesTable.addRow([n, packageFrom, packageTo, undefined, progress, 'delete', new Date(t), duration,'image',undefined,uid]);
                n++;
            });
        }
        <%--//Empty package to make sure the animation is always shown --%>
        packagesTable.addRow([n+1, 0, 0, undefined, 0, "create", new Date(), undefined,'image','${pageContext.request.contextPath}/img/empty.png',undefined]);
        
        options = {
            height: '550px'        
            ,stabilize: true
            ,'groups': {            
                'current': {
                    'backgroundColor': 'blue'
                    ,'fontColor': 'white'
                    ,'fontSize': 11
                }
            ,'common': {
                    'fontSize': 11
                }
            }
        };
        
        network = new links.Network(document.getElementById('<portlet:namespace/>mynetwork'));
        network.draw(nodesTable, linksTable, packagesTable, options);
        setTimeout("startAnimation<portlet:namespace/>(" + tmpSliderValue + ")", 1000);
//        links.Network.removeEventListener(document, "mouseup", sincronyzeSelection);
//        links.Network.addEventListener(document, "mouseup", sincronyzeSelection);
    }
    
//    function sincronyzeSelection() {
//        selectedPackages = network.selectedPackages;
//    }
    
    function startAnimation<portlet:namespace/>(tmpSliderValue) {
        if (tmpSliderValue != null) {            
            network.slider.value = tmpSliderValue;            
        }        
        network.animationStart();        
    }
    
    function reloadAnalyticsData<portlet:namespace/>() {
        var selectedRange = jQuery('input:radio[name=<portlet:namespace/>range]:checked').val();        
        if (network.slider != null && network.slider.value == network.slider.end && selectedRange == <%=TimelineRange.REAL_TIME.getId()%>) {            
            var newStartDate = new Date(startTime);
            var newEndDate   = new Date(endTime);
            var nowDate   = new Date();
            jQuery.get("${getAnalyticsRangeJSONURL}"
                ,{
                    "startTime" : newStartDate.getTime()                
                    ,"endTime" : nowDate.getTime()
                }
                ,function(returned_data) {
                    rows = jQuery.parseJSON(returned_data);
//                    sincronyzeSelection();
                    drawVisualization<portlet:namespace/>(rows.pages, rows.liferaySensorsData, rows.stepSeconds, network.slider.value);
                    reloadTimeline<portlet:namespace/>();
                }
            );
        }
        clearTimeout(runningProcess);        
        runningProcess = setTimeout("reloadAnalyticsData<portlet:namespace/>()", ${autoReloadTime * 1000});        
    }    
    
    jQuery(".various").fancybox({
            maxWidth	: 800,
            maxHeight	: 600,
            fitToView	: false,
            width	: '70%',
            height	: '70%',
            autoSize	: false,
            closeClick	: false,
            openEffect	: 'none',
            closeEffect	: 'none'
    });
    
    jQuery(function () {
        drawVisualization<portlet:namespace/>();
        clearTimeout(runningProcess);
        reloadAnalyticsData<portlet:namespace/>();        
    }); 
</script>

<div><strong><fmt:message key="com.rcs.sense.admin.analytics.detailed.report"/>: </strong></div>
<div id="<portlet:namespace/>mynetwork" class="sense-graphic-container sense-graphic-container-white"></div>