
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
                &nbsp; To: &nbsp;<@s.textfield name="searchBean.toDose" cssClass="search_input"/>  &nbsp; &nbsp; <font color="#858585">(ng/ml)</font>
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
    </table>
</div>

<div class="search_line_space"></div>

<div class="search_data_outer_div">
    <div class="search_field_row">
        <table>
            <tr>
                <td>
                    <div class="search_field_title">Normal | Abnormal:</div>
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
        <div class="search_field_radio">
            <@s.radio name="searchBean.anyRangeFold" list="anyOrRanges" value="searchBean.anyRangeFold" id="search_fold" title="search by fold change"/>

            <@s.if test="%{searchBean.anyRangeFold =='any'}">
            <@s.select name="upordown" id="selected_up_down" style="display: none;" headerKey="-1" headerValue=" - select up or down - " list="upDown" cssClass="search_select_mid"  /> &nbsp;
            </@s.if>
            <@s.else>
            <@s.select name="upordown" id="selected_up_down" headerKey="-1" headerValue=" - select up or down - " list="upDown" cssClass="search_select_mid"  /> &nbsp;
            </@s.else>

            <div class="fold_change_div">
            <@s.if test="%{searchBean.anyRangeFold =='any'}">
                <div class="updown_range" id="foldchange_up"></div>
                <div class="updown_range" id="foldchange_down"></div>
            </@s.if>
            <@s.else>
                <div class="updown_range" id="foldchange_up">
                    <@s.if test="%{searchBean.upProvided == true}">
                    <table>
                        <tr>
                            <td width="50">Up: <@s.hidden name="searchBean.upProvided" id="upProvided"/></td>
                            <td><@s.textfield name="searchBean.upValue" id="upValue" cssClass="search_input"/></td>
                            <td class="search_unit">(value >= 1.0)</td>
                            <td><img src="${base}/images/delete.png" id="remove_up" class="remove_image" /></td>
                        </tr>
                    </table>
                    </@s.if>
                </div>
                <div class="updown_range" id="foldchange_down">
                    <@s.if test="%{searchBean.downProvided == true}">
                    <table>
                        <tr>
                            <td width="50">Down: <@s.hidden name="searchBean.downProvided" id="downProvided" /></td>
                            <td><@s.textfield name="searchBean.downValue" id="downValue" cssClass="search_input"/></td>
                            <td class="search_unit">(value >= 1.0)</td>
                            <td><img src="${base}/images/delete.png" id="remove_down" class="remove_image" /></td>
                        </tr>
                    </table>
                    </@s.if>
                </div>
            </@s.else>
            </div>
            <div style="clear:both"></div>
        </div>

    </div>
</div>
<div class="search_line_space"></div>

<div class="search_data_outer_div">
    <div class="search_field_row">
        <div class="search_field_title">Gene Symbol List:</div>
        <div class="search_field_list">
            <@s.textarea  name="searchBean.genes" cols="80" rows="5" cssClass="input_textarea" />  &nbsp; &nbsp; (separated by comma)
        </div>
        <div style="clear:both"></div>
    </div>
</div>

<div class="search_line_space"></div>

<div class="search_data_outer_div">
    <div class="search_field_row">
        <div class="search_field_title">GenBank Accession List:</div>
        <div class="search_field_list">
            <@s.textarea  name="searchBean.genBanks" cols="80" rows="5" cssClass="input_textarea" /> &nbsp; &nbsp; (separated by comma)
        </div>
        <div style="clear:both"></div>
    </div>
</div>

<div class="search_line_space"></div>

<div class="search_data_outer_div">
    <div class="search_field_row">
        <div class="search_field_title">Ensembl Id List:</div>
        <div class="search_field_list">
            <@s.textarea  name="searchBean.ensembls" cols="80" rows="5" cssClass="input_textarea" /> &nbsp; &nbsp; (separated by comma)
        </div>
        <div style="clear:both"></div>
    </div>
</div>

<div class="search_line_space"></div>
