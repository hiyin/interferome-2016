<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><@s.text name="tissue.import.action.title" /></title>
<#include "../template/header.ftl"/>
</head>
<body>
<#include "../template/navsection.ftl"/>
<div class="nav_namebar_div nav_title_gray">
    Tissues
    <img border="0" src="${base}/images/grayarrow.png">
    <a href="${base}/data/showImportTissue.jspx"><@s.text name="tissue.import.action.title" /></a>
</div>
<div style="clear:both"></div>
<div class="main_container">
    <div class="container_inner_left">
        <div class="empty_div"></div>
    <#include "../template/success_info_pane.ftl"/>
        <div class="empty_div"></div>
        <div class="empty_div"></div>
        <div class="empty_div"></div>
        <div class="empty_div"></div>
        <br/>
    </div>
    <div class="container_inner_right">
    <#include "../template/user_nav.ftl">
    </div>
    <div style="clear:both"></div>
</div>
<#include "../template/footer.ftl"/>
</body>
</html>