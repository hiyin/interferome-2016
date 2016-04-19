<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>User <@s.text name="user.profile.action.title" /></title>
<#include "../template/header.ftl"/>
</head>
<body>
	<#include "../template/navsection.ftl"/>
	<div class="nav_namebar_div nav_title_gray">
		User
		<img border="0" src="${base}/images/grayarrow.png">
		<a href="${base}/manage/userHome.jspx">My Home</a>
		<img border="0" src="${base}/images/grayarrow.png">
		<a href="${base}/manage/showProfile.jspx"><@s.text name="user.profile.action.title" /></a>
	</div>
	<div style="clear:both"></div>
 	<div class="main_container">
 		<#include "../template/action_errors.ftl">
 		<#include "../template/action_message.ftl">
	 	<div class="container_inner_left">
 			<div class="blank_separator"></div>	
			<div class="parag_title_div">
				Your basic information
			</div>
			<div class="data_outer_div">
				<div class="data_label_div">
					User first name:
				</div>
				<div class="data_value_div">
					<@s.property value ="user.firstName" />
				</div>
				<div class="data_label_div">
					User last name:
				</div>
				<div class="data_value_div">
					<@s.property value="user.lastName" />
				</div>
				<div class="data_label_div">
					Joined:
				</div>
				<div class="data_value_div">
					<@s.date name="user.registedDate" format="dd-MM-yyyy 'at' hh:mm a" />
				</div>
				<div class="data_label_div">
					Gender:
				</div>
				<div class="data_value_div">
				 	<@s.property value="profile.gender" />
				</div>
				<div style="clear:both"></div>  
			</div>
				
			<div class="parag_title_div">
				Your contact details
			</div>
			<div class="data_outer_div">
				<div class="data_label_div">
					E-mail:
				</div>
				<div class="data_value_div">
					<@s.property value="user.email" />
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
				Your location
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
				Your work (professional) life
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
			<div class="data_outer_noborder_div">
				<div class="data_right_div">
					<a href="${base}/manage/showProfileUpdate.jspx"> Edit Profile </a> 
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