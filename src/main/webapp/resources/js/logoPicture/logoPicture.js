var logoPicture = new LogoPicture();
function LogoPicture(){

    var urls = {};
    var data = {};
    var that = this;
    var reader = new FileReader();
    reader.onload = function(evt) {
        var image = {};
        image.src = evt.target.result;
        image.type = "base64";
        uploadImage(image);
    };

    this.initializePicturePage = function(){
        urls = {
            srcPrefix: "./",
            crop: "./crop.html",
            save: "./save.html",
            upload: "./upload.html",
            delete: "./delete.html",
            getImageData: "./getImageData.html",
            getPrototypes: "./getPrototypes.html",
            inputEditDates: "./getInputEditDates.html"
        };

        onScroll();
    };

    this.cropImageOnClick = function(data){
        if($(this).attr("square") !== "true"){
            data.$logoBlock.find("#infoText").hide();
            data.$logoBlock.find("#cropImage").hide();
            data.$logoBlock.find("#cropImageDiv").append($("#processingLabel").html());
            data.$logoBlock.find('#cropContainer').cropImage({
                pictureId: data.imageId,
                cropLink: data.cropLink,
                srcPrefix: data.srcPrefix,
                entityType: data.entityType,
                removeImgAreaSelect: false,
                $prototypesBlock: data.$prototypesBlock,
                image: {
                    originalWidth: data.answer.maxSize,
                    originalHeight: data.answer.maxSize,
                    src: urls.srcPrefix + data.answer.squareSrc + "&t=" + Date.now() + "&algorithm=" +
                        data.$logoBlock.find('#cropContainer').find("#algorithm").find(".algorithm:checked").val() + "&entityType=" + data.entityType
                }
            });

            data.$logoBlock.find("#cropImage").load(function(){
                data.$logoBlock.find("#cropImageDiv").find(".processingLabel").remove();
                $(this).show();
                var tmpData = data.$logoBlock.find('#cropContainer').data('cropImage').$image.data('imgAreaSelect');
                tmpData.setOptions({show: true});
                if(data.answer.show){
                    var coefficient = data.answer.maxSize/data.$logoBlock.find('#cropImage').width();
                    data.answer.x1 = Math.floor(data.answer.x1/coefficient);
                    data.answer.x2 = Math.floor(data.answer.x2/coefficient);
                    data.answer.y1 = Math.floor(data.answer.y1/coefficient);
                    data.answer.y2 = Math.floor(data.answer.y2/coefficient);
                    tmpData.setSelection(data.answer.x1, data.answer.y1, data.answer.x2, data.answer.y2, true);
                } else {
                    var size = data.$logoBlock.find('#cropImage').width();
                    var x1y1 = Math.floor(size/2-size/4);
                    var x2y2 = Math.floor(size/2+size/4);
                    tmpData.setSelection(x1y1, x1y1, x2y2, x2y2, true);
                }
                tmpData.update();
                var $cropButton = data.$logoBlock.find("#cropButton");
                $cropButton.off();
                $cropButton.click(function(){
                    data.$logoBlock.find('#cropContainer').data('cropImage').settings.crop(tmpData.getSelection(true));
                });
                $cropButton.val("Crop");
            });
            data.$logoBlock.find("#cropImage").attr("square", "true");
        }
    };

    this.getInputEditDates = function(entityId, entityType){
        var inputEdit = {};
        $.ajax({
            type: 'POST',
            url: urls.inputEditDates,
            async: false,
            data: {
                entityId: entityId,
                entityType: entityType
            },
            success: function(resp){
                inputEdit = jQuery.parseJSON(resp);
            }
        });
        return inputEdit;
    };

    this.setInputEdit = function($block, entityId, entityType){
        var inputEdit = that.getInputEditDates(entityId, entityType);
        $block.find("#input").html(inputEdit.input);
        $block.find("#edit").html(inputEdit.edit);
    };

    function uploadImage(image){
        if(null != image && null != image.src){
            var regexp = new RegExp('(data:image\/(jpeg|png|gif|jpg|bmp);base64,)', 'g');
            image.src = image.src.replace(regexp, "");

            if(image.src.indexOf("data:") == -1){
                $.ajax({
                    type: "POST",
                    url: urls.upload,
                    async: false,
                    data: {
                        image: JSON.stringify(image),
                        additionData: JSON.stringify(data)
                    },
                    success: function(resp){
                        var answer = jQuery.parseJSON(resp);
                        if(answer.status === 'error'){
                            alert(answer.message);
                        } else {
                            data.item.find(".image").attr("src", urls.srcPrefix + answer.src + "&t=" + Date.now());
                            data.item.find(".image").show();
                        }
                    },
                    error: function(){
                        alert("Error while uploading image.");
                    }
                });
            }
        }
    }

    function makePrototype(listItem, size, entityId, entityType) {
        var $item = $($("#prototypeTemplate").html());

        /** value and html */
        $item.find("#code").val(listItem.code);
        $item.find("#name").html(listItem.name);
        $item.find("#size").html("jpeg - " + listItem.size + " - size");
        /** value and html */

        /** attributes */
        $item.find("#upload").attr("entityId", entityId);
        $item.find("#upload").attr("entityType", entityType);
        $item.find("#upload").attr("pictureId", listItem.entityId);
        $item.find("#upload").attr("requiredWidth", listItem.width);
        $item.find("#upload").attr("requiredHeight", listItem.height);

        if(null != listItem.src && listItem.src.length > 0){
            $item.find(".image").attr("src", urls.srcPrefix + listItem.src + "&t=" + Date.now());
        }
        $item.find(".image").attr("manuallyAdded", listItem.manuallyAdded); /** not used now (but maybe will use in future)*/
        /** attributes */

        /** css */
        $item.css({width: 100 / size + "%"});
        $item.css({height: listItem.height+60});
        $item.find(".image").css({height: listItem.height, width: listItem.width});
        $item.find("#block").css({height: listItem.height, width: listItem.width});

        if(null == listItem.src || listItem.src.length == 0){
            $item.find(".image").hide();
        }
        /** css */

        /** events */
        $item.find("#upload").click(function(){
            data = {};
            data.item = $item;
            data.entityId = $(this).attr("entityId");
            data.pictureId = $(this).attr("pictureId");
            data.entityType = $(this).attr("entityType");
            data.requiredWidth = $(this).attr("requiredWidth");
            data.requiredHeight = $(this).attr("requiredHeight");

            data.pictureType = $item.find('#code').val();

            $item.find("#uploadPrototype").click();
        });
        $item.find("#uploadPrototype").change(function(){
            var $fileInput = $(this);
            if($fileInput[0] && $fileInput[0].files && $fileInput[0].files.length > 0){
                var files = $fileInput[0].files;
                reader.readAsDataURL(files[0]);
            }
        });
        /** events */

        return $item;
    }

    function onScroll(){
        $("#maincontainer").scroll(function(){
            $(".cropContainer").each(function(){
                if(null != $(this).data('cropImage')){
                    var data = $(this).data('cropImage').$image.data('imgAreaSelect');
                    if(null != data){
                        data.setOptions({show: false});
                        data.update();
                    }
                }
            });
        });
    }
}
