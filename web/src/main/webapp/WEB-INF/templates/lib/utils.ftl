<#macro page>
<!DOCTYPE html>
<html lang="${pageBean.siteLocale}" class="bg-white antialiased w-full">
  <head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <title>${pageBean.pageTitle}</title>
    <base href="${pageBean.siteUrl}"/>
    <link rel="icon" type="image/x-icon" href="static/img/favicon.ico"/>
    <link rel="stylesheet" href="static/css/main.css"/>
    <link rel="stylesheet" href="static/css/custom.css"/>
    <script src="static/js/utils.js"></script>
  </head>
  <body>
    <div class="flex fixed top-0 inset-x-0 z-100 h-12 items-center pl-4 bg-purple-600 text-white">
      <nav>
        <ul>
          <li class="inline-block pr-4 font-semibold"><a href="/" title="Home">Home</a></li>
          <li class="inline-block pr-4 font-semibold"><a href="#" title="Transactions">Transactions</a></li>
          <li class="inline-block pr-4 font-semibold"><a href="<@s.url action="list" namespace="accounts"/>" title="Accounts">Accounts</a></li>
        </ul>
      </nav>
    </div>
    <div class="container border border-white mt-12">
<#nested>
    </div>
  </body>
</html>
</#macro>

<#macro contentHeader>
<div class="w-full h-12 bg-purple-300 mt-4">
<#nested>
</div>
</#macro>

<#macro addEditDelete>
    <a href="<@s.url action="delete"/>" id="delete" title="Delete" class="pointer-events-none"><span class="float-right hover:bg-blue-500 w-7 mr-1 p-1 mb-1">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" width="20px" height="20px">
        <path d="M3.389 7.113L4.49 18.021c.061.461 2.287 1.977 5.51 1.979 3.225-.002 5.451-1.518 5.511-1.979l1.102-10.908C14.929 8.055 12.412 8.5 10 8.5c-2.41 0-4.928-.445-6.611-1.387zm9.779-5.603l-.859-.951C11.977.086 11.617 0 10.916 0H9.085c-.7 0-1.061.086-1.392.559l-.859.951C4.264 1.959 2.4 3.15 2.4 4.029v.17C2.4 5.746 5.803 7 10 7c4.198 0 7.601-1.254 7.601-2.801v-.17c0-.879-1.863-2.07-4.433-2.519zM12.07 4.34L11 3H9L7.932 4.34h-1.7s1.862-2.221 2.111-2.522c.19-.23.384-.318.636-.318h2.043c.253 0 .447.088.637.318.248.301 2.111 2.522 2.111 2.522h-1.7z"/>
      </svg>
    </span></a>
    <a href="<@s.url action="edit"/>" id="edit" title="Edit" class="pointer-events-none"><span class="float-right hover:bg-blue-500 w-7 mr-1 p-1 mb-1">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" width="20px" height="20px">
        <path d="M18.174 1.826c-1.102-1.102-2.082-.777-2.082-.777L7.453 9.681 6 14l4.317-1.454 8.634-8.638s.324-.98-.777-2.082zm-7.569 9.779l-.471.47-1.473.5a2.216 2.216 0 0 0-.498-.74 2.226 2.226 0 0 0-.74-.498l.5-1.473.471-.47s.776-.089 1.537.673c.762.761.674 1.538.674 1.538zM16 17H3V4h5l2-2H3c-1.1 0-2 .9-2 2v13c0 1.1.9 2 2 2h13c1.1 0 2-.9 2-2v-7l-2 2v5z"/>
      </svg>
    </span></a>
    <a href="<@s.url action="add"/>" title="Add"><span class="float-right hover:bg-blue-500 w-7 mr-1 p-1 mb-1">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" width="20px" height="20px">
        <path d="M19.4 9H16V5.6c0-.6-.4-.6-1-.6s-1 0-1 .6V9h-3.4c-.6 0-.6.4-.6 1s0 1 .6 1H14v3.4c0 .6.4.6 1 .6s1 0 1-.6V11h3.4c.6 0 .6-.4.6-1s0-1-.6-1zm-12 0H.6C0 9 0 9.4 0 10s0 1 .6 1h6.8c.6 0 .6-.4.6-1s0-1-.6-1zm0 5H.6c-.6 0-.6.4-.6 1s0 1 .6 1h6.8c.6 0 .6-.4.6-1s0-1-.6-1zm0-10H.6C0 4 0 4.4 0 5s0 1 .6 1h6.8C8 6 8 5.6 8 5s0-1-.6-1z"/>
      </svg>
    </span></a>
</#macro>

<#macro accountListSelect>
<@s.form>
<@s.hidden id="id" name="id"/>
<@s.select name="listView" list="viewMap" listKey="key" listValue="value" listCssClass="bg-purple-100" cssClass="bg-purple-300" onchange="this.form.submit();"/>
<input type="checkbox" name="includeDeleted" value="true" onchange="this.form.submit();" <@s.if test="includeDeleted">checked</@s.if>>
<label for="includeDeleted">Include Deleted</label>
</@s.form>
</#macro>
