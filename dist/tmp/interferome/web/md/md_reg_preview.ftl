<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>${pageTitle}</title>
<#include "../template/jquery_header.ftl"/>
</head>
<body>
<#include "../template/navsection.ftl"/>
<#include "../template/action_nav_bar.ftl"/>
<div class="main_container">
    <#include "../template/action_errors.ftl">
    <div class="container_inner_left">
        <div class="blank_separator"></div>
        <div class="parag_title_div">
            Public registration of the following metadata associated with this experiment with the Research Data Australia website
        </div>
        <div class="data_outer_div">
        <table class="data_no_border_tab">
            <tr>
                <td width="100">
                    <div class="md_subtitle">
                        Name:
                    </div>
                </td>
                <td>
                    <div class="exp_name_title">
                        ${namePrefix}<@s.property value="experiment.id" />
			        </div>

                </td>
            </tr>
            <tr>
                <td>
                    <div class="md_subtitle">
                        Description:
                    </div>
                </td>
                <td>
                    <div class="md_justify">
                        <@s.property value="experiment.description" escape=false  />
			        </div>

                </td>
            </tr>
        </table>
        <div style="clear:both"></div>
        </div>

        <div class="blank_separator"></div>

        <div class="data_outer_div">
        <table class="data_no_border_tab">
            <tr>
                <td width="100">
                    <div class="md_subtitle">
                        Title:
                    </div>
                </td>
                <td>
                    <div class="md_justify">
                        <@s.property value="experiment.pubTitle" />
			        </div>

                </td>
            </tr>
            <tr>
                <td>
                    <div class="md_subtitle">
                        Publication Date:
                    </div>
                </td>
                <td>
                    <div class="md_justify">
                        <@s.date name="experiment.publicationDate"  format="yyyy-MM-dd" />
			        </div>

                </td>
            </tr>
            <tr>
                <td>
                    <div class="md_subtitle">
                        Authors:
                    </div>
                </td>
                <td>
                    <div class="md_justify">
                        <@s.property value="experiment.authors" />
			        </div>

                </td>
            </tr>
             <tr>
                <td>
                    <div class="md_subtitle">
                        Abstract:
                    </div>
                </td>
                <td>
                    <div class="md_justify">
                        <@s.property value="experiment.abstraction" escape=false />
			        </div>

                </td>
            </tr>
        </table>
        </div>
        <div class="blank_separator"></div>
        <@s.form action="mdReg.jspx" namespace="/data" method="post">
        <div class="data_outer_div">
            <@s.hidden name="experiment.id" />
			<@s.hidden name="experiment.name" />
			<@s.hidden name="experiment.description" />
            <@s.hidden name="experiment.pubTitle" />
            <@s.hidden name="experiment.publicationDate" />
            <@s.hidden name="experiment.authors" />
            <@s.hidden name="experiment.abstraction" />
            <@s.hidden name="fromMyExp" />
            <table class="data_no_border_tab" >
                <tr>
                    <td width="100">
                        <div class="md_subtitle">
                            Address:
                        </div>
                    </td>
                    <td>
                        <div class="md_justify">
                            ${physicalAddress}
                        </div>
                    </td>
                </tr>
                <tr>
                    <td align="left">
                        <div class="md_subtitle">
                            Field of research (ANZSRC):
                        </div>
                    </td>
                    <td>
                        <div class="md_justify">
                            ${anzSrcCode}
                        </div>
                    </td>
                </tr>
                <@s.if test="%{projectList == null || projectList.size == 0}">
                <tr>
                    <td align="left">
                        <div class="md_subtitle">
                            Output of:
                        </div>
                    </td>
                    <td>
                        <div class="md_justify">
                            Not Provided
                        </div>
                    </td>
                </tr>
                </@s.if>
                <@s.else>
                    <@s.iterator status="pState" value="projectList" id="proj" >
                    <tr>
                        <td align="left">
                            <div class="md_subtitle">
                                Output of:
                            </div>
                        </td>
                        <td>
                            <div class="md_justify">
                                <@s.property value="#proj.title" />
                            </div>
                            <@s.hidden name="projectList[${pState.index}].activityKey" />
                            <@s.hidden name="projectList[${pState.index}].title" />
                            <@s.hidden name="projectList[${pState.index}].grantCode" />
                            <@s.hidden name="projectList[${pState.index}].appliedDate"  />
                        </td>
                    </tr>
                    </@s.iterator>
                </@s.else>
                <@s.iterator status="ptState" value="partyList" id="party" >
                <tr>
                    <td align="left">
                        <div class="md_subtitle">
                            Managed by:
                        </div>
                    </td>
                    <td>
                        <div class="md_justify">
                            <@s.property value="#party.personTitle" /> <@s.property value="#party.personGivenName" /> <@s.property value="#party.personFamilyName" /> - <@s.property value="#party.groupName" />
                        </div>
                        <@s.hidden name="partyList[${ptState.index}].partyKey" />
                        <@s.hidden name="partyList[${ptState.index}].personTitle" />
                        <@s.hidden name="partyList[${ptState.index}].personGivenName" />
                        <@s.hidden name="partyList[${ptState.index}].personFamilyName" />
                        <@s.hidden name="partyList[${ptState.index}].email" />
                        <@s.hidden name="partyList[${ptState.index}].address" />
                        <@s.hidden name="partyList[${ptState.index}].url" />
                        <@s.hidden name="partyList[${ptState.index}].identifierType" />
                        <@s.hidden name="partyList[${ptState.index}].identifierValue" />
                        <@s.hidden name="partyList[${ptState.index}].originateSourceType" />
                        <@s.hidden name="partyList[${ptState.index}].originateSourceValue" />
                        <@s.hidden name="partyList[${ptState.index}].groupName" />
                        <@s.hidden name="partyList[${ptState.index}].fromRm" />
                    </td>
                </tr>
                </@s.iterator>
                <tr>
                    <td align="left">
                        <div class="md_subtitle">
                            License:
                        </div>
                    </td>
                    <td>
                        <div class="md_justify">
                            <@s.property value="licence.licenceContents" />
                        </div>
                        <@s.hidden name="licence.id" id="flicence_id"/>
                        <@s.hidden name="licence.licenceType" id="flicence_type"/>
                        <@s.hidden name="licence.commercial" id="flicence_comm"/>
                        <@s.hidden name="licence.derivatives" id="flicence_deri"/>
                        <@s.hidden name="licence.jurisdiction" id="flicence_juri"/>
                        <@s.hidden name="licence.licenceContents" id="flicence_cont" />
                    </td>
                </tr>
                <tr>
                    <td align="left">
                        <div class="md_subtitle">
                            Access rights:
                        </div>
                    </td>
                    <td>
                        <div class="md_justify">
                            <@s.property value="accessRights" />
                        </div>
                         <@s.hidden name="accessRights" />
                    </td>
                </tr>
            </table>
        </div>

        <div class="data_header_div">
            <@s.submit value="Register" cssClass="input_button2" />
        </div>
		</@s.form>
        <div class="blank_separator"></div>
    </div>
    <div class="container_inner_right">
    <#include "../template/user_nav.ftl">
    </div>
    <div style="clear:both"></div>
</div>
<#include "../template/footer.ftl"/>
</body>
</html>