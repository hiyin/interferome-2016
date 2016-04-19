<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><@s.text name="page.action.title" /></title>
<#include "../template/header.ftl"/>
</head>
<body>
	<#include "../template/navsection.ftl"/>
	<!-- nav title -->
	<div class="nav_namebar_div nav_title_gray">
		Name <img border="0" src="${base}/images/grayarrow.png"> <a href="">Name2</a>
	</div>
	<div style="clear:both"></div>
	
 	<div class="main_container">
 		<#include "../template/action_errors.ftl">
 		<div class="container_inner">
			<div class="empty_div"></div>  	
			<div class="empty_div"></div>
			<div class="empty_div"></div>
			<div class="empty_div"></div>
			<div style="clear:both"></div>  
		</div>
 	</div>
	<#include "../template/footer.ftl"/>
</body>
</html>


