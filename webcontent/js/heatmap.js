/**
 * Created with IntelliJ IDEA.
 * User: samf
 * Date: 3/07/12
 * Time: 11:17 PM
 * To change this template use File | Settings | File Templates.
 */

function HeatMap(container_obj){
    //This is the background

    container = $(container_obj);
    //
    var oTable = container.find("table.tesites")[0];
    var row_header_container = container.find('div.tissueexp_headers')[0];
    var row_data_container = container.find('div.tissueexp_container')[0];
    var link = $("span.saveimage a", container);
    var form_input = $("form input", container);

    if (oTable.rows.length == 0){
        container.css("display", "none");
        return;
    }

    // for display
    drawHeatmap(oTable, row_data_container, row_header_container);

    // for saving
    var hiddenPaper = $('<div></div>').appendTo(container)[0];
    var paper = drawHiddenHeatmap(oTable, hiddenPaper);
    $(hiddenPaper).css("display", "none");

    var paperSVG = paper.toSVG("end");
    var b64 = Base64.encode(paperSVG);
    link.attr("href", "data:image/svg+xml;base64,\n"+b64);
    link.attr("title", "heatmap.svg");

    $(oTable).css("display", "none");
}
function scroll_d(a,b) {document.getElementById(a).scrollTop = document.getElementById(b).scrollTop; }
function scroll_h(a,b) {document.getElementById(b).scrollTop = document.getElementById(a).scrollTop; }

function drawHiddenHeatmap(oTable, row_data_container){

    //Skip first (header) row
    var rowLength = oTable.rows.length;
    var dataColumns = oTable.rows.item(0).cells.length - 2;
    var dataStart = 2;
    var boxWidth = 10;
    var boxHeight = 10;
    var rowHeaderWidth = 160;
    var totalHeight = 250+(boxHeight*rowLength);

    $(row_data_container).height(totalHeight);

    var paper = new Raphael(row_data_container, rowHeaderWidth + (boxWidth*dataColumns) +20 , totalHeight);

    var colorStats = calculateColorStatistics(oTable, dataColumns, dataStart);
    var yPos = 200;

    // Add Header for row titles
    var geneEndPos = 80;
    var probeEndPos = rowHeaderWidth - 10;
    var geneColTitle = paper.text(geneEndPos, yPos - 18, "Gene");
    geneColTitle.attr({'text-anchor': 'end'});
    var probeColTitle = paper.text(probeEndPos, yPos - 20, "Probe Id");
    probeColTitle.attr({'text-anchor': 'end'});

    //Draw Header
    var initial_x = rowHeaderWidth + 10;
    var header_x = initial_x;
    for(var k = 0; k < dataColumns;k++){
        var tissue = oTable.rows.item(0).cells.item(k+dataStart).innerHTML;
        var text = paper.text(header_x, 190, tissue);
        text.transform("t5,100r90t-100,0");
        text.attr({'text-anchor': 'end'});
        header_x += boxWidth;
    }

    //Add Data

    //Skip the tissues row;
    for (i = 1; i < oTable.rows.length; i++){
        var oCells = oTable.rows.item(i).cells;
        //Add Gene Name
        var geneName = oCells.item(0).innerHTML;
        var probeName = oCells.item(1).innerHTML;
        var geneText = paper.text(geneEndPos, yPos+(boxHeight/2), geneName);
        geneText.attr({'text-anchor': 'end'});
        var probeText = paper.text(probeEndPos, yPos+(boxHeight/2),probeName);
        probeText.attr({'text-anchor': 'end'});

        // check that the text does not exceed the width of the box. If it does, widen the container!
        /*
        var textRight = probeText.getBBox().x2;
        if (textRight > rowHeaderWidth){
            //alert("pause")    ;
            $(row_header_container).width(textRight+20);
        }
        */



        var data_x = initial_x;
        //Skip the header columns (gene name, probe id)
        for(var j = 0; j < dataColumns; j++){
            var cell = oCells.item(j+dataStart);
            if(cell != null){
                var cellVal = cell.innerHTML;
                if(cellVal != null){
                    if(cellVal != null && cellVal != ""){
                        var rect = paper.rect(data_x, yPos, boxWidth, boxHeight);
                        var color = getColor(colorStats[0], colorStats[1], colorStats[2], cellVal);
                        //alert("color returned: " + color);
                        rect.attr({fill: color, stroke:"black"});// set to black for debugging
                        data_x += boxWidth;
                    }
                }
            }

        }
        yPos=yPos+boxHeight;
    }
    return paper;
}

