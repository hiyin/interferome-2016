<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><@s.text name="user.self.register.action.title" /></title>
<#include "../template/header.ftl"/>
<script type="text/javascript">
function refresh()
{
	document.getElementById("imagevalue").src='${base}/security/securityCode.jspx?now=' + new Date();
}
</script>
</head>
<body>
	<#include "../template/navsection.ftl"/>
	<!-- nav title -->
	<div class="nav_namebar_div nav_title_gray">
		User <img border="0" src="${base}/images/grayarrow.png">
		<a href="${base}/user/register_options"><@s.text name="user.register.options.title" /></a>
		<img border="0" src="${base}/images/grayarrow.png">
		<a href="${base}/user/user_register"><@s.text name="user.self.register.action.title" /></a>
	</div>
	<div style="clear:both"></div>
	<!-- main body -->
 	<div class="main_container">
 		<#include "../template/action_errors.ftl">
 		<div class="container_inner">
 			<div class="reguser_div">
 			
 			<@s.form action="registerUser.jspx" name="registeruser" namespace="/user" method="post">
 				 <div class="input_field_div"> First Name: </div>
 				 <div class="input_field_div">
 				 	<@s.textfield name="user.firstName" cssClass="input_field_normal" />
 				 </div>	
 				 <div class="input_field_comment">
 				 	* (<@s.text name="user.register.first.name.spec" />)
 				 </div>
 				 
 				 <div class="input_field_div"> Last Name: </div>
 				 <div class="input_field_div">
 				 	<@s.textfield name="user.lastName" cssClass="input_field_normal" />
 				 </div>	
 				 <div class="input_field_comment">
 				 	* (<@s.text name="user.register.last.name.spec" />)
 				 </div>
 				 
 				 <div class="input_field_div"> E-mail: </div>
 				 <div class="input_field_div">
 				 	<@s.textfield name="user.email" cssClass="input_field_normal" />
 				 </div>	
 				 <div class="input_field_comment">
 				 	* (<@s.text name="user.register.email.spec" />)
 				 </div>
 				 
 				 <div class="input_field_div"> Password: </div>
 				 <div class="input_field_div">
 				 	<@s.password name="user.password" cssClass="input_field_normal" />
 				 </div>	
 				 <div class="input_field_comment">
 				 	* (<@s.text name="user.register.password.spec" />)
 				 </div>
 				 
 				 <div class="input_field_div"> Organization: </div>
 				 <div class="input_field_div">
 				 	<@s.textfield name="organization" cssClass="input_field_normal" />
 				 </div>	
 				 <div class="input_field_comment">
 				 	* (<@s.text name="user.register.organization.spec" />)
 				 </div>
 				 
 				  <div class="input_field_div"> Word Verification: </div>
 				 <div class="input_field_div">
 				 	<@s.textfield name="securityCode" cssClass="input_field_normal" />
 				 </div>	
 				 <div class="input_field_comment">
 				 	* (<@s.text name="security.code.spec" />)
 				 </div>
 				 <div class="security_code">
 				 	<img src="${base}/security/securityCode.jspx?now=new Date()" border="0" id="imagevalue" name="imagevalue" />
 				 	&nbsp; <a href="#" onclick="refresh()"><img src="${base}/images/refresh.png" class="security_code_img" /> can't read this?</a>
 				 </div>
 				 <br>
 				 <div>
 				 	<div class="button_div">
						<@s.submit value="Register" cssClass="input_button" /> &nbsp; <@s.reset value="Clear" cssClass="input_button" />
					</div>
					<div>
						<span class="line_span">If you already have an account, please <a href="${base}/user/showLogin.jspx">Sign in now </a></span>
					</div>
					<div style="clear:both"></div>
				</div>
			</@s.form>
 			</div>
            <br/>
		 	<div style="clear:both"></div>
		</div>
 	</div>
	<#include "../template/footer.ftl"/>
</body>
</html>


