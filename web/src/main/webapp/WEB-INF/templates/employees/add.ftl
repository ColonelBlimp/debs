<#--
 * org.veary.debs.web.struts2.actions.employees.EmployeeAdd.java
 -->
<#import "../lib/utils.ftl" as f>
<@f.payePage>
<@f.contentHeader>
<div class="p-3"><span class="font-bold">${pageBean.mainHeadingText}</span></div>
</@f.contentHeader>
<@s.form autocomplete="off">
  <div class="flex mt-6 mb-2 items-center">
    <div class="w-32">
<@s.label for="fullname" value="Fullname:" class="inline-block font-bold"/>
    </div>
    <div class="w-56">
<@s.textfield name="bean.fullname" id="fullname" maxlength="32" cssErrorClass="text-red-700 font-bold inline-block" autocomplete="off" autofocus="autofocus" class="bg-gray-200 appearance-none border-2 border-gray-200 rounded py-2 px-4 text-gray-800 leading-tight focus:outline-none focus:bg-white focus:border-purple-500"/>
    </div>
    <div class="w-96 pl-6 font-bold text-red-700 italic"><@s.fielderror><@s.param value="%{'fullname'}"/></@s.fielderror></div>
  </div>
  <div class="flex mt-4 items-center">
    <div class="w-32"></div>
    <div>
<@s.submit name="submitType" id="create" value="Create" class="shadow bg-purple-500 hover:bg-purple-400 focus:shadow-outline focus:outline-none text-white font-bold py-2 px-4 rounded mr-2"/>
<@s.submit name="submitType" id="cancel" value="Cancel" class="shadow bg-purple-500 hover:bg-purple-400 focus:shadow-outline focus:outline-none text-white font-bold py-2 px-4 rounded mr-2"/>
    </div>
    <div class="w-96 pl-6"></div>
  </div>
</@s.form>
</@f.payePage>
