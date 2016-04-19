<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><@s.text name="forgot.password.action.title" /></title>
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
		<a href="${base}/user/user_request_resetpwd"><@s.text name="forgot.password.action.title" /></a>
	</div>
	<div style="clear:both"></div>
	<!-- main body -->
 	<div class="main_container">
 		<#include "../template/action_errors.ftl">	
 		<div class="pannel_info">
			Reset Password Request 
		</div>
 		<div class="container_inner">
 			<div class="reguser_div">
 			<@s.form action="forgotPasswd.jspx" namespace="/user" method="post">
 				 <div class="input_field_div"> First Name: </div>
 				 <div class="input_field_div">
 				 	<@s.textfield name="user.firstName" cssClass="input_field_normal" />
 				 </div>	
 				 <div class="input_field_comment">
 				 	* (You first name)
 				 </div>
 				 
 				 <div class="input_field_div"> Last Name: </div>
 				 <div class="input_field_div">
 				 	<@s.textfield name="user.lastName" cssClass="input_field_normal" />
 				 </div>	
 				 <div class="input_field_comment">
 				 	* (You last name)
 				 </div>
 				 
 				 <div class="input_field_div"> Your E-mail: </div>
 				 <div class="input_field_div">
 				 	<@s.textfield name="user.email" cssClass="input_field_normal" />
 				 </div>	
 				 <div class="input_field_comment">
 				 	* (Your registed E-mail, e.g. yourname@example.com.)
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
						<@s.submit value="Request" cssClass="input_button" /> &nbsp; <@s.reset value="Clear" cssClass="input_button" />
					</div>
					<div>
						<span class="line_span"><img src="${base}/images/hint.png" class="hints_image" /> Monash Authcate user, please contact ITS support service to reset your password.</span>
					</div>
					<div style="clear:both"></div>
				</div>
			</@s.form>
 			</div>
		 	<br/>
		</div>
 	</div>
	<#include "../template/footer.ftl"/>
</body>
</html>


