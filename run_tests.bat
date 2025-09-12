@echo off
echo Running Pipeline4J Tests...
echo.

REM 编译测试代码
javac -cp "src\main\java;src\test\java;libs\*" -d build\test-classes src\test\java\com\example\pipeline\*.java src\test\java\com\example\*.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

REM 运行测试
java -cp "build\test-classes;src\main\java;libs\*" org.junit.platform.console.ConsoleLauncher --scan-classpath

echo.
echo Tests completed!
pause