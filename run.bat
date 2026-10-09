@echo off
cd /d "%~dp0GameDev"
if exist "C:\Program Files\Java\jdk-26\bin\javaw.exe" (
    start "Spirits of the Vale" "C:\Program Files\Java\jdk-26\bin\javaw.exe" -cp "bin;res" main.Main
) else (
    start "Spirits of the Vale" javaw -cp "bin;res" main.Main
)
