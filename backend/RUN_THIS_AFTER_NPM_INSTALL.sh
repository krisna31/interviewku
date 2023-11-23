#!/bin/bash

source_file="node_modules/@tensorflow/tfjs-node/deps/lib/tensorflow.dll"
destination_folder="node_modules/@tensorflow/tfjs-node/lib/napi-v7"
destination_file="$destination_folder/tensorflow.dll"

echo "Copying tensorflow.dll to $destination_folder..."

if [ -f "$source_file" ]; then
    echo "File $source_file exists."
else
    echo "File $source_file does not exist. Exiting program."
    exit 1
fi

mkdir -p "$destination_folder"

cp -f "$source_file" "$destination_folder"

if [ -f "$destination_file" ]; then
    echo "File copied successfully."
else
    echo "Failed to copy file."
fi
