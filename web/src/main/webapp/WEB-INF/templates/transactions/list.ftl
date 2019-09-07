<#import "../lib/utils.ftl" as f>
<@f.page>
<@f.contentHeader>
<div class="float-left w-1/2 p-3">
<div class="float-left pr-2 font-bold">Viewing:</div><div class="float-left"></div>
</div>
<div class="float-right w-1/2 p-3"><@f.addEditDelete/></div>
</@f.contentHeader>
<div>
  <table class="table-fixed w-full">
    <thead>
      <tr></tr>
    </thead>
    <tbody>
<@s.iterator value="transactions" status="stats">
<@s.if test="#stats.odd == true">
      <tr class="bg-purple-200">
</@s.if>
<@s.else>
      <tr>
</@s.else>

      </tr>
</@s.iterator>
    </tbody>
  </table>      
</div>
</@f.page>