var hfr = new HFR();

function HFR() {

    this.init = function(width, height, recognitionType) {
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

        settingsBuilder.init();

        $("button[class='close']").each(function () {
            $(this).click(function () {
                $(this).parent().hide();
            });
        });

        $("#classify-button").click(function () {
            $.ajax({
                type: "POST",
                url: "./hfr/classify",
                data: storeData(recognitionType),
                success: function (resp) {
                    var data = $.parseJSON(resp);
                    var $res = $("#results");
                    $res.find(".each").html("");
                    if (data.status === "ok") {
                        for (var i in data.storedImages) {
                            $res.find(".each").append(img(data.storedImages[i], "stored-image"));
                        }
                        $res.show();
                    } else {
                        $res.hide();
                    }
                }
            });
        });
        $("#eigen-button").click(function () {
            if(settingsBuilder.algorithm.code != "NBC") {
                $.ajax({
                    type: "POST",
                    url: "./hfr/eigenVectors",
                    data: storeData(recognitionType),
                    success: function (resp) {
                        var data = $.parseJSON(resp);
                        var $res = $("#eigenvectors");
                        $res.find(".each").html("");
                        if (data.status === "ok") {
                            for (var i in data.storedImages) {
                                $res.find(".each").append(img(data.storedImages[i], "stored-image"));
                            }
                            $res.show();
                        } else {
                            $res.hide();
                        }
                    }
                });
            }
        });
        this.getAllStoredImages(recognitionType);
    };

    this.getAllStoredImages = function(recognitionType) {
        $.ajax({
            type: "POST",
            url: "./picture/storedImages",
            data: {
                type: recognitionType
            },
            success: function (resp) {
                var data = $.parseJSON(resp);
                var $collapse = $("#collapse");
                var $storedImages = $collapse.find("#stored-images");
                var $storedClasses = $collapse.find("#stored-classes");
                var ts = Object.keys(data).sort();
                for (var i = 0; i < ts.length; i++) {
                    var $image = $('<div/>').addClass("accordion-inner centered").attr("id", ts[i]);
                    var $class = $('<li><a href="#' + ts[i] + '">' + ts[i] + '</a></li>');
                    $image.append($('<div/>').addClass("info-text").html(ts[i]));
                    for (var j = 0; j < data[ts[i]].length; j++) {
                        $image.append(img(data[ts[i]][j], "stored-image"));
                    }
                    $storedImages.append($image);
                    $storedClasses.append($class);
                }
            }
        });
    };

    function storeData(recognitionType) {
        var data = {
            recognitionType: recognitionType,
            fileId: $("#cropped").attr("file-id"),
            algorithm: settingsBuilder.algorithm.code
        };
        var settings = settingsBuilder.getSettings();
        for(var i in settings) {
            data[i + "Type"] = settings[i].type;
            data[i + "Value"] = settings[i].value;
        }
        return data;
    }

    function img(src, styleClass) {
        return $('<img/>').addClass(styleClass).attr("src", src);
    }
}