<#import "../lib/utils.ftl" as f>
<@f.page>
<@f.contentHeader>
SOMETHING
</@f.contentHeader>
<div>
  <table>
    <thead>
      <tr>
        <th><span class="block text-left pl-1 w-96">Account</span></th>
        <th><span class="block text-left pl-1 w-64">Balance</span></th>
      </tr>
    </thead>
    <tbody>
<@s.iterator value="chart" status="stats">
<@s.if test="#stats.odd == true">
      <tr>
</@s.if>
<@s.else>
      <tr class="bg-purple-200">
</@s.else>
        <td><span class="block <@s.if test="data.type.getId()==8">folder-bg</@s.if><@s.else>account-bg</@s.else> level-<@s.property value="level"/>"><@s.property value="data.name"/></span></td>
        <td><span class="block"><@s.property value="data.balance"/></span></td>
      </tr>
</@s.iterator>
    </tbody>
  </table>
</div>
</@f.page>
