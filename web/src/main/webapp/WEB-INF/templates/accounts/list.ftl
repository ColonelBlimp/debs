<#import "../lib/utils.ftl" as f>
<@f.page>
<@f.contentHeader>
<div class="float-left w-1/2"><h1>${pageBean.mainHeadingText}</h1></div>
<div class="float-left w-1/2"><@f.addEditDelete/></div>
</@f.contentHeader>
<div>
  <table class="table-fixed w-full">
    <thead>
      <tr>
        <th class="w-6"><span class="block text-left">&nbsp;</span></th>
        <th class="w-56"><span class="block text-left pl-1">Name</span></th>
        <th class="w-56><span class="block text-left">Description</span></th>
        <th class="w-56"><span class="block text-left">Type</span></th>
        <th class="w-56"><span class="block text-left">Group</span></th>
        <th><span class="block text-left">Balance</span></th>
      </tr>
    </thead>
    <tbody>
<@s.iterator value="accounts" status="stats">
<@s.if test="#stats.odd == true">
      <tr class="bg-purple-200">
</@s.if>
<@s.else>
      <tr>
</@s.else>
        <td><span class="block text-left pl-1"><input type="radio" name="selected" value="<@s.property value="id"/>" onchange="updateSelected('/accounts',<@s.property value="id"/>)"/></span></td>
        <td><span class="block truncate text-left"><a href="#"><@s.property value="name"/></a></span></td>
        <td><span class="block truncate text-left"><@s.property value="description"/></span></td>
        <td><span class="block truncate text-left"><@s.property value="type"/></span></td>
        <td><span class="block truncate text-left">?</span></td>
        <td><span class="block truncate text-left"><@s.property value="balance"/></span></td>
      </tr>
</@s.iterator>
    </tbody>
  </table>
</div>
</@f.page>