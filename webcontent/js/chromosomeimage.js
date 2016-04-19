/**
 * Created with IntelliJ IDEA.
 * User: helen
 * Date: 3/07/12
 * Time: 11:17 PM
 * To change this template use File | Settings | File Templates.
 */

function ChromosomeImage(){
// to draw chromosomes

    var separator = 0;
    var spacer = 0;


    //Human Chromosome
    var hsChrom = new Object(); // or just {}
    hsChrom['1'] = new Array(121, 246);
    hsChrom['2'] = new Array(92, 244);
    hsChrom['3'] = new Array(90, 200);
    hsChrom['4'] = new Array(50, 192);
    hsChrom['5'] = new Array(46, 181);
    hsChrom['6'] = new Array(59, 171);
    hsChrom['7'] = new Array(58, 159);
    hsChrom['8'] = new Array(44, 146);
    hsChrom['9'] = new Array(47, 135);
    hsChrom['10'] = new Array(40, 136);
    hsChrom['11'] = new Array(53, 135);
    hsChrom['12'] = new Array(36, 134);
    hsChrom['13'] = new Array(17, 115);
    hsChrom['14'] = new Array(17, 106);
    hsChrom['15'] = new Array(18, 101);
    hsChrom['16'] = new Array(37, 90);
    hsChrom['17'] = new Array(24, 82);
    hsChrom['18'] = new Array(17, 78);
    hsChrom['19'] = new Array(26, 64);
    hsChrom['20'] = new Array(28, 64);
    hsChrom['21'] = new Array(13, 47);
    hsChrom['22'] = new Array(15, 50);
    hsChrom['X'] = new Array(60, 153);
    hsChrom['Y'] = new Array(12, 51);


    //Mouse Chromosome
    var mmChrom = new Object(); // or just {}
    mmChrom['1'] = new Array(0, 198);
    mmChrom['2'] = new Array(0, 182);
    mmChrom['3'] = new Array(0, 160);
    mmChrom['4'] = new Array(0, 153);
    mmChrom['5'] = new Array(0, 153);
    mmChrom['6'] = new Array(0, 150);
    mmChrom['7'] = new Array(0, 153);
    mmChrom['8'] = new Array(0, 132);
    mmChrom['9'] = new Array(0, 125);
    mmChrom['10'] = new Array(0, 130);
    mmChrom['11'] = new Array(0, 122);
    mmChrom['12'] = new Array(0, 122);
    mmChrom['13'] = new Array(0, 121);
    mmChrom['14'] = new Array(0, 126);
    mmChrom['15'] = new Array(0, 104);
    mmChrom['16'] = new Array(0, 99);
    mmChrom['17'] = new Array(0, 96);
    mmChrom['18'] = new Array(0, 91);
    mmChrom['19'] = new Array(0, 62);
    mmChrom['X'] = new Array(0, 167);
    mmChrom['Y'] = new Array(0, 16);

    //count how  many results for human ...
    var hsl= document.getElementById("hsGenePos");
    var rowh=0;
    for (var r = 1, row; row = hsl.rows[r]; r++) {
        rowh=r;
    }
    // count how many results for mouse ...
    var mml= document.getElementById("mmGenePos");
    var rowm=0;
    for (var r = 1, row; row = mml.rows[r]; r++) {
        rowm=r;
    }
    //draw both genomes is there are genes for both human and mouse ...

    var mmPaper;
    var hsPaper;

    if (rowh != 0 && rowm !=0){
        mmPaper = new Raphael(document.getElementById('mm_chromosome_container'), 500, 350);
        hsPaper = new Raphael(document.getElementById('hs_chromosome_container'), 500, 350);
        drawChromosome(hsPaper, hsChrom, document.getElementById("hsGenePos"), "Human Chromosomal Locations");
        drawChromosome(mmPaper, mmChrom, document.getElementById("mmGenePos"), "Mouse Chromosomal Locations");
        saveChromosome(hsPaper, "hs_saveimage", "human");
        saveChromosome(mmPaper, "mm_saveimage", "mouse");

    }
    //else draw only human if mouse is missing
    else if (rowh != 0 && rowm ==0){
        hsPaper = new Raphael(document.getElementById('hs_chromosome_container'), 500, 350);
        drawChromosome(hsPaper, hsChrom, document.getElementById("hsGenePos"), "Human Chromosomal Locations");
        saveChromosome(hsPaper, "hs_saveimage", "human");
    }
    //else draw only mouse if human is missing ...
    else if (rowh == 0 && rowm !=0){
        mmPaper = new Raphael(document.getElementById('mm_chromosome_container'), 500, 350);
        drawChromosome(mmPaper, mmChrom, document.getElementById("mmGenePos"), "Mouse Chromosomal Locations");
        saveChromosome(mmPaper, "mm_saveimage", "mouse");
    }

     //Successful Drawing of Chromosomes hide data table
    document.getElementById("chromosome_result_tab").style.display="none";
    document.getElementById("gene_table").style.display="none";
}

