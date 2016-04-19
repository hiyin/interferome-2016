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
    </div>

    <div class="data_outer_div">
        <br/>
        <div class="each_field_row">
            <div class="license_content">
                <@s.property value="licence.licenceContents" />
            </div>
        </div>
        <br/>
    </div>

    <div class="data_outer_noborder_div">
        <@s.hidden name="licence.licenceType" id="plicence_type"/>
        <@s.hidden name="licence.commercial" id="plicence_comm"/>
        <@s.hidden name="licence.derivatives" id="plicence_deri"/>
        <@s.hidden name="licence.jurisdiction" id="plicence_juri"/>
        <@s.hidden name="licence.licenceContents" id="plicence_cont" />

        <div class="each_field_row">
            <input type="button" value=" Back " class="input_button" onclick="window.location = '${base}/data/selectLicence.jspx?experiment.id=<@s.property value='experiment.id' />&licence.licenceType=${licence.licenceType}&licence.commericial=${licence.commercial}&licence.derivatives=${licence.derivatives}&licence.jurisdiction=${licence.jurisdiction}';">
            <input type="button"  value=" Cancel " class="input_button" id="cancelLicence" /> &nbsp;&nbsp; <input type="button"  value=" Save " id="saveLicence" class="input_button" />
        </div>
    </div>
</div>
<br/>

</body>
</html>