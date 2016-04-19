<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>User <@s.text name="user.profile.edit.action.title" /></title>
<#include "../template/header.ftl"/>
</head>
<body>
	<#include "../template/navsection.ftl"/>
	<div class="nav_namebar_div nav_title_gray">
		User
		<img border="0" src="${base}/images/grayarrow.png">
		<a href="${base}/manage/userHome.jspx">My Home</a>
		<img border="0" src="${base}/images/grayarrow.png">
		<a href="${base}/manage/showProfileUpdate.jspx"><@s.text name="user.profile.edit.action.title" /></a>
	</div>
	<div style="clear:both"></div>
 	<div class="main_container">
 		<#include "../template/action_errors.ftl">
 		<#include "../template/action_message.ftl">
	 	<div class="container_inner_left">
 			<div class="blank_separator"></div>	
			<form action="updateProfile.jspx" namespace="/manage" method="post">	
				<div class="parag_title_div">
					Your basic information
				</div>
				<div class="data_outer_div">
					<div class="data_label_div">
						User first name:
					</div>
					<div class="data_value_div">
						<@s.textfield name="user.firstName" cssClass="input_field_normal"/>
					</div>
					<div class="data_label_div">
						User last name:
					</div>
					<div class="data_value_div">
						<@s.textfield name="user.lastName" cssClass="input_field_normal"/>
					</div>
					<div class="data_label_div">
						Joined:
					</div>
					<div class="data_value_div">
						<@s.date name="user.registedDate" format="yyyy-MM-dd" /> <@s.hidden name="user.registedDate" />
					</div>
					<div class="data_label_div">
						Gender:
					</div>
					<div class="data_value_div">
					 	<@s.select name="profile.gender"  headerKey="${profile.gender}" list="genderMap"  cssClass="input_select_normal"/>
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
						<@s.property value="user.email" />  <@s.hidden name="user.email" />
					</div>
					<div class="data_label_div">
						Contact details:
					</div>
					<div class="data_value_div">
						<@s.textarea  name="profile.contactDetails" cols="50" rows="5" cssClass="input_textarea" />
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
						<@s.textfield name="profile.address" cssClass="input_field_normal" /> 
					</div>
					
					<div class="data_label_div">
						City:
					</div>
					<div class="data_value_div">
						<@s.textfield name="profile.city" cssClass="input_field_normal" /> 
					</div>
					
					<div class="data_label_div">
						State:
					</div>
					<div class="data_value_div">
						<@s.textfield name="profile.state" cssClass="input_field_normal" /> 
					</div>
					
					<div class="data_label_div">
						Post code:
					</div>
					<div class="data_value_div">
						<@s.textfield name="profile.postcode" cssClass="input_field_normal" /> 
					</div>
					
					<div class="data_label_div">
						Country:
					</div>
					<div class="data_value_div">
						<@s.select name="profile.country" headerKey="${profile.country}"  list="countryMap" cssClass="input_select_normal"/> 
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
						<@s.textfield name="profile.industryField" cssClass="input_field_normal" />
					</div>
					
					<div class="data_label_div">
						Occupation (Roles):
					</div>
					<div class="data_value_div">
						<@s.textfield name="profile.occupation" cssClass="input_field_normal" />
					</div>
					
					<div class="data_label_div">
						Organization you belong to:
					</div>
					<div class="data_value_div">
						<td><@s.textfield name="profile.organization" cssClass="input_field_normal" />
					</div>
					
					<div class="data_label_div">
						Professional Interests:
					</div>
					<div class="data_value_div">
						<@s.textarea  name="profile.interests" cols="50" rows="5" cssClass="input_textarea" />
					</div>
				</div>
				
				<div class="data_outer_noborder_div">
					<@s.submit value="Update" cssClass="input_button" /> &nbsp; <@s.reset value="Clear" cssClass="input_button" />
				</div>
				<div style="clear:both"></div>
			</form> 		
			<br/>
		</div>
		<div class="container_inner_right">
			<#include "../template/user_nav.ftl">
		</div>
		<div style="clear:both"></div>  
 	</div>
	<#include "../template/footer.ftl"/>
</body>
</html>


