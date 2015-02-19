$(document).ready(function() {
    init(92, 112);
});

function img(src, styleClass){
    return $('<img/>').addClass(styleClass).attr("src", src);
}

function init(width, height){
    var dropParameters = {
        okWidth: width,
        okHeight: height,
        cropLink: "./picture/crop",
        parentCropContainerId: "crop",
        uploadLink: "./picture/upload",
        cropContainer: 'cropContainer',
        aspectRatio: width + ':' + height,
        cropSuccess: function (resp) {
            var data = $.parseJSON(resp);
            $("#cropped").attr("src", data.src + "?t=" + Date.now()).attr("file-id", data.fileId).show();
            $("#classify-block").show();
        }
    };
    $("#dragAndDrop").find(".dragAndDropContainer").dropImage(dropParameters);
    $("#dragAndDropDup").find(".dragAndDropContainer").dropImage(dropParameters);

    $("button[class='close']").each(function(){
        $(this).click(function(){
            $(this).parent().hide();
        });
    });
    $(".menu").click(function(){
        $(".menu[group='" + $(this).attr("group") + "']").each(function(){
            $(this).parent().removeClass("active");
        }).find(".li-input").each(function(){
            $(this).hide();
        });
        $(this).parent().addClass("active");
        if($(this).has("need-number")){
            $("#" + $(this).attr("enum-name") + "-" + $(this).attr("group")).show();
        }
    });
    $(".menu[group='spc']").each(function(){
        $(this).tooltip();
    });
    $(".menu[group='knn']").each(function(){
        $(this).tooltip();
    });
    $(".menu[group='pca']").each(function(){
        $(this).tooltip();
    });
    $("#classify-button").click(function(){
        $.ajax({
            type: "POST",
            url: "./hfr/classify",
            data: storeData(),
            success: function(resp) {
                var data = $.parseJSON(resp);
                var $res = $("#results");
                $res.find(".each").html("");
                if(data.status === "ok"){
                    for(var i in data.storedImages){
                        $res.find(".each").append(img(data.storedImages[i], "stored-image"));
                    }
                    $res.show();
                } else {
                    $res.hide();
                }
            }
        });
    });
    $("#eigen-button").click(function(){
        $.ajax({
            type: "POST",
            url: "./hfr/eigenVectors",
            data: storeData(),
            success: function(resp) {
                var data = $.parseJSON(resp);
                var $res = $("#eigenvectors");
                $res.find(".each").html("");
                if(data.status === "ok"){
                    for(var i in data.storedImages){
                        $res.find(".each").append(img(data.storedImages[i], "stored-image"));
                    }
                    $res.show();
                } else {
                    $res.hide();
                }
            }
        });
    });
    getAllStoredImages("HMF");
}

function storeData() {
    var metric = null;
    var algorithm = null;
    var trainingImage = null;
    var trainingImageCount = null;
    var knnComponent = null;
    var knnCount = null;
    var pca = null;
    var pcaCount = null;
    var fileId = $("#cropped").attr("file-id");
    $(".menu[group='algorithm']").each(function(){
        if($(this).parent().hasClass("active")){
            algorithm = $(this).attr("enum-name");
        }
    });
    $(".menu[group='metric']").each(function(){
        if($(this).parent().hasClass("active")) {
            metric = $(this).attr("enum-name");
        }
    });
    $(".menu[group='spc']").each(function(){
        if($(this).parent().hasClass("active")) {
            trainingImage = $(this).attr("enum-name");
            trainingImageCount = $("#" + $(this).attr("enum-name") + "-" + $(this).attr("group")).val();
        }
    });
    $(".menu[group='knn']").each(function(){
        if($(this).parent().hasClass("active")) {
            knnComponent = $(this).attr("enum-name");
            knnCount = $("#" + $(this).attr("enum-name") + "-" + $(this).attr("group")).val();
        }
    });
    $(".menu[group='pca']").each(function(){
        if($(this).parent().hasClass("active")) {
            pca = $(this).attr("enum-name");
            pcaCount = $("#" + $(this).attr("enum-name") + "-" + $(this).attr("group")).val();
        }
    });
    return {
        fileId: fileId,
        metric: metric,
        knnCount: knnCount,
        algorithm: algorithm,
        principalComponents: pca,
        knnComponent: knnComponent,
        recognizerTrainType: trainingImage,
        principalComponentsCount: pcaCount,
        trainingImageCount: trainingImageCount
    };
}

function getAllStoredImages(recognitionType){
    $.ajax({
        type: "POST",
        url: "./picture/storedImages",
        data: {
            type: recognitionType
        },
        success: function(resp) {
            var data = $.parseJSON(resp);
            var $collapse = $("#collapse");
            var $storedImages = $collapse.find("#stored-images");
            var $storedClasses = $collapse.find("#stored-classes");
            var ts = Object.keys(data).sort();
            for(var i = 0; i < ts.length; i++){
                var $image = $('<div/>').addClass("accordion-inner centered").attr("id", ts[i]);
                var $class = $('<li><a href="#' + ts[i] + '">' + ts[i] + '</a></li>');
                $image.append($('<div/>').addClass("info-text").html(ts[i]));
                for(var j = 0; j < data[ts[i]].length; j++){
                    $image.append(img(data[ts[i]][j], "stored-image"));
                }
                $storedImages.append($image);
                $storedClasses.append($class);
            }
        }
    });
}