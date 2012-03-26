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
		<div class="container_inner_left">
			<div class="blank_separator"></div>	
			<@s.if test="%{exPagination.pageResults.size() > 0}">
			<div class="data_header_div">
				<@s.if test="%{successActMsg != null}">
			 		<#include "../template/success_act_message.ftl"/>
			 	</@s.if>
				<span class="name_title">A total of <font color="green"> ${exPagination.totalRecords} </font> experiments</span>
				<div class="blank_separator"></div>
				<div class="url_contect">
					<a href="${base}/${pageLink}${pageSuffix}<@s.property value='exPaginationt.pageNo' />" class="page_url"></a>
				</div>
				<#include "../pagination/pagination_header.ftl"/>
			</div>
			</@s.if>
			<@s.else>
			<div class="data_header_div">
				<span class="name_title">A total of <font color="green"> ${exPagination.totalRecords} </font> experiments</span>
			</div>
			<div class="empty_div"></div>
			<div class="empty_div"></div>
			<div class="empty_div"></div>
			<div class="empty_div"></div>
			<div class="empty_div"></div>
			<div class="empty_div"></div>
			<br/>
			<div style="clear:both"></div>  
			</@s.else>
			<@s.if test="%{exPagination.pageResults.size() > 0}">
			<@s.iterator status="expStat" value="exPagination.pageResults" id="expResult" >
			<div class="data_outer_div">
				<div class="each_field_row">
					<div class="exp_name_title"><a href="${base}/pubdata/viewExperiment.jspx?experiment.id=<@s.property value='#expResult.id' />">${namePrefix}<@s.property value="#expResult.id" /></a></div>
				</div>
				 
				<div class="each_field_row">
					<div class="exp_desc_div"><@s.property value="#expResult.description" /></div>
				</div>
				<div class="each_field_row">
					<div class="exp_audit_info">
						Imported by <@s.property value="#expResult.owner.displayName" />,&nbsp;&nbsp;
						Imported Date: <@s.date name="#expResult.createdTime"  format="dd-MM-yyyy 'at' hh:mm a" />,&nbsp;&nbsp;
						Modified by <@s.property value="#expResult.modifiedByUser.displayName" />,&nbsp;&nbsp; 
						Modified date: <@s.date name="#expResult.modifiedTime" format="dd-MM-yyyy 'at' hh:mm a"  />
					</div>
				</div>
			 	<div class="data_link">
			 		<a href="${base}/pubdata/viewExperiment.jspx?experiment.id=<@s.property value='#expResult.id' />">View Details</a>
			 	</div>
			 	<div style="clear:both"></div>  
			</div>
			</@s.iterator>
			<div class="blank_separator"></div>	
			<#include "../pagination/exp_page_style.ftl" />
			<div class="blank_separator"></div>	
		    </@s.if>
		</div>
         <!--
		<div class="container_inner_right"> -->
			<!-- #include "../template/user_nav.ftl" >
		</div>
		-->
		<div style="clear:both"></div>  
	</div>
	<#include "../template/footer.ftl"/>
</body>
</html>