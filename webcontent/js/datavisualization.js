window.onload = function() {
    if(document.getElementById('promoter_container')){
        var pi = new PromoterImage();
    }

    if(document.getElementById('hs_chromosome_container')){
        var ci = new ChromosomeImage();
    }

    if(document.getElementById('human_picture_container')){
        new HeatMap(document.getElementById('human_picture_container'));
    }

    if(document.getElementById('mouse_picture_container')){
        new HeatMap(document.getElementById('mouse_picture_container'));
    }

    if(document.getElementById('venn')){
        drawVenn();
    }

    try {
        if(document.getElementById('tags1') != null){
            TagCanvas.textColour = 'blue';
            TagCanvas.outlineColour = '#ff9999';
            TagCanvas.outlineColour	= "#ffff99";
            TagCanvas.outlineMethod = "outline"
            TagCanvas.weight = true;
            TagCanvas.freezeActive	= true;
            TagCanvas.minBrightness	= 0.3;
            TagCanvas.Start('myCanvas1', 'tags1');
        }
    } catch(e) {
        // something went wrong, hide the canvas
        document.getElementById('myCanvasContainer1').style.display = 'none';
    }
    try {
        if(document.getElementById('tags2') != null){
            TagCanvas.textColour = 'blue';
            TagCanvas.outlineColour = '#ff9999';
            TagCanvas.outlineColour	= "#ffff99";
            TagCanvas.outlineMethod = "outline"
            TagCanvas.weight = true;
            TagCanvas.freezeActive	= true;
            TagCanvas.minBrightness	= 0.3;
            TagCanvas.Start('myCanvas2', 'tags2');
        }
    } catch(e) {
        // something went wrong, hide the canvas
        document.getElementById('myCanvasContainer2').style.display = 'none';
    }
    try {
        if(document.getElementById('tags3') != null){
            TagCanvas.textColour = 'blue';
            TagCanvas.outlineColour = '#ff9999';
            TagCanvas.outlineColour	= "#ffff99";
            TagCanvas.outlineMethod = "outline"
            TagCanvas.weight = true;
            TagCanvas.freezeActive	= true;
            TagCanvas.minBrightness	= 0.3;
            TagCanvas.Start('myCanvas3', 'tags3');
        }
    } catch(e) {
        // something went wrong, hide the canvas
        document.getElementById('myCanvasContainer3').style.display = 'none';
    }

    //Everything worked remove the java required tag
    var noJavaDivs = document.getElementsByClassName('nojava')
    var ie8 = document.getElementsByClassName("IE8");
    if ( !$.browser.msie || parseInt($.browser.version, 10) > 8) {
        $(ie8).css("display", "none");
    }

    for (var i = 0; i < noJavaDivs.length; i++) {
        noJavaDivs[i].style.display = 'none';
    }

};

function drawVenn(){
    var paper = new Raphael(document.getElementById('venn'), 1000, 400);
    var t1 = paper.circle(150, 150, 100);
    t1.attr({
        "fill": "red",
        "fill-opacity": 0.25
    });    var t2 = paper.circle(300, 150, 100);
    t2.attr({
        "fill": "blue",
        "fill-opacity": 0.25
    });
    var t3 = paper.circle(225, 275, 100);
    t3.attr({
        "fill": "green",
        "fill-opacity": 0.25
    });
    paper.text(150,150, document.getElementById('t1').value);
    paper.text(300,150, document.getElementById('t2').value);
    paper.text(225,275, document.getElementById('t3').value);
    paper.text(225,150, document.getElementById('t1t2').value);
    paper.text(265,215, document.getElementById('t2t3').value);
    paper.text(185,215, document.getElementById('t1t3').value);
    //paper.text(225,150, document.getElementById('t2t3').value);
    paper.text(225,195, document.getElementById('t1t2t3').value);

    var t1Title = paper.text(110, 110, "Type I");
    t1Title.attr({
        "font-size": "16pt"
    });
    var t2Title = paper.text(340, 110, "Type II");
    t2Title.attr({
        "font-size": "16pt"
    });
    var t3Title = paper.text(225, 340, "Type III");
    t3Title.attr({
        "font-size": "16pt"
    });

    var svg = paper.toSVG();
    var b64 = Base64.encode(svg);

    document.getElementById("saveimage").innerHTML = "Save as an Image " +
        "<a href-lang='image/svg+xml'  target='_blank'  href='data:image/svg+xml;base64,\n"+b64+"' title='Venn.svg'>"
        + '<img src="/interferome/images/save_picture_icon.png" />' +
        '<div id="saveimage" class="export_pic"></div>';
    document.getElementById("hiddenlist").style.display = "none";

}

