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
		<a href="${base}/site/dbStat.jspx"><@s.text name="interferome.database.statistic.action.title" /></a>
	</div>
	<div style="clear:both"></div>
 	<div class="main_container">
 		<#include "../template/action_errors.ftl">
		<div class="container_inner_left">
	        <div class="blank_separator"></div>

			<div class="data_outer_div">
                <div class="parag_title_div">
                    How data was processed
                </div>
                <div class="db_stat_div">
                    <p>
                        One of the aims of Interferome DB is to provide the storage and analysis of gene expression data obtained from microarray experiments of interferon treated cells or animals.
                    </p>

                    <p>
                        Microarray datasets were manually selected from EBI Array express, GEO open sources DBs and in-house experiments. Selection criteria are variety cells or tissues treated with any type of interferon (IFNs), with or without other treatments. The cells/tissues/enimals could be normal or abnormal (for example, originating from a diseased organism or one with a genetic modification).
                    </p>
                    <p>
                        Data was collected from various microarray platforms and array designs. In relation to each platform normalization and filtering algorithms was applied, such as RMA, GCRMA, MAS5, Percentile.
                    </p>
                    <p>
                        The microarray server runs BioArray Software Environment (BASE 2) for the storage, management and analysis of microarray experiments. BASE2 use plug-ins for data analysis which provides also data normalization, transformation and filtering. Statistical analysis of technical and biological replicates of experimental samples includes Welch t-test and Fold change calculations. Analysis is performed using the BASE2 platform and the Bioconductor packages.
                    </p>
                    <p>
                        The outcome of the analyses from BASE2 is presented in Interferome ‘search’ page as a list of statistically significant genes (p-value < 0.05) with fold change expression between treated and untreated samples.
                    </p>
                    <p>
                        The data is also annotated for various parameters including type of IFN, concentration, time of treatment, cell type and normal/abnormal status and others.
                    </p>
                </div>
                <br/>
                <br/>
                <div class="parag_title_div">
                    References
                </div>
                <div class="db_stat_ref_row">
                    <div class="citation_img"><img src="${base}/images/dot_grey.png" align="top" border="0" /></div>
                    <div class="db_stat_ref_title">
                        Vallon-Christersson J, Nordborg N, Svensson M, Hakkinen J. (2009). BASE--2nd generation software for microarray data management and analysis. BMC Bioinformatics 10:330.
                    </div>
                    <div style="clear:both"></div>
			    </div>
                <div class="db_stat_ref_row">
                    <div class="citation_img"><img src="${base}/images/dot_grey.png" align="top" border="0" /></div>
                    <div class="db_stat_ref_title">
                        Pan, W. (2002). A comparative review of statistical methods for discovering differentially expressed genes in replicated microarray experiments. Bioinformatics 18: 546-554.
                    </div>
                    <div style="clear:both"></div>
			    </div>
                <div class="db_stat_ref_row">
                    <div class="citation_img"><img src="${base}/images/dot_grey.png" align="top" border="0" /></div>
                    <div class="db_stat_ref_title">
                        Dudoit, S., Y.H. Yang, M.J. Callow, and T. Speed (2000).Statistical methods for identifying differentially expressed genes in replicated cDNA microarray experiments. Technical report 2000 Statistics Department, University of California, Berkeley.
                    </div>
                    <div style="clear:both"></div>
			    </div>
                <br/>
                <br/>
                <br/>
                <br/>
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


