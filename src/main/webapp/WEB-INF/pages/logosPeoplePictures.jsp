<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title></title>
    <link href="${pageContext.request.contextPath}/css/cropper/imgareaselect-default.css" rel="stylesheet" type="text/css" />

    <script src="${pageContext.request.contextPath}/js/jquery-2.1.1.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/jquery-ui.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/jquery.imgareaselect.js" type="text/javascript"></script>

    <script src="${pageContext.request.contextPath}/js/logoPicture/cropImage.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/logoPicture/dropImage.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/logoPicture/logoPicture.js" type="text/javascript"></script>
</head>
<body class="contentBody" onload="logoPicture.initializePicturePage();">
<div class="body_top_bg"></div>
<div class="under_header_shadow"></div>
<div id="maincontainer">
    <table border="0" align="center" cellpadding="0" cellspacing="0"><tr><td>
        <div style="display: block;">
            <table border="0" cellpadding="0" cellspacing="0" width="960"><tr valign="top"><td>
                <table class="area_border" width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td>
                    <table class="data_field_row" width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td>
                        <div id="topBlock" class="logoOneBlock">
                            <div style="float: left; width: 100%;">
                                <div style="float: left; width: 100px;">Entity Name:</div>
                                <div style="float: left; width: 800px; font-weight: bold;">{entity.entityName}</div>
                            </div>
                        </div>
                    </td></tr></table>
                </td></tr><tr><td>
                    <table class="data_field_row" width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td>
                        <div id="entityLogoBlock" class="logoOneBlock">
                            <input type="hidden" id="entityId" value="{entity.entityId}">
                            <input type="hidden" id="pictureId" value="<c:if test="{null != picture}">{picture.entityId}</c:if>">
                            <div style="float: left; width: 100%;">
                                <div style="float: left; width: 70px; font-weight: bold; padding-bottom: 5px;">Logo</div>
                                <c:forEach items="{entity.businessEntityDomains}" var="item">
                                    <div style="float: left; padding-right: 10px;">
                                        <a class="link" style="font-size: 14px; margin-top: 0;" target="_blank" href="http://{item.value}">{item.value}</a>
                                    </div>
                                </c:forEach>
                                <div style="float: left; padding-right: 10px;">
                                    <a class="link" style="font-size: 14px; margin-top: 0;" target="_blank" href="https://www.google.com/images?q={entity.entityName}">Search in Google</a>
                                </div>
                            </div>
                            <div style="float: left; width: 100%;">
                                <div id="dragAndDropContainer" style="float: left; width: 125px;"></div>
                                <div id="cropContainer" style="float: left; width: 780px;" class="cropContainer"></div>
                            </div>
                            <div style="float: left; width: 100%; margin-top: 10px;">
                                <div style="float: left; width: 780px; margin-left: 135px;" id="prototypes"></div>
                            </div>
                        </div>
                    </td></tr></table>
                </td></tr><tr><td>
                    <table class="data_field_row" width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td>
                        <div id="personLogoBlock" style="float: left;">
                            <div style="float: left; width: 100%;">
                                <div style="float: left; width: 100%;">
                                    <div style="float: left; width: 70px; font-weight: bold; padding-bottom: 5px; font-size: 14px;">People</div>
                                    <c:forEach items="{crawlLinks}" var="link">
                                        <div style="float: left; padding-right: 10px;">
                                            <a class="link" style="font-size: 14px; margin-top: 0;" target="_blank" href="{link.link}">{link.link}</a>
                                        </div>
                                    </c:forEach>
                                </div>
                                <div style="float: left; width: 350px; padding-left: 10px; font-size: 14px; line-height: 20px;">
                                    <label>
                                        <input type="radio" name="currentPriorityOnly" class="currentPriorityOnly" value="0">Current Only
                                    </label>
                                    <label style="padding-left: 10px;">
                                        <input type="radio" name="currentPriorityOnly" class="currentPriorityOnly" value="1">Show Priority People Only
                                    </label>
                                </div>
                                <div style="float: right; margin-right: 30px;">
                                    <input type="button" id="giveMe20" value="Give Me 20" class="buttonView">
                                </div>
                            </div>
                            <div id="persons" style="margin-top: 20px; float: left; width: 100%;"></div>
                        </div>
                    </td></tr></table>
                </td></tr></table>
            </td></tr></table>
        </div>
    </td></tr></table>
</div>
<%@ include file="logoPictureTemplates.jsp"%>
</body>
</html>