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
	<div class="parag_title_div">License Options</div>

    <@s.form action="selectLicence.jspx" namespace="/data" method="post">
	<div class="data_outer_noborder_div">
        <div class="each_field_row">
		    Select the License you want to apply to this experiment so that interested people understand what they are entitled to do with your published data
        </div>
    </div>
    <div class="data_outer_div">
        <div class="blank_separator"></div>
        <div class="each_field_row">
            <@s.radio name="licence.licenceType" theme = "merctheme" list="licenceMap" id="licenseType" value="licence.licenceType"  title="Please select a License"/>
        </div>
        <div class="blank_separator"></div>
    </div>
    <div class="data_outer_noborder_div">
        <div class="each_field_row">
            <@s.hidden name="experiment.id" />
            <input type="button"  value=" Cancel " class="input_button" id="cancelLicence" /> &nbsp;&nbsp; <@s.submit value="Next" cssClass="input_button" />
        </div>
    </div>
    </@s.form>
</div>
<br/>

</body>
</html>