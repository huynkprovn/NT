@echo off
set RUN_DIR=.
set MYAPP_LIBS=%RUN_DIR%\lib
set MYAPP_CLASSPATH=%RUN_DIR%\bin
set MYAPP_CLASSPATH=%MYAPP_CLASSPATH%;%MYAPP_LIBS%\*
%JAVA_HOME%\bin\java -cp %MYAPP_CLASSPATH% com.vht.sms.content.ContentListener
goto end

:end
