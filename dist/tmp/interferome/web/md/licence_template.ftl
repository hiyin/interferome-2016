<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "../template/jquery_header.ftl"/>
<style type="text/css">
.error_msg_section{
	background: none repeat scroll 0 0 white;
    border: 1px solid #E1E8F0;
    margin: 5px auto;
    text-align: center;
    width: 448px;
    padding-left: 5px;
    padding-right: 5px;
}
</style>
</head>
<body>
<br/>
<div class="md_popup_main">
	<#include "../template/action_errors.ftl" />
	<div class="parag_title_div">
        <@s.if test="%{licence.licenceType == 'cccl_license'}">
            Creative Commons License
        </@s.if>

        <@s.if test="%{licence.licenceType == 'user_license'}">
            Define Your Own License
        </@s.if>
    </div>

    <@s.if test="%{licence.licenceType == 'cccl_license'}">
        <@s.form action="ccLicence.jspx" namespace="/data" method="post">
        <div class="data_outer_div">
            <div class="license_div"><b>${commercialField.label}</b></div>
            <@s.iterator status="cmf_stat" value="commercialField.ccLicenseFields" id="cmf" >
            <div class="license_div">
                <@s.if test="%{licence.commercial == #cmf.id}">
                    <input type="radio" name ="licence.commercial" value='${cmf.id}' checked="checked" />
                </@s.if>
                <@s.else>
                    <input type="radio" name ="licence.commercial" value='${cmf.id}' />
                </@s.else>
                ${cmf.label} &nbsp;&nbsp; <img src="${base}/images/info.png" class="lInfo_image" title="${cmf.description}" />
            </div>
            <div style="clear:both"></div>
            </@s.iterator>
            <div style="clear:both"></div>

            <div class="license_div"><b>${derivativesField.label}</b></div>
            <@s.iterator status="derf_stat" value="derivativesField.ccLicenseFields" id="derf" >
            <div class="license_div">
            <@s.if test="%{licence.derivatives == #derf.id}">
                <input type="radio" name ="licence.derivatives" value='${derf.id}' checked="checked"/>
            </@s.if>
            <@s.else>
                <input type="radio" name ="licence.derivatives" value='${derf.id}' />
            </@s.else>
                ${derf.label} &nbsp;&nbsp; <img src="${base}/images/info.png" class="lInfo_image" title="${derf.description}" />
            </div>
            </@s.iterator>
            <div style="clear:both"></div>
            <div class="license_div"><b>${jurisdictionField.label}</b> &nbsp;&nbsp;  <img src="${base}/images/info.png" class="lInfo_image" title="${jurisdictionField.description}" /></div>
            <div class="license_div"><@s.select name="licence.jurisdiction" headerKey="${licence.jurisdiction}" list="jurisMap" cssClass="input_select_normal"/></div>
            <div style="clear:both"></div>
            <br/>
        </div>
        <div class="data_outer_noborder_div">
            <@s.hidden name="licence.licenceType" />
            <@s.hidden name="experiment.id" />
            <div class="each_field_row">
                <input type="button" value=" Back " class="input_button" onclick="window.location = '${base}/data/licenceOptions.jspx?experiment.id=<@s.property value='experiment.id' />&licence.licenceType=${licence.licenceType}';"> &nbsp;&nbsp; <input type="button"  value=" Cancel " class="input_button" id="cancelLicence" /> &nbsp;&nbsp; <input type="submit" name="options" value=" Next " class="input_button" />
            </div>
        </div>
        </@s.form>
    </@s.if>

    <!-- user own license -->
    <@s.if test="%{licence.licenceType == 'user_license'}">

    <div class="data_outer_div">
        <div class="each_field_row">
            <@s.textarea name="licence.licenceContents" id="plicence_cont" cssClass="input_textarea" style="width:434px; height: 240px;" />
        </div>
    </div>
    <div class="data_outer_noborder_div">
        <@s.hidden name="licence.licenceType" id="plicence_type"/>
        <@s.hidden name="licence.commercial" id="plicence_comm"/>
        <@s.hidden name="licence.derivatives" id="plicence_deri"/>
        <@s.hidden name="licence.jurisdiction" id="plicence_juri"/>
        <@s.hidden name="experiment.id" />
        <div class="each_field_row">
            <input type="button" value=" Back " class="input_button" onclick="window.location = '${base}/data/licenceOptions.jspx?experiment.id=<@s.property value='experiment.id' />&licence.licenceType=${licence.licenceType}';"> &nbsp;&nbsp;
            <input type="button"  value=" Cancel " class="input_button" id="cancelLicence" /> &nbsp;&nbsp; <input type="button"  value=" Save " id="saveLicence" class="input_button" />
        </div>
    </div>

    </@s.if>
</div>
<br/>

</body>
</html>