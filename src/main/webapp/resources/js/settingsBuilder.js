var settingsBuilder = new SettingsBuilder();

function SettingsBuilder() {
    var that = this;
    this.algorithm = null;

    this.baseBuild = function(data, $parent, $child) {
        $parent.append(li("nav-header", data.sectionName));
        for(var i = 0; i < data.items.length; i++) {
            var className = (i == 0 ? "active" : "");
            $parent.append(this.build(data.items[i], data.group, className, $child))
        }
        $parent.find(".menu").each(function() {
            $(this).tooltip();
        }).click(function(){
            $parent.find(".menu[group='" + $(this).attr("group") + "']").each(function(){
                $(this).parent().removeClass("active");
            }).find(".li-input").each(function(){
                $(this).hide();
            }).off().keypress(function(e) {
                return (inputDigitsOnly(e) || e.which == 45) && e.which != 46;
            }).change(function() {
                $(this).val($(this).val().replace(/[^0-9]/g, ""));
                if($(this).val().trim().length == 0) {
                    $(this).val($(this).attr("default-value"));
                }
            });

            $(this).parent().addClass("active");
            var find = $(this).parent().find("input");
            if(!find.val() || find.val().trim().length == 0) {
                find.val(find.attr("default-value"));
            }
            find.show();
        });
    };

    this.build = function(dataItem, group, className, $settingSection) {
        var $link = link("menu", "#", dataItem.name, dataItem.code, group, dataItem.description);
        if(dataItem.input) {
            var $inputField = input(dataItem.defaultValue);
            $link.append($inputField);
            $inputField.hide();
        }
        var $item = li(className);
        $item.append($link);
        if($settingSection && dataItem.settings) {
            $item.click(function () {
                that.algorithm = dataItem;
                $settingSection.empty();
                for(var i = 0; i < dataItem.settings.length; i++) {
                    that.baseBuild(dataItem.settings[i], $settingSection)
                }
            });
        }
        return $item;
    };

    this.init = function() {
        var $recognition = $(".recognition-algorithms");
        this.baseBuild(algorithms, $recognition, $(".recognition-settings"));
        $recognition.find("[enum-name=PCA]").click();
    };

    this.getSettings = function() {
        var settings = {};
        var $recognitionSettings = $(".recognition-settings");
        for(var i = 0; i < this.algorithm.settings.length; i++) {
            var group = this.algorithm.settings[i].group;
            var $link = $recognitionSettings.find(".active").find("[group=" + group + "]");
            settings[group] = {
                type: $link.attr("enum-name"),
                value: $link.find("input").val()
            };
        }
        return settings;
    };

    function li(className, html) {
        return $("<li/>").addClass(className).html(html);
    }

    function link(className, href, html, enumName, group, tooltip) {
        var $link = $("<a/>");
        $link.addClass(className)
            .attr("href", href)
            .attr("enum-name", enumName)
            .attr("group", group)
            .html(html);
        if(tooltip) {
            $link.attr("data-toggle", "tooltip")
                .attr("data-placement", "right")
                .attr("data-original-title", tooltip);
        }
        return $link;
    }

    function input(defaultValue) {
        return $("<input/>")
            .addClass("li-input")
            .attr("type", "text")
            .attr("default-value", defaultValue);
    }

    function inputDigitsOnly(event){
        return !(event.which != 8 && event.which != 0 &&
        (event.which < 48 || event.which > 57) && event.which != 46);
    }
}