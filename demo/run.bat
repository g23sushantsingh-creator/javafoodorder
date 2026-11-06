@echo off
cd /d "%~dp0"
echo ==============================================
echo  Starting Online Food Feedback System (Spring Boot)
echo ==============================================
call mvnw.cmd spring-boot:run
pause
