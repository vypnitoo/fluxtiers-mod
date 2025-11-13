#!/bin/bash

echo "Building FluxTiers for all Minecraft versions..."
echo ""

VERSIONS=("1.19.2")

for version in "${VERSIONS[@]}"; do
    echo "========================================"
    echo "Building for Minecraft $version"
    echo "========================================"
    ./gradlew clean build -PmcVersion=$version
    if [ $? -ne 0 ]; then
        echo "Failed to build for Minecraft $version"
        exit 1
    fi
    echo ""
done

echo "========================================"
echo "All builds completed successfully!"
echo "========================================"
echo "Built jars are in build/libs/"
