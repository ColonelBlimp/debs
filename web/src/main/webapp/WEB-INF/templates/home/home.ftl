<#import "../lib/utils.ftl" as f>
<#macro chartRow>
<td>
<span class="block w-full h-full <@s.if test="data.type.getId()==${GROUP_TYPE}">folder-bg</@s.if><@s.else>account-bg</@s.else> truncate level-<@s.property value="level"/>">
<@s.property value="data.name"/>
</span>
</td>
</#macro>
<#macro dataHref>
data-href="<@s.url action="home" namespace="/"><@s.param name="id" value="data.id"/></@s.url>" title=<@s.if test="data.type.getId()==8">"View Chart Balances"</@s.if><@s.else>"View Account's Transactions"</@s.else> onclick="homeView(this);"
</#macro>
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
