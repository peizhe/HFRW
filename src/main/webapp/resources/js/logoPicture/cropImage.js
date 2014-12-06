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
            image: {
                src: null,
                fileName: null,
                originalWidth: null,
                originalHeight: null
            },
            cropButton: {
                enableClass: "btn btn-primary",
                disableClass: "btn btn-primary disabled"
            },
            removeImgAreaSelect: false,
            imgAreaSelectSettings: {
                show: false,
                handles: true,
                movable: true,
                instance: true,
                resizable: true,
                aspectRatio: '92:112',
                onInit: function(containes, selections){},
                onSelectEnd: function(containes, selections){
                    var $cropButton = that.$container.find("#cropButton");
                    $cropButton.off();
                    if(selections.height > 0 && selections.width > 0){
                        $cropButton.prop("disabled", false).attr("class", that.settings.cropButton.enableClass);
                        $cropButton.click(function(){
                            that.settings.crop(selections);
                        });
                    }
                },
                onSelectStart: function(containes, selections){},
                onSelectChange: function(containes, selections){}
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
                        file: that.settings.image.fileName,
                        selection: JSON.stringify(selections),
                        algorithm: that.$container.find("#algorithm").find(".algorithm:checked").val()
                    },
                    success: function(resp){
                        console.log(jQuery.parseJSON(resp));
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
                that.$image = img("cropImage");
                that.$container.append(div("cropImageDiv").css({float: "left", marginRight:"10px"}).append(that.$image));
            }
            that.$image.attr("src", that.settings.image.src + "&t=" + Date.now());
            if(that.settings.removeImgAreaSelect){
                that.$image.imgAreaSelect({remove:true});
            } else {
                that.$image.imgAreaSelect(that.settings.imgAreaSelectSettings);
            }
            if(that.$container.find("#cropButton").length > 0){
                that.$container.find("#cropButton").attr("class", that.settings.cropButton.disableClass).prop("disabled", true).off();
            } else {
                that.$container.append(div("info").css({float: "left", marginLeft: "10px", width: 170}));
                that.$container.find("#info").append(div("infoText").css({float: "left"}).append(div().css(that.settings.infoText.css).append(that.settings.infoText.text)));

                that.$container.find("#info").append(div().css({float: "left", width: "100%"}).append(button("cropButton", that.settings.cropButton.disableClass, "Crop", true).css({float: "left"})));
                that.$container.find("#info").append(div("algorithm").css({width: "100%", float: "left"}).append(
                    "<input type='radio' name='algorithm' class='algorithm' value='0' checked='checked'>Bileniar" +
                    "<input type='radio' name='algorithm' class='algorithm' value='1' style='margin-left: 10px;'>Lanczos"
                ));

                that.$container.find("#info").find("#algorithm").find(".algorithm").change(function(){
                    that.$container.find("#cropButton").click(function(){
                        that.settings.crop(that.$container.data('cropImage').$image.data('imgAreaSelect').getSelection(true));
                    });
                });
            }

            that.$container.find("#cropButton").show();
        };

        this.$container = $(item);
        this.setOptions(options);
        this.buildUpdateStructure();
    };
}(jQuery));