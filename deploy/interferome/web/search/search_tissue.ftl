
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
        href="${base}/search/exportCsvFileTissueExpression.jspx">
    <img src="${base}/images/export.png" class="search_ctip_image" id="export_pic"/></a>
</div>
<br>
<br/>
<br>
<br/>
<div class="search_table_div">
    <div id="human_picture_container"
         style="margin-right: auto; margin-left: auto; padding: 15px; border: thin solid teal;">
        <h3>Human Expression in Unstimulated Tissues </h3>
            <div class="export_options" style="float: right;">
                <span class="saveimage">
                    <a href-lang='image/svg+xml' target='_blank' href="">
                        <img src="${base}/images/save_picture_icon.png" class="save_picture"/>
                    </a>

                </span>
            </div>
            <br />
            <br />
        <br />
        <br />
        <div class="tissueexp_headers" style="float:left;"></div>
        <div class="tissueexp_container" style="overflow-x: scroll;overflow-y: hidden;"></div>
        <div class="teid_table">
            <table class="tesites">
            <@s.iterator status="tissStat" value="humanGeneExpressionList" id="tissueResult">
            <tr>
                <@s.if test="#tissStat.first == true">
                    <th>Gene</th>
                    <th>Probe Id</th>
                    <@s.iterator status="headerStat" value="#tissueResult.tissueExpressionList" id="headerVal">
                        <th class="tissues"><@s.property value='#headerVal.tissue.tissueId' /></th>
                    </@s.iterator>
                </tr>
                <tr>
                </@s.if>
                <td class="geneName"><@s.property value='#tissueResult.geneName' /></td>
                <td class="probeId"><@s.property value='#tissueResult.probe.probeId' /> </td>
                <@s.iterator status="expStat" value="#tissueResult.tissueExpressionList" id="expVal">
                    <td class="expressionVal"><@s.property value='#expVal.expression' /></td>
                </@s.iterator>
            </tr>

            </@s.iterator>
            </table>
        </div>
    </div>

    <div id="mouse_picture_container"
         style="margin-right: auto; margin-left: auto; padding: 15px; border: thin solid teal;">
        <h3>Mouse Expression in Unstimulated Tissues</h3>

        <div class="export_options" style="float: right;">
                <span class="saveimage">
                    <a href-lang='image/svg+xml' target='_blank' href="">
                        <img src="${base}/images/save_picture_icon.png" class="save_picture"/>
                    </a>
                </span>
        </div>
        <br />
        <br />
        <br />
        <br />
        <div class="tissueexp_headers" style="float:left;"></div>
        <div class="tissueexp_container"  style="overflow-x: scroll;overflow-y: hidden;"></div>

        <div class="tissueexp_headers" style="float:left;"></div>
        <div class="tissueexp_container"></div>
        <div class="teid_table">
            <table class="tesites">
            <@s.iterator status="tissStat" value="mouseGeneExpressionList" id="tissueResult">
            <tr>
                <@s.if test="#tissStat.first == true">
                    <th>Gene</th>
                    <th>Probe Id</th>
                    <@s.iterator status="headerStat" value="#tissueResult.tissueExpressionList" id="headerVal">
                        <th class="tissues"><@s.property value='#headerVal.tissue.tissueId' /></th>
                    </@s.iterator>
                </tr>
                <tr>
                </@s.if>
                <td class="geneName"><@s.property value='#tissueResult.geneName' /></td>
                <td class="probeId"><@s.property value='#tissueResult.probe.probeId' /> </td>
                <@s.iterator status="expStat" value="#tissueResult.tissueExpressionList" id="expVal">
                    <td class="expressionVal"><@s.property value='#expVal.expression' /></td>
                </@s.iterator>
            </tr>

            </@s.iterator>
            </table>

        </div>

    </div>
 <span style="color: gray; font-size: 10pt">
     <p>The images on top use a heat map to display the expression of
     interferon regulated genes (IRGs) in their resting, unstimulated state, across various tissues and cells.</p>
          <p>A separate plot is produced for human and mouse data.</p>
          <p>The human and mouse expression data were obtained from the tissues and cell lines data in the BioGPS portal<sup>*</sup>.
          </p>
          <p>The first two columns show the name of the gene returned in the result set and the name of probe asociated
              with this gene within the BioGPS dataset</p>
          <p>The IRG list resulting from the search is plotted against expression in these tissues and cells; deep red indicates very high expression, pale red
              indicates moderately high expression, pale blue indicates moderately low
              expression, and deep blue indicates very low expression.  Boxes that
              appear to be white have expression values very close to the median for that tissue.</p>
          <p>Some gene names are associated with more than one probe within the BioGPS data set, in which case data from
              both probes are presented on different rows.</p>
    </span>

    <br>
    <br/>

    <p><sup>*</sup>Chunlei Wu, Ian MacLeod, and Andrew I. Su. (2012) <i>BioGPS and MyGene.info: organizing online,
        gene-centric information.</i> Nucleic Acids Research<b>41(D1):</b> D561-D565.</p>
</div>
<br/>