function drawChromosome(paper, visHashChrom, gene_pos_table, header){
    // show the values stored

    var spacer = 0;
    for (var chromName in visHashChrom) {
        // use hasOwnProperty to filter out keys from the Object.prototype
        if (visHashChrom.hasOwnProperty(chromName)) {
            //
            var centromere_pos = visHashChrom[chromName][0];
            var height = visHashChrom[chromName][1];

            //writing the name of each chromosome
            paper.text((5 + spacer), 330, chromName);
            // drawing each chromosome

            var top_length = height - centromere_pos - 10;
            if(top_length < 0){
                top_length = 0;
            }
            var bottom_length = height - top_length - 10;
            if(bottom_length < 0){
                bottom_length = 0;
            }
            var height_add = 288 - bottom_length;
            var centromere_height = 294 - bottom_length;
            var centromere_space = spacer + 5;

            var chromo_bottom = paper.path("M "+spacer+" 300 l 0 -"+bottom_length+" c 0 -5 10 -5 10 0 l 0 "+bottom_length+" c 0 5 -10 5 -10 0");
            chromo_bottom.attr({gradient:'0-#333-#fff-#333'});

            var chromo_top = paper.path("M "+spacer+" "+height_add+" l 0 -"+top_length+" c 0 -5 10 -5 10 0 l 0 "+top_length+" c 0 5 -10 5 -10 0");
            chromo_top.attr({gradient:'0-#333-#fff-#333'});



            if (centromere_pos != height){
                var centromere = paper.ellipse(centromere_space, centromere_height, 5, 2);
                centromere.attr({fill: 'black'});
            }

            else {

                var top = 294 - centromere_pos;
                var centromere = paper.ellipse(centromere_space, top , 5, 2);
                centromere.attr({fill: 'black'});
            }
        }
        addGenesToChromosome(gene_pos_table, chromName, paper, spacer);
        spacer = spacer + 20;
    }
    //Add Header
    var lbl = paper.text(250, 30, header);
    lbl.attr({ "font-size": 16, "font-family": "Arial, Helvetica, sans-serif", "font-weight": "bold"});
}

function addGenesToChromosome(gene_pos_table, chromName, paper, spacer){
    // Taking each position of a gene from a table, then determining if a gene needs to be drawn on the current chromosome being drawn and if so
    // drawing the position of the genes on the chromosome


    for (var r = 1, row; row = gene_pos_table.rows[r]; r++) {
        var gene_name = "";
        var chromosome = "";
        var start = "";
        var end = "";

        for (var c = 0, col; col = row.cells[c]; c++) {

            if(col.firstChild != null){
                var cell_val = col.firstChild.nodeValue;

                if (c == 0){
                    gene_name = cell_val;
                }

                if (c==1){
                    chromosome = cell_val;
                }

                if (c==2){
                    start = cell_val;
                    start = start/1000000;
                }

                if (c==3){
                    end = cell_val;
                    end=end/1000000;
                }


                if (c==4){
                    var link = cell_val;

                    if (chromName == chromosome && start != null){

                        var line_height = 300 - start;
                        var gd = paper.path("M "+spacer+" "+line_height+" l 10 0");
                        gd.attr({'stroke-width': 2,
                            stroke: 'red',
                            gradient: '90-#700-#FF0-#700'});

                        // Allows information on the gene to be displayed when the mouse is held over the gene position

                        var display = gene_name + "  "+ chromName + ":" + (start*1000000) + " - " + (end*1000000);
                        gd.attr({title: display});

                        // This function in a function is to force js to evaluate the function and link the corrent gene name to a mouse click and
                        // then link to an ensembl gene summary page for that gene.
                        //							alert("before mouseover gene name ='"+gene_name + "'");
                        gd.node.onclick = linkOnClick(link);

                    }
                }
            }
        }
    }

    //This is the space between the chromosomes, it fits a maximum of 24 chromosomes in a 500 x 500 canvas. It could be changed to a dynamic variable that changed depending on the number of chromosomes.
}


function linkOnClick(gene_name){
    return function(){
//  	alert("from linkOnClick: " + gene_name);
        openGenePage(gene_name);
    };

}



function openGenePage(link) {
    //alert("in mouseover name =" + geneName);
    window.open(link);

}