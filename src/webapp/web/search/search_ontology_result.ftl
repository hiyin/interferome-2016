<div class="result_title_div">
    Search Results
</div>
<div class="data_header_div">
    <div class="export_div">
        Save as a TXT file <a
        href="${base}/search/exportCsvFileOntology.jspx">
        <img src="${base}/images/export.png" class="search_ctip_image" id="export_csv"/></a>
    </div>
</div>
<div class="search_table_div">
    <@s.iterator status="ontologyList" value="ontologyList" id="ontlResults">
            <div class="nojava">
                <p>(Javascript Must Be Enabled to View These Results)</p>
            </div>
            <div id="myCanvasContainer<@s.property value='%{#ontologyList.count}' />">
                <canvas width="500" height="300" id="myCanvas<@s.property value='%{#ontologyList.count}' />">
                    <p>In Internet Explorer versions up to 8, things inside the canvas are inaccessible!</p>
                </canvas>
            </div>
            <div id="tags<@s.property value='%{#ontologyList.count}' />">
                 <ul>
                  <@s.iterator status="ontlResults" value="#ontlResults" id="onResults">
                      <@s.if test="%{#ontlResults.count < 16}">
                        <@s.if test="%{#onResults[1] < 100}">
                            <li><a style='font-size:<@s.property value="%{#onResults[1]}/10"/>pt' href="${goLink}<@s.property value="#onResults[0].goTermAccession"/>" target="_blank"><@s.property value="#onResults[0].goTermName"/></a></li>
                        </@s.if>
                        <@s.else>
                            <li><a style='font-size:10pt' href="${goLink}<@s.property value="#onResults[0].goTermAccession"/>" target="_blank"><@s.property value="#onResults[0].goTermName"/></a></li>
                        </@s.else>
                      </@s.if>
                  </@s.iterator>
                </ul>
            </div>
        <@s.if test="#onResults[0].goDomain.id == 3">
            <span style="font-size: 12pt;font-weight: bold;">Molecular Function</span>
        </@s.if>
        <@s.if test="#onResults[0].goDomain.id == 2">
            <span style="font-size: 12pt;font-weight: bold;">Cellular Component</span>
        </@s.if>
        <@s.if test="#onResults[0].goDomain.id == 1">
            <span style="font-size: 12pt;font-weight: bold;">Biological Process</span>
        </@s.if>
        <table  class="search_result_tab">
            <tr class="search_result_header"><td>Accession</td><td>Term Name</td><td>Term Definition</td><td>Gene Count</td><td>p Value<sup>*</sup></td></tr>
            <@s.iterator status="ontlResults" value="#ontlResults" id="onResults">
                <tr>
                    <td><@s.property value="#onResults[0].goTermAccession"/></td>
                    <td><@s.property value="#onResults[0].goTermName"/></td>
                    <td><@s.property value="#onResults[0].goTermDefinition"/></td>
                    <td><@s.property value="#onResults[1]"/></td>
                    <td><@s.property value="#onResults[2]"/></td>
                    <!-- td>td>N/A</td-->
                </tr>
            </@s.iterator>
       </table>
    </@s.iterator>
    </table>
</div>
<br>
<br/>
<br>
<br/>
<span style="color: gray; font-size: 10pt" xmlns="http://www.w3.org/1999/html">*The enrichment of each gene ontology term was tested for significance using the hypergeometric mean.  The p value returned represents the probability of that each term would be the observed with the given frequency had the genes been drawn at random from across the entire genome.  A low p value (less than 0.05) for an ontological term indicates a low probability that so many hits to that term would have been observed if the results had been due to random effects, and such terms might be considered to be enriched in the result set.</span>
<span style="color: gray; font-size: 10pt"><p>Tavazoie S, Hughes JD, Campbell MJ, Cho RJ, Church GM. (1999) <i>Systematic determination of genetic network architecture.</i> Nature Genetics. <b>22:</b> 281–285.</p>
<span style="color: gray; font-size: 10pt"><p>Isabelle Rivals, Leon Personnaz, Lieng Taing and Marie-Claude Potier. (2007) <i>Enrichment or depletion of a GO category within a class of genes: which test?</i>  Bioinformatics.  <b>23:</b> 401-407.</p></span>

<br/>