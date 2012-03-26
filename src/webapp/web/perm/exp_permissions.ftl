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
			 
			<div class="data_outer_div">
				<div class="each_field_row">
					<div class="exp_name_title">${namePrefix}<@s.property value="experiment.id" /></div>
				</div>
				<div class="each_field_row">
					<div class="exp_desc_div"><@s.property value="experiment.description" /></div>
				</div>
				<div class="each_field_row">
					<div class="exp_audit_info">
						Imported by <@s.property value="experiment.owner.displayName" />,&nbsp;&nbsp;
						Imported Date: <@s.date name="experiment.createdTime"  format="dd-MM-yyyy 'at' hh:mm a" />,&nbsp;&nbsp;
						Modified by <@s.property value="experiment.modifiedByUser.displayName" />,&nbsp;&nbsp; 
						Modified date: <@s.date name="experiment.modifiedTime" format="dd-MM-yyyy 'at' hh:mm a"  />
					</div>
				</div>
				<div class="data_link">
			 		<a href="${base}/${viewExpActName}?experiment.id=<@s.property value='experiment.id' />">View Details</a>
			 	</div>
			 	<div style="clear:both"></div>  
			</div>
			<div class="parag_title_div">
				Permissions
			</div>
			<div class="data_outer_div">
				There are three types of the accesss control permissions for an experiment in the system:
	 			<ul>
	 				<li><b>All Anonymous Users Permissions</b> - Permissions which are granted to all users who are not logged in the system</li>
	 				<li><b>All Registered Users Permissions</b> - Permissions which are granted to all registered users in the system</li>
	 				<li><b>An Individual User Permissions</b> - Permissions which are granted to a registered user in the system</li>
	 			</ul>
	 			<p>Permissions can be granted to <b>All Registered Users</b> or<b> All Anonymous Users</b> or <b>An Individual User</b>. 
	 			<p>If the experiment permissions are neither granted to <b>All Registered Users</b> nor <b>All Anonymous Users</b>, which means this experiment is a private experiment.</p>
	 			<p>The default permissions of an experiment will be private, Only the owner of an experiment and the system administrators have access.</p>
	 			<p>You can grant the specific permissions to an individual user in an experiment, and the allowed permissions for <b>All Anonymous Users</b> in this experiment will be inherited.</p>
			</div>
			<@s.form action="setExpPerms.jspx" namespace="/perm" method="post">	
			<@s.hidden name="experiment.id" />
			<div class="data_header_div">
		 		<div class="name_title">Grant permission to &nbsp;&nbsp;
			 		<@s.select name="selected_username" id="selected_username" headerKey="-1" headerValue="-- Select User --" list="allActiveUsers" value="-1" cssClass="input_select_normal"/> &nbsp;&nbsp;
					<input type="button" name="add_permission" id="add_permission" value = "Add" class="data_button_normal" />
				</div>
		 	</div>
		 	<div class="data_header_div">
		 		<@s.submit value="Save All" cssClass="data_button_normal" id="perm_form" />
		 	</div>
			<div class="data_table_div">
				<table class="data_table" id="user_permissions">
					<thead>
						<tr class="dt_header">
							<td width="200"><center><b> User Name </b></center></td>
							<td><center><b> View </b></center></td>
							<td><center><b> Update </b></center></td>
							<td><center><b> Import </b></center></td>
							<td><center><b> Export </b></center></td>
							<td><center><b> Delete </b></center></td>
							<td width="100"><center><b> Access Control </b></center></td>
					 	</tr>
						<tr>
							<td>
								<center>All anonymous users</center>
							 	<@s.hidden name="permForAnonyUser.id"  />
							 	<@s.hidden name="permForAnonyUser.userId" />
							 	<@s.hidden name="permForAnonyUser.name" />
							</td>
							<td><center><@s.checkbox name="permForAnonyUser.viewAllowed" /></center></td>
							<td><center><@s.hidden name="permForAnonyUser.updateAllowed" /></center></td>
							<td><center><@s.hidden name="permForAnonyUser.importAllowed" /></center></td>
							<td><center><@s.checkbox name="permForAnonyUser.exportAllowed" /></center></td>
							<td><center><@s.hidden name="permForAnonyUser.deleteAllowed" /></center></td>
							<td><center><@s.hidden name="permForAnonyUser.changePermAllowed" /></center></td>
						</tr>
						<tr>
							<td colspan="7"></td>
						</tr>
						<tr>
							<td>
								<center>All registered users</center>
								<@s.hidden name="permForAllRegUser.id" />
								<@s.hidden name="permForAllRegUser.userId" />
								<@s.hidden name="permForAllRegUser.name" />
							</td>
							<td><center><@s.checkbox name="permForAllRegUser.viewAllowed" /></center></td>
							<td><center><@s.checkbox name="permForAllRegUser.updateAllowed" /></center></td>
							<td><center><@s.checkbox name="permForAllRegUser.importAllowed" /></center></td>
							<td><center><@s.checkbox name="permForAllRegUser.exportAllowed" /></center></td>
							<td><center><@s.checkbox name="permForAllRegUser.deleteAllowed" /></center></td>
							<td><center><@s.checkbox name="permForAllRegUser.changePermAllowed" /></center></td>
					 	</tr>
						<tr>
							<td colspan="7"></td>
						</tr>
					</thead>
					<tbody>
						<@s.iterator status="permStatus" value="permissionBeans" id="permBean" >
						<tr>
						 	<td>
						 	 	<center><@s.property  value="#permBean.name" /></center>
						 	 	<@s.hidden name="permissionBeans[%{#permStatus.index}].id"  value="%{#permBean.id}" />
						 	 	<@s.hidden name="permissionBeans[%{#permStatus.index}].userId" id ="user_id" value="%{#permBean.userId}" />
						 	 	<@s.hidden name="permissionBeans[%{#permStatus.index}].name" value="%{#permBean.name}" />
						 	</td>
						 	<td><center><@s.checkbox name="permissionBeans[%{#permStatus.index}].viewAllowed" /></center></td>
						 	<td><center><@s.checkbox name="permissionBeans[%{#permStatus.index}].updateAllowed" /></center></td>
						 	<td><center><@s.checkbox name="permissionBeans[%{#permStatus.index}].importAllowed" /></center></td>
						 	<td><center><@s.checkbox name="permissionBeans[%{#permStatus.index}].exportAllowed" /></center></td>
						 	<td><center><@s.checkbox name="permissionBeans[%{#permStatus.index}].deleteAllowed" /></center></td>
						 	<td><center><@s.checkbox name="permissionBeans[%{#permStatus.index}].changePermAllowed" /></center></td>
						</tr>
						</@s.iterator>
					</tbody>
				</table>
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