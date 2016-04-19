<!-- action nav bar -->
<div class="nav_namebar_div nav_title_gray">
<@s.if test = "%{navBar != null}">
	<@s.if test = "%{navBar.firstNavName != null}">
		<@s.if test="%{navBar.firstNavLink != null}">
			<a href="${base}/${navBar.firstNavLink}">${navBar.firstNavName}</a>
		</@s.if>
		<@s.else>
			${navBar.firstNavName}
		</@s.else>
	</@s.if>
	
	<@s.if test = "%{navBar.secondNavName != null}">
		<img border="0" src="${base}/images/grayarrow.png">
		<@s.if test = "%{navBar.secondNavLink != null}">
			<a href="${base}/${navBar.secondNavLink}">${navBar.secondNavName}</a>
		</@s.if>
		<@s.else>
			${navBar.secondNavName}
		</@s.else>
	</@s.if>
	
	<@s.if test = "%{navBar.thirdNavName != null}">
		<img border="0" src="${base}/images/grayarrow.png">
		<@s.if test = "%{navBar.thirdNavLink != null}">
			<a href="${base}/${navBar.thirdNavLink}">${navBar.thirdNavName}</a>
		</@s.if>
		<@s.else>
			${navBar.thirdNavName}
		</@s.else>
	</@s.if>
</@s.if>
</div>
<div style="clear:both"></div>