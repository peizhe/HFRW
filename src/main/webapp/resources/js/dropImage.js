(function ($) {
    "use strict";
    function div(id, styleClass){
        return $('<div/>').attr("id", id).addClass(styleClass);
    }
    function file(id, styleClass){
        return $('<input type="file" id="' + id + '"/>').addClass(styleClass);
    }

    $.fn.dropImage = function(options){
        this.each(function(){
            if($(this).data('dropImage')){
                if(options.remove){
                    $(this).data('dropImage').remove();
                    $(this).removeData('dropImage');
                } else {
                    $(this).data('dropImage').setOptions(options);
                    $(this).data('dropImage').buildStructure();
                }
            } else if(!options.remove){
                $(this).data('dropImage', new $.dropImage(this, options));
            }
            return this;
        });
    };

    $.dropImage = function(item, options){
        var that = this;
        var reader = new FileReader();
        var defaults = {
            okWidth: 0,
            okHeight: 0,
            remove: false,
            cropLink: null,
            uploadLink: null,
            aspectRatio: '1:1',
            cropContainer: null,
            parentDropContainerId: "",
            parentCropContainerId: "",
            uploadDivClass: "img-circle drop",
            processingLabelId: "processingLabel",
            allowedImages: "(jpeg|png|gif|jpg|bmp)",
            texts: {
                uploadDivText: "Drag & Drop<br>Image Here<br>(or click)",
                uploadDivDisabledText: "Disabled",
                uploadError: 'Error while uploading',
                notImageAlert : 'Please, use only image files'
            },
            cropSuccess: function(data){},
            cropImageOnClick: function(data){
                var $cropContainer = $("#" + that.settings.parentCropContainerId).find("." + data.cropContainer);
                $cropContainer.cropImage({
                    cropLink: data.cropLink,
                    removeImgAreaSelect: false,
                    imgAreaSelectSettings: {
                        aspectRatio: that.settings.aspectRatio
                    },
                    image: {
                        src: data.answer.src,
                        fileName: data.answer.fileName,
                        originalWidth: data.answer.width,
                        originalHeight: data.answer.height
                    },
                    cropSuccess: that.settings.cropSuccess
                });

                var $image = $cropContainer.data('cropImage').$image;
                $image.load(function(){
                    $(this).show();
                    var tmpData = $image.data('imgAreaSelect');
                    tmpData.setOptions({show: true});
                    var width = $image.width();
                    var height = $image.height();
                    var size = Math.min(width, height);
                    tmpData.setSelection(
                        Math.max(Math.ceil(size/2) - that.settings.okWidth, 0),
                        Math.max(Math.ceil(size/2) - that.settings.okHeight, 0),
                        Math.min(Math.ceil(size/2) + that.settings.okWidth, width),
                        Math.min(Math.ceil(size/2) + that.settings.okHeight, height), true
                    );
                    tmpData.update();
                });
            },
            successUploadFunction: function(resp){
                var answer = jQuery.parseJSON(resp);
                var $cropContainer = $("#" + that.settings.parentCropContainerId).find("." + that.settings.cropContainer);

                that.$container.dropImage(that.settings);
                $cropContainer.cropImage({remove: true});
                $cropContainer.cropImage({
                    removeImgAreaSelect: true,
                    imgAreaSelectSettings: {
                        aspectRatio: that.settings.aspectRatio
                    },
                    cropSuccess: that.settings.cropSuccess,
                    cropLink: that.settings.cropLink,
                    image: {
                        src: answer.src,
                        fileId: answer.fileId,
                        originalWidth: answer.width,
                        originalHeight: answer.height
                    }
                });
                $cropContainer.data('cropImage').$image.click(function(){
                    $(this).off();
                    that.settings.cropImageOnClick({
                        answer: answer,
                        cropLink: that.settings.cropLink,
                        cropContainer: that.settings.cropContainer
                    });
                });
            }
        };

        reader.onload = function(evt) {
            var image = {};
            image.src = evt.target.result;
            that.uploadImage(image);
        };

        this.uploadImage = function(image){
            if(null != image && null != image.src){
                that.remove();
                this.$container.find("#uploadDiv").find("#text").html($("#" + that.settings.processingLabelId).html());
                var regexp = new RegExp('(data:image\/' + that.settings.allowedImages + ';base64,)', 'g');
                if(regexp.test(image.src)){
                    image.type = "base64";
                } else {
                    image.type = "link";
                }
                image.src = image.src.replace(regexp, "");
                try {
                    if(image.src.indexOf("data:") == -1){
                        $.ajax({
                            type: "POST",
                            url: that.settings.uploadLink,
                            async: false,
                            data: {
                                image: JSON.stringify(image)
                            },
                            success: that.settings.successUploadFunction,
                            error: function(){
                                that.$container.dropImage(that.settings);
                                alert(that.settings.texts.uploadError);
                            }
                        });
                    } else {
                        that.$container.dropImage(that.settings);
                        alert(that.settings.texts.notImageAlert);
                    }
                } finally {
                    if(!that.$container.data('dropImage')){
                        that.$container.dropImage(that.settings);
                    }
                }
            }
        };

        this.onDrop = function(e){
            e.stopPropagation();
            e.preventDefault();
            $(this).removeClass("dropHover");
            var image = {};
            if(e.dataTransfer && e.dataTransfer.files.length != 0){
                var files = e.dataTransfer.files;
                reader.readAsDataURL(files[0]);
            } else if(e.dataTransfer && e.dataTransfer.getData){
                var $data = $(e.dataTransfer.getData('text/html'));
                image.src = $data.find("img").attr("src");
                if(null == image.src){
                    image.src = $data.attr("src");
                }
                that.uploadImage(image);
            }
        };

        this.onPaste = function(e){
            e.stopPropagation();
            e.preventDefault();
            $(this).removeClass("dropHover");
            var image = {};
            if(e.clipboardData && e.clipboardData.files && e.clipboardData.files.length > 0){
                var files = e.clipboardData.files;
                reader.readAsDataURL(files[0]);
            } else if(e.clipboardData && e.clipboardData.getData){
                try{
                    image.src = $(e.clipboardData.getData('text/html')).attr("src");
                    that.uploadImage(image);
                } catch(e){}
            }
        };

        this.setOptions = function(newOptions){
            this.settings = $.extend(true, defaults, newOptions);
        };

        this.remove = function(){
            that.$container.find("#uploadDiv").get(0).removeEventListener("drop", that.onDrop);
            that.$container.find("#uploadDiv").get(0).removeEventListener("paste", that.onPaste);
            this.$container.find("#uploadDiv").off();
            this.$container.find("#uploadDiv").find("#text").html(that.settings.texts.uploadDivDisabledText);
        };

        this.buildStructure = function(){
            this.$container.html("");
            this.$container.append(div("uploadDiv"));
            this.$container.append(file("uploadFile"));

            var $uploadDiv = this.$container.find("#uploadDiv");
            $uploadDiv.append(div("text").addClass("dropText").html(that.settings.texts.uploadDivText));
            $uploadDiv.attr("contenteditable", "true").addClass(that.settings.uploadDivClass);
            this.$container.find("#uploadFile").css({display: "none"});
            that.$container.find("#uploadFile").change(function(){
                var $fileInput = $(this);
                if($fileInput[0] && $fileInput[0].files && $fileInput[0].files.length > 0){
                    var files = $fileInput[0].files;
                    reader.readAsDataURL(files[0]);
                }
            });

            $uploadDiv.click(function(){
                that.$container.find("#uploadFile").click();
            });
            that.$container.find("#uploadDiv").get(0).addEventListener("drop", that.onDrop, false);
            that.$container.find("#uploadDiv").get(0).addEventListener("paste", that.onPaste, false);
            that.$container.find("#uploadDiv").get(0).addEventListener("dragover", function(){$(this).addClass("dropHover");}, false);
            that.$container.find("#uploadDiv").get(0).addEventListener("dragleave", function(){$(this).removeClass("dropHover");}, false);
        };

        this.$container = $(item);
        this.setOptions(options);
        this.buildStructure();
    };
}(jQuery));