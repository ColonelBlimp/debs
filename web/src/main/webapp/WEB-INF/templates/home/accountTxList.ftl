  <div class="w-2/6 float-left">
    <table class="table-fixed w-full">
      <thead>
        <tr>
          <th><span class="block text-left pl-1">Account</span></th>
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
        </tr>
</@s.iterator>
      </tbody>
    </table>
  </div>
  <div class="w-4/6 float-left pl-2">
    <table class="table-fixed w-full">
      <thead>
        <tr>
          <th class="w-32"><span class="block truncate text-left pl-1">Date</span></th>
          <th class="w-56"><span class="block truncate text-left">Description</span></th>
          <th class="w-32"><span class="block truncate text-right">Amount From</span></th>
          <th class="w-32"><span class="block truncate text-right pr-2">Amount To</span></th>
          <th><span class="block truncate text-left">Other Account</span></th>
        </tr>
      </thead>
      <tbody>
<@s.iterator value="transactions" status="stats">
<@s.if test="#stats.odd == true">
        <tr class="bg-purple-200 cursor-pointer" <@otherAccDataHref/>>
</@s.if>
<@s.else>
        <tr class="cursor-pointer" <@otherAccDataHref/>>
</@s.else>
          <td><span class="block truncate text-left pl-2"><@s.property value="date"/></span></td>
          <td><span class="block truncate text-left"><@s.property value="narrative"/></span></td>
          <td><span class="block truncate text-right"><@s.property value="amountFrom"/></span></td>
          <td><span class="block truncate text-right pr-2"><@s.property value="amountTo"/></span></td>
          <td><span class="block truncate text-left"><@s.property value="otherAccountName"/></span></td>
        </tr>
</@s.iterator>
      </tbody>
    </table>
  </div>
