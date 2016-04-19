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
	<#include "../template/action_errors.ftl" />
	<div class="parag_title_div">
        <@s.if test="%{addPartyType == 'rm_party'}">
            Search a researcher from the Research Master Web Service
        </@s.if>
        <@s.if test="%{addPartyType == 'user_defined_party'}">
            Manually input a researcher information
        </@s.if>
    </div>


    <div class="data_outer_noborder_div">
        <div class="blank_separator"></div>
        <div class="each_field_row">
            <@s.if test="%{addPartyType == 'rm_party'}">
			    <div class="name_title">Found a researcher information as bellow:</div>
            </@s.if>
            <@s.else>
                <div class="name_title">A researcher information was created successfully:</div>
            </@s.else>
        </div>
        <div class="blank_separator"></div>
    </div>
    <div class="data_outer_div">
        <div class="blank_separator"></div>
        <div class="each_field_row">
            <@s.property value="addedPartyBean.personTitle" /> <@s.property value="addedPartyBean.personGivenName" /> <@s.property value="addedPartyBean.personFamilyName" /> - <@s.property value="addedPartyBean.groupName" />
        </div>
        <div class="blank_separator"></div>
    </div>

    <div class="data_outer_noborder_div">
        <div class="each_field_row">
            <@s.hidden name="addedPartyBean.partyKey" id = "ands_p_key" />
            <@s.hidden name="addedPartyBean.personTitle" id = "ands_p_title"/>
            <@s.hidden name="addedPartyBean.personGivenName" id = "ands_p_givenname" />
            <@s.hidden name="addedPartyBean.personFamilyName" id = "ands_p_sname"/>
            <@s.hidden name="addedPartyBean.email" id = "ands_p_email"/>
            <@s.hidden name="addedPartyBean.address" id = "ands_p_address" />
            <@s.hidden name="addedPartyBean.url" id = "ands_p_url"/>
            <@s.hidden name="addedPartyBean.identifierType" id = "ands_p_idtype"/>
            <@s.hidden name="addedPartyBean.identifierValue" id = "ands_p_idvalue" />
            <@s.hidden name="addedPartyBean.originateSourceType" id = "ands_p_srctype"/>
            <@s.hidden name="addedPartyBean.originateSourceValue" id = "ands_p_srcvalue" />
            <@s.hidden name="addedPartyBean.groupName" id = "ands_p_groupname"/>
            <@s.hidden name="addedPartyBean.fromRm" id = "ands_p_fromrm"/>
        </div>
        <div class="each_field_row">
            <input type="button" value=" Back " class="input_button" onclick="window.location = '${base}/data/selectPartyType?addPartyType=${addPartyType}';" /> &nbsp;&nbsp; <input type="submit" name="options" value=" Save " class="input_button" id="save_rm_party" />
        </div>
    </div>
</div>
<br/>

</body>
</html>