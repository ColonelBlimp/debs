<#-- 
 * org.veary.debs.web.struts2.actions.paye.employees.EmployeeList.java
-->
<#import "../lib/utils.ftl" as f>
<@f.payePage>
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
        <th class="w-56"><span class="block text-left pl-1">Fullname</span></th>
        <th class="w-56"><span class="block truncate text-left">Number</span></th>
        <th class="w-32"><span class="block text-right pr-4">National ID</span></th>
        <th class="w-6"><span class="block text-left">X</span></th>
      </tr>
    </thead>
    <tbody>
<@s.iterator value="employees" status="stats">
<@s.if test="#stats.odd == true">
      <tr class="bg-purple-200">
</@s.if>
<@s.else>
      <tr>
</@s.else>
        <td><span class="block text-left pl-1"><input type="radio" name="selected" value="<@s.property value="id"/>" onchange="updateSelected('/employees',<@s.property value="id"/>)"/></span></td>
        <td><span class="block truncate text-left"><@s.property value="fullname"/></span></td>
        <td><span class="block truncate text-left"><@s.property value="nationalIdNumber"/></span></td>
        <td><span class="block truncate text-left"><@s.property value="contactNumber"/></span></td>
        <td><span class="block truncate text-left"><input type="checkbox" <@s.if test="deleted">checked</@s.if> disabled="true"></span></td>
      </tr>
</@s.iterator>
    </tbody>
  </table>
</div>
</@f.payePage>
