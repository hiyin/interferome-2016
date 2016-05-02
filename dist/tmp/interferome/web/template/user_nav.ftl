<@s.if test="%{#session.authentication_flag =='authenticated'}">
<div class="blank_separator"></div>
<div class="blank_separator"></div>
<div class="user_avatar">
	<img  src='${base}/user/viewAvatar.jspx?avatarUserId=<@s.property value="#session.authen_user_id" />' />
</div>
<span class="line_span">
	<@s.if test="%{user.userType == 1}">
		Super Admin &nbsp;
	</@s.if>
	<@s.elseif test="%{user.userType == 2}">
		Admin &nbsp;
	</@s.elseif>
	<@s.property value="#session.authen_user_name" />
	&nbsp;&nbsp&nbsp;<a href="${base}/manage/showAvatarUpload.jspx"><img src="${base}/images/edit.png" border="0"/></a>
</span>
<div style="clear:both"></div>
<div class="user_nav">
	<ul>
		<li><a href="${base}/manage/userHome.jspx">My Home</a></li>
		<li><a href="${base}/data/listMyExperiments.jspx">My Experiments</a></li>
		<li><a href="${base}/data/showImportExp.jspx">Import Experiments</a></li>
		<li><a href="${base}/data/listExperiments.jspx">Experiments</a></li>
        <@s.if test="%{user.userType == 1 || user.userType ==2}">
            <li><a href="${base}/data/showImportProbes.jspx">Import Probes</a></li>
            <li><a href="${base}/data/showImportTFSite.jspx">Import Transcription Site</a></li>
            <li><a href="${base}/data/showImportTissue.jspx">Import Tissues</a></li>
        </@s.if>
		<li><a href="${base}/manage/getUserEvents.jspx">Events</a></li>
		<li><a href="${base}/admin/listAllUsers.jspx">Users</a></li>
		<li><a href="${base}/user/userLogout.jspx">Logout</a></li> 
	</ul>
	<div class="empty_div"></div>
	<br/>
</div>
</@s.if>
<@s.else>
	<div class="empty_fix_div">
		&nbsp;
	</div>
</@s.else>