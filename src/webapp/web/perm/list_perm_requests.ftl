<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${pageTitle}</title>
<#include "../template/header.ftl"/>
</head>
<body>
	<#include "../template/navsection.ftl"/>
	<#include "../template/action_nav_bar.ftl"/>
 	<div class="main_container">
        <#include "../template/action_errors.ftl">
		<div class="container_inner_left">
			<div class="blank_separator"></div>
			<@s.if test="%{successActMsg != null}">
			<div class="data_header_div">
			 	<#include "../template/success_act_message.ftl"/>
			</div>
			</@s.if>
			<@s.if test="%{permRequests.size() > 0}">
            <@s.iterator status="permReqStat" value="permRequests" id="pRequest" >
            <div class="data_outer_div">
                <div class="perm_req_left">
                    <table>

                        <tr>
                            <td colspan="3">&nbsp; </td>
                        </tr>
                        <tr>
                            <td width="100">User name:</td>
						 	<td><@s.property value="#pRequest.requestUser.displayName" /></td>
						 	<td></td>
                        </tr>
                        <tr>
                            <td>Email:</td>
                            <td><@s.property value="#pRequest.requestUser.email" /></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Organization:</td>
                            <td><@s.property value="#pRequest.requestUser.profile.organization" /></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td colspan="3">&nbsp; </td>
                        </tr>
                    </table>
                </div>
                <div class="perm_req_right">
                    <@s.form action="approvePermReq.jspx" id="approve_req_perm" namespace="/perm" method="post" >
                        <table>
                            <tr>
                                 <td colspan="4" align="center"><span class="perm_req_right_title">Applied Permissions for the IFM<@s.property value='#pRequest.experiment.id' /> experiment</span></td>
                            </tr>
                            <tr>
                                 <td colspan="4">
                                    <@s.hidden name="permRequest.id" value="%{#pRequest.id}" />
                                    <@s.hidden name="permRequest.experiment.id" value="%{#pRequest.experiment.id}" />
                                </td>
                            </tr>
                            <tr>
                                 <td width="120"><center> View </center></td>
                                 <td width="120"><center><@s.checkbox name="permRequest.viewAllowed"  value="%{#pRequest.viewAllowed}" /></center></td>
                                 <td width="120"><center>Export</center></td>
                                 <td width="120"><center><@s.checkbox name="permRequest.exportAllowed" value="%{#pRequest.exportAllowed}" /></center></td>
                            </tr>
                            <tr>
                                 <td><center>Edit</center></td>
                                 <td><center><@s.checkbox name="permRequest.updateAllowed" value="%{#pRequest.updateAllowed}"/></center></td>
                                 <td><center>Delete</center></td>
                                  <td><center><@s.checkbox name="permRequest.deleteAllowed"  value="%{#pRequest.deleteAllowed}"/></center></td>
                            </tr>
                            <tr>
                                 <td><center>Import</center></td>
                                 <td><center><@s.checkbox name="permRequest.importAllowed" value="%{#pRequest.importAllowed}"/></center></td>
                                 <td><center>Access Control</center></td>
                                 <td><center><@s.checkbox name="permRequest.changePermAllowed" value="%{#pRequest.changePermAllowed}"/></center></td>
                            </tr>
                            <tr>
                                 <td colspan="4"></td>
                            </tr>
                            <tr>
                                 <td></td>
                                 <td align="right">
                                    <@s.submit value="Grant" cssClass="input_button3" />
                                 </td>
                                 <td>
                                   <a href="${base}/perm/rejectPermReq.jspx?permRequest.id=<@s.property value='#pRequest.id' />&permRequest.experiment.id=<@s.property value='#pRequest.experiment.id' />" class="linkbutton">&nbsp; Reject &nbsp; </a>
                                 </td>
                                 <td></td>
                            </tr>
                    </table>
                    </@s.form>
                </div>
                <div style="clear:both"></div>
            </div>
            </@s.iterator>
            <div class="blank_separator"></div>
            </@s.if>
			<@s.else>
            <div class="data_header_div">
				<span class="name_title">A total of <font color="green"> 0 </font> Permission Requests</span>
			</div>
			<div class="empty_div"></div>
			<div class="empty_div"></div>
			<div class="empty_div"></div>
			<div class="empty_div"></div>
			<div class="empty_div"></div>
			<div class="empty_div"></div>
            <br/>
            <br/>
            <br/>
            <div style="clear:both"></div>
            </@s.else>
		</div>
		<div class="container_inner_right">
			<#include "../template/user_nav.ftl">
		</div>
		<div style="clear:both"></div>
	</div>
	<#include "../template/footer.ftl"/>
</body>
</html>