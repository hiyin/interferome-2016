<div class="page_style">
	<span class="total">Total ${exPagination.totalPages} Pages</span>
<#if exPagination.firstPage>
	<span class="disabled"> <img src="${base}/images/dis_first.png" class="page_style_img" /> Prev </span>
<#else>
	<a href="${base}/${pageLink}${pageSuffix}1&pageSize=${pageSize}&orderBy=${orderBy}&orderByType=${orderByType}"> <img src="${base}/images/first.png" class="page_style_img" /> First </a>
	<a href="${base}/${pageLink}${pageSuffix}${exPagination.prevPage}&pageSize=${pageSize}&orderBy=${orderBy}&orderByType=${orderByType}"> <img src="${base}/images/prev.png" class="page_style_img" /> Prev </a>
</#if>
<#if exPagination.pageNo-5 gt 1>
	<#if exPagination.totalPages gt exPagination.pageNo+4>
		<#list exPagination.pageNo-5..exPagination.pageNo+4 as i>
			<#if i == exPagination.pageNo>
				<span class="current">${i}</span>
			<#else>
				<a href="${base}/${pageLink}${pageSuffix}<#if i gt 0>${i}&pageSize=${pageSize}&orderBy=${orderBy}&orderByType=${orderByType}</#if>">${i}</a>
			</#if>
		</#list>
	<#else>
		<#list exPagination.totalPages-9..exPagination.totalPages as i>
			<#if i == exPagination.pageNo>
				<span class="current">${i}</span>
			<#else>
				<a href="${base}/${pageLink}${pageSuffix}<#if i gt 0>${i}&pageSize=${pageSize}&orderBy=${orderBy}&orderByType=${orderByType}</#if>">${i}</a>
			</#if>
		</#list>
	</#if>
<#else>
	<#if exPagination.totalPages gt 10>
		<#list 1..10 as i>
			<#if i == exPagination.pageNo>
				<span class="current">${i}</span>
			<#else>
				<a href="${base}/${pageLink}${pageSuffix}<#if i gt 0>${i}&pageSize=${pageSize}&orderBy=${orderBy}&orderByType=${orderByType}</#if>">${i}</a>
			</#if>
		</#list>
	<#else>
		<#list 1..exPagination.totalPages as i>
			<#if i == exPagination.pageNo>
				<span class="current">${i}</span>
			<#else>
				<a href="${base}/${pageLink}${pageSuffix}<#if i gt 0>${i}&pageSize=${pageSize}&orderBy=${orderBy}&orderByType=${orderByType}</#if>">${i}</a>
			</#if>
		</#list>
	</#if>
</#if>
<#if exPagination.lastPage>	
	<span class="disabled"> Next <img src="${base}/images/dis_next.png" class="page_style_img" /> </span><span class="disabled"> Last <img src="${base}/images/dis_last.png" class="page_style_img" /> </span>
<#else>
	<a href="${base}/${pageLink}${pageSuffix}${exPagination.nextPage}&pageSize=${pageSize}&orderBy=${orderBy}&orderByType=${orderByType}"> Next <img src="${base}/images/next.png" class="page_style_img" /> </a>
	<a href="${base}/${pageLink}${pageSuffix}${exPagination.totalPages}&pageSize=${pageSize}&orderBy=${orderBy}&orderByType=${orderByType}"> Last <img src="${base}/images/last.png" class="page_style_img" /> </a>
</#if>
</div>