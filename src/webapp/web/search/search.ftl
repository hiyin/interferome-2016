<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><@s.text name="experiment.search.action.title" /></title>
<#include "../template/jquery_header.ftl"/>
<script type="text/javascript">
	$(document).ready(function(){
		$("#tooltip_link").easyTooltip({
			tooltipId: "search_tips",
			content: '<h4>Treatment Concentration International Unit</h4><p>Please use the following formula to convert the international unit to interferome unit.</p>'
		});
	});
</script>

</head>
<body>
<#include "../template/navsection.ftl"/>
<div class="nav_namebar_div nav_title_gray">
    <a href="${base}/search/showSearch.jspx"><@s.text name="experiment.search.action.title" /></a>
</div>
<div style="clear:both"></div>
<div class="main_container">
<#include "../template/action_errors.ftl">
    <div class="container_inner_left">
        <div class="search_hints_outer_div">
            Please select the following search condition(s):
        </div>
        <@s.form action="search.jspx" namespace="/search" method="post">
        <#include "../search/search_con.ftl">
        <div class="data_header_div">
            <@s.submit value=" Search " cssClass="input_button" />  &nbsp; <@s.reset value="Clear" cssClass="input_button" />
        </div>
        </@s.form>
        <#include "../search/search_result.ftl" />
    </div>

    <@s.if test="%{#session.authentication_flag =='authenticated'}">
    <div class="container_inner_right">
        <#include "../template/user_nav.ftl">
    </div>
    </@s.if>
    <div style="clear:both"></div>
</div>
<#include "../template/footer.ftl"/>
</body>
</html>