# Create necessary directories
$directories = @(
    "todo",
    "todo_project",
    "templates",
    "static"
)

foreach ($dir in $directories) {
    $path = "..\todo-clean\$dir"
    if (-not (Test-Path $path)) {
        New-Item -ItemType Directory -Force -Path $path | Out-Null
    }
}

# Copy essential files
$files = @(
    "manage.py",
    "requirements.txt",
    ".gitignore",
    "README.md"
)

foreach ($file in $files) {
    if (Test-Path $file) {
        Copy-Item -Path $file -Destination "..\todo-clean\$file" -Force
    }
}

# Copy essential directories
$dirsToCopy = @{
    "todo" = @("__init__.py", "admin.py", "apps.py", "models.py", "views.py", "urls.py", "forms.py")
    "todo_project" = @("__init__.py", "settings.py", "urls.py", "wsgi.py", "asgi.py")
}

foreach ($dir in $dirsToCopy.Keys) {
    foreach ($file in $dirsToCopy[$dir]) {
        $source = "$dir\$file"
        $dest = "..\todo-clean\$dir"
        if (Test-Path $source) {
            Copy-Item -Path $source -Destination $dest -Force
        }
    }
}

# Copy templates and static directories
if (Test-Path "templates") {
    Copy-Item -Path "templates\*" -Destination "..\todo-clean\templates" -Recurse -Force
}

if (Test-Path "static") {
    Copy-Item -Path "static\*" -Destination "..\todo-clean\static" -Recurse -Force
}

Write-Host "Essential files have been copied to ..\todo-clean"