function saveChromosome(paper, div, species){
    var svg = paper.toSVG();
    var b64 = Base64.encode(svg);
    document.getElementById(div).innerHTML  = "Save as an Image<a href-lang='image/svg+xml' target='_blank' href='data:image/svg+xml;base64,\n"+b64+"' title='"+species+"_chromosomes.svg'>" +
        '<img src="/interferome/images/save_picture_icon.png" />'
    "</a>";
}

var Base64 = {
// private property
    _keyStr : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",

// public method for encoding
    encode : function (input) {
        var output = "";
        var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
        var i = 0;

        input = Base64._utf8_encode(input);

        while (i < input.length) {

            chr1 = input.charCodeAt(i++);
            chr2 = input.charCodeAt(i++);
            chr3 = input.charCodeAt(i++);

            enc1 = chr1 >> 2;
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
            enc4 = chr3 & 63;

            if (isNaN(chr2)) {
                enc3 = enc4 = 64;
            } else if (isNaN(chr3)) {
                enc4 = 64;
            }

            output = output +
                Base64._keyStr.charAt(enc1) + Base64._keyStr.charAt(enc2) +
                Base64._keyStr.charAt(enc3) + Base64._keyStr.charAt(enc4);

        }

        return output;
    },

// public method for decoding
    decode : function (input) {
        var output = "";
        var chr1, chr2, chr3;
        var enc1, enc2, enc3, enc4;
        var i = 0;

        input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

        while (i < input.length) {

            enc1 = Base64._keyStr.indexOf(input.charAt(i++));
            enc2 = Base64._keyStr.indexOf(input.charAt(i++));
            enc3 = Base64._keyStr.indexOf(input.charAt(i++));
            enc4 = Base64._keyStr.indexOf(input.charAt(i++));

            chr1 = (enc1 << 2) | (enc2 >> 4);
            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
            chr3 = ((enc3 & 3) << 6) | enc4;

            output = output + String.fromCharCode(chr1);

            if (enc3 != 64) {
                output = output + String.fromCharCode(chr2);
            }
            if (enc4 != 64) {
                output = output + String.fromCharCode(chr3);
            }

        }

        output = Base64._utf8_decode(output);

        return output;

    },

// private method for UTF-8 encoding
    _utf8_encode : function (string) {
        string = string.replace(/\r\n/g,"\n");
        var utftext = "";

        for (var n = 0; n < string.length; n++) {

            var c = string.charCodeAt(n);

            if (c < 128) {
                utftext += String.fromCharCode(c);
            }
            else if((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            }
            else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }

        }

        return utftext;
    },

// private method for UTF-8 decoding
    _utf8_decode : function (utftext) {
        var string = "";
        var i = 0;
        var c = c1 = c2 = 0;

        while ( i < utftext.length ) {

            c = utftext.charCodeAt(i);

            if (c < 128) {
                string += String.fromCharCode(c);
                i++;
            }
            else if((c > 191) && (c < 224)) {
                c2 = utftext.charCodeAt(i+1);
                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                i += 2;
            }
            else {
                c2 = utftext.charCodeAt(i+1);
                c3 = utftext.charCodeAt(i+2);
                string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                i += 3;
            }

        }
        return string;
    }
}


/*
 * Canvas2Image v0.1
 * Copyright (c) 2008 Jacob Seidelin, jseidelin@nihilogic.dk
 * MIT License [http://www.opensource.org/licenses/mit-license.php]
 */

