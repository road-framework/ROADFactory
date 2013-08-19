@echo off

echo ********** Please install ROAD4WS before installing ROAD in Axis2**********
echo Installing ROADFactory ...
echo --------------------------        
echo Following settings were detected. 
echo AXIS2_HOME = "%AXIS2_HOME%"
echo CATALINA_HOME = "%CATALINA_HOME%"
echo If correct, 
pause


IF "%AXIS2_HOME%" == "" GOTO NO_AXIS2_HOME

:OK
xcopy .\lib\*.jar %AXIS2_HOME%\lib /Y 
xcopy .\properties\log4j.properties %AXIS2_HOME%\classes\ /Y
xcopy .\properties\serendip.properties %CATALINA_HOME%\bin\ /Y
xcopy .\sample\Scenario1\RoSaS.xml %AXIS2_HOME%\road_composites\ /Y
xcopy .\sample\Scenario2\USDL.xml %AXIS2_HOME%\road_composites\ /Y
xcopy .\sample\Scenario1\data\* %AXIS2_HOME%\sample\Scenario1\data\ /E /Y
xcopy .\sample\Scenario2\data\* %AXIS2_HOME%\sample\Scenario2\data\ /E /Y
xcopy .\sample\Scenario1\data\* %CATALINA_HOME%\bin\sample\Scenario1\data\ /E /Y
xcopy .\sample\Scenario2\data\* %CATALINA_HOME%\bin\sample\Scenario2\data\ /E /Y
xcopy .\pojo\* %AXIS2_HOME%\pojo\ /E /Y
xcopy .\dot\* %CATALINA_HOME%\bin\dot /E /Y
xcopy .\images\* %CATALINA_HOME%\bin\images/E /Y

echo ROADFactory install complete. Please start the server. 
pause
GOTO END

:NO_AXIS2_HOME
echo Please set AXIS2_HOME environmental variable. The value should point to .../WEB_INF/
echo You need to have Apache Tomcat 6.0 or later installed (if not already)
echo You need to have Apache Axis2 1.5.0 or later installed (if not already)

pause
GOTO END

:END