<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>User <@s.text name="avatar.image.action.title" /></title>
<#include "../template/jquery_header.ftl"/>
<script language="Javascript">
	jQuery(window).load(function(){
		jQuery('#cropbox').Jcrop({
			onChange: showPreview,
			onSelect: showPreview,
			aspectRatio: 1,
			bgOpacity:   .8,
			setSelect: [0,0,50,50],
			minSize: [50,50],
			});
	});
	var imageWidth= <@s.property value='imgWidth' />;
	var imageHeight = <@s.property value='imgHeight' />;
	
	// Our simple event handler, called from onChange and onSelect
	// event handlers, as per the Jcrop invocation above
	function showPreview(coords)
	{
		if (parseInt(coords.w) > 0)
		{
			var rx = 48 / coords.w; 
			var ry = 48 / coords.h; 

			jQuery('#preview').css({
				width: Math.round(rx * imageWidth) + 'px',
				height: Math.round(ry * imageHeight) + 'px',
				marginLeft: '-' + Math.round(rx * coords.x) + 'px',
				marginTop: '-' + Math.round(ry * coords.y) + 'px'
			});
			//set the coordinates.
			jQuery('#imageX1').val(coords.x);
			jQuery('#imageY1').val(coords.y);
			jQuery('#imageX2').val(coords.x2);
			jQuery('#imageY2').val(coords.y2);
			jQuery('#imageW').val(coords.w);
			jQuery('#imageH').val(coords.h);
		}
	}
</script>
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
			<div class="data_outer_div">
				<table border='0'>
					<tr>
						<td>
							<div>
								<img src="${base}/<@s.property value='userImageName' />" id="cropbox" />
							</div>
						</td>
						<td width="20">&nbsp;</td>
						<td>
							<div style="width:48px;height:48px;overflow:hidden;">
								<img src="${base}/<@s.property value='userImageName' />" id="preview" />
							</div>
						</td>
					</tr>
					<tr>
						<td align="center">
							<div class="blank_separator"></div>	
							<@s.form action="saveAvatar.jspx" namespace="/manage" method="post">
								<@s.hidden name="imageX1" id="imageX1"/>
							    <@s.hidden name="imageY1" id="imageY1"/>
							    <@s.hidden name="imageX2" id="imageX2"/>
							    <@s.hidden name="imageY2" id="imageY2"/>
							    <@s.hidden name="imageW" id="imageW"/>
							    <@s.hidden name="imageH" id="imageH"/>
							    <@s.hidden name="userImageName" />
							    <@s.hidden name="imgWidth" />
							    <@s.hidden name="imgHeight" />
							    
							    <@s.submit value=" Save " cssClass="input_button" />
						    </@s.form>
						</td>
						<td></td><td></td>
					</tr>
				</table>
			</div>
			<div style="clear:both"></div> 
			<div class="blank_separator"></div>	
		</div>
		<div class="container_inner_right">
			<#include "../template/user_nav.ftl">
		</div>
		<div style="clear:both"></div>  
	</div>
	<#include "../template/footer.ftl"/>
</body>
</html>