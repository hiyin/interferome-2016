<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Welcome to Interferome</title>
<#include "template/header.ftl"/>
    <script>
        $(document).ready(function () {
            $('#merc').coinslider({ hoverPause:true, width:400, height:220, opacity:0.3 });
        });
    </script>
</head>
<body>

<#include "template/navsection.ftl"/>
<!-- temp -->
<div class="trans_temp">
    Interferome has recently undergone a major redevelopment. Please select <a
        href="https://interferome-v1.erc.monash.edu.au" target="_blank">Interferome V1.0</a> to access the previous
    version.
</div>

<!-- end of temp -->
<!-- nav title -->
<div class="nav_namebar_div nav_title_gray">
    Home
</div>
<div style="clear:both"></div>
<div class="main_container">
    <div class="container_inner">
        <div class="parag_top_div">
            <div class="slidshow_frame_panel">
                <div id="merc" name="merc">
                    <a href="#">
                        <img src="${base}/slideshow/ifn_wordle.png" alt="Monash Institute of medical research"/>
                    </a>
                    <a href="#">
                        <img src="${base}/slideshow/ifnar_ternary_model.png"
                             alt="Monash Institute of medical research"/>
                    </a>
                    <a href="#">
                        <img src="${base}/slideshow/heatmap.png" alt="Monash Institute of medical research"/>
                    </a>
                    <a href="#">
                        <img src="${base}/slideshow/cy3_cy5.png" alt="Monash Institute of medical research"/>
                    </a>
                    <a href="#">
                        <img src="${base}/slideshow/typei_ii_iii.png" alt="Monash Institute of medical research"/>
                    </a>
                </div>
            </div>
            <div class="slide_right_div">
                <div class="content_title">
                    The Database of IFN Regulated Genes
                </div>
                <div class="content_div">
                    This database is an upgrade of the original database and contains type I, II and III interferon
                    (IFN)
                    regulated genes, manually curated from publicly available microarray datasets.
                </div>
                <br/>

                <div class="content_title">
                    Introduction
                </div>
                <div class="content_div">
                    IFNs were identified as antiviral proteins more than 50 years ago and since then have been shown to
                    regulate cell proliferation, survival, migration and specialised functions. Consequently they are
                    involved in numerous homeostatic and pathological processes including infections, cancer,
                    autoimmunity, inflammation and metabolic disorders. These cytokines are also used as therapeutics in
                    diseases such as chronic viral infections, cancer and multiple sclerosis. IFNs potentially regulate
                    the transcription of up to 2000 genes in an IFN subtype, dose, cell type and stimulus dependent
                    manner. This database of IFN regulated genes is an attempt at integrating information from
                    high-throughput experiments to gain a detailed understanding of the various IFN activated pathways
                    that regulate subsets of genes to enhance our understanding of pathophysiological processes.
                </div>
                <br/>

                <div class="content_title">
                    Definitions
                </div>
                <div class="content_div">
                    Interferon Regulated Genes (IRGs) were identified from experiments where cells or organisms were
                    treated with an IFN. Genes that were significantly up or down regulated relative to control samples
                    were defined as IRGs, annotated and uploaded into the database. We have set a default limit of
                    2-fold change in expression for searches because this is a commonly accepted parameter; although the
                    option remains to change this when implementing a search.
                </div>

            </div>
        </div>
        <div style="clear:both"></div>

        <div class="content_title">
            Database Scope
        </div>
        <div class="content_div">
            This database will enable the reliable identification of an individual IRG or IRG signatures from
            high-throughput data sets (i.e. microarray, proteomic data etc.). It will also assist in identifying
            regulatory elements, chromosomal location and tissue expression of IRGs in human and mouse. This
            upgraded version, Interferome v2.0 has quantitative data, more detailed annotation and search
            capabilities and can be queried for one gene or thousands as in a gene list from a microarray
            experiment.
        </div>
        <div class="empty_div"></div>
        <div class="empty_div"></div>
    </div>
</div>
<#include "template/footer.ftl"/>
<br/>
<br/>
</body>
</html>