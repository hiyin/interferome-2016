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
			 		<a href="${base}/data/viewExperiment.jspx?experiment.id=<@s.property value='experiment.id' />">View Details</a>
			 	</div>
			 	<div style="clear:both"></div>
			</div>
			<div class="parag_title_div">
				Apply For Permissions
			</div>
			<div class="data_outer_div">
			    <br/>
                <@s.if test="%{permReq.viewAllowed == false && permReq.updateAllowed == false && permReq.importAllowed == false && permReq.exportAllowed == false && permReq.deleteAllowed == false && permReq.changePermAllowed == false }">
			        Please select which permissions you would like to apply for
                </@s.if>
                <@s.else>
                    The permissions you already applied for
                </@s.else>
                <br/>
			</div>
			<@s.form action="applyForPerms.jspx" namespace="/perm" method="post">
			<div class="data_table_div">
				<table class="data_table" id="permission_req">
					<thead>
						<tr class="dt_header">
							<td><center><b> View </b></center></td>
							<td><center><b> Update </b></center></td>
							<td><center><b> Import </b></center></td>
							<td><center><b> Export </b></center></td>
							<td><center><b> Delete </b></center></td>
							<td width="100"><center><b> Access Control </b></center></td>
					 	</tr>
					</thead>
                    <tbody>
                         <tr>
                             <td>
                                <@s.hidden name="experiment.id" />
                                <@s.hidden name="experiment.name"/>
                                <@s.hidden name="experiment.description" />
                                <@s.hidden name="experiment.owner.id" />
                                <@s.hidden name="experiment.owner.displayName"  />
                                <@s.hidden name="experiment.createdTime" />
                                <@s.hidden name=="experiment.modifiedByUser.displayName" />
                                <@s.hidden name="experiment.modifiedTime" />
                                <@s.hidden name="permReq.id" />
                                <center><@s.checkbox name="permReq.viewAllowed" /></center>
                             </td>
                             <td><center><@s.checkbox name="permReq.updateAllowed" /></center></td>
                             <td><center><@s.checkbox name="permReq.importAllowed" /></center></td>
                             <td><center><@s.checkbox name="permReq.exportAllowed" /></center></td>
                             <td><center><@s.checkbox name="permReq.deleteAllowed" /></center></td>
                             <td><center><@s.checkbox name="permReq.changePermAllowed" /></center></td>
                         </tr>
                    </tbody>
				</table>
			</div>
            <div class="data_header_div">
                 <@s.if test="%{permReq.viewAllowed == false && permReq.updateAllowed == false && permReq.importAllowed == false && permReq.exportAllowed == false && permReq.deleteAllowed == false && permReq.changePermAllowed == false }">
			        <@s.submit value="Apply" cssClass="input_button" />
                </@s.if>
                <@s.else>
                   <@s.submit value="Apply Again" cssClass="input_button2" />
                </@s.else>
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