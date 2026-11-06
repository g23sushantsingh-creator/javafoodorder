Set-Location -Path $PSScriptRoot
Write-Host "==============================================" -ForegroundColor Cyan
Write-Host " Starting Online Food Feedback System (Spring Boot)" -ForegroundColor Green
Write-Host "==============================================" -ForegroundColor Cyan
.\mvnw.cmd spring-boot:run
