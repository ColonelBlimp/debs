<#-- 
 * org.veary.debs.web.struts2.actions.paye.employee.EmployeeList.java
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
        <td>Number</td>
        <td>Name</td>
        <td>National ID</td>
      </tr>
    </thead>
    <tbody>
    </tbody>
  </table>
</div>
</@f.payePage>
