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
	<div class="parag_title_div">Adding Researcher Options</div>
    <@s.form action="selectPartyType.jspx" namespace="/data" method="post">
	<div class="data_outer_noborder_div">
        <div class="each_field_row">
		    Select one of the following adding researcher options:
        </div>
    </div>
    <div class="data_outer_div">
        <div class="blank_separator"></div>
        <div class="each_field_row">
            <@s.radio name="addPartyType" theme = "merctheme" list="addPartyTypeMap" id="addPartyType" value="addPartyType"  title="Please select an adding researcher option"/>
        </div>
        <div class="blank_separator"></div>
    </div>
    <div class="data_outer_noborder_div">
        <div class="each_field_row">
            <input type="button"  value=" Cancel " class="input_button" id="cancelAddParty" /> &nbsp;&nbsp; <@s.submit value="Next" cssClass="input_button" />
        </div>
    </div>
    </@s.form>
</div>
<br/>

</body>
</html>