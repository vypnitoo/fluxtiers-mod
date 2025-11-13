# FluxTiers Multi-Version Build System

This mod supports building for multiple Minecraft versions using a single codebase.

## Supported Versions

The following Minecraft versions are officially supported:

- **1.21** (Latest)
- **1.20.6**
- **1.20.4**
- **1.20.1**
- **1.19.4**
- **1.19.2**
- **1.18.2**
- **1.17.1**
- **1.16.5**

## Building for a Specific Version

To build the mod for a specific Minecraft version, use the `-PmcVersion` parameter:

### Windows
```batch
gradlew.bat clean build -PmcVersion=1.20.1
```

### Linux/Mac
```bash
./gradlew clean build -PmcVersion=1.20.1
```

### Examples

Build for Minecraft 1.21:
```bash
./gradlew clean build -PmcVersion=1.21
```

Build for Minecraft 1.19.4:
```bash
./gradlew clean build -PmcVersion=1.19.4
```

Build for Minecraft 1.16.5:
```bash
./gradlew clean build -PmcVersion=1.16.5
```

## Building All Versions

To build the mod for all supported versions at once:

### Windows
```batch
build-all.bat
```

### Linux/Mac
```bash
chmod +x build-all.sh
./build-all.sh
```

This will create separate jars for each Minecraft version in the `build/libs/` directory.

## Output Files

Built jars will be named with the following format:
```
fluxtiers-{mc-version}-{mod-version}+mc{mc-version}.jar
```

Examples:
- `fluxtiers-1.20.1-1.0.0+mc1.20.1.jar`
- `fluxtiers-1.19.4-1.0.0+mc1.19.4.jar`
- `fluxtiers-1.16.5-1.0.0+mc1.16.5.jar`

## Default Build

If you don't specify a version, it defaults to **1.20.1**:
```bash
./gradlew clean build
```

## Adding New Versions

To add support for a new Minecraft version:

1. Open `versions.gradle`
2. Add a new entry in the `minecraftVersions` map:
```groovy
'1.xx.x': [
    minecraft: '1.xx.x',
    yarn: '1.xx.x+build.x',
    loader: '0.xx.x',
    fabric: '0.xx.x+1.xx.x'
]
```
3. Update `build-all.bat` and `build-all.sh` to include the new version

## Version Configuration

All version-specific dependencies are defined in `versions.gradle`:
- Minecraft version
- Yarn mappings version
- Fabric Loader version
- Fabric API version

## Notes

- Each build is completely independent
- The `clean` task is recommended to ensure no cross-version contamination
- All versions share the same source code
- Version-specific code can be added using conditional compilation if needed

## Troubleshooting

**Build fails for a specific version:**
- Check that the version exists in `versions.gradle`
- Verify the dependency versions are correct
- Ensure you're using the latest Fabric Loom

**Jar file is missing classes:**
- Make sure you're using `include(implementation(...))` for dependencies
- Check that Fabric Loom version is compatible

**Multiple versions conflict:**
- Always run `clean` before building a different version
- Use the build-all scripts which handle this automatically
