  <div>
  <table class="table-fixed w-full">
    <thead>
      <tr>
        <th class="w-2/6"><span class="block text-left pl-1">Account</span></th>
        <th class="w-32"><span class="block text-right">Balance</span></th>
        <th></th>
      </tr>
    </thead>
    <tbody>
<@s.iterator value="chart" status="stats">
<@s.if test="#stats.odd == true">
      <tr class="bg-purple-200 cursor-pointer" <@chartDataHref check="false"/>>
</@s.if>
<@s.else>
      <tr class="cursor-pointer" <@chartDataHref check="false"/>>
</@s.else>
<@chartRow/> <#-- See home.ftl -->
        <td><#if data.displayBalance?starts_with("-")><span class="block font-bold text-red-700 text-right"><#else><span class="block font-bold text-right"></#if><@s.property value="data.displayBalance"/></span></td>
        <td></td>
      </tr>
</@s.iterator>
    </tbody>
  </table>
  </div>
