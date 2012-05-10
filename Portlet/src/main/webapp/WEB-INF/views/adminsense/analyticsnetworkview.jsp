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

<script type="text/javascript">
    var nodesTable = null;
    var linksTable = null;
    var network = null;

    function drawVisualization<portlet:namespace/>() {
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
        <c:forEach items="${pages}" var="row">
            nodesTable.addRow([
                     ${row.id}
                    ,'${row.page}<c:if test="${row.currentvisitors > 0}" > \n <fmt:message key="com.rcs.sense.admin.analytics.current"/>: ${row.currentvisitors}</c:if>'
                    ,'<c:if test="${row.id == 1}" >circle</c:if><c:if test="${row.id != 1}" >rect</c:if>'
                    ,<c:if test="${row.current == true}" >'current'</c:if><c:if test="${row.current != true}" >'common'</c:if>
                    ,'<a class="various" data-fancybox-type="iframe" href="${row.url}" ><fmt:message key="com.rcs.sense.admin.analytics.page.preview"/></a><br /><fmt:message key="com.rcs.sense.admin.analytics.total.views"/>: ${row.visits}<br />${row.usersInPageInfo}'
                    ,undefined
                    ,undefined
                ]);
        </c:forEach>

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

        var n = 0;
        var action = 'create';
        var color = undefined;
        var stepCount = 10;
        var second = ${stepSeconds};
        <c:forEach items="${liferaySensorsData}" var="row">
            var t = new Date(${row.timestamp});          
            var title = 'node ' + n;
            var duration = undefined;
            var packageFrom = ${row.previous_pageId};
            var packageTo = ${row.pageId};
            if (packageFrom == packageTo) { packageFrom = 0; }
            var c = 0;
            while (c < stepCount + 1) {
                    var progress = c / stepCount;
                    var action = 'create';
                    var title = '<b>' + n + '</b> ' + Math.round(progress * 100) + '%<br />' + '<span style="color:gray;">(' + t.getTime() + ')</span><br /> ${row.browser}';                
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
                        ,'${pageContext.request.contextPath}/img/${row.browser}.png'
                    ]);                
                    c += 1;
                    t = new Date(t.getTime() + second);
            }
            packagesTable.addRow([n, packageFrom, packageTo, undefined, progress, 'delete', new Date(t), duration,'image',undefined]);
            n++;
        </c:forEach>

        options = {
            height: '550px'        
            ,stabilize: false
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
    }); 
</script>

<div><strong><fmt:message key="com.rcs.sense.admin.analytics.detailed.report"/>: </strong></div>
<div id="<portlet:namespace/>mynetwork" class="sense-graphic-container sense-graphic-container-white"></div>