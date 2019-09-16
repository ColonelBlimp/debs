<#--
 * org.veary.debs.web.struts2.actions.accounts.AccountTransactionsList.java
-->
<#import "../lib/utils.ftl" as f>
<@f.page>
<#if (session.getAttribute('VOUCHER_NAME'))??>
<span class="hidden" id="voucherName">${session.getAttribute('VOUCHER_NAME')}</span>
<script>
document.addEventListener('readystatechange', event => {
  if (event.target.readyState === 'interactive') {
    setTimeout(function() {
      var elem = document.getElementById('voucherName');
      window.location = '/accounts/download.action?voucherFileName='+elem.textContent;
    }, 1000);
  }
});
</script>
</#if>
<@f.contentHeader>
<div class="float-left w-1/2 p-3">
  <div class="float-left font-bold">Viewing:</div><div class="float-left"><@f.listSelect/></div>
</div>
<div class="float-right w-1/2 p-3">
  <div class="float-right">
    <div class="float-left"></div>
    <div class="pl-4 float-left"><#include "../includes/voucherModal.ftl"></div>
  </div>
</div>
</@f.contentHeader>
  <div class="w-full">
    <table class="table-fixed w-full">
      <thead>
        <tr>
          <th class="w-6"><span class="block text-left pl-1">ID</span></th>
          <th class="w-32"><span class="block truncate text-left pl-1">Date</span></th>
          <th class="w-56"><span class="block truncate text-left">Description</span></th>
          <th class="w-32"><span class="block truncate text-right">Amount From</span></th>
          <th class="w-32"><span class="block truncate text-right">Amount To</span></th>
          <th class="w-40"><span class="block truncate text-left pl-2">Other Account</span></th>
          <th class="w-48"><span class="block text-left">Reference</span></th>
          <th class="w-6"><span class="block text-left">X</span></th>
        </tr>
      </thead>
      <tfoot>
        <tr class="border-b border-t border-gray-600">
          <td colspan="3"></td>
          <td><div class="text-right tcw-132"><@s.property value="fromColumnTotal"/></div></td>
          <td><div class="text-right tcw-132"><@s.property value="toColumnTotal"/></div></td>
          <td colspan="3"></td>
        </tr>
      </tfoot>
      <tbody>
<@s.iterator value="transactions" status="stats">
<@s.if test="#stats.odd == true">
        <tr class="bg-purple-200">
</@s.if>
<@s.else>
        <tr>
</@s.else>
          <td><span class="block text-left pl-2"><@s.property value="id"/></span></td>
          <td><span class="block truncate text-left pl-1"><@s.property value="date"/></span></td>
          <td><span class="block truncate text-left"><@s.property value="narrative"/></span></td>
          <td><span class="block truncate text-right"><@s.property value="amountFrom"/></span></td>
          <td><span class="block truncate text-right"><@s.property value="amountTo"/></span></td>
          <td><span class="block truncate text-left pl-2"><@s.property value="otherAccountName"/></span></td>
          <td><span class="block truncate text-left"><@s.property value="reference"/></span></td>
          <td><span class="block truncate text-left"><input type="checkbox" <@s.if test="deleted">checked</@s.if> disabled></span></td>
        </tr>
</@s.iterator>
      </tbody>
    </table>
  </div>
</@f.page>
