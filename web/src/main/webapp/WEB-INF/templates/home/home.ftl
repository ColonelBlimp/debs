<#import "../lib/utils.ftl" as f>
<@f.page>
<@f.contentHeader>
</@f.contentHeader>
<div>
<#if showChartBalance == true>
<#include "chartBalance.ftl">
<#else>
<#include "accountTxList.ftl">
</#if>
</div>
</@f.page>
