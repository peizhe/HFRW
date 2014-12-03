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
            $block: null,           //parent div for drop and crop
            entityId: null,         //entityId of BusinessEntity or PersonEntity
            cropLink: null,
            srcPrefix: null,        //prefix for image src
            pictureId: null,        //id of picture if it already exists
            entityType: null,       //PERSON, ENTITY
            uploadLink: null,
            $prototypesBlock: null,
            processingLabelId: "processingLabel",
            getAdditionData: function(){
                var data = {};
                data.entityId = that.settings.entityId;
                data.pictureId = that.settings.pictureId;
                data.entityType = that.settings.entityType;
                data.pictureType = 'ORIG';
                return data;
            },
            remove: false,
            allowedImages: "(jpeg|png|gif|jpg|bmp)",
            texts: {
                uploadDivText: "Drag & Drop<br>Image Here<br>(or click)",
                uploadDivDisabledText: "Disabled",
                uploadError: 'Error while uploading',
                notImageAlert : 'Please, use only image files'
            },
            cssStyleUploadDiv:{
                width: 120,
                height: 90,
                fontSize: 11,
                color: "gray",
                lineHeight: "20px",
                paddingTop: "30px",
                textAlign: "center",
                verticalAlign: "middle",
                backgroundColor: "white",
                border: "1px grey solid",
                "-moz-user-select": "none",
                fontFamily: "Arial, Tahoma, Helvetica, serif"
            },

            cropImageOnClick: function(data){},
            successUploadFunction: function(resp){
                var answer = jQuery.parseJSON(resp);
                var $cropContainer = that.settings.$block.find('#cropContainer');

                that.settings.pictureId = answer.imageId;
                that.settings.$block.find("#pictureId").val(answer.imageId);

                that.$container.dropImage(that.settings);
                $cropContainer.cropImage({remove: true});
                $cropContainer.cropImage({
                    pictureId: answer.imageId,
                    removeImgAreaSelect: true,
                    cropLink: that.settings.cropLink,
                    srcPrefix: that.settings.srcPrefix,
                    entityType: that.settings.entityType,
                    $prototypesBlock: that.settings.$prototypesBlock,
                    image: {
                        originalWidth: answer.width,
                        originalHeight: answer.height,
                        src: that.settings.srcPrefix + answer.src + "&t=" + Date.now()
                    }
                });
                $cropContainer.find('#cropImage').click(function(){
                    $(this).off();
                    that.settings.cropImageOnClick({
                        answer: answer,
                        imageId: answer.imageId,
                        cropLink: that.settings.cropLink,
                        $logoBlock: that.settings.$block,
                        srcPrefix: that.settings.srcPrefix,
                        entityType: that.settings.entityType,
                        $prototypesBlock: that.settings.$prototypesBlock
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
                                image: JSON.stringify(image),
                                additionData: JSON.stringify(that.settings.getAdditionData())
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
            var image = {};
            if(e.clipboardData && e.clipboardData.files != 0 && e.clipboardData.files.length > 0){
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
            $uploadDiv.append(div("text").html(that.settings.texts.uploadDivText));
            $uploadDiv.css(that.settings.cssStyleUploadDiv).attr("contenteditable", "true");
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
        };

        this.$container = $(item);
        this.setOptions(options);
        this.buildStructure();
    };
}(jQuery));