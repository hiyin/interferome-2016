<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><@s.text name="user.register.options.title" /></title>
<#include "../template/header.ftl"/>
</head>
<body>
	<#include "../template/navsection.ftl"/>
	<!-- nav title -->
	<div class="nav_namebar_div nav_title_gray">
		User <img border="0" src="${base}/images/grayarrow.png"> <a href="${base}/user/register_options"><@s.text name="user.register.options.title" /></a>
	</div>
	<div style="clear:both"></div>
	
 	<div class="main_container">
 		<div class="container_inner">
 			<br/>
 			<div class="paragraph_title">
 				<@s.text name="user.register.options.message" />
			</div>
	 		<br/>
            <br/>
	 		<div class="reg_choices_div">
				<div class="reg_choices">
					<img src="${base}/images/mon_reg.png"  border="0" /> <a href="${base}/user/ldap_user_register"><strong><@s.text name="user.ldap.register.action.title" /></strong></a>
				</div>
				<br/>
				<br/>
				<br/>
                <br/>
				<div class="reg_choices">
					<img src="${base}/images/self_reg.png" border="0" /> <a href="${base}/user/user_register"><strong><@s.text name="user.self.register.action.title" /></strong></a>
				</div>
			</div>
            <div style="clear:both"></div>
            <br/>
            <br/>
            <br/>
            <br/>
		</div>
 	</div>
	<#include "../template/footer.ftl"/>
</body>
</html>


