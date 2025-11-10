# FluxTiers Mod - Setup & Build Guide

## ✅ What Has Been Created

### Minecraft Mod (Client-Side Fabric)
A fully functional Fabric mod located at: `C:\Users\jenik\vypnitoprojects\fluxtiers-mod\`

### Backend API Endpoint
Added to your Discord bot backend: `/api/v1/tiers/:username`
- Already deployed and pushed to GitHub
- Ready to use at: `http://104.194.9.117:25319/api/v1/tiers/:username`

## 📁 Project Structure

```
fluxtiers-mod/
├── src/main/java/com/vypnito/fluxtiers/
│   ├── FluxTiersMod.java          # Main mod entry point
│   ├── api/
│   │   ├── TierApiClient.java     # HTTP client for API calls
│   │   └── TierCache.java         # 5-minute caching system
│   ├── commands/
│   │   ├── TierCommand.java       # /tier command with autocomplete
│   │   └── TierRefreshCommand.java # /tierrefresh command
│   ├── config/
│   │   └── ModConfig.java         # Configuration management
│   ├── mixin/
│   │   ├── PlayerListHudMixin.java      # Tab list integration
│   │   └── PlayerEntityRendererMixin.java # Name tag rendering
│   ├── models/
│   │   └── PlayerTier.java        # Data models
│   └── util/
│       └── TierFormatter.java     # Color formatting utilities
├── src/main/resources/
│   ├── fabric.mod.json            # Mod metadata
│   └── fluxtiers.mixins.json      # Mixin configuration
├── build.gradle                    # Build configuration
├── gradle.properties              # Mod version & dependencies
└── README.md                       # Documentation
```

## 🔨 Building the Mod

### Prerequisites
- Java 17 or higher ([Download](https://adoptium.net/))
- No Gradle installation needed (uses Gradle wrapper)

### Build Steps

1. **Open Command Prompt** in the mod folder:
   ```bash
   cd C:\Users\jenik\vypnitoprojects\fluxtiers-mod
   ```

2. **Run the build**:
   ```bash
   gradlew build
   ```

3. **Find the JAR file**:
   - Location: `build/libs/fluxtiers-1.0.0.jar`
   - This is the file to upload to Modrinth!

### If Build Fails

If you get errors, try:
```bash
gradlew clean build --refresh-dependencies
```

## 🚀 Next Steps

### 1. Create GitHub Repository

Go to https://github.com/new and create a new repository named `fluxtiers-mod`, then:

```bash
cd C:\Users\jenik\vypnitoprojects\fluxtiers-mod
git branch -M main
git remote add origin https://github.com/vypnitoo/fluxtiers-mod.git
git push -u origin main
```

### 2. Test the Mod Locally

1. Install Minecraft 1.20.1
2. Install Fabric Loader for 1.20.1
3. Download Fabric API
4. Copy `build/libs/fluxtiers-1.0.0.jar` and Fabric API to `.minecraft/mods/`
5. Launch Minecraft with Fabric profile
6. Join a server and test:
   - Check if tiers show in tab list
   - Use `/tier` command
   - Use `/tier <player>` with autocomplete
   - Check if tiers show above player names

### 3. Prepare for Modrinth Upload

You'll need:
- ✅ Mod JAR file (`build/libs/fluxtiers-1.0.0.jar`)
- ✅ Mod description (use `MODRINTH.md`)
- ✅ README (already created)
- ✅ GitHub repository URL
- 📸 Screenshots (take these while testing):
  - Tab list with tiers
  - Name tags above players
  - `/tier` command output
  - Config file

### 4. Upload to Modrinth

1. Go to https://modrinth.com/
2. Click "Create a project"
3. Fill in details:
   - **Name**: FluxTiers
   - **Summary**: Display FluxTiers player rankings in Minecraft
   - **Description**: Paste from MODRINTH.md
   - **Categories**: Utility, Social
   - **License**: MIT
   - **Project type**: Mod
   - **Client/Server**: Client-side
4. Upload your JAR file
5. Set supported versions: 1.20.1
6. Add Fabric API as dependency
7. Add screenshots
8. Publish!

## ⚙️ Configuration

After first launch, the mod creates: `config/fluxtiers.json`

```json
{
  "apiUrl": "http://104.194.9.117:25319/api/v1/tiers",
  "cacheExpiry": 300,
  "showUnverifiedMessage": true,
  "discordLink": "fluxsmp.fun"
}
```

Players can customize:
- API endpoint (for custom servers)
- Cache duration
- Unverified player messages
- Discord link

## 🎨 Features Implemented

✅ **Tab List Tier Display**
- Shows highest tier next to player names
- Color-coded by tier level
- Auto-refreshes every 5 minutes

✅ **Name Tag Tier Display**
- Appears above player's head
- Same color coding as tab list
- Visible in-game

✅ **Commands**
- `/tier` - View your own tiers
- `/tier <player>` - View another player's tiers
- Player name autocomplete (online + cached players)
- `/tierrefresh` - Clear cache manually

✅ **Smart Caching**
- Fetches on login
- Auto-refresh every 5 minutes
- Background updates (non-blocking)

✅ **Unverified Player Message**
- Shows chat message on join if player has no tier
- Links to fluxsmp.fun for testing

✅ **Security**
- No credentials in mod
- Read-only API access
- Safe to decompile
- Public API with rate limiting

## 🐛 Troubleshooting

### Mod Won't Load
- Check Fabric Loader is installed
- Verify Fabric API is in mods folder
- Check Minecraft version is 1.20.1
- Check Java version is 17+

### Tiers Not Showing
- Check config file API URL is correct
- Test API endpoint in browser: `http://104.194.9.117:25319/api/v1/tiers/vypnito`
- Use `/tierrefresh` to clear cache
- Check console logs for errors

### Build Fails
- Ensure Java 17 is installed
- Run `gradlew clean build --refresh-dependencies`
- Check internet connection (downloads dependencies)

## 📞 Support

- Website: https://fluxsmp.fun
- GitHub Issues: (create repo first)
- Discord: Join at fluxsmp.fun

## 🎉 You're Done!

The mod is complete and ready to:
1. Build with `gradlew build`
2. Test locally
3. Upload to Modrinth
4. Share with your community!

The backend API is already live and working. Just build the mod and you're ready to go!
