<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>Human Face Recognition</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="author" content="Oleksandr KOL Kucher">

    <script src="/js/jquery-2.1.1.js"></script>
    <script src="/bootstrap/js/bootstrap.js"></script>
    <link rel="stylesheet" href="/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="/bootstrap/css/bootstrap-responsive.css">

    <style type="text/css">
        body {
            padding-top: 50px;
            padding-bottom: 20px;
        }
        .sidebar-nav {
            padding: 9px 0;
        }
        .docs {
            padding-left: 5%;
            padding-right: 15%;
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
                    <div class="row-fluid" style="text-align: justify;">
                        <div id="main" class="span10 thumbnail docs" style="width: 100%; margin-left: 0; min-height: 293px;">
                            <h4 class="text-center">Черкаський національний університет імені Богдана Хмельницького</h4>
                            <h4 class="text-center">ННІ фізики, математики та комп'ютерно-інформаційних систем</h4>
                            <h4 class="text-center">Кафедра прикладної математики та інформатики</h4>
                            <p><b>Дисципліна:</b> Методика та організація наукових досліджень</p>
                            <p><b>Спеціальність:</b> Прикладна математика</p>
                            <p><b>Група:</b> МПМ1</p>
                            <p><b>Навчальний рік:</b> 2014-2015</p>
                            <p><b>Лабораторні роботи виконав: </b> Олександр Кучер</p>
                            <p><b>Викладач: </b> Красношлик Наталія Олександрівна</p>
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
</body>
</html>