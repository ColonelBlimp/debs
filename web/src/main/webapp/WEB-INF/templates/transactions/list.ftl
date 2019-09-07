<#import "../lib/utils.ftl" as f>
<@f.page>
<@f.contentHeader>
<div class="float-left w-1/2 p-3">
<div class="float-left pr-2 font-bold">Viewing:</div><div class="float-left"><@f.listSelect/></div>
</div>
<div class="float-right w-1/2 p-3"><@f.addEditLinks/></div>
</@f.contentHeader>
<div>
  <table class="table-fixed w-full">
    <thead>
      <tr>
        <th class="w-6"><span class="block text-left">&nbsp;</span></th>
        <th class="w-32"><span class="block text-left">Date</span></th>
        <th class="w-96"><span class="block text-left">Description</span></th>
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
        <td><span class="block text-left pl-1"><input type="radio" name="selected" value="<@s.property value="id"/>" onchange="updateSelected('/accounts',<@s.property value="id"/>)"/></span></td>
      </tr>
</@s.iterator>
    </tbody>
  </table>      
</div>
</@f.page>