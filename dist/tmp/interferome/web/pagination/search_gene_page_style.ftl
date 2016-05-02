<div class="page_style">
    <span class="total">Total ${genePagination.totalPages} Pages</span>
<#if genePagination.firstPage>
    <span class="disabled"> <img src="${base}/images/dis_first.png" class="page_style_img" /> Prev </span>
<#else>
    <a href="${base}/${pageLink}${pageSuffix}1&pageSize=<@s.property value='pageSize' />&orderBy=${orderBy}&orderByType=${orderByType}"> <img src="${base}/images/first.png" class="page_style_img" /> First </a>
    <a href="${base}/${pageLink}${pageSuffix}<@s.property value='genePagination.prevPage' />&pageSize=<@s.property value='pageSize' />&orderBy=${orderBy}&orderByType=${orderByType}"> <img src="${base}/images/prev.png" class="page_style_img" /> Prev </a>
</#if>
<#if genePagination.pageNo-5 gt 1>
    <#if genePagination.totalPages gt genePagination.pageNo+4>
        <#list genePagination.pageNo-5..genePagination.pageNo+4 as i>
            <#if i == genePagination.pageNo>
                <span class="current">${i?c}</span>
            <#else>
                <a href="${base}/${pageLink}${pageSuffix}<#if i gt 0>${i?c}&pageSize=<@s.property value='pageSize' />&orderBy=${orderBy}&orderByType=${orderByType}</#if>">${i?c}</a>
            </#if>
        </#list>
    <#else>
        <#list genePagination.totalPages-9..genePagination.totalPages as i>
            <#if i == genePagination.pageNo>
                <span class="current">${i?c}</span>
            <#else>
                <a href="${base}/${pageLink}${pageSuffix}<#if i gt 0>${i?c}&pageSize=<@s.property value='pageSize' />&orderBy=${orderBy}&orderByType=${orderByType}</#if>">${i?c}</a>
            </#if>
        </#list>
    </#if>
<#else>
    <#if genePagination.totalPages gt 10>
        <#list 1..10 as i>
            <#if i == genePagination.pageNo>
                <span class="current">${i?c}</span>
            <#else>
                <a href="${base}/${pageLink}${pageSuffix}<#if i gt 0>${i?c}&pageSize=<@s.property value='pageSize' />&orderBy=${orderBy}&orderByType=${orderByType}</#if>">${i?c}</a>
            </#if>
        </#list>
    <#else>
        <#list 1..genePagination.totalPages as i>
            <#if i == genePagination.pageNo>
                <span class="current">${i?c}</span>
            <#else>
                <a href="${base}/${pageLink}${pageSuffix}<#if i gt 0>${i?c}&pageSize=<@s.property value='pageSize' />&orderBy=${orderBy}&orderByType=${orderByType}</#if>">${i?c}</a>
            </#if>
        </#list>
    </#if>
</#if>
<#if genePagination.lastPage>
    <span class="disabled"> Next <img src="${base}/images/dis_next.png" class="page_style_img" /> </span><span class="disabled"> Last <img src="${base}/images/dis_last.png" class="page_style_img" /> </span>
<#else>
    <a href="${base}/${pageLink}${pageSuffix}<@s.property value='genePagination.nextPage' />&pageSize=<@s.property value='pageSize' />&orderBy=${orderBy}&orderByType=${orderByType}"> Next <img src="${base}/images/next.png" class="page_style_img" /> </a>
    <a href="${base}/${pageLink}${pageSuffix}<@s.property value='genePagination.totalPages' />&pageSize=<@s.property value='pageSize' />&orderBy=${orderBy}&orderByType=${orderByType}"> Last <img src="${base}/images/last.png" class="page_style_img" /> </a>
</#if>
</div>