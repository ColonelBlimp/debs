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

</@s.iterator>
    </tbody>
  </table>
</div>
</@f.page>
