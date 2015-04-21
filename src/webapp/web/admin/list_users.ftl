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
		<div class="container_inner_left">
			<div class="blank_separator"></div>	
			<@s.if test="%{userPagination.pageResults.size() > 0}">
			<div class="data_header_div">
				<@s.if test="%{successActMsg != null}">
			 		<#include "../template/success_act_message.ftl"/>
			 	</@s.if>
				<span class="name_title">A total of <font color="green"> ${userPagination.totalRecords} </font> users</span>
				<div class="blank_separator"></div>
				<div class="url_contect">
					<a href="${base}/${pageLink}${pageSuffix}<@s.property value='userPagination.pageNo' />" class="page_url"></a>
				</div>
				<#include "../pagination/pagination_header.ftl"/>
			</div>
			</@s.if>
			<@s.else>
			<div class="data_header_div">
				<span class="name_title">A total of <font color="green"> ${userPagination.totalRecords} </font> users</span>
			</div>
			<div class="empty_div"></div>
			<div class="empty_div"></div>
			<div class="empty_div"></div>
			<div class="empty_div"></div>
			<div class="empty_div"></div>
			<div class="empty_div"></div>
			<br/>
			<div style="clear:both"></div>  
			</@s.else>
			
			
			<@s.if test="%{userPagination.pageResults.size() > 0}">
			<div class="data_table_div">
				<table class="data_table">
					<tr class="dt_header">
						<td width="100"><center><b>Name</b></center></td>
					 	<td width="180" ><center><b>Email</b></center></td>
					 	<td width="250"><center><b>Organization</b></center></td>
					 	<td width="70"><center><b>User Type</b></center></td>
					 	<td width="40"><center><b>Active</b></center></td>
					 	<td width="70"><center><b>&nbsp;</b></center></td>
					 </tr>
					
					 <@s.iterator status="userStat" value="userPagination.pageResults" id="userResult" >
					<tr class="dt_tr_small">
						<td style="color: black"><center><@s.property value="#userResult.displayName" /></center></td>
						<td><center><@s.property value="#userResult.email" /></center></td>
						<td><center><@s.property value="#userResult.profile.organization" /></center></td>
						<td>
							<center>
								<@s.if test = "%{#userResult.userType == 1 }">Super Admin</@s.if>
								<@s.elseif test = "%{#userResult.userType == 2 }">Admin</@s.elseif>
								<@s.else>User</@s.else>
							</center>
						</td>
						<td><center><@s.property value="#userResult.activated"/></center></td>
						<td>
							<div class="data_link_small">
						 		<center>
						 			<@s.if test="%{(user.userType == 1 || user.userType == 2) && (#session.authen_user_id != #userResult.id) && (#userResult.userType !=1)}">
						 				<a href="${base}/admin/showUserDetails.jspx?regUser.id=<@s.property value='#userResult.id' />">Manage</a>
						 			</@s.if>
						 			<@s.else>
						 				<a href="${base}/admin/showUserDetails.jspx?regUser.id=<@s.property value='#userResult.id'/>">View</a>
						 			</@s.else>
						 		</center>
							</div>
						</td>
					</tr>
					</@s.iterator>
				</table>
			</div>
			<div class="blank_separator"></div>	
			<#include "../pagination/user_page_style.ftl" />
			<div class="blank_separator"></div>	
			</@s.if>
		</div>
		<div class="container_inner_right">
			<#include "../template/user_nav.ftl">
		</div>
		<div style="clear:both"></div>  
	</div>
	<#include "../template/footer.ftl"/>
</body>
</html>