<br/>
<script type="text/javascript">       ;
$(document).ready(function(){$(".search_menu_link a").each(function(index){
    var curPage = document.URL;
    curPage = curPage.substr(curPage.lastIndexOf("/")) ;
    var linkPage = $(this).attr("href");
    linkPage = linkPage.substr(linkPage.lastIndexOf("/"));
    if (curPage == linkPage){
        $(this).addClass("current_link") ;//$(this).css("background", "#90BADE") ;
    }
})});
</script>
<div class="search_sub_menue">
    <div class="search_menu_link">
        <a class="expand_collapse" href="">Search Conditions</a>
    </div>
    <div class="search_menu_link">
        <a href="${base}/search/searchGene.jspx">Gene Summary</a>
    </div>
    <div class="search_menu_link">
        <a href="${base}/search/searchData.jspx">Experiment Data</a>
    </div>
    <div class="search_menu_link">
        <a href="${base}/search/searchOntology.jspx">Ontology Analysis</a>
    </div>
    <div class="search_menu_link">
        <a href="${base}/search/searchTFSite.jspx">TF Analysis</a>
    </div>
    <div class="search_menu_link">
        <a href="${base}/search/searchChromosome.jspx">Chromosome</a>
    </div>
    <div class="search_menu_link">
        <a href="${base}/search/searchSubtype.jspx">IFN Type</a>
    </div>
    <div class="search_menu_link">
        <a href="${base}/search/searchTissueExpression.jspx">Basal Expression</a>
    </div>
    <div style="clear:both"></div>
</div>
<br/>