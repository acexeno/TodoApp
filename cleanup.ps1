# Set source and destination paths
$sourceDir = "C:\Users\Kenniell\OneDrive\Desktop\tp velarde"
$cleanDir = "C:\Users\Kenniell\OneDrive\Desktop\todo-clean-final"

# Create clean directory if it doesn't exist
if (-not (Test-Path $cleanDir)) {
    New-Item -ItemType Directory -Path $cleanDir -Force | Out-Null
}

# 1. Copy root files
$rootFiles = @("manage.py", "requirements.txt", ".gitignore", "README.md")
foreach ($file in $rootFiles) {
    $sourceFile = Join-Path $sourceDir $file
    if (Test-Path $sourceFile) {
        Copy-Item -Path $sourceFile -Destination $cleanDir -Force
        Write-Host "Copied: $file"
    }
}

# 2. Copy todo app
$todoSrc = Join-Path $sourceDir "todo"
$todoDest = Join-Path $cleanDir "todo"
if (-not (Test-Path $todoDest)) {
    New-Item -ItemType Directory -Path $todoDest -Force | Out-Null
}
Get-ChildItem -Path $todoSrc -Filter "*.py" | ForEach-Object {
    Copy-Item -Path $_.FullName -Destination $todoDest -Force
    Write-Host "Copied: todo\$($_.Name)"
}

# 3. Copy todo_project
$projectSrc = Join-Path $sourceDir "todo_project"
$projectDest = Join-Path $cleanDir "todo_project"
if (-not (Test-Path $projectDest)) {
    New-Item -ItemType Directory -Path $projectDest -Force | Out-Null
}
Get-ChildItem -Path $projectSrc -Filter "*.py" | ForEach-Object {
    Copy-Item -Path $_.FullName -Destination $projectDest -Force
    Write-Host "Copied: todo_project\$($_.Name)"
}

# 4. Copy templates directory
$templatesSrc = Join-Path $sourceDir "templates"
$templatesDest = Join-Path $cleanDir "templates"
if (Test-Path $templatesSrc) {
    Copy-Item -Path "$templatesSrc\*" -Destination $templatesDest -Recurse -Force
    Write-Host "Copied: templates directory"
}

# 5. Copy static directory
$staticSrc = Join-Path $sourceDir "static"
$staticDest = Join-Path $cleanDir "static"
if (Test-Path $staticSrc) {
    Copy-Item -Path "$staticSrc\*" -Destination $staticDest -Recurse -Force
    Write-Host "Copied: static directory"
}

Write-Host "`nCleanup completed. Clean project is available at: $cleanDir"
