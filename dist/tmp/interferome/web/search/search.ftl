<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><@s.text name="experiment.search.action.title" /></title>
<#include "../template/jquery_header.ftl"/>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#tooltip_link").easyTooltip({
                tooltipId:"search_tips",
                clickRemove:true,
                content:'<h4>Treatment Concentration International Unit</h4><p>Please use the following formula to convert an interferome unit to an international unit.</p>'
            });
        });

        $(document).ready(function () {
            $(".export_pic").easyTooltip({
                tooltipId:"expcsv_tips",
                content:'<h4>Save The Search Data Results To An SVG File</h4>'
            });
        });

        $(document).ready(function () {
            $("#export_csv").easyTooltip({
                tooltipId:"expcsv_tips",
                content:'<h4>Save The Search Results To A TXT File</h4><p>(Maximum records are up to ${maxRecords})</p>'
            });
        });
    </script>

</head>
<body>
<#include "../template/navsection.ftl"/>
<div class="nav_namebar_div nav_title_gray">
<@s.if test="%{searched == true}">
    <@s.if test="%{searchType == 'gene'}">
        <a href="${base}/search/searchGene.jspx"><@s.text name="experiment.search.action.title" /> - Gene Summary</a>
    </@s.if>
    <@s.if test="%{searchType == 'data'}">
        <a href="${base}/search/searchData.jspx"><@s.text name="experiment.search.action.title" /> - Experiment Data</a>
    </@s.if>
    <@s.if test="%{searchType == 'transcript'}">
        <a href="${base}/search/searchTFSite.jspx"><@s.text name="experiment.search.action.title" /> - TF Anaylsis</a>
    </@s.if>
    <@s.if test="%{searchType == 'geneontology'}">
        <a href="${base}/search/searchOntology.jspx"><@s.text name="experiment.search.action.title" /> - Ontology Analysis</a>
    </@s.if>
    <@s.if test="%{searchType == 'chromosome'}">
        <a href="${base}/search/searchChromosome.jspx"><@s.text name="experiment.search.action.title" /> - Chromosome</a>
    </@s.if>
    <@s.if test="%{searchType == 'subtype'}">
        <a href="${base}/search/searchSubtype.jspx"><@s.text name="experiment.search.action.title" /> - IFN Type</a>
    </@s.if>
    <@s.if test="%{searchType == 'tissueexp'}">
        <a href="${base}/search/searchTissueExpression.jspx"><@s.text name="experiment.search.action.title" /> - Basal Expression</a>
    </@s.if>
</@s.if>
<@s.else>
    <a href="${base}/search/showSearch.jspx"><@s.text name="experiment.search.action.title" /></a>
</@s.else>
</div>
<div style="clear:both"></div>
<div class="main_container">
<#include "../template/action_errors.ftl">

    <div class="container_inner_left">
        <!-- search conditions -->
        <!-- expand and collapse section, to see the search conditions are collapsed or opened -->
        <!-- if it is seached -->
    <@s.if test="%{searched == true}">
        <!-- and the search type is not a gene search, it's always collapsed  -->
        <@s.if test="%{searchType != 'gene'}">
        <div class="hide_search_cond" id="search_close" style="display: none;">
        </@s.if>
        <@s.else>
            <!-- it's a gene search, and result is greater than zero, then we collapse the search conditions-->
            <@s.if test="%{genePagination != null && genePagination.totalRecords >0 }">
            <div class="hide_search_cond" id="search_close" style="display: none;">
            </@s.if>
            <@s.else>  <!-- otherwise we keep the search condition open -->
                <div class="hide_search_cond" id="search_open"">
            </@s.else>
        </@s.else>
    </@s.if>
    <@s.else>
        <!-- if it's not searched, we keep the search conditions open -->
    <div class="hide_search_cond" id="search_open">
    </@s.else>
        <!-- end of expand and collapse section -->

        <div class="search_hints_outer_div">
            Please select the following search condition(s):
        </div>
    <@s.form action="searchGene.jspx" namespace="/search" method="post">
        <#include "../search/search_con.ftl">
        <div class="data_header_div">
            <@s.submit value=" Search " cssClass="input_button" />
            &nbsp; <@s.reset value="Clear" cssClass="input_button" />
        </div>
    </@s.form>
    </div>
        <!-- end of search condition section -->

        <!-- search sub menu -->
    <@s.if test="%{searched == true}">
        <@s.if test="%{searchType != 'gene'}">
            <div class="search_menu_div">
                <#include "../search/search_sub_menu.ftl"/>
            </div>
        </@s.if>
        <@s.else>
            <@s.if test="%{genePagination != null && genePagination.totalRecords >0 }">
                <div class="search_menu_div">
                    <#include "../search/search_sub_menu.ftl"/>
                </div>
            </@s.if>
        </@s.else>
    </@s.if>
        <!-- end of search sub menu -->

        <!-- search results -->
    <@s.if test="%{searched == true}">
        <@s.if test="%{searchType == 'gene'}">
            <@s.if test="%{genePagination != null}">
                <div class="search_results_div" id='gene'>
                    <#include "../search/search_gene_result.ftl" />
                </div>
            </@s.if>
        </@s.if>
        <@s.if test="%{searchType == 'data'}">
            <@s.if test="%{dataPagination != null}">
                <div class="search_results_div" id='data'>
                    <#include "../search/search_data_result.ftl" />
                </div>
            </@s.if>
        </@s.if>
        <@s.if test="%{searchType == 'transcript'}">
            <@s.if test="%{tfSiteList != null}">
                <div class="search_results_div" id='data'>
                    <#include "../search/search_tfsite_result.ftl" />
                </div>
            </@s.if>
        </@s.if>
        <@s.if test="%{searchType == 'geneontology'}">
            <@s.if test="%{ontologyList != null}">
                <div class="search_results_div" id='data'>
                    <#include "../search/search_ontology_result.ftl" />
                </div>
            </@s.if>
        </@s.if>
        <@s.if test="%{searchType == 'chromosome'}">
            <@s.if test="%{chromosomeList != null}">
                <div class="search_results_div" id='data'>
                    <#include "../search/search_chromosome_result.ftl" />
                </div>
            </@s.if>
        </@s.if>
        <@s.if test="%{searchType == 'subtype'}">
            <@s.if test="%{subtypeList != null}">
                <div class="search_results_div" id='data'>
                    <#include "../search/search_subtype_result.ftl" />
                </div>
            </@s.if>
        </@s.if>
        <@s.if test="%{searchType == 'tissueexp'}">
                <div class="search_results_div" id='data'>
                    <#include "../search/search_tissue.ftl" />
                </div>
        </@s.if>
    </@s.if>

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