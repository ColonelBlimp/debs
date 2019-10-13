<#--
 * org.veary.debs.web.struts2.actions.transactions.TransactionAdd
-->
<#import "../lib/utils.ftl" as f>
<@f.accountsPage>
<@f.contentHeader>
<div class="p-3"><span class="font-bold">${pageBean.mainHeadingText}</span></div>
</@f.contentHeader>
<@s.form autocomplete="off">
<@s.hidden name="sessionDate" id="sessionDate"/>
  <div class="flex mt-6 mb-2 items-center">
    <div class="w-32">
<@s.label for="date" value="Date:" class="inline-block font-bold"/>
    </div>
    <div class="w-56">
<@s.textfield name="bean.date" id="date" maxlength="10" cssErrorClass="text-red-700 font-bold inline-block" autocomplete="off" autofocus="autofocus" placeholder="${sessionDate}" onkeydown="checkForTab(event)" class="bg-gray-200 appearance-none border-2 border-gray-200 rounded py-2 px-4 text-gray-800 leading-tight focus:outline-none focus:bg-white focus:border-purple-500"/>
    </div>
    <div class="w-96 pl-6 font-bold text-red-700 italic"><@s.fielderror><@s.param value="%{'date'}"/></@s.fielderror></div>
  </div>
  <div class="flex mb-2 items-center">
    <div class="w-32">
<@s.label for="narrative" value="Narrative:" class="inline-block font-bold"/>
    </div>
    <div class="w-56">
<@s.textfield name="bean.narrative" id="narrative" maxlength="100" cssErrorClass="text-red-700 font-bold inline-block" autocomplete="off" class="bg-gray-200 appearance-none border-2 border-gray-200 rounded py-2 px-4 text-gray-800 leading-tight focus:outline-none focus:bg-white focus:border-purple-500"/>
    </div>
    <div class="w-96 pl-6 font-bold text-red-700 italic"><@s.fielderror><@s.param value="%{'narrative'}"/></@s.fielderror></div>
  </div>
  <div class="flex mb-2 items-center">
    <div class="w-32">
<@s.label for="amount" value="Amount:" class="inline-block font-bold"/>
    </div>
    <div class="w-56">
<@s.textfield name="bean.amount" id="amount" maxlength="20" pattern="^(-{0,1}\\d+)([.]\\d{2})?" placeholder="0.00" cssErrorClass="text-red-700 font-bold inline-block" autocomplete="off" class="bg-gray-200 appearance-none border-2 border-gray-200 rounded py-2 px-4 text-gray-800 leading-tight focus:outline-none focus:bg-white focus:border-purple-500"/>
    </div>
    <div class="w-96 pl-6 font-bold text-red-700 italic"><@s.fielderror><@s.param value="%{'amount'}"/></@s.fielderror></div>
  </div>
  <div class="flex mb-2 items-center">
    <div class="w-32">
<@s.label for="from" value="From Account:" class="inline-block font-bold"/>
    </div>
    <div class="w-56">
<@s.select name="bean.fromAccountId" id="from" list="accountsMap" value="selectedFromAccount" class="bg-gray-200 p-2 w-full border-2 border-gray-200 rounded text-gray-800 focus:border-purple-500"/>
    </div>
    <div class="w-96 pl-6 font-bold text-red-700 italic"></div>
  </div>
  <div class="flex mb-2 items-center">
    <div class="w-32">
<@s.label for="to" value="To Account:" class="inline-block font-bold"/>
    </div>
    <div class="w-56">
<@s.select name="bean.toAccountId" id="to" list="accountsMap" value="selectedToAccount" class="bg-gray-200 p-2 w-full border-2 border-gray-200 rounded text-gray-800 focus:border-purple-500"/>
    </div>
    <div class="w-96 pl-6 font-bold text-red-700 italic"><@s.fielderror><@s.param value="%{'to'}"/></@s.fielderror></div>
  </div>
  <div class="flex mb-2 items-center">
    <div class="w-32">
<@s.label for="reference" value="Reference:" class="inline-block font-bold"/>
    </div>
    <div class="w-56">
<@s.textfield name="bean.reference" id="reference" maxlength="20" cssErrorClass="text-red-700 font-bold inline-block" autocomplete="off" class="bg-gray-200 appearance-none border-2 border-gray-200 rounded py-2 px-4 text-gray-800 leading-tight focus:outline-none focus:bg-white focus:border-purple-500"/>
    </div>
    <div class="w-96 pl-6 font-bold text-red-700 italic"><@s.fielderror><@s.param value="%{'reference'}"/></@s.fielderror></div>
  </div>
  <div class="flex items-center">
    <div class="w-32"></div>
    <div class="w-88">
<@s.submit name="submitType" id="create" value="Create" class="shadow bg-purple-500 hover:bg-purple-400 focus:shadow-outline focus:outline-none text-white font-bold py-2 px-4 rounded"/>
<@s.submit name="submitType" id="cancel" value="Cancel" formnovalidate="true" class="shadow bg-purple-500 hover:bg-purple-400 focus:shadow-outline focus:outline-none text-white font-bold py-2 px-4 rounded"/>
    </div>
    <div class="w-96 pl-6"></div>
  </div>
</@s.form>
</@f.accountsPage>
