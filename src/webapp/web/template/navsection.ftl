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
		<@s.else>
			<li><a href="${base}/pubdata/listExperiments.jspx">Experiments</a></li>
		</@s.else>
		<li><a href="${base}/search/showSearch.jspx">Search</a></li>
		<li><span class="disabled_link">Tissue Expression</span></li>
		<li><span class="disabled_link">Regulatory Analysis</span></li>
		<li><span class="disabled_link">Sequence Download</span></li>
		<li><span class="disabled_link">Database Statistics</span></li>
		<li><span class="disabled_link">References</span></li>
		<li><span class="disabled_link">How to cite</span></li>
		<li><span class="disabled_link">Cited by</span></li>
		<li><a href="#">Help</a></li>
		<li><a href="#">Contact Us</a></li>
		<div style="clear:both"></div> 
	</ul>
	<div style="clear:both"></div> 	
</div>

<div class="blank_separator"></div>	
<div style="clear:both"></div> 	