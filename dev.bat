@echo off
title Poobcraft Dev Launcher

echo [Step 1] Building and copying plugin .jar...
call gradlew.bat build
call gradlew.bat copyJarToPlugins

echo [Step 2] Starting test server...
cd /d "C:\Users\Justi\OneDrive\Documents\Plugin Dev\Test Server"
java -Xms1G -Xmx2G -jar paper-1.21.7-17.jar --nojline --nogui