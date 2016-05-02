<div class="result_title_div">
    Search Results
</div>
<div class="nojava">
    <p>(Javascript Must Be Enabled to View These Results)</p>
</div>
<div class="IE8">
    <p>
        Visualisations are not compatible with IE8 and below. Please upgrade to
        the latest version of Internet Explorer, or use another browser such as Mozilla Firefox
        or Google Chrome.
    </p>
</div>
<div class="export_div">
    Save as a TXT file<a
    href="${base}/search/exportCsvFileChromosome.jspx">
    <img src="${base}/images/export.png" class="search_ctip_image" id="export_pic"/></a>
</div>
<!--<div id="hs_saveimage" class="export_pic" ></div>
<div id="mm_saveimage" class="export_pic" ></div>  --->
<div class="search_table_div">
    <br>
    <br/>
    <br>
    <br/>
    <div id="hs_saveimage" class="export_pic" ></div>
    <div id="hs_chromosome_container"></div>
    <div id="mm_saveimage" class="export_pic" ></div>
    <div id="mm_chromosome_container"></div>

    <div id="gene_table">

        <!-- test if human and draw human table-->
        <table id="hsGenePos">
            <tr class="search_result_header">
                <td>Gene Name</td><td>Chromosome</td><td>Start</td><td>End</td><td>Link</td>
            </tr>
        <@s.iterator status="chromosomeGeneList" value="chromosomeGeneList" id="chrGeneResults">
                    <@s.if test="#chrGeneResults.ensgAccession.indexOf('ENSG') != -1">
                        <tr class="search_result_tab">
                            <td align="center"><@s.property value="#chrGeneResults.geneName"/></td>
                            <td align="center"><@s.property value="#chrGeneResults.chromosome"/></td>
                            <td align="center"><@s.property value="#chrGeneResults.startPosition"/></td>
                            <td align="center"><@s.property value="#chrGeneResults.endPosition"/></td>
                            <td align="center">${ensemblLink}<@s.property value="#chrGeneResults.ensgAccession"/></td>
                        </tr>
                    </@s.if>
            </@s.iterator>
        </table>

        <table id="mmGenePos">
            <tr class="search_result_header">
                <td>Gene Name</td><td>Chromosome</td><td>Start</td><td>End</td><td>Link</td>
            </tr>
        <@s.iterator status="chromosomeGeneList" value="chromosomeGeneList" id="chrGeneResults">
            <@s.if test="#chrGeneResults.ensgAccession.indexOf('ENSMUSG') != -1">
                <tr class="search_result_tab">
                    <td align="center"><@s.property value="#chrGeneResults.geneName"/></td>
                    <td align="center"><@s.property value="#chrGeneResults.chromosome"/></td>
                    <td align="center"><@s.property value="#chrGeneResults.startPosition"/></td>
                    <td align="center"><@s.property value="#chrGeneResults.endPosition"/></td>
                    <td align="center">${ensemblLink}<@s.property value="#chrGeneResults.ensgAccession"/></td>
                </tr>
            </@s.if>
        </@s.iterator>
        </table>
    </div>


    <table  id="chromosome_result_tab">
    <tr class="search_result_header"><td>Chromosome</td><td>Gene Count</td></tr>
    <@s.iterator status="chromosomeList" value="chromosomeList" id="chrResults">
       <tr class="search_result_tab">
            <td align="center"><@s.property value="#chrResults[0]"/></td>
            <td align="center"><@s.property value="#chrResults[1]"/></td>
       </tr>
    </@s.iterator>
    </table>



</div>
<br/>