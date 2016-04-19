<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${pageTitle}</title>
<#include "../template/header.ftl"/>
<script>
	function reject()
	{
		targetForm = document.forms[0];
		targetForm.action = "rejectUserAccount.jspx";
		targetForm.submit();
	}
</script>
</head>
<body>
	<#include "../template/navsection.ftl"/>
	<#include "../template/action_nav_bar.ftl"/>
 	<div class="main_container">
 		<#include "../template/action_errors.ftl">
 		<#include "../template/action_message.ftl">
	 	<div class="container_inner_left">
 			<div class="blank_separator"></div>	
			<@s.form  action="activateUserAccount.jspx" namespace="/admin" method="post">	
			<@s.hidden name="regUser.id" />
			<@s.hidden name="regUser.activationHashCode" />
			<@s.hidden name="regUser.email" />
			<@s.hidden name="regUser.registedDate" />
			<@s.hidden name="regUsrProfile.organization" />
			<div class="parag_title_div">
				User Registration Information
			</div>
			<div class="data_outer_div">
				<div class="data_label_div">
					User Name:
				</div>
				<div class="data_value_div">
					<@s.property value="regUser.displayName"/>
				</div>
				<div class="data_label_div">
					User E-mail:
				</div>
				<div class="data_value_div">
					<@s.property value="regUser.email"/>
				</div>
				<div class="data_label_div">
					Joined:
				</div>
				<div class="data_value_div">
					<@s.date name="regUser.registedDate" format="dd-MM-yyyy 'at' hh:mm a" /> 
				</div>
				<div class="data_label_div">
					Organization:
				</div>
				<div class="data_value_div">
				 	<@s.property value="regUsrProfile.organization"/>
				</div>
				<div style="clear:both"></div>  
			 </div>
			<div class="data_outer_noborder_div">
				<@s.submit value="Activate" cssClass="input_button" /> &nbsp; <@s.submit value="Reject" onclick="reject();" cssClass="input_button" />
			</div>
			</@s.form>
			<div class="empty_div"></div> 
			<div class="empty_div"></div> 
			<div class="empty_div"></div>
			<div class="blank_separator"></div>	
			<div class="blank_separator"></div>		
			<br/>
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