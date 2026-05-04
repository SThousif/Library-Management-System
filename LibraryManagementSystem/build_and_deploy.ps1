# ============================================================
# build_and_deploy.ps1
# Compiles and deploys LibraryManagementSystem to Tomcat 9
# Run this from: LibraryManagementSystem folder
# Usage: powershell -ExecutionPolicy Bypass -File build_and_deploy.ps1
# ============================================================

$ErrorActionPreference = "Stop"

# ---- Paths ----
$PROJECT_DIR  = $PSScriptRoot
$SRC_DIR      = "$PROJECT_DIR\src"
$WEBCONTENT   = "$PROJECT_DIR\WebContent"
$WEB_INF      = "$WEBCONTENT\WEB-INF"
$LIB_DIR      = "$WEB_INF\lib"
$CLASSES_DIR  = "$WEB_INF\classes"
$TOMCAT_HOME  = "C:\Program Files\Apache Software Foundation\Tomcat 9.0"
$TOMCAT_LIB   = "$TOMCAT_HOME\lib"
$WEBAPP_DIR   = "$TOMCAT_HOME\webapps\LibraryManagementSystem"
$APP_NAME     = "LibraryManagementSystem"

Write-Host "`n===================================================" -ForegroundColor Cyan
Write-Host " Library Management System - Build & Deploy Script" -ForegroundColor Cyan
Write-Host "===================================================`n" -ForegroundColor Cyan

# ---- Step 1: Find MySQL Connector JAR ----
Write-Host "[1/6] Locating MySQL Connector JAR..." -ForegroundColor Yellow
$mysqlJar = Get-ChildItem "C:\Users\91797\Downloads" -Recurse -Depth 4 |
            Where-Object { $_.Name -like "*mysql-connector*.jar" -and $_.Name -notlike "*sources*" } |
            Select-Object -First 1

if (-not $mysqlJar) {
    Write-Host "ERROR: mysql-connector JAR not found in Downloads!" -ForegroundColor Red
    Write-Host "Please download it from: https://dev.mysql.com/downloads/connector/j/" -ForegroundColor Red
    exit 1
}
Write-Host "  Found: $($mysqlJar.FullName)" -ForegroundColor Green

# ---- Step 1b: Find JSTL JAR ----
Write-Host "[1b/6] Locating JSTL JAR..." -ForegroundColor Yellow
$jstlJar = Get-ChildItem "C:\Users\91797\Downloads" -Recurse -Depth 5 -ErrorAction SilentlyContinue |
            Where-Object { 
                ($_.Name -like "jstl-1.2.jar" -or $_.Name -like "jstl.jar") -and 
                ($_.FullName -notlike "*Project*") 
            } |
            Select-Object -First 1

if (-not $jstlJar) {
    Write-Host "WARNING: JSTL JAR not found in Downloads! JSPs using JSTL may fail." -ForegroundColor Yellow
} else {
    Write-Host "  Found: $($jstlJar.FullName)" -ForegroundColor Green
}

# ---- Step 2: Prepare WEB-INF/lib and WEB-INF/classes ----
Write-Host "`n[2/6] Preparing build directories..." -ForegroundColor Yellow
New-Item -ItemType Directory -Force -Path $LIB_DIR     | Out-Null
New-Item -ItemType Directory -Force -Path $CLASSES_DIR | Out-Null

# Copy MySQL JAR to WEB-INF/lib
$destJar = "$LIB_DIR\mysql-connector-j.jar"
Copy-Item -Path $mysqlJar.FullName -Destination $destJar -Force
Write-Host "  Copied MySQL JAR to WEB-INF/lib/" -ForegroundColor Green

# Copy JSTL JAR to WEB-INF/lib
if ($jstlJar) {
    $destJstl = "$LIB_DIR\jstl.jar"
    Copy-Item -Path $jstlJar.FullName -Destination $destJstl -Force
    Write-Host "  Copied JSTL JAR to WEB-INF/lib/" -ForegroundColor Green
}

# ---- Step 3: Build classpath ----
Write-Host "`n[3/6] Building classpath..." -ForegroundColor Yellow
$CP = "$TOMCAT_LIB\servlet-api.jar;$TOMCAT_LIB\jsp-api.jar;$destJar"
if ($jstlJar) { $CP += ";$LIB_DIR\jstl.jar" }
Write-Host "  Classpath built" -ForegroundColor Green

# ---- Step 4: Collect all .java files ----
Write-Host "`n[4/6] Collecting Java source files..." -ForegroundColor Yellow
$javaFiles = Get-ChildItem -Path $SRC_DIR -Filter "*.java" -Recurse |
             Select-Object -ExpandProperty FullName
Write-Host "  Found $($javaFiles.Count) source files:" -ForegroundColor Green
$javaFiles | ForEach-Object { Write-Host "    - $(Split-Path $_ -Leaf)" }

if ($javaFiles.Count -eq 0) {
    Write-Host "ERROR: No Java source files found in src/" -ForegroundColor Red
    exit 1
}

# Write source list to temp file (avoids command-line length limits)
$sourceList = "$PROJECT_DIR\sources.txt"
$javaFiles | Set-Content -Path $sourceList

# ---- Step 5: Compile ----
Write-Host "`n[5/6] Compiling Java sources..." -ForegroundColor Yellow
$javac = "javac"

$compileArgs = @(
    "-cp", $CP,
    "-d", $CLASSES_DIR,
    "-source", "11",
    "-target", "11",
    "-encoding", "UTF-8",
    "@$sourceList"
)

Write-Host "  Running: javac -cp [classpath] -d WEB-INF/classes @sources.txt" -ForegroundColor Gray
& $javac @compileArgs

if ($LASTEXITCODE -ne 0) {
    Write-Host "`nERROR: Compilation failed! Check errors above." -ForegroundColor Red
    Remove-Item $sourceList -Force -ErrorAction SilentlyContinue
    exit 1
}

Remove-Item $sourceList -Force -ErrorAction SilentlyContinue
Write-Host "  Compilation SUCCESSFUL!" -ForegroundColor Green

# ---- Step 6: Deploy to Tomcat WebApps ----
Write-Host "`n[6/6] Deploying to Tomcat webapps..." -ForegroundColor Yellow

# Remove old deployment if exists
if (Test-Path $WEBAPP_DIR) {
    Write-Host "  Removing old deployment..." -ForegroundColor Gray
    Remove-Item -Recurse -Force $WEBAPP_DIR
}

# Copy WebContent to Tomcat webapps
Copy-Item -Recurse -Force -Path $WEBCONTENT -Destination $WEBAPP_DIR
Write-Host "  Deployed to: $WEBAPP_DIR" -ForegroundColor Green

# ---- Done! ----
Write-Host "`n===================================================" -ForegroundColor Green
Write-Host " BUILD & DEPLOY SUCCESSFUL!" -ForegroundColor Green
Write-Host "===================================================`n" -ForegroundColor Green
Write-Host "  App URL  : http://localhost:8080/$APP_NAME/" -ForegroundColor Cyan
Write-Host "  Login    : admin / admin123" -ForegroundColor Cyan
Write-Host ""
Write-Host "NEXT STEP: Start Tomcat and then open the URL above." -ForegroundColor Yellow
Write-Host "  OR if Tomcat is running, it will auto-deploy from webapps/." -ForegroundColor Yellow
Write-Host ""
