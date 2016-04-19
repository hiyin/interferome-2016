<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><@s.text name="interferome.citation.action.title" /></title>
<#include "../template/header.ftl"/>
</head>
<body>
	<#include "../template/navsection.ftl"/>
	<div class="nav_namebar_div nav_title_gray">
		<a href="${base}/site/showCitation.jspx"><@s.text name="interferome.citation.action.title" /></a>
	</div>
	<div style="clear:both"></div>
 	<div class="main_container">
 		<#include "../template/action_errors.ftl">
		<div class="container_inner_left">
	        <div class="blank_separator"></div>
            <div class="data_outer_div">
                <p>If you use the Interferome please cite:</p>
                <div class="data_each_row">
                    <div class="citation_img"><img src="${base}/images/dot_grey.png" align="top" border="0" /></div>
                    <div class="citation_title">
                        <a href="http://nar.oxfordjournals.org/content/41/D1/D1040.full" target="_blank">
                            Rusinova, I.; Forster, S.; Yu, S.; Kannan, A.; Masse, M.; Cumming, H.; Chapman, R.; Hertzog, P.J.<br /> INTERFEROME v2. 0: an updated database of annotated interferon-regulated genes.  <br />Nucleic Acids Research. 2013 January;  41 (database issue):   D1040-D1046.
                        </a>
                    </div>
                    <div style="clear:both"></div>
                </div>
            </div>
            <div class="blank_separator"></div>
            <div class="data_outer_div">
                <p>To assist you in using the Interferome database a number of example citations are included below:</p>
                <div class="data_each_row">
                    <div class="citation_img"><img src="${base}/images/dot_grey.png" align="top" border="0" /></div>
                    <div class="citation_title">
                        <a href="http://www.ncbi.nlm.nih.gov/pubmed/21226606" target="_blank">
                            PJ Hertzog, S Forster, SA Samarajiwa; Systems biology of interferon responses Journal of Interferon and Cytokine Research 2011 Jan 31(1): 5-11
                        </a>
                    </div>
                    <div style="clear:both"></div>
                </div>
            </div>
            <div class="blank_separator"></div>
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