var Canvas2Image = (function() {

    // check if we have canvas support
    var bHasCanvas = false;
    var oCanvas = document.createElement("canvas");
    if (oCanvas.getContext("2d")) {
        bHasCanvas = true;
    }

    // no canvas, bail out.
    if (!bHasCanvas) {
        return {
            saveAsBMP : function(){},
            saveAsPNG : function(){},
            saveAsJPEG : function(){}
        }
    }

    var bHasImageData = !!(oCanvas.getContext("2d").getImageData);
    var bHasDataURL = !!(oCanvas.toDataURL);
    var bHasBase64 = !!(window.btoa);

    var strDownloadMime = "image/octet-stream";

    // ok, we're good
    var readCanvasData = function(oCanvas) {
        var iWidth = parseInt(oCanvas.width);
        var iHeight = parseInt(oCanvas.height);
        return oCanvas.getContext("2d").getImageData(0,0,iWidth,iHeight);
    }

    // base64 encodes either a string or an array of charcodes
    var encodeData = function(data) {
        var strData = "";
        if (typeof data == "string") {
            strData = data;
        } else {
            var aData = data;
            for (var i=0;i<aData.length;i++) {
                strData += String.fromCharCode(aData[i]);
            }
        }
        return btoa(strData);
    }

    // creates a base64 encoded string containing BMP data
    // takes an imagedata object as argument
    var createBMP = function(oData) {
        var aHeader = [];

        var iWidth = oData.width;
        var iHeight = oData.height;

        aHeader.push(0x42); // magic 1
        aHeader.push(0x4D);

        var iFileSize = iWidth*iHeight*3 + 54; // total header size = 54 bytes
        aHeader.push(iFileSize % 256); iFileSize = Math.floor(iFileSize / 256);
        aHeader.push(iFileSize % 256); iFileSize = Math.floor(iFileSize / 256);
        aHeader.push(iFileSize % 256); iFileSize = Math.floor(iFileSize / 256);
        aHeader.push(iFileSize % 256);

        aHeader.push(0); // reserved
        aHeader.push(0);
        aHeader.push(0); // reserved
        aHeader.push(0);

        aHeader.push(54); // dataoffset
        aHeader.push(0);
        aHeader.push(0);
        aHeader.push(0);

        var aInfoHeader = [];
        aInfoHeader.push(40); // info header size
        aInfoHeader.push(0);
        aInfoHeader.push(0);
        aInfoHeader.push(0);

        var iImageWidth = iWidth;
        aInfoHeader.push(iImageWidth % 256); iImageWidth = Math.floor(iImageWidth / 256);
        aInfoHeader.push(iImageWidth % 256); iImageWidth = Math.floor(iImageWidth / 256);
        aInfoHeader.push(iImageWidth % 256); iImageWidth = Math.floor(iImageWidth / 256);
        aInfoHeader.push(iImageWidth % 256);

        var iImageHeight = iHeight;
        aInfoHeader.push(iImageHeight % 256); iImageHeight = Math.floor(iImageHeight / 256);
        aInfoHeader.push(iImageHeight % 256); iImageHeight = Math.floor(iImageHeight / 256);
        aInfoHeader.push(iImageHeight % 256); iImageHeight = Math.floor(iImageHeight / 256);
        aInfoHeader.push(iImageHeight % 256);

        aInfoHeader.push(1); // num of planes
        aInfoHeader.push(0);

        aInfoHeader.push(24); // num of bits per pixel
        aInfoHeader.push(0);

        aInfoHeader.push(0); // compression = none
        aInfoHeader.push(0);
        aInfoHeader.push(0);
        aInfoHeader.push(0);

        var iDataSize = iWidth*iHeight*3;
        aInfoHeader.push(iDataSize % 256); iDataSize = Math.floor(iDataSize / 256);
        aInfoHeader.push(iDataSize % 256); iDataSize = Math.floor(iDataSize / 256);
        aInfoHeader.push(iDataSize % 256); iDataSize = Math.floor(iDataSize / 256);
        aInfoHeader.push(iDataSize % 256);

        for (var i=0;i<16;i++) {
            aInfoHeader.push(0);	// these bytes not used
        }

        var iPadding = (4 - ((iWidth * 3) % 4)) % 4;

        var aImgData = oData.data;

        var strPixelData = "";
        var y = iHeight;
        do {
            var iOffsetY = iWidth*(y-1)*4;
            var strPixelRow = "";
            for (var x=0;x<iWidth;x++) {
                var iOffsetX = 4*x;

                strPixelRow += String.fromCharCode(aImgData[iOffsetY+iOffsetX+2]);
                strPixelRow += String.fromCharCode(aImgData[iOffsetY+iOffsetX+1]);
                strPixelRow += String.fromCharCode(aImgData[iOffsetY+iOffsetX]);
            }
            for (var c=0;c<iPadding;c++) {
                strPixelRow += String.fromCharCode(0);
            }
            strPixelData += strPixelRow;
        } while (--y);

        var strEncoded = encodeData(aHeader.concat(aInfoHeader)) + encodeData(strPixelData);

        return strEncoded;
    }


    // sends the generated file to the client
    var saveFile = function(strData) {
        document.location.href = strData;
    }

    var makeDataURI = function(strData, strMime) {
        return "data:" + strMime + ";base64," + strData;
    }

    // generates a <img> object containing the imagedata
    var makeImageObject = function(strSource) {
        var oImgElement = document.createElement("img");
        oImgElement.src = strSource;
        return oImgElement;
    }

    var scaleCanvas = function(oCanvas, iWidth, iHeight) {
        if (iWidth && iHeight) {
            var oSaveCanvas = document.createElement("canvas");
            oSaveCanvas.width = iWidth;
            oSaveCanvas.height = iHeight;
            oSaveCanvas.style.width = iWidth+"px";
            oSaveCanvas.style.height = iHeight+"px";

            var oSaveCtx = oSaveCanvas.getContext("2d");

            oSaveCtx.drawImage(oCanvas, 0, 0, oCanvas.width, oCanvas.height, 0, 0, iWidth, iHeight);
            return oSaveCanvas;
        }
        return oCanvas;
    }

    return {

        saveAsPNG : function(oCanvas, bReturnImg, iWidth, iHeight) {
            if (!bHasDataURL) {
                return false;
            }
            var oScaledCanvas = scaleCanvas(oCanvas, iWidth, iHeight);
            var strData = oScaledCanvas.toDataURL("image/png");
            if (bReturnImg) {
                return makeImageObject(strData);
            } else {
                saveFile(strData.replace("image/png", strDownloadMime));
            }
            return true;
        },

        saveAsJPEG : function(oCanvas, bReturnImg, iWidth, iHeight) {
            if (!bHasDataURL) {
                return false;
            }

            var oScaledCanvas = scaleCanvas(oCanvas, iWidth, iHeight);
            var strMime = "image/jpeg";
            var strData = oScaledCanvas.toDataURL(strMime);

            // check if browser actually supports jpeg by looking for the mime type in the data uri.
            // if not, return false
            if (strData.indexOf(strMime) != 5) {
                return false;
            }

            if (bReturnImg) {
                return makeImageObject(strData);
            } else {
                saveFile(strData.replace(strMime, strDownloadMime));
            }
            return true;
        },

        saveAsBMP : function(oCanvas, bReturnImg, iWidth, iHeight) {
            if (!(bHasImageData && bHasBase64)) {
                return false;
            }

            var oScaledCanvas = scaleCanvas(oCanvas, iWidth, iHeight);

            var oData = readCanvasData(oScaledCanvas);
            var strImgData = createBMP(oData);
            if (bReturnImg) {
                return makeImageObject(makeDataURI(strImgData, "image/bmp"));
            } else {
                saveFile(makeDataURI(strImgData, strDownloadMime));
            }
            return true;
        }
    };

})();