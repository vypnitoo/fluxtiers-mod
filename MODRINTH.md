# FluxTiers - Player Tier Display Mod

Display FluxTiers rankings directly in your Minecraft client!

## 🎮 What is FluxTiers?

FluxTiers is a comprehensive player ranking and tier system for competitive Minecraft servers. This mod allows you to see player tiers in-game, making it easy to identify skill levels at a glance.

## ✨ Features

### 🏆 Tab List Integration
See player tiers right in the tab list with color-coded formatting:
- **Gold [S+]** for S-tier players
- **Green [A]** for A-tier players
- **Blue [B]** for B-tier players
- And more!

### 👤 Name Tag Display
Tiers appear above players' heads in-game, so you always know who you're facing.

### 💬 Commands
- `/tier` - View your own tiers across all gamemodes
- `/tier <player>` - Check any player's tiers (with tab autocomplete!)
- `/tierrefresh` - Manually refresh cached tier data

### ⚡ Smart Caching
- Automatic refresh on login
- Background refresh every 5 minutes
- Instant display with cached data

### 🎯 Multi-Gamemode Support
Track different tiers for different gamemodes:
- Sword PvP
- NPOT (No Potion) PvP
- SMP (Survival Multiplayer)
- And custom gamemodes!

## 📦 Installation

1. **Install Fabric Loader** - [Download here](https://fabricmc.net/use/)
2. **Install Fabric API** - [Required dependency](https://modrinth.com/mod/fabric-api)
3. **Download FluxTiers Mod** - Get it from Modrinth
4. Place the mod JAR in `.minecraft/mods/`
5. Launch and play!

## 🎨 Tier Colors

The mod uses a professional color scheme to distinguish tier levels:

| Tier | Color |
|------|-------|
| S+, S, S- | §6 Gold |
| A+, A, A- | §a Green |
| B+, B, B- | §9 Blue |
| C+, C, C- | §e Yellow |
| D+, D, D- | §c Red |
| E+, E, E-, F+, F, F- | §7 Gray |

## 🔒 Privacy & Security

- **Client-Side Only** - Works on any server, no server installation required
- **Read-Only** - Only fetches tier data, never sends your information
- **No Login Required** - Just install and go
- **Public API** - Uses a public tier database with no sensitive data

## ⚙️ Configuration

The mod creates a config file at `config/fluxtiers.json`:

```json
{
  "apiUrl": "http://104.194.9.117:25319/api/v1/tiers",
  "cacheExpiry": 300,
  "showUnverifiedMessage": true,
  "discordLink": "fluxsmp.fun"
}
```

Feel free to customize:
- API endpoint (if you're running your own FluxTiers instance)
- Cache duration (in seconds)
- Unverified player messages
- Discord link in messages

## 🌐 Compatibility

- **Minecraft**: 1.20.1+ (more versions coming soon!)
- **Mod Loader**: Fabric Loader 0.14.0+
- **Dependencies**: Fabric API (required)
- **Side**: Client-side only

## 📸 Screenshots

*(Add screenshots here showing:)*
- Tab list with tiers
- Name tags above players
- `/tier` command output
- Settings/config

## 🤝 Join the Community

- **Website**: https://fluxsmp.fun
- **Get Tested**: Join the Discord to earn your tier!
- **Support**: Report issues or request features

## 📝 Changelog

### Version 1.0.0
- Initial release
- Tab list tier display
- Name tag tier display
- `/tier` and `/tierrefresh` commands
- Autocomplete for player names
- 5-minute auto-refresh
- Unverified player notifications

## 💖 Credits

Created with ❤️ by **vypnito** for the FluxTiers community.

Special thanks to all the testers and players who helped make this mod possible!

---

**Not ranked yet?** Join the FluxTiers Discord at **fluxsmp.fun** and get tested today!
