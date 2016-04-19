<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><@s.text name="interferome.database.statistic.action.title" /></title>
<#include "../template/header.ftl"/>
</head>
<body>
	<#include "../template/navsection.ftl"/>
	<div class="nav_namebar_div nav_title_gray">
		<a href="${base}/site/dbStat.jspx"><@s.text name="interferome.database.statistic.action.title" /></a>
	</div>
	<div style="clear:both"></div>
 	<div class="main_container">
 		<#include "../template/action_errors.ftl">
		<div class="container_inner_left">
	        <div class="blank_separator"></div>

            <div class="summary">
                <div class="table_title_div">
                    Homo sapiens statistics
                </div>
                <table class="stat_table">
                    <tbody><tr>
                        <th>Data base contents</th>  <th>Interferon Type I</th>    <th>Interferon Type II</th>   <th>Interferon Type III</th>   <th>Total</th>
                    </tr>
                    <tr>
                        <td>Experiments</td> <td align="center"><@s.property value="stats.human.experimentsI" /></td>     <td align="center"><@s.property value="stats.human.experimentsII" /></td> <td align="center"><@s.property value="stats.human.experimentsIII" /></td> <td align="center"><@s.property value="stats.human.experiments" /></td>
                    </tr>
                    <tr>
                        <td>Datasets</td> <td align="center"><@s.property value="stats.human.datasetsI" /></td>     <td align="center"><@s.property value="stats.human.datasetsII" /></td> <td align="center"><@s.property value="stats.human.datasetsIII" /></td> <td align="center"><@s.property value="stats.human.datasets" /></td>
                    </tr>
                    <tr>
                        <td>Genes with p-value &lt;0.05 and fold change &gt;=2</td> <td align="center"><@s.property value="stats.human.fcI" /></td>     <td align="center"><@s.property value="stats.human.fcII" /></td> <td align="center"><@s.property value="stats.human.fcIII" /></td> <td align="center"><@s.property value="stats.human.fc" /></td>
                    </tr>
                    <!-- <tr>
                        <td>Genes with p-value &lt;0.05</td> <td align="center"><@s.property value="stats.human.allDataI" /></td>     <td align="center"><@s.property value="stats.human.allDataII" /></td> <td align="center"><@s.property value="stats.human.allDataIII" /></td> <td align="center"><@s.property value="stats.human.allData" /></td>
                    </tr> -->
                    </tbody>
                </table>
                <div class="blank_separator"></div>
                <div class="table_title_div">
                    Mus musculus statistics
                </div>
                <table class="stat_table">
                    <tbody><tr>
                        <th>Data base contents</th>  <th>Interferon Type I</th>    <th>Interferon Type II</th>   <th>Interferon Type III</th>   <th>Total</th>
                    </tr>
                    <tr>
                        <td>Experiments</td>   <td align="center"><@s.property value="stats.mouse.experimentsI" /></td>     <td align="center"><@s.property value="stats.mouse.experimentsII" /></td> <td align="center"><@s.property value="stats.mouse.experimentsIII" /></td> <td align="center"><@s.property value="stats.mouse.experiments" /></td>
                    </tr>
                    <tr>
                        <td>Datasets</td>      <td align="center"><@s.property value="stats.mouse.datasetsI" /></td>        <td align="center"><@s.property value="stats.mouse.datasetsII" /></td>      <td align="center"><@s.property value="stats.mouse.datasetsIII" /></td> <td align="center"><@s.property value="stats.mouse.datasets" /></td>
                    </tr>
                    <tr>
                        <td>Genes with p-value &lt;0.05 and fold change &gt;=2</td> <td align="center"><@s.property value="stats.mouse.fcI" /></td>          <td align="center"><@s.property value="stats.mouse.fcII" /></td>         <td align="center"><@s.property value="stats.mouse.fcIII" /></td> <td align="center"><@s.property value="stats.mouse.fc" /></td>
                    </tr>
                    <!--<tr>
                        <td>Genes with p-value &lt;0.05</td>  <td align="center"><@s.property value="stats.mouse.allDataI" /></td>        <td align="center"><@s.property value="stats.mouse.allDataII" /></td>  <td align="center"><@s.property value="stats.mouse.allDataIII" /></td> <td align="center"><@s.property value="stats.mouse.allData" /></td>
                    </tr> -->
                </tbody></table>
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


