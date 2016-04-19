<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>User <@s.text name="avatar.image.action.title" /></title>
<#include "../template/jquery_header.ftl"/>
</head>
<body>
	<#include "../template/navsection.ftl"/>
	<div class="nav_namebar_div nav_title_gray">
		User
		<img border="0" src="${base}/images/grayarrow.png">
		<a href="${base}/manage/showAvatarUpload.jspx"><@s.text name="avatar.image.action.title" /></a>
	</div>
	<div style="clear:both"></div>
 	<div class="main_container">
 		<#include "../template/action_errors.ftl">
 		<#include "../template/action_message.ftl">
		<div class="container_inner_left">
			<div class="blank_separator"></div>	
			<@s.form action="uploadAvatar.jspx" namespace="/manage" method="post" enctype="multipart/form-data" >
			<div class="data_outer_div">
				<div class="input_field_div">Upload Your Avatar Image: </div>
				<div class="blank_separator"></div>	
				<div class="input_field_div">
					<@s.file name="image" cssClass="file_upload" />
				</div>	
				<div class="blank_separator"></div>	
				<div class="input_field_comment">
				 	* (Only the <b>jpg</b>, <b>png</b> and <b>gif</b> image formats are supported. The minimum image size:[48x48] )
				</div>
				<div style="clear:both"></div>  
				
			</div>
			<div class="data_outer_noborder_div">
				<@s.submit value="Upload" cssClass="input_button" /> 
			</div>
			</@s.form>
			<div class="empty_div"></div>
			<div class="empty_div"></div>
			<div class="empty_div"></div>
			<div class="empty_div"></div>
		</div>
		<div class="container_inner_right">
			<#include "../template/user_nav.ftl">
		</div>
		<div style="clear:both"></div>  
	</div>
	<#include "../template/footer.ftl"/>
</body>
</html>