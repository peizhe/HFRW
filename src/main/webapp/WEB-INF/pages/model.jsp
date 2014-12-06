<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>Human Face Recognition</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="author" content="Oleksandr KOL Kucher">

    <script src="/js/jquery-2.1.1.js"></script>
    <script src="/js/jquery.imgareaselect.js"></script>
    <script src="/bootstrap/js/bootstrap.js"></script>
    <link rel="stylesheet" href="/css/dropImage.css">
    <link rel="stylesheet" href="/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="/bootstrap/css/bootstrap-responsive.css">
    <link rel="stylesheet" href="/css/cropper/imgareaselect-default.css"/>
    <script src="${pageContext.request.contextPath}/js/logoPicture/cropImage.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/logoPicture/dropImage.js" type="text/javascript"></script>

    <style type="text/css">
        body {
            padding-top: 50px;
            padding-bottom: 20px;
        }
        .sidebar-nav {
            padding: 9px 0;
        }
        p {text-indent: 20px;}
        .footer {
            text-align: center;
            padding: 30px 0;
            margin-top: 70px;
            border-top: 1px solid #e5e5e5;
            background-color: #f5f5f5;
        }
    </style>
    <script>
        function init(){
            $("#dragAndDropContainer").dropImage({
                okWidth: 92,
                okHeight: 0,
                cropLink: "/picture/crop",
                uploadLink: "/picture/upload",
                cropContainerId: 'cropContainer'
            });
        }
    </script>
</head>
<body onload="init();">
    <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container-fluid">
                <button type="button" class="btn btn-navbar collapsed" data-toggle="collapse" data-target=".nav-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="brand">Human Face Recognition</a>
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
                        <li class="active"><a href="#" group="algorithm" enum-name="PCA" class="menu active">PCA</a></li>
                        <li><a href="#" group="algorithm" enum-name="LDA" class="menu">LDA</a></li>
                        <li class="nav-header">Metrics</li>
                        <li class="active"><a href="#" group="metric" enum-name="EUCLIDEAN" class="menu">Euclidean</a></li>
                        <li><a href="#" group="metric" enum-name="COSINE" class="menu">Cosine</a></li>
                        <li><a href="#" group="metric" enum-name="L1D" class="menu">Linear</a></li>
                    </ul>
                </div>
            </div>
            <div class="span9">
                <div class="hero-unit" style="padding-bottom: 10px; padding-top: 10px;">
                    <h2 class="text-center">Course Work</h2>
                    <h3 class="text-center">Human Face Recognition</h3>
                </div>
                <div class="row-fluid">
                    <div class="row-fluid">
                        <div class="row-fluid">
                            <div id="dragAndDrop" class="span thumbnail" style="padding-left: 45%;">
                                <div id="dragAndDropContainer"></div>
                            </div>
                        </div>
                        <div class="row-fluid" style="margin-top: 5px;">
                            <div id="crop" class="span thumbnail" style="padding-left: 45%; min-height: 200px;">
                                <div id="cropContainer"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <hr>
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