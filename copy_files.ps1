# Source and destination directories
$sourceDir = "C:\Users\Kenniell\OneDrive\Desktop\tp velarde\mobile"
$destDir = "C:\Users\Kenniell\OneDrive\Desktop\tp velarde\todo-mobile-new"

# Create necessary directories if they don't exist
$dirsToCreate = @(
    "components",
    "context",
    "hooks",
    "screens",
    "theme",
    "utils"
)

foreach ($dir in $dirsToCreate) {
    $path = Join-Path -Path $destDir -ChildPath $dir
    if (-not (Test-Path $path)) {
        New-Item -ItemType Directory -Path $path -Force | Out-Null
    }
}

# Files to copy
$filesToCopy = @(
    "App.js",
    "api.js",
    "config.js"
)

# Copy files
foreach ($file in $filesToCopy) {
    $sourcePath = Join-Path -Path $sourceDir -ChildPath $file
    $destPath = Join-Path -Path $destDir -ChildPath $file
    
    if (Test-Path $sourcePath) {
        Copy-Item -Path $sourcePath -Destination $destPath -Force
        Write-Host "Copied: $file" -ForegroundColor Green
    } else {
        Write-Host "Source not found: $file" -ForegroundColor Yellow
    }
}

# Function to copy directory contents
function Copy-DirectoryContents {
    param (
        [string]$source,
        [string]$destination
    )
    
    if (-not (Test-Path $source)) {
        Write-Host "Source directory not found: $source" -ForegroundColor Red
        return
    }
    
    if (-not (Test-Path $destination)) {
        New-Item -ItemType Directory -Path $destination -Force | Out-Null
    }
    
    Get-ChildItem -Path $source -File -Recurse | ForEach-Object {
        $relativePath = $_.FullName.Substring($source.Length + 1)
        $targetPath = Join-Path -Path $destination -ChildPath $relativePath
        $targetDir = [System.IO.Path]::GetDirectoryName($targetPath)
        
        if (-not (Test-Path $targetDir)) {
            New-Item -ItemType Directory -Path $targetDir -Force | Out-Null
        }
        
        Copy-Item -Path $_.FullName -Destination $targetPath -Force
        Write-Host "Copied: $relativePath" -ForegroundColor Green
    }
}

# Copy directories
$dirsToCopy = @(
    "components",
    "context",
    "hooks",
    "screens",
    "theme",
    "utils"
)

foreach ($dir in $dirsToCopy) {
    $sourcePath = Join-Path -Path $sourceDir -ChildPath $dir
    $destPath = Join-Path -Path $destDir -ChildPath $dir
    
    Write-Host "Copying directory: $dir" -ForegroundColor Cyan
    Copy-DirectoryContents -source $sourcePath -destination $destPath
}

Write-Host "\nCopy operation completed!" -ForegroundColor Green
