var dnd = new DragAndDrop();
function DragAndDrop(){
    var that = this;
    var reader = new FileReader();
    reader.onload = function(){
        that.setSrc(reader.result);
    };

    this.init = function(){
        document.getElementById("dragAndDropContainer").addEventListener("drop", that.onDrop, false);
        document.getElementById("dragAndDropContainer").addEventListener("paste", that.onPaste, false);
    };

    this.onDrop = function(e){
        e.stopPropagation();
        e.preventDefault();
        if(null != e.dataTransfer && null != e.dataTransfer.files){
            var files = e.dataTransfer.files;
            if(files.length > 0){
                reader.readAsDataURL(files[0]);
            } else if(e.dataTransfer.getData){
                var data = e.dataTransfer.getData("text/html");
                var parser = new DOMParser();
                var el = parser.parseFromString(data, "text/html");
                that.setSrc(el.getElementsByTagName("img")[0].src);
            }
        }
    };

    this.onPaste = function(e){
        e.stopPropagation();
        e.preventDefault();
        if(null != e.clipboardData && null != e.clipboardData.files){
            var files = e.clipboardData.files;
            if(files.length > 0){
                reader.readAsDataURL(files[0]);
            } else if(e.clipboardData.getData){
                var data = e.clipboardData.getData("text/html");
                var parser = new DOMParser();
                var el = parser.parseFromString(data, "text/html");
                that.setSrc(el.getElementsByTagName("img")[0].src);
            }
        }
    };

    this.setSrc = function(src){
        var regexBase64 = /data:image\/(png|bmp|jpeg|gif);base64,/g;
        var regexLink = /http[s]?:\/\//g;
        if(null != src && regexBase64.test(src)){
            document.getElementById("image").src = src;
            document.getElementById("image").style.display = "block";
            document.getElementById("info").innerHTML = "Used base64";
            that.upload(src.replace(regexBase64, ""), "base64");
        } else if(null != src && regexLink.test(src)){
            document.getElementById("image").src = src;
            document.getElementById("image").style.display = "block";
            document.getElementById("info").innerHTML = "Used link to some site";
            that.upload(src, "link");
        }
    };

    this.upload = function(src, type){
        /*$.ajax({
            type: "POST",
            url: "uploadImage.html",
            data: {
                src: src,
                type: type
            },
            success: function(resp){
                alert("Image " + resp + " saved successful.")
            },
            error: function(){
                alert("Error while image upload.");
            }
        })*/
    }
}