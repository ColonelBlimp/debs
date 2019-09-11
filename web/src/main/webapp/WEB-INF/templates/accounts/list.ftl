<#--
 * AccountList.java
-->
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
        <th class="w-56"><span class="block text-left pl-1">Name</span></th>
        <th class="w-56"><span class="block truncate text-left">Description</span></th>
        <th class="w-48"><span class="block text-left">Type</span></th>
        <th class="w-48"><span class="block text-left">Group</span></th>
        <th class="w-32"><span class="block text-left">Balance</span></th>
        <th class="w-6"><span class="block text-left">X</span></th>
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
        <td><span class="block truncate text-left"><@s.if test="typeId!=${GROUP_TYPE}"><a href="<@s.url action="transactions" namespace="accounts"><@s.param name="id" value="id"/></@s.url>"></@s.if><@s.property value="name"/></a></span></td>
        <td><span class="block truncate text-left"><@s.property value="description"/></span></td>
        <td><span class="block truncate text-left"><@s.property value="typeName"/></span></td>
        <td><span class="block truncate text-left"><@s.property value="parentName"/></span></td>
        <td><span class="block truncate text-left"><@s.property value="balance"/></span></td>
        <td><span class="block truncate text-left"><input type="checkbox" <@s.if test="deleted">checked</@s.if> disabled="true"></span></td>
      </tr>
</@s.iterator>
    </tbody>
  </table>
</div>
</@f.page>
