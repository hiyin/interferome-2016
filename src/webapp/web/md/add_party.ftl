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
        <@s.if test="%{addPartyType == 'rm_party'}">
            Search a researcher from the Research Master Web Service
        </@s.if>
        <@s.if test="%{addPartyType == 'user_defined_party'}">
            Manually input a researcher information
        </@s.if>
    </div>
    <@s.if test="%{addPartyType == 'rm_party'}">
    <@s.form action="addRMParty.jspx" namespace="/data" method="post">
    <div class="data_outer_div">
        <div class="each_field_row">
            Enter the researcher name or email bellow:
        </div>
        <div class="blank_separator"></div>
        <div class="each_field_row">
            <@s.textfield name="searchCnOrEmail" cssClass="input_field_normal"/>
		    <@s.hidden name="addPartyType" id="party_type"/>
        </div>
        <div class="blank_separator"></div>
        <br/>
    </div>
    <div class="data_outer_noborder_div">
        <div class="each_field_row">
            <input type="button"  value=" Back " class="input_button" onclick="window.location = '${base}/data/addPartyOptions.jspx?addPartyType=${addPartyType}';" /> &nbsp;&nbsp; <@s.submit value="Search" cssClass="input_button" />
        </div>
    </div>
    </@s.form>
    </@s.if>
    <@s.if test="%{addPartyType == 'user_defined_party'}">
    <@s.form action="addUDParty.jspx" namespace="/data" method="post">
    <@s.hidden name="addPartyType" id="party_type"/>
    <div class="data_outer_div">
        <table class="party_tab">
            <tr>
                <td>
                    Title:
                    <div class="fname_comment">* (<@s.text name="ands.add.party.party.title.hint" />)</div>
                </td>
            </tr>
            <tr>
                <td><@s.textfield name="addedPartyBean.personTitle" cssClass="input_field_normal" /></td>
            </tr>

            <tr>
                <td>
                    First Name:
                    <div class="fname_comment">* (<@s.text name="ands.add.party.party.first.name.hint" />)</div>
                </td>
            </tr>
            <tr>
                <td><@s.textfield name="addedPartyBean.personGivenName" cssClass="input_field_normal" /></td>
            </tr>
            <tr>
                <td>
                    Last Name:
                    <div class="fname_comment">* (<@s.text name="ands.add.party.party.last.name.hint" />)</div>
                </td>
            </tr>
            <tr>
                <td><@s.textfield name="addedPartyBean.personFamilyName" cssClass="input_field_normal" /></td>
            </tr>
            <tr>
                <td>
                    Email:
                    <div class="fname_comment">* (<@s.text name="ands.add.party.party.email.hint" />)</div>
                </td>
            </tr>
            <tr>
                <td><@s.textfield name="addedPartyBean.email" cssClass="input_field_normal" /></td>
            </tr>
            <tr>
                <td>
                    Address:
                    <div class="fname_comment">* (<@s.text name="ands.add.party.party.address.hint" />)</div>
                </td>
            </tr>
            <tr>
                <td><@s.textarea name="addedPartyBean.address"  cssClass="input_textarea" style="width: 310px; height: 80px;" /></td>
            </tr>
            <tr>
                <td>
                    Web URL:
                    <div class="fname_comment">* (<@s.text name="ands.add.party.party.url.hint" />)</div>
                </td>
            </tr>
            <tr>
                <td><@s.textfield name="addedPartyBean.url" cssClass="input_field_normal" /></td>
            </tr>
            <tr>
                <td>
                    Group Name:
                    <div class="fname_comment">* (<@s.text name="ands.add.party.party.group.name.hint" />)</div>
                </td>
            </tr>
            <tr>
                <td><@s.textfield name="addedPartyBean.groupName" cssClass="input_field_normal" /></td>
            </tr>
            <tr>
                <td>
                    Group Web Site:
                    <div class="fname_comment">* (<@s.text name="ands.add.party.party.group.url.hint" />)</div>
                </td>
            </tr>
            <tr>
                <td><@s.textfield name="addedPartyBean.originateSourceValue" cssClass="input_field_normal" /></td>
            </tr>
        </table>
    </div>
    <div class="data_outer_noborder_div">
        <div class="each_field_row">
            <input type="button"  value=" Back " class="input_button" onclick="window.location = '${base}/data/addPartyOptions.jspx?addPartyType=${addPartyType}';" /> &nbsp;&nbsp; <@s.submit value="Add" cssClass="input_button" />
        </div>
    </div>
    </@s.form>
    </@s.if>
</div>
<br/>

</body>
</html>