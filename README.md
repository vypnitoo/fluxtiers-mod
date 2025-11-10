# FluxTiers Minecraft Mod

A professional Fabric mod that displays player tiers and rankings from the FluxTiers system directly in Minecraft.

## Features

- **Tab List Display**: Shows the highest tier next to player names in the tab list (e.g., `vypnito [S+]`)
- **Name Tag Display**: Displays tier above players' heads in-game with color-coded formatting
- **`/tier` Command**: View all gamemode tiers for any player with autocomplete
- **`/tierrefresh` Command**: Manually refresh the tier cache
- **Auto-Refresh**: Tier data refreshes on login and every 5 minutes
- **Unverified Message**: Displays a message when unverified players join

## Tier Colors

- **S+/S/S-**: Gold
- **A+/A/A-**: Green
- **B+/B/B-**: Blue
- **C+/C/C-**: Yellow
- **D+/D/D-**: Red
- **E+/E/E-/F+/F/F-**: Gray

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for your Minecraft version
2. Download [Fabric API](https://modrinth.com/mod/fabric-api)
3. Download FluxTiers mod from [Modrinth](https://modrinth.com/)
4. Place both JAR files in your `.minecraft/mods` folder
5. Launch Minecraft with the Fabric profile

## Supported Versions

- Minecraft 1.20.1+ (more versions coming soon)
- Fabric Loader 0.14.0+
- Fabric API required

## Commands

- `/tier` - View your own tiers
- `/tier <player>` - View another player's tiers (with autocomplete)
- `/tierrefresh` - Clear cache and refresh tier data

## Configuration

The mod creates a configuration file at `config/fluxtiers.json`:

```json
{
  "apiUrl": "http://104.194.9.117:25319/api/v1/tiers",
  "cacheExpiry": 300,
  "showUnverifiedMessage": true,
  "discordLink": "fluxsmp.fun"
}
```

- `apiUrl`: API endpoint for tier data
- `cacheExpiry`: Cache duration in seconds (default: 300 = 5 minutes)
- `showUnverifiedMessage`: Show message to unverified players on join
- `discordLink`: Discord link shown in messages

## How It Works

1. The mod connects to the FluxTiers API to fetch tier data
2. Tier data is cached locally for 5 minutes to reduce API calls
3. On login, tiers are refreshed automatically
4. Player names in tab list and above heads show the highest tier
5. The `/tier` command shows all gamemode-specific tiers

## Security

- **Read-Only Access**: The mod only reads tier data from a public API
- **No Credentials**: No database credentials or sensitive data in the mod
- **Safe to Decompile**: Even if decompiled, no security risks
- **Rate Limited**: API has rate limiting to prevent abuse

## Building from Source

Requirements:
- Java 17 or higher
- Gradle (included via wrapper)

```bash
./gradlew build
```

The compiled JAR will be in `build/libs/`

## Support

- **Website**: https://fluxsmp.fun
- **Discord**: Join at fluxsmp.fun
- **Issues**: Report bugs on GitHub

## License

MIT License - See LICENSE file for details

## Credits

Created by vypnito for the FluxTiers ranking system.
