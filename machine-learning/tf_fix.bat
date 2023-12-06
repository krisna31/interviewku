@echo off
set "source=node_modules\@tensorflow\tfjs-node\deps\lib\tensorflow.dll"
set "destination=node_modules\@tensorflow\tfjs-node\lib\napi-v7"

echo Moving tensorflow.dll to %destination%...

if not exist "%source%" (
    echo Source file not found.
    pause
    exit /b
)

if not exist "%destination%" (
    mkdir "%destination%"
)

move /y "%source%" "%destination%"

if errorlevel 1 (
    echo Failed to move file.
) else (
    echo File moved successfully.
)

pause
