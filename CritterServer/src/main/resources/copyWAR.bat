echo Make up for intellij 14 not supporting TomEE 7
CD /D %~dp0
CD ..
CD ..
CD ..
echo %CD%
RMDIR /S /Q "%CATALINA_HOME%\webapps\api"
COPY %CD%\target\api.war %CATALINA_HOME%\webapps