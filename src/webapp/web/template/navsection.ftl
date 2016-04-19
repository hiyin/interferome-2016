<div class="logo_section">
    <div class="logo_section_left">
       <@s.text name="app.version" />
    </div>
</div>
<div style="clear:both"></div> 
<div class="navcontainer">
    <script type="text/javascript">
        $(document).ready(function(){$(".navlist li a").each(function(index){
            var curPage = document.URL;
            curPage = curPage.substr(curPage.lastIndexOf("/")) ;
            var linkPage = $(this).attr("href");
            linkPage = linkPage.substr(linkPage.lastIndexOf("/"));
            if (curPage == linkPage){
                $(this).addClass("current_link") ;
            }
        })});
    </script>
	<ul class="navlist">
		<li><a href="${base}/home.jspx">Home</a></li>
		<@s.if test="%{#session.authentication_flag =='authenticated'}">
			<li><a href="${base}/manage/userHome.jspx">My Home</a></li>
			<li><a href="${base}/data/listExperiments.jspx">Experiments</a></li>
		</@s.if>
		<li><a href="${base}/search/showSearch.jspx">Search</a></li>
        <li><a href="${base}/site/dbStat.jspx">Database Statistics</a></li>
        <li><a href="${base}/site/showSubmitData.jspx">Submit Data</a></li>
        <li><a href="${base}/site/showHelp.jspx">Help</a></li>
        <li><a href="${base}/site/showCitation.jspx">How To Cite</a></li>
        <li><a href="${base}/site/showContactUs.jspx">Contact Us</a></li>
		<div style="clear:both"></div>
	</ul>
	<div style="clear:both"></div> 	
</div>

<div class="blank_separator"></div>	
<div style="clear:both"></div> 	