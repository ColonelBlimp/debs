<#macro page>
<!DOCTYPE html>
<html lang="${pageBean.siteLocale}" class="bg-white antialiased w-full">
  <head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <title>${pageBean.pageTitle}</title>
    <base href="${pageBean.siteUrl}"/>
    <link rel="stylesheet" href="/static/css/main.css"/>
  </head>
  <body>
  <#nested>
  </body>
</html>
</#macro>
