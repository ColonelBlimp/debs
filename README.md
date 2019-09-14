Double Entry Bookkeeping System (DEBS)
====
**Status**<br/>

[![Maintainability](https://api.codeclimate.com/v1/badges/61ead2ac0c68890618e2/maintainability)](https://codeclimate.com/github/ColonelBlimp/debs/maintainability)

**Building**<br/>

Modify the maven _settings.xml:_

    <profile>
      <id>dev</id>
	   <properties>
	     <dev.webappDirectory>[path-to-webappDir]</dev.webappDirectory>
	  </properties>
	</profile>

Don't forget to make the profile active.

**Tomcat Configuration**

This web application is developed and tested the latest version of Tomcat 9. Very little configuration is needed
to deploy and run the web application:

**server.xml**

Add a _Resource_ similar to the following.  Note: you might need to modify the URL, username and password!

    <GlobalNamingResources>
      <Resource name="org.veary.debs.datasource.H2DataSource"
	          auth="Container" 
              type="javax.sql.DataSource" 
              driverClassName="org.h2.Driver"
              username="sa"
              password=""
              url="jdbc:h2:${catalina.base}/webapps/ROOT/WEB-INF/db/debs"
              factory="org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory" />
    </GlobalNamingResources>

**Acknowledgements**

[Tailwindcss](https://tailwindcss.com)

[Heroicons](https://github.com/sschoger/heroicons-ui)

[Apache Tomcat](https://tomcat.apache.org)
