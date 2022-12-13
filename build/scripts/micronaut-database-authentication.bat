@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  micronaut-database-authentication startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and MICRONAUT_DATABASE_AUTHENTICATION_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\micronaut-database-authentication-0.1.jar;%APP_HOME%\lib\micronaut-data-jdbc-3.8.1.jar;%APP_HOME%\lib\micronaut-reactor-2.4.1.jar;%APP_HOME%\lib\micronaut-security-jwt-3.8.3.jar;%APP_HOME%\lib\micronaut-security-3.8.3.jar;%APP_HOME%\lib\micronaut-serde-jackson-1.3.3.jar;%APP_HOME%\lib\micronaut-jdbc-hikari-4.7.2.jar;%APP_HOME%\lib\micronaut-views-thymeleaf-3.6.0.jar;%APP_HOME%\lib\micronaut-data-runtime-3.8.1.jar;%APP_HOME%\lib\micronaut-jdbc-4.7.2.jar;%APP_HOME%\lib\micronaut-security-annotations-3.8.3.jar;%APP_HOME%\lib\micronaut-serde-support-1.3.3.jar;%APP_HOME%\lib\micronaut-serde-api-1.3.3.jar;%APP_HOME%\lib\micronaut-views-core-3.6.0.jar;%APP_HOME%\lib\micronaut-data-model-3.8.1.jar;%APP_HOME%\lib\micronaut-data-tx-3.8.1.jar;%APP_HOME%\lib\micronaut-validation-3.7.4.jar;%APP_HOME%\lib\micronaut-http-client-3.7.4.jar;%APP_HOME%\lib\spring-security-crypto-5.7.3.jar;%APP_HOME%\lib\jcl-over-slf4j-1.7.36.jar;%APP_HOME%\lib\ksuid-1.0.0.jar;%APP_HOME%\lib\micronaut-http-server-netty-3.7.4.jar;%APP_HOME%\lib\micronaut-http-netty-3.7.4.jar;%APP_HOME%\lib\micronaut-http-server-3.7.4.jar;%APP_HOME%\lib\micronaut-websocket-3.7.4.jar;%APP_HOME%\lib\micronaut-http-client-core-3.7.4.jar;%APP_HOME%\lib\micronaut-runtime-3.7.4.jar;%APP_HOME%\lib\micronaut-router-3.7.4.jar;%APP_HOME%\lib\micronaut-jackson-databind-3.7.4.jar;%APP_HOME%\lib\micronaut-jackson-core-3.7.4.jar;%APP_HOME%\lib\micronaut-json-core-3.7.4.jar;%APP_HOME%\lib\micronaut-http-3.7.4.jar;%APP_HOME%\lib\micronaut-context-3.7.4.jar;%APP_HOME%\lib\micronaut-aop-3.7.4.jar;%APP_HOME%\lib\micronaut-buffer-netty-3.7.4.jar;%APP_HOME%\lib\micronaut-inject-3.7.4.jar;%APP_HOME%\lib\logback-classic-1.2.11.jar;%APP_HOME%\lib\mysql-connector-java-8.0.30.jar;%APP_HOME%\lib\jackson-datatype-jdk8-2.13.4.jar;%APP_HOME%\lib\jackson-datatype-jsr310-2.13.4.jar;%APP_HOME%\lib\jackson-databind-2.13.4.2.jar;%APP_HOME%\lib\jackson-annotations-2.13.4.jar;%APP_HOME%\lib\jackson-core-2.13.4.jar;%APP_HOME%\lib\jakarta.annotation-api-2.1.1.jar;%APP_HOME%\lib\javax.annotation-api-1.3.2.jar;%APP_HOME%\lib\netty-handler-proxy-4.1.84.Final.jar;%APP_HOME%\lib\netty-codec-http2-4.1.84.Final.jar;%APP_HOME%\lib\netty-codec-http-4.1.84.Final.jar;%APP_HOME%\lib\netty-codec-socks-4.1.84.Final.jar;%APP_HOME%\lib\netty-handler-4.1.84.Final.jar;%APP_HOME%\lib\netty-codec-4.1.84.Final.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.84.Final.jar;%APP_HOME%\lib\netty-transport-4.1.84.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.84.Final.jar;%APP_HOME%\lib\micronaut-core-reactive-3.7.4.jar;%APP_HOME%\lib\reactive-streams-1.0.4.jar;%APP_HOME%\lib\reactor-core-3.4.23.jar;%APP_HOME%\lib\micronaut-core-3.7.4.jar;%APP_HOME%\lib\HikariCP-4.0.3.jar;%APP_HOME%\lib\thymeleaf-extras-java8time-3.0.4.RELEASE.jar;%APP_HOME%\lib\thymeleaf-3.0.15.RELEASE.jar;%APP_HOME%\lib\slf4j-api-1.7.36.jar;%APP_HOME%\lib\snakeyaml-1.33.jar;%APP_HOME%\lib\validation-api-2.0.1.Final.jar;%APP_HOME%\lib\protobuf-java-3.20.1.jar;%APP_HOME%\lib\nimbus-jose-jwt-9.25.jar;%APP_HOME%\lib\netty-resolver-4.1.84.Final.jar;%APP_HOME%\lib\netty-common-4.1.84.Final.jar;%APP_HOME%\lib\logback-core-1.2.11.jar;%APP_HOME%\lib\jakarta.inject-api-2.0.1.jar;%APP_HOME%\lib\jcip-annotations-1.0-1.jar;%APP_HOME%\lib\ognl-3.1.26.jar;%APP_HOME%\lib\attoparser-2.0.5.RELEASE.jar;%APP_HOME%\lib\unbescape-1.1.6.RELEASE.jar;%APP_HOME%\lib\javassist-3.20.0-GA.jar;%APP_HOME%\lib\javax.transaction-api-1.3.jar


@rem Execute micronaut-database-authentication
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %MICRONAUT_DATABASE_AUTHENTICATION_OPTS%  -classpath "%CLASSPATH%" example.micronaut.Application %*

:end
@rem End local scope for the variables with windows NT shell
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
rem Set variable MICRONAUT_DATABASE_AUTHENTICATION_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%MICRONAUT_DATABASE_AUTHENTICATION_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
