<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><@s.text name="interferome.help.action.title" /></title>
<#include "../template/header.ftl"/>
</head>
<body>
	<#include "../template/navsection.ftl"/>
	<div class="nav_namebar_div nav_title_gray">
		<a href="${base}/site/showHelp.jspx"><@s.text name="interferome.help.action.title" /></a>
	</div>
	<div style="clear:both"></div>
 	<div class="main_container">
 		<#include "../template/action_errors.ftl">
		<div class="container_inner_left">
	        <div class="blank_separator"></div>

			<div class="data_outer_div">
                <div class="empty_div"></div>
                Please download the PDF version : <a href="http://interferome.googlecode.com/files/Data%20Publication%20to%20Interferome%20User%20Guide25.pdf">User Guide</a>
                <div class="empty_div"></div>
                <div class="empty_div"></div>
                <div class="empty_div"></div>
                <div class="empty_div"></div>
                <div class="empty_div"></div>
			</div>
            <div class="blank_separator"></div>
		</div>
        <@s.if test="%{#session.authentication_flag =='authenticated'}">
		<div class="container_inner_right">
			<#include "../template/user_nav.ftl">
		</div>
        </@s.if>
		<div style="clear:both"></div>
 	</div>
	<#include "../template/footer.ftl"/>
</body>
</html>
