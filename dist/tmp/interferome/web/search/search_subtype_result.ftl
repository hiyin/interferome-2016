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
    href="${base}/search/exportCsvFileTypes.jspx">
    <img src="${base}/images/export.png" class="search_ctip_image" id="export_pic"/></a>
</div>
<br>
<br/>
<br>
<br/>
<div id="saveimage" class="export_pic"></div>
<div class="search_table_div">
<div id="venn">
  <span style="color: gray; font-size: 10pt" xmlns="http://www.w3.org/1999/html">The Venn diagram shows the number of genes regulated by one or more IFN type (Type I, II or III).  It should be noted that there are far more  datasets for genes regulated by type I than for types II or III, this imbalance introduces the risk of false nagatives and bias for the under-represented types II and III; caution is therefore encouraged in interpreting low or negative results from these types.</span>
<div id="hiddenlist">
<ul>
<li id="t1" value="<@s.property value="subtypeList[0]"/>">Type I: &nbsp; <@s.property value="subtypeList[0]" /></li>
                    <li id="t2" value="<@s.property value="subtypeList[1]"/>">Type II: &nbsp; <@s.property value="subtypeList[1]"/></li>
    <li id="t3" value="<@s.property value="subtypeList[2]"/>">Type III: &nbsp; <@s.property value="subtypeList[2]"/></li>
    <li id="t1t2" value="<@s.property value="subtypeList[3]"/>">Type I + II: &nbsp; <@s.property value="subtypeList[3]"/></li>
    <li id="t1t3" value="<@s.property value="subtypeList[4]"/>">Type I + III: &nbsp; <@s.property value="subtypeList[4]"/></li>
    <li id="t2t3" value="<@s.property value="subtypeList[5]"/>">Type II + III: &nbsp; <@s.property value="subtypeList[5]"/></li>
    <li id="t1t2t3" value="<@s.property value="subtypeList[6]"/>">Type I + II + III: &nbsp; <@s.property value="subtypeList[6]"/></li>
</ul>
</div>
</div>
</div>
<br/>