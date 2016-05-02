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
 		<div>
	 		<div class="container_inner_left">
	 			<div class="empty_div"></div>
				<div class="redirect_pane"> 
					<br/>
					<br/>
					<b><@s.property value="user.displayName"/></b> &nbsp;&nbsp; Logged in successfully.&nbsp;&nbsp;  Welcome to <b>${appName}</b>! 
					<br/>
					<br/>
					<span class="redirect_span">After a few seconds, the page will redirect ...</span>
					<br/>	
					<br/>
					<span class="redirect_span">Problems with the redirect? Please use this <a href='${base}/<@s.property value="requestUrl" escape=false />'>direct link</a>.</span>
					<br/>
					<div style="clear:both"></div> 
				</div>
				<div class="empty_div"></div>
				<div class="empty_div"></div>  	
				<div class="empty_div"></div>
			</div>
			<div class="container_inner_right">
				<#include "../template/user_nav.ftl">
			</div>
			<div style="clear:both"></div>  
		</div>
 	</div>
	<#include "../template/footer.ftl"/>
	<script>
		function jump()
		{
			location.href = '${base}/<@s.property value="requestUrl" escape=false />';
		}
		setTimeout("jump()", 3000);
	</script>
</body>
</html>


