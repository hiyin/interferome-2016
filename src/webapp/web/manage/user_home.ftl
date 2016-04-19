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
 		<#include "../template/action_message.ftl">
		<div class="container_inner_left">
			<div class="blank_separator"></div>	
			 <div class="parag_title_div">
			 	Profile
			 </div>
			 <div class="data_outer_div">
			 	<div class="data_label_div">
			 		User name:
				</div>
				<div class="data_value_div">
					<@s.property value="user.displayName"/>
				</div>
				
				<div class="data_label_div">
			 		Gender:
				</div>
				<div class="data_value_div">
					<@s.if test="%{profile.gender != null}">
			 			<@s.property value="profile.gender" />
			 		</@s.if>
			 		<@s.else>
			 			Not specified
			 		</@s.else>
				</div>
				
				<div class="data_label_div">
			 		Joined:
				</div>
				<div class="data_value_div">
					<@s.date name="user.registedDate" format="dd-MM-yyyy 'at' hh:mm a" />
				</div>
				
				<div class="data_label_div">
			 		E-mail:
				</div>
				<div class="data_value_div">
					<@s.property value="user.email" />
				</div>
				
				<div class="data_label_div">
			 		Organization:
				</div>
				<div class="data_value_div">
					<@s.if test="%{profile.organization != null}">
			 			<@s.property value="profile.organization" />
			 		</@s.if>
			 		<@s.else>
			 			Not specified
			 		</@s.else>
				</div>
				
				<div class="data_right_div">
					<a href="${base}/manage/showProfileUpdate.jspx"> Edit Profile </a> &nbsp; <a href="${base}/manage/showChangePwd.jspx">Change Password</a>
				</div>
				<div style="clear:both"></div>  
			 </div>
			
			 <div class="parag_title_div">
			 	Permission Requests
			 </div>
			<div class="data_outer_div">
			    <div class="data_each_row">
                    <div class="blank_separator"></div>
				    &nbsp;&nbsp;A total of <font color="#0E774A"><@s.property value="totalPermRequests" /></font> permission request(s)
                    <@s.if test="%{totalPermRequests > 0}">
                    <div class="data_right_div">
					    <a href="${base}/perm/listPermRequests.jspx">View Details</a>
				    </div>
                    </@s.if>
                    <div class="blank_separator"></div>
                    <div style="clear:both"></div>
				</div>
			</div>
			 <div class="parag_title_div">
			 	Latest Events
			 </div>
			 <div class="data_outer_div">
			 <@s.if test="%{eventPagination.pageResults.size() > 0}">
				<@s.iterator status="eventStat" value="eventPagination.pageResults" id="eventResult" >
					<@s.if test="%{#eventStat.index +1 <= 5}">	
					<div class="data_each_row">
						<div class="data_img1"><img src="${base}/images/dot_grey.png" align="top" border="0" /></div>
					 	<div class="data_inline2">
					 		<font color="#0E774A"><@s.date name="#eventResult.createdTime"  format="dd-MM-yyyy 'at' hh:mm a" /></font> &nbsp;&nbsp;
					 		<@s.property value="#eventResult.event" />, by <@s.property value="#eventResult.operator.displayName" />
					 	</div>
					 	<div style="clear:both"></div>  
					</div>
					</@s.if>
				</@s.iterator>
			</@s.if>
			<@s.else>
				<div class="empty_div"></div>
					No Events
				<div class="empty_div"></div>
			</@s.else>
			<@s.if test="%{eventPagination.pageResults.size() > 5}">
				<div class="data_right_div">
					<a href="${base}/manage/getUserEvents.jspx">More Events</a>
				</div>
				<div style="clear:both"></div> 
			</@s.if>
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