function drawHeatmap(oTable, row_data_container, row_header_container){



    //Skip first (header) row
    var rowLength = oTable.rows.length;
    var dataColumns = oTable.rows.item(0).cells.length - 2;
    var dataStart = 2;
    var boxWidth = 10;
    var boxHeight = 10;
    var rowHeaderWidth = 50;

    var totalHeight = 230+(boxHeight*rowLength);

    $(row_data_container).height(totalHeight);
    $(row_header_container).height(totalHeight);
    $(row_header_container).width(rowHeaderWidth);
    var rowHeader
        = new Raphael(row_header_container, "100%", totalHeight);

    var paper = new Raphael(row_data_container, (boxWidth*dataColumns) +20 , totalHeight);

    var colorStats = calculateColorStatistics(oTable, dataColumns, dataStart);
    var yPos = 200;

    // Add Header for row titles
    var geneEndPos = 70;
    var probeStartPos = 80;
    var separatorPos = 75;
    var geneColTitle = rowHeader.text(geneEndPos, yPos - 20, "Gene");
    geneColTitle.attr({'text-anchor': 'end'});
    rowHeader.text(separatorPos, yPos - 20, " - ");
    var probeColTitle = rowHeader.text(probeStartPos, yPos - 20, "Probe Id");
    probeColTitle.attr({'text-anchor': 'start'});

    //Draw Header
    var initial_x = 10;
    var header_x = initial_x;
    for(var k = 0; k < dataColumns;k++){
        var tissue = oTable.rows.item(0).cells.item(k+dataStart).innerHTML;
        var text = paper.text(header_x, 190, tissue);
        text.transform("t5,100r90t-100,0");
        text.attr({'text-anchor': 'end'});
        header_x += boxWidth;
    }

    //Add Data

    //Skip the tissues row;
    for (i = 1; i < oTable.rows.length; i++){
        var oCells = oTable.rows.item(i).cells;
        //Add Gene Name
        var geneName = oCells.item(0).innerHTML;
        var probeName = oCells.item(1).innerHTML;
        var geneText = rowHeader.text(geneEndPos, yPos+(boxHeight/2), geneName);
        geneText.attr({'text-anchor': 'end'});
        var probeText = rowHeader.text(probeStartPos, yPos+(boxHeight/2),probeName);
        probeText.attr({'text-anchor': 'start'});
        rowHeader.text(separatorPos, yPos+(boxHeight/2), " - ");

        // check that the text does not exceed the width of the box. If it does, widen the container!

        var textRight = probeText.getBBox().x2;
        if (textRight > rowHeaderWidth){
            //alert("pause")    ;
            $(row_header_container).width(textRight+20);
        }

        var data_x = initial_x;
        //Skip the header columns (gene name, probe id)
        for(var j = 0; j < dataColumns; j++){
            var cell = oCells.item(j+dataStart);
            if(cell != null){
                var cellVal = cell.innerHTML;
                if(cellVal != null){
                    if(cellVal != null && cellVal != ""){
                        var rect = paper.rect(data_x, yPos, boxWidth, boxHeight);
                        var color = getColor(colorStats[0], colorStats[1], colorStats[2], cellVal);
                        //alert("color returned: " + color);
                        rect.attr({fill: color, stroke:"black"});// set to black for debugging
                        data_x += boxWidth;
                    }
                }
            }

        }
        yPos=yPos+boxHeight;
    }
    return paper;
}


function calculateColorStatistics(oTable, dataColumns, dataStart){
    var averageArray = new Array();
    var max = oTable.rows.item(1).cells.item(2).innerHTML;
    var min = oTable.rows.item(1).cells.item(2).innerHTML;
    for (i = 1; i < oTable.rows.length; i++){
        //Skip the header columns (gene name, probe id)
        var rowItem = oTable.rows.item(0);
        if(rowItem != null){
            for(var j = 0; j < dataColumns; j++){
                var row = oTable.rows.item(i);
                if(row != null){
                   cell = row.cells.item(j+dataStart);
                   if(cell != null){
                       var cellVal = cell.innerHTML;
                       if(cellVal != null){
                           cellVal = parseFloat(cellVal);
                           averageArray.push(cellVal);
                           if(cellVal < min){
                               min = cellVal;

                           }
                           if(cellVal > max){
                               max = cellVal;
                           }
                       }
                   }
                }
            }
        }
    }
    var returnVal = new Array();
    returnVal[0] = median(averageArray)
    returnVal[1] = min;
    returnVal[2] = max;
    return returnVal;
}

function getColor(median, min, max, value){
    //Choose Color on blue/green scale

    //alert("value: " + value + ", median: "+median);

    if(parseFloat(value) < median){
        var blueVal = Math.round(255 - (255/(min-median))*(parseFloat(value)-median));
        var color = "rgb(" + blueVal + ", " + blueVal + ", 255)";
        return color;
    }
    else{
        //Choose Color on red scale
        if(parseFloat(value) > median){
            var redVal = Math.round(255 - (255/(max-median))*(parseFloat(value)-median));
            var color = "rgb(255, " + redVal + ", " + redVal + ")";
            return color;
        }
        //Choose white
        else{
            //alert("returning white");
            return "White";
        }
    }


}

function median(values) {

    values.sort( function(a,b) {return a - b;} );

    var half = Math.floor(values.length/2);

    if(values.length % 2)
        return values[half];
    else
        return (values[half-1] + values[half]) / 2.0;
}


//Code From : http://haacked.com/archive/2009/12/29/convert-rgb-to-hex.aspx
function colorToHex(color) {
    if (color.substr(0, 1) === '#') {
        return color;
    }
    var digits = /(.*?)rgb\((\d+), (\d+), (\d+)\)/.exec(color);

    var red = parseInt(digits[2]);
    var green = parseInt(digits[3]);
    var blue = parseInt(digits[4]);

    var rgb = blue | (green << 8) | (red << 16);
    return digits[1] + '#' + rgb.toString(16);
};


