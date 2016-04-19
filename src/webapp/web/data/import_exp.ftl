<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><@s.text name="experiment.import.experiment.action.title" /></title>
<#include "../template/jquery_header.ftl"/>
</head>
<body>
	<#include "../template/navsection.ftl"/>
	<div class="nav_namebar_div nav_title_gray">
		Experiment
		<img border="0" src="${base}/images/grayarrow.png">
		<a href="${base}/data/listMyExperiments.jspx"><@s.text name="experiment.list.my.all.experiments.action.title" /></a>
		<img border="0" src="${base}/images/grayarrow.png">
		<a href="${base}/data/import_exp"><@s.text name="experiment.import.experiment.action.title" /></a>
	</div>
	<div style="clear:both"></div>
 	<div class="main_container">
 		<#include "../template/action_errors.ftl">
		<div class="container_inner_left">
			<div class="blank_separator"></div>	
			<@s.form action="importExp.jspx" namespace="/data" method="post" enctype="multipart/form-data" >
			<div class="data_outer_div">
				<div class="input_field_div">Choose the BASE output file of the experiment: </div>
				<div class="blank_separator"></div>	
				<div class="input_field_div">
					<@s.file name="upload" cssClass="file_upload"/>
				</div>	
				<div class="blank_separator"></div>	
				<div class="input_field_comment">
				 	* (Please choose the experiment output file which is generated from the BASE. Only the XML format is supported)
				</div>
				<div style="clear:both"></div>  
				
			</div>
			<div class="data_outer_noborder_div">
				<@s.submit value="Import" cssClass="input_button" /> 
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