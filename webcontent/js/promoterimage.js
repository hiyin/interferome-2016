/**
 * Created with IntelliJ IDEA.
 * User: samf
 * Date: 3/07/12
 * Time: 11:17 PM
 * To change this template use File | Settings | File Templates.
 */
var paper;
function PromoterImage(){
    //This is the background
    var rowCount = countRows();
    paper = new Raphael(document.getElementById('promoter_container'), 1000, 50+(87.5*rowCount));
    //

    paper.path("M 30 25 l 700 0");
    var label = 1;
    for(var i = 30; i <= 730; i = i+87.5){
        if(label == 1){
            var title = ((((i - 30)) * 2000)/700) - 1500;
            paper.text(i, 10, title + "bp");
            paper.path("M " + i + ", 20 l 0 10");
            label = 0;
        }
        else{
            label = 1;
            paper.path("M " + i + ", 25 l 0 5");
        }
    }

    var tfLegendSiteList = new Array();
    $('#tfsites tr').each(function() {

        var geneName = $(this).find(".tfgene").html();
        if(null != geneName){
            var text = $(this).find(".tfsites").html().replace(/[ \n]/g,"");
            var tfRow = text.split("<br>");
            var tfSites = new Array();
            for(var rowCount = 0; rowCount < tfRow.length; rowCount++){
                tfSites[rowCount] = tfRow[rowCount].split(",");
                //Add TF Site to Array if it isnt in there
                if(tfLegendSiteList.indexOf(tfSites[rowCount][0]) == -1){
                    tfLegendSiteList.push(tfSites[rowCount][0]);
                }

            }

            addLine(paper, geneName, tfSites);

        }

    });

    var xPos = 30;//100;
    var yPos=90;
    var legCount=4; //var to limit legend to 10 per line
    for(var j = 0; j < tfLegendSiteList.length; j++){
        if(j==legCount){
            legCount=legCount+4;
            yPos=yPos+20;
            xPos=30;//100;
        }
        if(tfLegendSiteList[j] != null && tfLegendSiteList[j] != ""){
            var color = findColor(tfLegendSiteList[j]);

            if(color != null){
                //var site = paper.rect(xPos, (25*rowCount)+90, 10, 10);
                var site = paper.rect(xPos, (25*rowCount)+yPos, 10, 10);
                site.attr({fill: color, stroke: color, title: tfLegendSiteList[j]});
                xPos = xPos + 65; // Default 15
                //var lbl = paper.text(xPos, (25*rowCount)+95, tfLegendSiteList[j]);
                var lbl = paper.text(xPos, (25*rowCount)+yPos+5, tfLegendSiteList[j]);
                lbl.attr({'text-anchor': 'start'});
                //yPos = yPos + (tfLegendSiteList[j].toString().length*2);
                var len = tfLegendSiteList[j].toString().length*10;
                //var len = tfLegendSiteList[colVar].toString().length*10;
                xPos = xPos + 130; // Default len
            }
        }

    }
    var svg = paper.toSVG();
    var b64 = Base64.encode(svg);
    document.getElementById("saveimage").innerHTML = "Save as an Image" +
        "<a href-lang='image/svg+xml' target='_blank' href='data:image/svg+xml;base64,\n"+b64+"' title='promoter.svg'>"
        + '<img src="/interferome/images/save_picture_icon.png" />' +
        "</a>";
    document.getElementById("tf_table").style.display="none";



}

function save(){
    alert(paper);
    savesvg(paper);
}

function countRows(){
    var count = 0;
    $('#tfsites tr').each(function() {

        count++;

    });
    return count;
}


function addLine(paper, gene, siteList){
    addLine.width = 700;
    if(null == addLine.yCoord){
        addLine.yCoord = 60;
    }
    else{
        addLine.yCoord = addLine.yCoord + 25;
    }

    paper.text(25, addLine.yCoord-10, gene);
    paper.path("M 30 " + addLine.yCoord + " l " + addLine.width + " 0");
    for(var i = 0; i < siteList.length; i++){
        var siteStart = (parseInt(siteList[i][3])*(addLine.width/2000))+30;
        var siteEnd = (parseInt(siteList[i][4])*(addLine.width/2000))+30;
        var siteLength = siteEnd - siteStart;

        var color = findColor(siteList[i][0]);
        if(color != null){
            var site = paper.rect(siteStart, addLine.yCoord-5, siteLength, 10);

            var tip = "Site: " + siteList[i][0] + "\n\tCore Match: " + siteList[i][1] + "\n\tMatrix Match: " + siteList[i][2];
            site.attr({fill: color, stroke: color, title: tip});
        }

    }
}

function findColor(site){
    if(findColor.factorColors == null){
        findColor.factorColors = new Array();
    }
    if(findColor.colorPos == null){
        findColor.colorPos = 0;
    }


    findColor.colors = new Array("Red","Green","Blue", "Yellow", "Purple", "Pink", "Cyan", "Orange", "Brown", "lightblue", "lightgreen", "Black", "Gray", "navy", "slateblue", "magenta", "wheat", "cornflowerblue", "goldenrod", "olive");
    if(findColor.factorColors[site] != null){
        return findColor.factorColors[site];
    }
    else{
        if(findColor.colorPos < 20){//< findColor.colors.length){//
            var color = findColor.colors[findColor.colorPos];
            findColor.factorColors[site] = color;
            findColor.colorPos = findColor.colorPos+1;
            if(findColor.colorPos==20){findColor.colorPos=0;}  // recycle colours for big list ...
            return color;
        }
        else{
            return null;
        }
    }
}
