@echo off
set "JRE_HOME=C:\Program Files\Java\jdk-23"
set "CATALINA_HOME=C:\Program Files\Apache Software Foundation\Tomcat 9.0"
cd /d "%CATALINA_HOME%\bin"
call catalina.bat run
