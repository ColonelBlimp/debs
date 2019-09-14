<#import "../lib/utils.ftl" as f>
<@f.page>
<@f.contentHeader>
<div class="float-left pt-2 pl-2 font-bold">Transactions for <@s.property value="account.name"/></div>
<div class="float-right pt-2 pr-2 font-bold">Current Balance: <@s.property value="account.balance"/></div>
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
</@f.page>
