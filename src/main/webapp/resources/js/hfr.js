$(document).ready(init);

function img(src, styleClass){
    return $('<img/>').addClass(styleClass).attr("src", src);
}
function init(){
    $("#dragAndDropContainer").dropImage({
        okWidth: 92,
        okHeight: 112,
        aspectRatio: '92:112',
        cropLink: "/picture/crop",
        uploadLink: "/picture/upload",
        cropContainerId: 'cropContainer',
        cropSuccess: function(resp) {
            var data = $.parseJSON(resp);
            $("#cropped").attr("src", data.src + "&t=" + Date.now()).attr("file-name", data.fileName);
            $("#classify-block").show();
        }
    });

    $(".menu").click(function(){
        $(".menu[group='" + $(this).attr("group") + "']").each(function(){
            $(this).parent().removeClass("active");
        });
        $(this).parent().addClass("active");
    });
    $("#classify-button").click(function(){
        var metric = null;
        var algorithm = null;
        var fileName = $("#cropped").attr("file-name");
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
        $.ajax({
            type: "POST",
            url: "/hfr/classify",
            data: {
                metric: metric,
                fileName: fileName,
                algorithm: algorithm
            },
            success: function(resp) {
                var data = $.parseJSON(resp);
                var $res = $("#results");
                $res.find(".each").html("");
                if(data.status === "ok"){
                    for(var i in data.storedImages){
                        $res.find(".each").append(img(data.storedImages[i], "stored-image"));
                    }
                    $res.show();
                }
            }
        });
    });
    getAllStoredImages();
}
function getAllStoredImages(){
    $.ajax({
        type: "POST",
        url: "/picture/storedImages",
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