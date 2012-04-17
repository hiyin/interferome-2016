<div class="page_style">
	<span class="total">Total ${dataPagination.totalPages} Pages</span>
<#if dataPagination.firstPage>
	<span class="disabled"> <img src="${base}/images/dis_first.png" class="page_style_img" /> Prev </span>
<#else>
	<a href="${base}/${pageLink}${pageSuffix}1&pageSize=<@s.property value='pageSize' />&orderBy=${orderBy}&orderByType=${orderByType}"> <img src="${base}/images/first.png" class="page_style_img" /> First </a>
	<a href="${base}/${pageLink}${pageSuffix}<@s.property value='dataPagination.prevPage' />&pageSize=<@s.property value='pageSize' />&orderBy=${orderBy}&orderByType=${orderByType}"> <img src="${base}/images/prev.png" class="page_style_img" /> Prev </a>
</#if>
<#if dataPagination.pageNo-5 gt 1>
	<#if dataPagination.totalPages gt dataPagination.pageNo+4>
		<#list dataPagination.pageNo-5..dataPagination.pageNo+4 as i>
			<#if i == dataPagination.pageNo>
				<span class="current">${i?c}</span>
			<#else>
				<a href="${base}/${pageLink}${pageSuffix}<#if i gt 0>${i?c}&pageSize=<@s.property value='pageSize' />&orderBy=${orderBy}&orderByType=${orderByType}</#if>">${i?c}</a>
			</#if>
		</#list>
	<#else>
		<#list dataPagination.totalPages-9..dataPagination.totalPages as i>
			<#if i == dataPagination.pageNo>
				<span class="current">${i?c}</span>
			<#else>
				<a href="${base}/${pageLink}${pageSuffix}<#if i gt 0>${i?c}&pageSize=<@s.property value='pageSize' />&orderBy=${orderBy}&orderByType=${orderByType}</#if>">${i?c}</a>
			</#if>
		</#list>
	</#if>
<#else>
	<#if dataPagination.totalPages gt 10>
		<#list 1..10 as i>
			<#if i == dataPagination.pageNo>
				<span class="current">${i?c}</span>
			<#else>
				<a href="${base}/${pageLink}${pageSuffix}<#if i gt 0>${i?c}&pageSize=<@s.property value='pageSize' />&orderBy=${orderBy}&orderByType=${orderByType}</#if>">${i?c}</a>
			</#if>
		</#list>
	<#else>
		<#list 1..dataPagination.totalPages as i>
			<#if i == dataPagination.pageNo>
				<span class="current">${i?c}</span>
			<#else>
				<a href="${base}/${pageLink}${pageSuffix}<#if i gt 0>${i?c}&pageSize=<@s.property value='pageSize' />&orderBy=${orderBy}&orderByType=${orderByType}</#if>">${i?c}</a>
			</#if>
		</#list>
	</#if>
</#if>
<#if dataPagination.lastPage>
	<span class="disabled"> Next <img src="${base}/images/dis_next.png" class="page_style_img" /> </span><span class="disabled"> Last <img src="${base}/images/dis_last.png" class="page_style_img" /> </span>
<#else>
	<a href="${base}/${pageLink}${pageSuffix}<@s.property value='dataPagination.nextPage' />&pageSize=<@s.property value='pageSize' />&orderBy=${orderBy}&orderByType=${orderByType}"> Next <img src="${base}/images/next.png" class="page_style_img" /> </a>
	<a href="${base}/${pageLink}${pageSuffix}<@s.property value='dataPagination.totalPages' />&pageSize=<@s.property value='pageSize' />&orderBy=${orderBy}&orderByType=${orderByType}"> Last <img src="${base}/images/last.png" class="page_style_img" /> </a>
</#if>
</div>