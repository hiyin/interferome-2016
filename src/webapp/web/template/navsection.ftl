<div class="logo_section">
	<div class="logo_section_right">
	 <@s.if test="%{#session.authentication_flag == null}">
		 <a href="${base}/user/showLogin.jspx">Login</a> <a href="${base}/user/register_options">Register</a> 
	</@s.if>
	</div>
</div>
<div style="clear:both"></div> 
<div class="navcontainer">
	<ul class="navlist">
		<li><a href="${base}/home.jspx">Home</a></li>
		<@s.if test="%{#session.authentication_flag =='authenticated'}">
			<li><a href="${base}/manage/userHome.jspx">My Home</a></li>
			<li><a href="${base}/data/listExperiments.jspx">Experiments</a></li>
		</@s.if>
		<li><a href="${base}/search/showSearch.jspx">Search</a></li>
		<li><a href="http://vera093.its.monash.edu.au/interferome/expression.php" target="_blank">Tissue Expression</a></li>
		<li><a href="http://vera093.its.monash.edu.au/interferome/tfbs.php" target="_blank">Regulatory Analysis</a></li>
		<li><span class="disabled_link">Sequence Download</span></li>
		<li><span class="disabled_link">Database Statistics</span></li>
		<li><a href="${base}/site/showCitation.jspx">Citation</a></li>
		<li><a href="${base}/site/showHelp.jspx">Help</a></li>
		<li><a href="${base}/site/showContactUs.jspx">Contact Us</a></li>
        <li><a href="http://vera093.its.monash.edu.au/interferome" target="_blank">Interferome V1.0</span></a></li>
		<div style="clear:both"></div> 
	</ul>
	<div style="clear:both"></div> 	
</div>

<div class="blank_separator"></div>	
<div style="clear:both"></div> 	