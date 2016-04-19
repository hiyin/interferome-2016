<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${pageTitle}</title>
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
	<#include "../template/action_nav_bar.ftl">
 	<div class="main_container">
 		<#include "../template/action_errors.ftl">
 		
 		<div class="pannel_info">
	 		<@s.property value="loginTryMsg" /> 
	 	</div>
 		
 		<div class="container_inner">
 			<div class="reguser_div">
 				
	 			<@s.form action="userLogin.jspx" namespace="/user" method="post">
					<div class="input_field_div"><@s.text name="user.unique.id" />: </div>
	 				<div class="input_field_div">
 				 		<@s.textfield name="user.uniqueId" cssClass="input_field_normal" />
 				 	</div>	
					<div class="input_field_comment">
						* (<@s.text name="user.login.unique.id.spec" />)
					</div>
					
					<div class="input_field_div"><@s.text name="user.password" />: </div>
	 				<div class="input_field_div">
 				 		<@s.password name="user.password" cssClass="input_field_normal" />
 				 	</div>	
					<div class="input_field_comment">
						* (<@s.text name="user.login.password.spec" />)
					</div>
					
					<div class="input_field_div">Word Verification: </div>
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
 				 	<div style="clear:both"></div>
 				 	<br>
 				 	<div>
						<div class="button_div"><@s.submit value="Login" cssClass="input_button" /> &nbsp; <@s.reset value="Reset" cssClass="input_button" /></div>
						<div>
							<span class="line_span">Don't have an Account, <a href="${base}/user/register_options">Register an account now </a></span>
							<br/><span class="line_span"><a href="${base}/user/user_request_resetpwd">Forgot your password?</a></span>
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

