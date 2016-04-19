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
            Metada Public Registration
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
        <div class="parag_title_div">
            Publication Information
        </div>
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
        <div class="parag_title_div">
            Researchers, Grants, License and Access Rights Information
        </div>
        <@s.form action="previewMdReg.jspx" namespace="/data" method="post" id="mdRegForm">
        <div class="no_border_tab_div">
            <@s.hidden name="experiment.id" />
			<@s.hidden name="experiment.name" />
			<@s.hidden name="experiment.description" />
            <@s.hidden name="experiment.pubTitle" />
            <@s.hidden name="experiment.publicationDate" />
            <@s.hidden name="experiment.authors" />
            <@s.hidden name="experiment.abstraction" />
            <@s.hidden name="fromMyExp" />
        <table class="data_no_border_tab">
            <tr>
                <td width="220">
                    <div class="md_subtitle">
                        The associated researcher(s):
                    </div>
                </td>
                <td>
                    <div class="para_norm_div">
                        <a href="${base}/data/addPartyOptions.jspx" title="Adding another researcher" id="addtionalParty">Add Researcher</a>
			        </div>
                    <div style="clear:both"></div>
                </td>
            </tr>
        </table>
        </div>
        <div style="clear:both"></div>
        <div class="data_table_div">
        <table class="md_reg_tab_data" id="md_reg_tab_data" >
            <@s.iterator status="ptState" value="partyList" id="party" >
            <tr>
                <td align="center" width="50">
                    <@s.checkbox name="partyList[${ptState.index}].selected" />
                </td>
                <td>
                    <div class="md_blue">
                        <@s.property value="#party.personTitle" /> <@s.property value="#party.personGivenName" /> <@s.property value="#party.personFamilyName" /> - <@s.property value="#party.groupName" />
                        <@s.hidden name="partyList[${ptState.index}].partyKey" />
                        <@s.hidden name="partyList[${ptState.index}].personTitle" />
                        <@s.hidden name="partyList[${ptState.index}].personGivenName" />
                        <@s.hidden name="partyList[${ptState.index}].personFamilyName" />
                        <@s.hidden name="partyList[${ptState.index}].email" />
                        <@s.hidden name="partyList[${ptState.index}].address" />
                        <@s.hidden name="partyList[${ptState.index}].url" />
                        <@s.hidden name="partyList[${ptState.index}].identifierType"  />
                        <@s.hidden name="partyList[${ptState.index}].identifierValue" />
                        <@s.hidden name="partyList[${ptState.index}].originateSourceType" />
                        <@s.hidden name="partyList[${ptState.index}].originateSourceValue" />
                        <@s.hidden name="partyList[${ptState.index}].groupName" />
                        <@s.hidden name="partyList[${ptState.index}].fromRm" />
                    </div>
                </td>
            </tr>
            </@s.iterator>
        </table>
        </div>
        <@s.if test="%{partyList == null || partyList.size == 0}">
		<div class="data_outer_div" id="party_not_found">
			<div class="md_reg_yellow_div" >
				The associated researcher(s) not found
			</div>
			<div style="clear:both"></div>
		</div>
		</@s.if>

        <div class="blank_separator"></div>
        <div class="no_border_tab_div">
        <table class="data_no_border_tab">
            <tr>
                <td width="220">
                    <div class="md_subtitle">
                        The associated grant(s) or project(s):
                    </div>
                </td>
                <td>
                     &nbsp;
                </td>
            </tr>
        </table>
        </div>
        <@s.if test="%{projectList != null && projectList.size > 0}">
        <div class="no_border_tab_div">
        <table class="md_reg_tab_data" >
            <@s.iterator status="pState" value="projectList" id="proj" >
                <tr>
                    <td align="center" width="50">
                        <@s.checkbox name="projectList[${pState.index}].selected" />
                        <@s.hidden name="projectList[${pState.index}].activityKey" />
                    </td>
                    <td>
                        <div class="md_blue">
                            <@s.property value="#proj.title" />
                            <@s.hidden name="projectList[${pState.index}].title"  />
                        </div>
                        <div class="md_gray">
                            Grant Code: <@s.property value="#proj.grantCode" />
                            <@s.hidden name="projectList[${pState.index}].grantCode"  />
                        </div>
                        <div class="md_green">
                            Project Date Applied: <@s.property value="#proj.appliedDate" />
                            <@s.hidden name="projectList[${pState.index}].appliedDate"  />
                        </div>
                    </td>
                </tr>
            </@s.iterator>
        </table>
        </div>
        </@s.if>
        <@s.if test="%{projectList == null || projectList.size == 0}">
		<div class="data_outer_div">
		    <div class="md_reg_yellow_div">
			    The associated grant(s) or project(s) not found
            </div>
			<div style="clear:both"></div>
		</div>
		</@s.if>

        <div class="blank_separator"></div>
        <div class="no_border_tab_div">
        <table class="data_no_border_tab">
            <tr>
                <td width="220">
                    <div class="md_subtitle">
                        The experiment license:
                    </div>
                </td>
                <td>
                    <div class="para_norm_div">
                         <a href="${base}/data/licenceOptions.jspx?experiment.id=<@s.property value='experiment.id' />" title="Select License" id="selectLicence">Select License</a>
			        </div>
                    <div style="clear:both"></div>
                </td>
            </tr>
        </table>
        </div>

        <div class="data_outer_div">
            <div class="each_field_row">
                <@s.hidden name="licence.id" id="flicence_id"/>
                <@s.hidden name="licence.licenceType" id="flicence_type"/>
                <@s.hidden name="licence.commercial" id="flicence_comm"/>
                <@s.hidden name="licence.derivatives" id="flicence_deri"/>
                <@s.hidden name="licence.jurisdiction" id="flicence_juri"/>
                <@s.hidden name="licence.licenceContents" id="flicence_cont" />
            </div>
            <div class="each_field_row">
                <div class="license_content">
                    <@s.property value="licence.licenceContents" />
                </div>
            </div>
        </div>
        <div class="no_border_tab_div">
        <table class="data_no_border_tab">
            <tr>
                <td width="220">
                    <div class="md_subtitle">
                        The access rights:
                    </div>
                </td>
                <td><@s.hidden name="accessRights" /></td>
            </tr>
        </table>
        </div>
        <div class="data_outer_div">
            <div class="each_field_row">
                <@s.property value="accessRights" />
            </div>
        </div>

        <div class="blank_separator"></div>
        <div class="parag_title_div">
            Terms and Conditions
        </div>
        <div class="data_outer_div">
            <div class="term_conditions">
                <p>You are about to publish or register the above research work outside Monash University to be available to the general public via Internet sites that can harvest this information.  Sites include but are not limited to: Research Data Australia and search engines.</p>

                <p>Before you proceed, please ensure you have selected a licence to associate with your research data and work.</p>

                <p>By using this system to publish or register your research work you are continuing to agree to adhere to the Terms and Conditions of use detailed at <a href="http://www.monash.edu/eresearch/about/ands-merc.html" target="_blank"><b>http://www.monash.edu/eresearch/about/ands-merc.html</b></a>. Please read these Terms and Conditions carefully before registering.</p>
            </div>
        </div>

        <div class="data_header_div">
		    <@s.submit value=" I accept, Preview" cssClass="input_button2" />
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