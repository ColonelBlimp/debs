<#macro page>
<!DOCTYPE html>
<html lang="${pageBean.siteLocale}" class="bg-white antialiased w-full">
  <head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <title>${pageBean.pageTitle}</title>
    <base href="${pageBean.siteUrl}"/>
    <link rel="stylesheet" href="static/css/main.css"/>
    <link rel="stylesheet" href="static/css/custom.css"/>
  </head>
  <body>
    <div class="flex fixed top-0 inset-x-0 z-100 h-12 items-center pl-4 bg-purple-600 text-white">
      <nav>
        <ul>
          <li class="inline-block pr-4 font-semibold"><a href="/" title="Home">Home</a></li>
          <li class="inline-block pr-4 font-semibold"><a href="#" title="Transactions">Transactions</a></li>
          <li class="inline-block pr-4 font-semibold"><a href="#" title="Accounts">Accounts</a></li>
        </ul>
      </nav>
    </div>
    <div class="container mt-12">
<#nested>
    </div>
  </body>
</html>
</#macro>

<#macro contentHeader>
<div class="w-full h-12 bg-purple-200 mt-4">
<#nested>
</div>
</#macro>