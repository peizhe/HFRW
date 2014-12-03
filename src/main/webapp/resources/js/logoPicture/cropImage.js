(function ($) {
    "use strict";
    function div(id, styleClass){
        return $('<div/>').attr("id", id).addClass(styleClass);
    }
    function img(id, styleClass){
        return $('<img/>').attr("id", id).addClass(styleClass);
    }
    function button(id, styleClass, value, disabled){
        var $button = $('<input type="button">');
        $button.attr("id", id).attr("value", value);
        $button.addClass(styleClass).prop("disabled", disabled);
        return $button;
    }

    $.fn.cropImage = function(options){
        this.each(function(){
            if($(this).data('cropImage')){
                if(options.remove){
                    $(this).data('cropImage').remove();
                    $(this).removeData('cropImage');
                } else {
                    $(this).data('cropImage').setOptions(options);
                    $(this).data('cropImage').buildUpdateStructure();
                }
            } else if(!options.remove){
                $(this).data('cropImage', new $.cropImage(this, options));
            }
            return this;
        });
    };

    $.cropImage = function(item, options){
        var that = this;
        this.$image = null;
        var defaults = {
            cropLink: null,
            srcPrefix: null,
            pictureId: null,
            entityType: null,
            $prototypesBlock: null,
            manuallyUploadId: "upload",
            prototypeClass: "prototype",
            prototypeImageClass: "image",
            image: {
                src: '',
                originalWidth: 0,
                originalHeight: 0,
                css: {
                    maxWidth: "570px",
                    /** do not forget to change size on server side PIC_WIDTH_HEIGHT */
                    maxHeight: "300px",
                    marginLeft: "10px",
                    border: "1px gray solid"
                }
            },
            cropButton: {
                enableClass: "buttonView",
                disableClass: "buttonView_disabled"
            },
            progressBar: {
                max: 0,
                value: 0,
                use: false,
                css: {
                    marginLeft: "10px"
                }
            },
            removeImgAreaSelect: false,
            imgAreaSelectSettings: {
                show: false,
                handles: true,
                movable: true,
                instance: true,
                resizable: true,
                aspectRatio: '1:1',
                onInit: function(containes, selections){
                    if(that.settings.progressBar.use){
                        that.$container.find('#progressbar').progressbar(that.settings.progressBar);
                    }
                },
                onSelectEnd: function(containes, selections){
                    var $cropButton = that.$container.find("#cropButton");
                    $cropButton.off();
                    if(selections.height > 0 && selections.width > 0){
                        $cropButton.prop("disabled", false).attr("class", that.settings.cropButton.enableClass);
                        $cropButton.click(function(){
                            that.settings.crop(selections);
                        });
                        $cropButton.val("Crop");
                        that.$container.find("#cropImage").attr("square", "true");
                    } else {
                        $cropButton.click(function(){
                            that.settings.acceptImage();
                        });
                        $cropButton.val("Accept Image").prop("disabled", false).attr("class", that.settings.cropButton.enableClass);
                        that.$container.find("#cropImage").attr("square", "");
                    }
                },
                onSelectStart: function(containes, selections){},
                onSelectChange: function(containes, selections){
                    var coefficient = that.settings.image.originalWidth/that.$container.find("#cropImage").width();
                    if(that.settings.progressBar.use){
                        that.$container.find('#progressbar').progressbar({value: Math.floor(
                            Math.sqrt(selections.width*coefficient*selections.width*coefficient +
                                selections.height*coefficient*selections.height*coefficient))});
                    }
                }
            },
            infoText: {
                text: "Click on image to start crop",
                css: {
                    float: "right",
                    fontSize: "12px",
                    fontFamily: "Arial, Tahoma, Helvetica, serif"
                }
            },
            crop: function(selections){
                var coefficient = that.settings.image.originalWidth/that.$container.find("#cropImage").width();
                for(var i in selections){
                    selections[i] = Math.floor(selections[i] * coefficient);
                }
                $.ajax({
                    type: "POST",
                    url: that.settings.cropLink,
                    data: {
                        selection: JSON.stringify(selections),
                        pictureId: that.settings.pictureId,
                        algorithm: that.$container.find("#algorithm").find(".algorithm:checked").val(),
                        entityType: that.settings.entityType
                    },
                    success: function(resp){
                        var answer = jQuery.parseJSON(resp);
                        var j = 0;
                        that.settings.$prototypesBlock.find("." + that.settings.prototypeClass).each(function(){
                            $(this).find("." + that.settings.prototypeImageClass).attr("src", that.settings.srcPrefix + answer[j].src + "&t=" + Date.now()).show();
                            $(this).find("#" + that.settings.manuallyUploadId).attr("pictureId", answer[j].entityId);
                            j++;
                        });
                        that.settings.$prototypesBlock.find("#delete").show();
                        that.$container.find("#cropButton").attr("class", that.settings.cropButton.disableClass).prop("disabled", true);
                    }
                });
            },
            acceptImage: function(){
                $.ajax({
                    type: "POST",
                    url: that.settings.cropLink,
                    data: {
                        pictureId: that.settings.pictureId,
                        algorithm: that.$container.find("#algorithm").find(".algorithm:checked").val(),
                        entityType: that.settings.entityType
                    },
                    success: function(resp){
                        var answer = jQuery.parseJSON(resp);
                        var j = 0;
                        that.settings.$prototypesBlock.find("." + that.settings.prototypeClass).each(function(){
                            $(this).find("." + that.settings.prototypeImageClass).attr("src", that.settings.srcPrefix + answer[j].src + "&t=" + Date.now()).show();
                            $(this).find("#" + that.settings.manuallyUploadId).attr("pictureId", answer[j].entityId);
                            j++;
                        });
                        that.settings.$prototypesBlock.find("#delete").show();
                    }
                });
            }
        };

        this.setOptions = function(newOptions){
            this.settings = $.extend(true, defaults, newOptions);
        };

        this.getOptions = function(){
            return this.settings;
        };

        this.remove = function(){
            this.$image.imgAreaSelect({remove:true});
            this.setOptions({disable: true});
            this.$container.html('');
        };

        this.buildUpdateStructure = function(){
            if(null == that.$image){
                that.$image = img("cropImage").css(that.settings.image.css);
                that.$container.append(div("cropImageDiv").css({float: "left", maxWidth: that.settings.image.css.maxWidth, marginRight:"10px"}).append(that.$image));
                if(that.settings.progressBar.use){
                    that.$container.find("#cropImageDiv").append(div("progressbar").css(that.settings.progressBar.css));
                }
            }
            that.$image.attr("src", that.settings.image.src + "&t=" + Date.now());
            if(that.settings.removeImgAreaSelect){
                that.$image.imgAreaSelect({remove:true});
            } else {
                that.$image.imgAreaSelect(that.settings.imgAreaSelectSettings);
            }
            if(that.$container.find("#cropButton").length > 0){
                that.$container.find("#cropButton").attr("class", that.settings.cropButton.enableClass).prop("disabled", false).off();
            } else {
                that.$container.append(div("info").css({float: "left", marginLeft: "10px", width: 170}));
                that.$container.find("#info").append(div("infoText").css({float: "left"}).
                    append(div().css(that.settings.infoText.css).append(that.settings.infoText.text)));
                that.$container.find("#info").append(div().css({float: "left", width: "100%"}).
                    append(button("cropButton", that.settings.cropButton.enableClass, "Accept Image", false).css({float: "left"})));
                var time = Date.now();
                that.$container.find("#info").append(div("algorithm").css({width: "100%", float: "left"}).append(
                    "<input type='radio' name='algorithm" + time + "' class='algorithm' value='0' checked='checked'>Bileniar" +
                    "<input type='radio' name='algorithm" + time + "' class='algorithm' value='1' style='margin-left: 10px;'>Lanczos"));
                that.$container.find("#info").find("#algorithm").find(".algorithm").change(function(){
                    that.$container.find("#cropButton").attr("class", that.settings.cropButton.enableClass).prop("disabled", false).off();
                    that.$container.find("#cropButton").click(function(){
                        if(that.$container.find("#cropImage").attr("square") === "true"){
                            that.settings.crop(that.$container.data('cropImage').$image.data('imgAreaSelect').getSelection(true));
                        } else {
                            that.settings.acceptImage();
                        }
                    });
                });
            }

            that.$container.find("#cropButton").click(function(){
                that.settings.acceptImage();
            });
            that.$container.find("#cropButton").show();
        };

        this.$container = $(item);
        this.setOptions(options);
        this.buildUpdateStructure();
    };
}(jQuery));