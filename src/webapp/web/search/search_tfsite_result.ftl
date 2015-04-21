<div class="result_title_div">
    Search Results
</div>
<div class="nojava">
    Sites shown are only the most prevalent for the dataset searched.
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
    href="${base}/search/exportCsvFileTFanalysis.jspx">
    <img src="${base}/images/export.png" class="search_ctip_image" id="export_pic"/></a>
</div>
<br>
<br/>
<br>
<br/>
<div id="saveimage" class="export_pic"></div>
<br>
<br/>
<br>
<br/>
<div class="search_table_div">
    <div id="promoter_container"></div>
    <div id="tf_table">
        <table id="tfsites">
                        <tr>
                            <td>Gene</td><td>Sites</td>
                        </tr>
                    <@s.iterator status="status" value="tfSiteList">

                        <tr>
                            <td class="tfgene"><@s.property value="key" /></td>
                            <td class="tfsites">
                                <@s.iterator status="tfStatus" value="%{value}" id="tfSite">
                                    <@s.property value="#tfSite.factor" />,
                                    <@s.property value="#tfSite.coreMatch" />,
                                    <@s.property value="#tfSite.matrixMatch" />,
                                    <@s.property value="#tfSite.start" />,
                                    <@s.property value="#tfSite.end" /><br />
                                </@s.iterator>

                            </td>

                    </@s.iterator>
        </table>
    </div>
        <span style="color: gray; font-size: 10pt"><p>The location of transcription factors on each of the genes from the region spaning -1500bp to + 500 bp from the start site.</p>
    <p>Each coloured box represents a specific transcription factor, the key for which is provided below the graphic.</p>
        <p>Move the cursor over the transciption factor's coloured box to reveal the TRANSFAC<sup>*</sup> predicted match between the transcription factor and its predicted binding site.</p>
    </span>
    <br>
    <br/>
    <p><sup>*</sup>V. Matys, E. Fricke,R. Geffers, E. Gößling, M. Haubrock, R. Hehl, K. Hornischer, D. Karas, A. E. Kel, O. V. Kel-Margoulis, D.-U. Kloos, S. Land, B. Lewicki-Potapov, H. Michael, R. Münch, I. Reuter, S. Rotert, H. Saxel, M. Scheer, S. Thiele, and E. Wingender. (2003) <i>TRANSFAC®: transcriptional regulation, from patterns to profiles</i> Nucleic Acids Research <b>31:</b> 374-378.</p>
</div>
<br/>

