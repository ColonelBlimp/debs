<#-- 
 *
  -->
<#import "../lib/utils.ftl" as f>
<@f.accountsPage>
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
        <th class="w-32"><span class="block text-left pl-1">Date</span></th>
        <th class="w-56"><span class="block text-left">Description</span></th>
        <th class="w-48"><span class="block text-right pr-4">Amount</span></th>
        <th class="w-48"><span class="block text-left">From</span></th>
        <th class="w-32"><span class="block text-left">To</span></th>
        <th class="w-48"><span class="block text-left">Reference</span></th>
        <th class="w-6"><span class="block text-left">X</span></th>
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
        <td><span class="block text-left pl-1"><input type="radio" name="selected" value="<@s.property value="id"/>" onchange="updateSelected('/transactions',<@s.property value="id"/>)"/></span></td>
        <td><span class="block truncate text-left"><@s.property value="date"/></span></td>
        <td><span class="block truncate text-left"><@s.property value="narrative"/></span></td>
        <td><span class="block truncate text-right pr-4"><@s.property value="amount"/></span></td>
        <td><span class="block truncate text-left"><a href="<@s.url action="transactions" namespace="accounts"><@s.param name="id" value="fromAccountId"/></@s.url>" title="View Account's Transactions" class="border-b border-dotted border-black"><@s.property value="fromAccountName"/></a></span></td>
        <td><span class="block truncate text-left"><a href="<@s.url action="transactions" namespace="accounts"><@s.param name="id" value="toAccountId"/></@s.url>" title="View Account's Transactions" class="border-b border-dotted border-black"><@s.property value="toAccountName"/></a></span></td>
		<td><span class="block truncate text-left"><@s.property value="reference"/></span></td>
		<td><span class="block truncate text-left"><input type="checkbox" <@s.if test="deleted">checked</@s.if> disabled="true"></span></td>
      </tr>
</@s.iterator>
    </tbody>
  </table>      
</div>
</@f.accountsPage>
