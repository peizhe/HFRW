(function ($) {
    "use strict";
    function div(styleClass){
        return $('<div/>').addClass(styleClass);
    }
    function img(styleClass){
        return $('<img/>').addClass(styleClass);
    }
    function button(styleClass, value, disabled){
        return $('<input type="button">').attr("value", value).addClass(styleClass).prop("disabled", disabled);
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
        this.$info = null;
        this.$image = null;
        var defaults = {
            cropLink: null,
            image: {
                src: null,
                fileName: null,
                originalWidth: null,
                originalHeight: null
            },
            button: {
                enabled: "btn btn-primary",
                disabled: "btn btn-primary disabled"
            },
            removeImgAreaSelect: false,
            imgAreaSelectSettings: {
                show: false,
                handles: true,
                movable: true,
                instance: true,
                resizable: true,
                aspectRatio: '1:1',
                onInit: function(containes, selections){},
                onSelectEnd: function(containes, selections){
                    if(selections.height > 0 && selections.width > 0){
                        that.$info.find("input[type='button'].cropImage-algorithm").each(function(){
                            $(this).removeClass("disabled").addClass("enabled").prop("disabled", false);
                        });
                    } else {
                        that.$info.find("input[type='button'].cropImage-algorithm").each(function(){
                            $(this).removeClass("enabled").addClass("disabled").prop("disabled", true);
                        });
                    }
                },
                onSelectStart: function(containes, selections){},
                onSelectChange: function(containes, selections){}
            },
            text: "Click on image to start crop",
            cropSuccess: function(data){},
            crop: function(selections, algorithm){
                var coefficient = that.settings.image.originalWidth/that.$image.width();
                var newSelections = {};
                for(var i in selections){
                    newSelections[i] = Math.floor(selections[i] * coefficient);
                }
                $.ajax({
                    type: "POST",
                    url: that.settings.cropLink,
                    data: {
                        algorithm: algorithm,
                        fileId: that.settings.image.fileId,
                        selection: JSON.stringify(newSelections)
                    },
                    success: that.settings.cropSuccess
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
                that.$image = img("cropImage-image");
                that.$container.append(div("cropImage-container").append(that.$image));
            }
            if(null == that.$info){
                that.$info = div("cropImage-info");
                that.$info.append(div("cropImage-text").html(that.settings.text));

                var $buttons = div("cropImage-buttons");
                $buttons.append(button(that.settings.button.disabled, "Crop Bileniar", true).addClass("cropImage-algorithm").attr("alg-val", "0"));
                $buttons.append(button(that.settings.button.disabled, "Crop Lanczos", true).addClass("cropImage-algorithm").attr("alg-val", "1"));
                that.$info.append($buttons);

                that.$container.append(that.$info);

                that.$info.find("input[type='button'].cropImage-algorithm").each(function(){
                    $(this).click(function(){
                        that.settings.crop(that.$image.data('imgAreaSelect').getSelection(true), $(this).attr("alg-val"));
                    });
                });
            }
            that.$image.attr("src", that.settings.image.src + "?t=" + Date.now());
            if(that.settings.removeImgAreaSelect){
                that.$image.imgAreaSelect({remove:true});
            } else {
                that.$image.imgAreaSelect(that.settings.imgAreaSelectSettings);
            }
        };

        this.$container = $(item);
        this.setOptions(options);
        this.buildUpdateStructure();
    };
}(jQuery));