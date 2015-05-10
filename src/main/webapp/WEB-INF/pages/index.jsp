<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>Human Face Recognition</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="author" content="Oleksandr KOL Kucher">

    <link rel="stylesheet" href="<c:url value="${pageContext.request.contextPath}/css/hfr.css"/>">
    <link rel="stylesheet" href="<c:url value="${pageContext.request.contextPath}/css/dropImage.css"/>">
    <link rel="stylesheet" href="<c:url value="${pageContext.request.contextPath}/bootstrap/css/bootstrap.css"/>">
    <link rel="stylesheet" href="<c:url value="${pageContext.request.contextPath}/bootstrap/css/bootstrap-responsive.css"/>">
    <link rel="stylesheet" href="<c:url value="${pageContext.request.contextPath}/css/cropper/imgareaselect-default.css"/>"/>

    <script src="<c:url value="${pageContext.request.contextPath}/js/jquery-2.1.1.js"/>" type="text/javascript"></script>
    <script src="<c:url value="${pageContext.request.contextPath}/bootstrap/js/bootstrap.js"/>" type="text/javascript"></script>
    <script src="<c:url value="${pageContext.request.contextPath}/js/jquery.imgareaselect.js"/>" type="text/javascript"></script>
    <script src="<c:url value="${pageContext.request.contextPath}/js/cropImage.js"/>" type="text/javascript"></script>
    <script src="<c:url value="${pageContext.request.contextPath}/js/dropImage.js"/>" type="text/javascript"></script>
    <script src="<c:url value="${pageContext.request.contextPath}/js/settingsJson.js"/>" type="text/javascript"></script>
    <script src="<c:url value="${pageContext.request.contextPath}/js/settingsBuilder.js"/>" type="text/javascript"></script>
    <script src="<c:url value="${pageContext.request.contextPath}/js/hfr.js"/>" type="text/javascript"></script>
</head>
<body onload="hfr.init(${width}, ${height}, '${recognitionType}');">
    <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container-fluid">
                <button type="button" class="btn btn-navbar collapsed" data-toggle="collapse" data-target=".nav-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="brand">Course Work</a>
                <div class="nav-collapse collapse">
                    <ul class="nav">
                        <li><a href="mailto:olexandr.kucher@gmail.com">Contact Oleksandr Kucher</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="span3">
                <div class="well sidebar-nav">
                    <ul class="nav nav-list recognition-algorithms"></ul>
                    <ul class="nav nav-list recognition-settings"></ul>
                </div>
            </div>
            <div class="span9">
                <div class="hero-unit" style="padding-bottom: 10px; padding-top: 10px;">
                    <h3 class="text-center">Human Face Recognition</h3>
                </div>
                <div class="row-fluid">
                    <div class="row-fluid">
                        <div class="row-fluid">
                            <div id="dragAndDrop" class="span thumbnail centered">
                                <div class="dragAndDropContainer"></div>
                            </div>
                        </div>
                        <div class="row-fluid" style="margin-top: 5px;">
                            <div id="crop" class="span6 thumbnail centered" style="min-height: 194px;">
                                <div class="cropContainer"></div>
                            </div>
                            <div id="view" class="span6 centered">
                                <div class="span thumbnail" style="min-height: 194px;">
                                    <div class="centered">
                                        <img src="${pageContext.request.contextPath}/images/face.bmp" id="cropped" class="stored-image" style="display: none;">
                                    </div>
                                    <div id="classify-block" style="display: none;">
                                        <div style="margin-top: 5px;" class="info-text">If image is fine, click 'Classify' button</div>
                                        <div class="centered">
                                            <input type="button" value="Classify" class="btn btn-success classify-button" id="classify-button">
                                            <input type="button" value="Show Eigen/Training" class="btn btn-success classify-button" id="eigen-button">
                                        </div>
                                    </div>
                                </div>
                                <div class="span thumbnail" id="results" style="margin: 5px 0 0 0; display: none;">
                                    <button type="button" class="close">x</button>
                                    <div class="info-text">Images from class, which was selected as native for your face</div>
                                    <div class="centered each"></div>
                                </div>
                                <div class="span thumbnail" id="hashImages" style="margin: 5px 0 0 0; display: none;">
                                    <button type="button" class="close">x</button>
                                    <div class="info-text">Hash Image</div>
                                    <div class="centered each"></div>
                                </div>
                                <div class="span thumbnail" id="eigenvectors" style="margin: 5px 0 0 0; display: none;">
                                    <button type="button" class="close">x</button>
                                    <div class="info-text">Eigen Vectors (Eigen Faces for PCA, Fisher Faces for LDA, Laplacian Faces for LPP or training images for PHash)</div>
                                    <div class="centered each"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row-fluid" style="margin-top: 5px;">
                        <div id="dragAndDropDup" class="span thumbnail centered">
                            <div class="dragAndDropContainer"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <hr>
        <div class="row-fluid">
            <div class="span12" style="margin-top: 5px;">
                <div class="accordion">
                    <div class="accordion-group">
                        <div class="accordion-heading">
                            <a class="accordion-toggle centered" data-toggle="collapse" href="#collapse">
                                Stored Images for Testing
                            </a>
                        </div>
                        <div id="collapse" class="accordion-body collapse">
                            <div id="faces" class="navbar navbar-static">
                                <div class="navbar-inner">
                                    <div class="container" style="width: auto;">
                                        <ul class="nav centered" id="stored-classes"></ul>
                                    </div>
                                </div>
                            </div>
                            <div data-spy="scroll" data-target="#faces" data-offset="0" class="scrollspy-example" id="stored-images" style="overflow-y: scroll; max-height: 500px;"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="footer">
        <div class="container">
            <p>Powered by <a href="http://getbootstrap.com">Twitter Bootstrap</a></p>
            <p>Author <a href="mailto:olexandr.kucher@gmail.com">Oleksandr KOL Kucher</a></p>
            <p>Code licensed under <a href="http://www.apache.org/licenses/LICENSE-2.0" target="_blank">Apache License v2.0</a>, documentation under <a href="http://creativecommons.org/licenses/by/3.0/">CC BY 3.0</a>.</p>
        </div>
    </div>
    <div id="processingLabel" style="float: left; display: none; margin-top: -5px; margin-bottom: -8px;">
        <div class="processingLabel"><img src="${pageContext.request.contextPath}/images/globe64.gif" align="absmiddle"/></div>
    </div>
</body>
</html>