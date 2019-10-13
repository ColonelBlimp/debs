<#--
 * org.veary.debs.web.struts2.actions.home.HomePageAction.java
-->
<#import "../lib/utils.ftl" as f>
<#macro chartRow>
<td>
<span class="block w-full h-full <@s.if test="data.type.getId()<8">folder-bg</@s.if><@s.else>account-bg</@s.else> truncate level-<@s.property value="level"/>">
<@s.property value="data.name"/>
</span>
</td>
</#macro>
<#macro chartDataHref check>
data-href="<@s.url action="home" namespace="/"><@s.param name="id" value="data.id"/></@s.url>" title=<@s.if test="data.type.getId()<8">"View Chart Balances"</@s.if><@s.else>"View Account's Transactions"</@s.else> onclick="dataHrefLink(this,${check});"<#rt>
</#macro>
<#macro otherAccDataHref check>
data-href="<@s.url action="home" namespace="/"><@s.param name="id" value="otherAccountId"/></@s.url>" title="View Other Account's Transactions" onclick="dataHrefLink(this,${check});"<#rt>
</#macro>
<@f.accountsPage>
<@f.contentHeader>
</@f.contentHeader>
<div>
<#if showChartBalance == true>
<#include "chartBalance.ftl">
<#else>
<#include "accountTxList.ftl">
</#if>
</div>
</@f.accountsPage>
