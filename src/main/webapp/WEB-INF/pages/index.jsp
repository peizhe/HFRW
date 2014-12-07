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
    <script src="<c:url value="${pageContext.request.contextPath}/js/hfr.js"/>" type="text/javascript"></script>
</head>
<body>
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
                    <ul class="nav nav-list">
                        <li class="nav-header">Algorithms</li>
                        <li class="active"><a href="#" group="algorithm" enum-name="PCA" class="menu">PCA</a></li>
                        <li><a href="#" group="algorithm" enum-name="LDA" class="menu">LDA</a></li>
                        <li class="nav-header">Metrics</li>
                        <li class="active"><a href="#" group="metric" enum-name="EUCLIDEAN" class="menu">Euclidean</a></li>
                        <li><a href="#" group="metric" enum-name="COSINE" class="menu">Cosine</a></li>
                        <li><a href="#" group="metric" enum-name="L1D" class="menu">Linear</a></li>
                        <li class="nav-header">Settings [Choose Training Images]</li>
                        <li class="active">
                            <a href="#" group="spc" enum-name="ALL" class="menu" data-toggle="tooltip" data-placement="right" data-original-title="Uses all exist images with faces">Use ALL Images</a>
                        </li>
                        <li>
                            <a href="#" group="spc" enum-name="FIRST" class="menu" need-number data-toggle="tooltip" data-placement="right" data-original-title="Uses first n exist images for each class">
                                Use FIRST images
                                <input type="text" id="FIRST-spc" placeholder="--> 5" class="li-input" style="display: none;">
                            </a>
                        </li>
                        <li>
                            <a href="#" group="spc" enum-name="RANDOM" class="menu" need-number data-toggle="tooltip" data-placement="right" data-original-title="Uses n randomly selected images for each class">
                                Use RANDOM images
                                <input type="text" id="RANDOM-spc" placeholder="--> 5" class="li-input" style="display: none;">
                            </a>
                        </li>
                        <li class="nav-header">Settings [KNN]</li>
                        <li class="active">
                            <a href="#" group="knn" enum-name="DEFAULT" class="menu" data-toggle="tooltip" data-placement="right" data-original-title="Uses DEFAULT number of Nearest Neighbor components from .properties file">Use DEFAULT number</a>
                        </li>
                        <li>
                            <a href="#" group="knn" enum-name="CUSTOM" class="menu" need-number data-toggle="tooltip" data-placement="right" data-original-title="Uses YOUR number of Nearest Neighbor components">
                                Use CUSTOM number
                                <input type="text" id="CUSTOM-knn" placeholder="--> 2" class="li-input" style="display: none;">
                            </a>
                        </li>
                        <li class="nav-header">Settings [Principal Components]</li>
                        <li class="active">
                            <a href="#" group="pca" enum-name="DEFAULT" class="menu" data-toggle="tooltip" data-placement="right" data-original-title="Uses DEFAULT number of Principal Components from .properties file">Use DEFAULT number</a>
                        </li>
                        <li>
                            <a href="#" group="pca" enum-name="CUSTOM" class="menu" need-number data-toggle="tooltip" data-placement="right" data-original-title="Uses YOUR number of Principal Components">
                                Use CUSTOM number
                                <input type="text" id="CUSTOM-pca" placeholder="--> 40" class="li-input" style="display: none;">
                            </a>
                        </li>
                    </ul>
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
                                <div id="dragAndDropContainer"></div>
                            </div>
                        </div>
                        <div class="row-fluid" style="margin-top: 5px;">
                            <div id="crop" class="span6 thumbnail centered" style="min-height: 194px;">
                                <div id="cropContainer"></div>
                            </div>
                            <div id="view" class="span6 centered">
                                <div class="span thumbnail" style="min-height: 194px;">
                                    <div class="centered">
                                        <img src="/images/face.bmp" id="cropped" class="stored-image">
                                    </div>
                                    <div id="classify-block" style="display: none;">
                                        <div style="margin-top: 5px;" class="info-text">If image is fine, click 'Classify' button</div>
                                        <div class="centered">
                                            <input type="button" value="Classify" class="btn btn-success classify-button" id="classify-button">
                                            <input type="button" value="Show Eigen" class="btn btn-success classify-button" id="eigen-button">
                                        </div>
                                    </div>
                                </div>
                                <div class="span thumbnail" id="results" style="margin: 5px 0 0 0; display: none;">
                                    <button type="button" class="close" data-dismiss="alert">x</button>
                                    <div class="info-text">Images from class, which was selected as native for your face</div>
                                    <div class="centered each"></div>
                                </div>
                                <div class="span thumbnail" id="eigenvectors" style="margin: 5px 0 0 0; display: none;">
                                    <button type="button" class="close" data-dismiss="alert">x</button>
                                    <div class="info-text">Eigen Vectors (EigenFaces for PCA or Fisher Faces fo LDA)</div>
                                    <div class="centered each"></div>
                                </div>
                            </div>
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
    <div id="processingLabel" style="float: left; display: none; width: 100px; margin-top: -5px; margin-bottom: -8px;">
        <center>
            <div class="processingLabel"><img src="${pageContext.request.contextPath}/images/loading.gif" align="absmiddle"/></div>
        </center>
    </div>
</body>
</html>