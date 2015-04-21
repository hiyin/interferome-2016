<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><@s.text name="admin.manage.view.user.details..action.title" /></title>
<#include "../template/header.ftl"/>
</head>
<body>
	<#include "../template/navsection.ftl"/>
	<div class="nav_namebar_div nav_title_gray">
		User
		<img border="0" src="${base}/images/grayarrow.png">
		<a href="${base}/admin/showUserDetails.jspx?regUser.id=<@s.property value='regUser.id' />"><@s.text name="admin.manage.view.user.details..action.title" /></a>
	</div>
	<div style="clear:both"></div>
 	<div class="main_container">
 		<#include "../template/action_errors.ftl">
 		<#include "../template/action_message.ftl">
	 	<div class="container_inner_left">
 			<div class="blank_separator"></div>	
 			
 			<@s.if test="%{successActMsg != null}">
 			<div class="data_header_div">
			 	<#include "../template/success_act_message.ftl"/>
			 </div>
			</@s.if>
			
			<div class="parag_title_div">
				Basic information
			</div>
			<div class="data_outer_div">
				<table width="100%">
					<tr>
						<td width="100" rowspan="5">
							<div class="user_avatar">
								<img src="${base}/user/viewAvatar.jspx?avatarUserId=<@s.property value='regUser.id' />">
							</div>
						</td>
					</tr>
					<tr>
						<td width="100"><span class="data_black">User name:</span></td>
						<td width="200" style="text-indent: 5px;"><@s.property value="regUser.displayName"/></td>
						<td></td>
					</tr>
					<tr>
						<td><span class="data_black">Gender:</span></td>
						<td style="text-indent: 5px;">
							<@s.if test="%{profile.gender != null}">
								<@s.property value="profile.gender" />
							</@s.if>
							<@s.else>
								Not specified
							</@s.else>
						</td>
						<td></td>
					</tr>
					<tr>
						<td><span class="data_black">Joined:</span></td>
						<td style="text-indent: 5px;"><@s.date name="regUser.registedDate" format="yyyy-MM-dd" /></td>
						<td></td>
					</tr>
					<tr>
						<td><span class="data_black">Email:</span></td>
						<td style="text-indent: 5px;"><@s.property value="regUser.email" /></td>
						<td ></td>
					</tr>
					<tr>
						<td></td>
						<td><span class="data_black">Active status:</span></td>
						<td style="text-indent: 5px;"><@s.property value="regUser.activated"/></td>
						<td align="center">
							<@s.if test="%{(user.userType == 1 || user.userType == 2) && (#session.authen_user_id != regUser.id) && (regUser.userType !=1)}">
							<@s.form action="manageUser.jspx" namespace="/admin" method="post">
								<@s.hidden name="regUser.id" />
								<@s.if test = "%{regUser.activated == true }">
									<@s.hidden name="manageType" value="deactivate" />
									<@s.submit value="Deactivate" cssClass="data_button_normal" />
								</@s.if>
								<@s.else>
									<@s.hidden name="manageType" value="activate" />
									<@s.submit value="Activate" cssClass="data_button_normal" />
								</@s.else>
							</@s.form>
							</@s.if>
						</td> 
					</tr>
					<tr>
						<td></td>
						<td><span class="data_black">User Type:</span></td>
						<td style="text-indent: 5px;">
							<@s.if test = "%{regUser.userType == 1 }">Super Admin</@s.if>
							<@s.elseif  test = "%{regUser.userType == 2 }">Admin</@s.elseif>
					 		<@s.else>User</@s.else>
						</td>
						<td align="center">
						<@s.if test="%{(user.userType == 1 || user.userType == 2) && (#session.authen_user_id != regUser.id)}">
							<@s.form action="manageUser.jspx" namespace="/admin" method="post">
							<@s.hidden name="regUser.id" />
								<@s.if test = "%{regUser.userType ==3 && regUser.activated == true }">
									<@s.hidden name="manageType" value="setasadmin" />
									<@s.submit value="Set As Admin" cssClass="data_button_normal" />
								</@s.if>
								<@s.if test = "%{regUser.userType ==2 && regUser.activated == true }">
									<@s.hidden name="manageType" value="setasuser" />
									<@s.submit value="Set As User" cssClass="data_button_normal" />
								</@s.if>
							</@s.form>
						</@s.if>
						</td> 
					</tr>
				</table>
			</div>
				
			<div class="parag_title_div">
				Contact details
			</div>
			<div class="data_outer_div">
				<div class="data_label_div">
					E-mail:
				</div>
				<div class="data_value_div">
					<@s.property value="regUser.email" />
				</div>
				<div class="data_label_div">
					Contact details:
				</div>
				<div class="data_value_text">
					<@s.if test="%{profile.contactDetails != null}">
						<@s.property value="profile.contactDetails" escape=false/>
					</@s.if>
					<@s.else>
						Not specified
					</@s.else>
					<div style="clear:both"></div>
				</div>
				<div style="clear:both"></div>
			</div>
				
			<div class="parag_title_div">
				Location
			</div>
			<div class="data_outer_div">
				<div class="data_label_div">
					Address:
				</div>
				<div class="data_value_div">
					<@s.if test="%{profile.address != null}">
						<@s.property value="profile.address" />
					</@s.if>
					<@s.else>
						Not specified
					</@s.else>
				</div>
				
				<div class="data_label_div">
					City:
				</div>
				<div class="data_value_div">
					<@s.if test="%{profile.city != null}">
						<@s.property value="profile.city" />
					</@s.if>
					<@s.else>
						Not specified
					</@s.else>
				</div>
				
				<div class="data_label_div">
					State:
				</div>
				<div class="data_value_div">
					<@s.if test="%{profile.state != null}">
						<@s.property value="profile.state" />
					</@s.if>
					<@s.else>
						Not specified
					</@s.else>
				</div>
				
				<div class="data_label_div">
					Post code:
				</div>
				<div class="data_value_div">
					<@s.if test="%{profile.postcode != null}">
						<@s.property value="profile.postcode" />
					</@s.if>
					<@s.else>
						Not specified
					</@s.else>
				</div>
				
				<div class="data_label_div">
					Country:
				</div>
				<div class="data_value_div">
					<@s.if test="%{profile.country != null}">
						<@s.property value="profile.country" />
					</@s.if>
					<@s.else>
						Not specified
					</@s.else>
				</div>
				<div style="clear:both"></div>  
			</div>
			<div class="parag_title_div">
				Work (professional) life
			</div>
			<div class="data_outer_div">
				<div class="data_label_div">
					Field(Industry):
				</div>
				<div class="data_value_div">
					<@s.if test="%{profile.industryField != null}">
						<@s.property value="profile.industryField" />
					</@s.if>
					<@s.else>
						Not specified
					</@s.else>
				</div>
				
				<div class="data_label_div">
					Occupation (Roles):
				</div>
				<div class="data_value_div">
					<@s.if test="%{profile.occupation != null}">
						<@s.property value="profile.occupation" />
					</@s.if>
					<@s.else>
						Not specified
					</@s.else>
				</div>
				
				<div class="data_label_div">
					Organization you belong to:
				</div>
				<div class="data_value_div">
					<@s.if test="%{profile.organization != null}">
						<@s.property value="profile.organization" />
					</@s.if>
					<@s.else>
						Not specified
					</@s.else>
				</div>
				<div class="data_label_div">
					Professional Interests:
				</div>
				<div class="data_value_text">
					<@s.if test="%{profile.interests != null}">
						<@s.property value="profile.interests"  escape=false  />
					</@s.if>
					<@s.else>
						Not specified
					</@s.else>
				</div>
				<div style="clear:both"></div> 
			</div>
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