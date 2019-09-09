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
        <tr class="bg-purple-200">
</@s.if>
<@s.else>
        <tr>
</@s.else>
          <td><span class="block <@s.if test="data.type.getId()==8">folder-bg</@s.if><@s.else>account-bg</@s.else> truncate level-<@s.property value="level"/>"><a href="#"><@s.property value="data.name"/></a></span></td>
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
          <th class="w-32"><span class="block truncate text-left">Amount From</span></th>
          <th class="w-32"><span class="block truncate text-left">Amount To</span></th>
          <th><span class="block truncate text-left">Other Account</span></th>
        </tr>
      </thead>
      <tbody>
<@s.iterator value="transactions" status="stats">
<@s.if test="#stats.odd == true">
        <tr class="bg-purple-200">
</@s.if>
<@s.else>
        <tr>
</@s.else>
          <td><span class="block truncate text-left pl-2"><@s.property value="date"/></span></td>
          <td><span class="block truncate text-left"><@s.property value="narrative"/></span></td>
          <td><span class="block truncate text-left">?</span></td>
          <td><span class="block truncate text-left">?</span></td>
          <td><span class="block truncate text-left">?</span></td>
        </tr>
</@s.iterator>
      </tbody>
    </table>
  </div>
