
<div class="search_data_outer_div">
    <div class="search_field_row">
       <div class="search_field_title">Interferon Type: </div>
       <div class="search_field_value">
           <@s.select name="searchBean.ifnType"  id="ifn_type" headerKey="-1" headerValue="All"  list="ifnTypeMap" cssClass="search_select_mid"  />&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
           SubType: &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
           <@s.select name="searchBean.ifnSubType" id="ifn_sub_type"  headerKey="-1" headerValue="All" list="ifnSubTypeMap" cssClass="search_select_mid"  /> &nbsp;
       </div>
       <div style="clear:both"></div>
   </div>
</div>

<div class="search_line_space"></div>

<div class="search_data_outer_div">
    <div class="search_field_row">
        <div class="search_field_title">Treatment Concentration:</div>
        <div class="search_field_radio">
            <@s.radio name="searchBean.anyRangeDose" list="anyOrRanges" value="searchBean.anyRangeDose" id="search_dose" title="search by concentration"/>

            <@s.if test="%{searchBean.anyRangeDose =='any'}">
            <div class="dose_range_div" style="display: none;">
            </@s.if>
            <@s.else>
            <div class="dose_range_div">
            </@s.else>
                From: &nbsp;<@s.textfield name="searchBean.fromDose" cssClass="search_input"/>&nbsp;
                &nbsp; To: &nbsp;<@s.textfield name="searchBean.toDose" cssClass="search_input"/>  &nbsp; <font color="#858585">(IU/ml)</font>
               <a href="${base}/site/showHelp.jspx" target="_blank" id="tooltip_link"> &nbsp; &nbsp; <img src="${base}/images/info.png" class="search_ctip_image" /> &nbsp; &nbsp; </a>
            </div>
        </div>
        <div style="clear:both"></div>
    </div>

    <div class="blank_separator"></div>

    <div class="search_field_row">
        <div class="search_field_title">Treatment Time:</div>
        <div class="search_field_radio">
            <@s.radio name="searchBean.anyRangeTime" list="anyOrRanges" value="searchBean.anyRangeTime" id="search_time" title="search by treatment time"/>

            <@s.if test="%{searchBean.anyRangeTime =='any'}">
            <div class="time_range_div" style="display: none;">
            </@s.if>
            <@s.else>
            <div class="time_range_div">
            </@s.else>
                From: &nbsp;<@s.textfield name="searchBean.fromTime" cssClass="search_input"/>&nbsp;
                &nbsp; To: &nbsp;<@s.textfield name="searchBean.toTime" cssClass="search_input"/>  &nbsp; &nbsp; <font color="#858585">(hour)</font>
            </div>
        </div>
        <div style="clear:both"></div>
    </div>

</div>

<div class="search_line_space"></div>

<div class="search_data_outer_div">
    <div class="search_field_row">
       <div class="search_field_title">In Vivo | In Vitro:</div>
       <div class="search_field_value">
           <@s.select name="searchBean.vivoVitro" headerKey="-1" headerValue="All" list="vivoVitroMap" cssClass="search_select_mid"  />&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
           Species: &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
           <@s.select name="searchBean.species"  headerKey="-1" headerValue="All" list="speciesMap" cssClass="search_select_mid"  /> &nbsp;
       </div>
       <div style="clear:both"></div>
   </div>
</div>
<div class="search_line_space"></div>
<div class="search_data_outer_div">
    <table class="multiple_select_tab">
        <tr>
            <td width="60">System:</td><td><@s.select name="searchBean.system"  headerKey="-1" headerValue="All"   list="systemMap" cssClass="search_select_mid"  /></td>
            <td width="60" align="center">Organ:</td>
            <td><@s.select name="searchBean.organs" id="select_organ" headerKey="-1" headerValue="All"  multiple="true" size="4" list="organMap" cssClass="search_mul_select"  /></td>
            <td width="60" align="center">Cell:</td>
            <td><@s.select name="searchBean.cells" id="select_cell" headerKey="-1" headerValue="All"  multiple="true" size="4" list="cellMap" cssClass="search_mul_select"  /></td>
            <td width="80" align="center">Cell Line:</td>
            <td><@s.select name="searchBean.cellLines" id="select_cellline" headerKey="-1" headerValue="All"  multiple="true" size="4" list="cellLineMap" cssClass="search_mul_select"  /></td>
        </tr>
        <tr>
            <td></td><td></td><td></td>
            <td colspan="6">
                <div class="comment_gray">
                    (Select multiple values using Shift key)
                </div>
            </td>
        </tr>
    </table>
</div>

<div class="search_line_space"></div>

<div class="search_data_outer_div">
    <div class="search_field_row">
        <table>
            <tr>
                <td>
                    <div class="search_field_title">Sample Types:</div>
                </td>
                <td>
                     <@s.radio name="searchBean.variation" list="variationMap" value="searchBean.variation" id="search_normal" title="search by normal or abnormal"/>
                </td>
                <td>
                    <@s.if test="%{searchBean.variation =='Normal' || searchBean.variation =='any' }">
                    <div class="normal_ab_div" style="display: none;">
                    </@s.if>
                    <@s.else>
                    <div class="normal_ab_div">
                    </@s.else>
                        <@s.select name="searchBean.abVariation" id="ab_variation"  headerKey="-1" headerValue="All" list="abnormalMap" cssClass="search_select_mid"  />
                    </div>
                </td>
            </tr>

        </div>
        </table>
    </div>
</div>

<div class="search_line_space"></div>

<div class="search_data_outer_div">
    <div class="search_field_row">
        <div class="search_field_title">Fold Change:</div>
        <div class="search_field_value_block">
            Up: <@s.textfield name="searchBean.upValue" id="upValue" cssClass="search_input"/>&nbsp; Down: <@s.textfield name="searchBean.downValue" id="downValue" cssClass="search_input"/>

            <div class="comment_gray">
                (The Fold Change value must be greater or equal to 1)
            </div>
        </div>
    </div>
</div>
<div class="search_line_space"></div>

<div class="search_data_outer_div">
    <div class="search_field_row">
        <div class="search_field_title">Gene Symbol List:</div>
        <div class="search_field_list">
            <@s.textarea  name="searchBean.genes" cols="80" rows="5" cssClass="input_textarea" />  &nbsp; &nbsp; <br/>(eg: Isg15, Cxcl10, Stat1 &nbsp; Separated by Comma or Tab or NewLine)
        </div>
        <div style="clear:both"></div>
    </div>
</div>

<div class="search_line_space"></div>

<div class="search_data_outer_div">
    <div class="search_field_row">
        <div class="search_field_title">GenBank Accession List:</div>
        <div class="search_field_list">
            <@s.textarea  name="searchBean.genBanks" cols="80" rows="5" cssClass="input_textarea" /> &nbsp; &nbsp; <br/> (eg: L13852,X55005 &nbsp; Separated by Comma or Tab or NewLine)
        </div>
        <div style="clear:both"></div>
    </div>
</div>

<div class="search_line_space"></div>

<div class="search_data_outer_div">
    <div class="search_field_row">
        <div class="search_field_title">Ensembl Id List:</div>
        <div class="search_field_list">
            <@s.textarea  name="searchBean.ensembls" cols="80" rows="5" cssClass="input_textarea" /> &nbsp; &nbsp; <br/>(eg: ENSG00000182179,ENSG00000126351 &nbsp; Separated by Comma or Tab or NewLine)
        </div>
        <div style="clear:both"></div>
    </div>
</div>

<div class="search_line_space"></div>
