  <div>
  <table class="table-fixed w-full">
    <thead>
      <tr>
        <th class="w-2/6"><span class="block text-left pl-1">Account</span></th>
        <th><span class="block text-left">Balance</span></th>
      </tr>
    </thead>
    <tbody>
<@s.iterator value="chart" status="stats">
<@s.if test="#stats.odd == true">
      <tr class="bg-purple-200 cursor-pointer" <@chartDataHref/>>
</@s.if>
<@s.else>
      <tr class="cursor-pointer" <@chartDataHref/>>
</@s.else>
<@chartRow/> <#-- See home.ftl -->
        <td><span class="block"><@s.property value="data.displayBalance"/></span></td>
      </tr>
</@s.iterator>
    </tbody>
  </table>
  </div>
