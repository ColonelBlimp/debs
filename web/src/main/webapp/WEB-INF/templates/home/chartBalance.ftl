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
      <tr class="bg-purple-200">
</@s.if>
<@s.else>
      <tr>
</@s.else>
<@chartRow/>
        <td><span class="block"><@s.property value="data.balance"/></span></td>
      </tr>
</@s.iterator>
    </tbody>
  </table>
  </div>
