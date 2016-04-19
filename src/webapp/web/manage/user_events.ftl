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
			<@s.if test="%{eventPagination.pageResults.size() > 0}">
			<div class="data_header_div">
				<@s.if test="%{successActMsg != null}">
			 		<#include "../template/success_act_message.ftl"/>
			 	</@s.if>
				<span class="name_title">A total of <font color="green"> ${eventPagination.totalRecords} </font> events</span>
				<div class="blank_separator"></div>
				<div class="url_contect">
					<a href="${base}/${pageLink}${pageSuffix}<@s.property value='eventPagination.pageNo' />" class="page_url"></a>
				</div>
				<#include "../pagination/pagination_header.ftl"/>
			</div>
			</@s.if>
			<@s.else>
			<div class="data_header_div">
				<span class="name_title">A total of <font color="green"> ${eventPagination.totalRecords} </font> events</span>
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
			<@s.if test="%{eventPagination.pageResults.size() > 0}">
			<div class="data_outer_div">
				<@s.iterator status="eventStat" value="eventPagination.pageResults" id="eventResult" >
					<div class="data_each_row">
						<div class="data_img1"><img src="${base}/images/dot_grey.png" align="top" border="0" /></div>
					 	<div class="data_inline">
					 		<font color="#0E774A"><@s.date name="#eventResult.createdTime"  format="dd-MM-yyyy 'at' hh:mm a" /></font> &nbsp;&nbsp;
					 		<@s.property value="#eventResult.event" />, by <@s.property value="#eventResult.operator.displayName" />
					 	</div>
					 	<div class="data_link_small">
					 		<a href="${base}/manage/deleteUserEvent.jspx?auditEvent.id=<@s.property value='#eventResult.id' />" id="delete_event">Delete</a>
					 	</div>
					 	<div style="clear:both"></div>  
					</div>
				</@s.iterator>
			</div>
			<div class="blank_separator"></div>	
			<#include "../pagination/events_page_style.ftl" />
			<div class="blank_separator"></div>	
		    </@s.if>
		</div>
		<div class="container_inner_right">
			<#include "../template/user_nav.ftl">
		</div>
		<div style="clear:both"></div>  
	</div>
	<#include "../template/footer.ftl"/>
</body>
</html>