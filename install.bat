@echo off
setlocal

set "INSTALL_DIR=%USERPROFILE%\.todo-cli"
set "BIN_DIR=%USERPROFILE%\.local\bin"
set "JAR_NAME=cli-todoer.jar"
set "CMD_NAME=todo.bat"

echo Creating install directory at %INSTALL_DIR%...
mkdir "%INSTALL_DIR%" 2>nul

echo Creating bin directory at %BIN_DIR%...
mkdir "%BIN_DIR%" 2>nul

echo Copying %JAR_NAME% to %INSTALL_DIR%...
copy "%JAR_NAME%" "%INSTALL_DIR%\%JAR_NAME%"

echo Creating launcher script at %BIN_DIR%\%CMD_NAME%...
(
  echo @echo off
  echo java -jar "%INSTALL_DIR%\%JAR_NAME%" %%*
) > "%BIN_DIR%\%CMD_NAME%"

REM Add BIN_DIR to user PATH if not already present
echo Checking if %BIN_DIR% is in PATH...
echo %PATH% | find /I "%BIN_DIR%" >nul
if errorlevel 1 (
  echo Adding %BIN_DIR% to user PATH...
  setx PATH "%BIN_DIR%;%PATH%"
  echo Please restart your terminal or log out and log in again for PATH changes to take effect.
) else (
  echo PATH already contains %BIN_DIR%.
)

echo Installed! Try running:
echo todo --help

endlocal