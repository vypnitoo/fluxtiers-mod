# Pterodactyl Server Manager 🚀

A powerful command-line interface (CLI) tool to manage your Pterodactyl game server remotely! Perfect for managing Discord bots, Minecraft servers, or any other service running on Pterodactyl.

## Features ✨

- 📊 **Server Status** - View CPU, RAM, disk usage, and network stats
- ⚡ **Power Control** - Start, stop, and restart your server
- 📁 **File Management** - Browse, read, and edit files on your server
- 📥 **File Download** - Get download URLs for any file
- ⌨️ **Console Commands** - Send commands directly to your server console
- 🎨 **Colorful Interface** - Easy-to-read colored output

## Prerequisites 📋

- Python 3.7 or higher
- Access to a Pterodactyl panel
- A valid API key from your Pterodactyl account

## Installation 🔧

1. **Navigate to the project directory:**
   ```bash
   cd pterodactyl-manager
   ```

2. **Install dependencies:**
   ```bash
   pip install -r requirements.txt
   ```

3. **Your credentials are already configured in the `.env` file!**
   (The file has been created with your server details)

## Configuration ⚙️

Your `.env` file is already set up with:
- Panel URL: `https://panel.foliumhosting.net`
- Server ID: `05594c07`
- API Key: Already configured

**Note:** The `.env` file is gitignored for security. Never commit it to version control!

## Usage 🎮

### Run the manager:

```bash
python pterodactyl_manager.py
```

### Available Commands:

1. **View Server Status** - Check CPU, RAM, disk usage, and current state
2. **Start Server** - Boot up your server
3. **Stop Server** - Gracefully shut down your server
4. **Restart Server** - Restart your server
5. **List Files** - Browse files and directories
6. **Read File** - View the contents of any file
7. **Edit File** - Modify file contents directly
8. **Download File URL** - Get a temporary download link for a file
9. **Send Console Command** - Execute commands on your server
10. **View Console** - Information about console access

### Example Workflows:

#### Check if your Discord bot is running:
```
1. Select option 1 (View Server Status)
2. Check if state is "running"
3. View CPU and RAM usage
```

#### Edit your bot's config file:
```
1. Select option 5 (List Files)
2. Enter "/" to see all files
3. Select option 7 (Edit File)
4. Enter the file path (e.g., "config.json")
5. View current content, then enter new content
```

#### Restart your Discord bot:
```
1. Select option 4 (Restart Server)
2. Wait a moment, then check status with option 1
```

#### Send a command to your bot:
```
1. Select option 9 (Send Console Command)
2. Enter your command (depends on your bot)
```

## Security 🔒

- Your API key is stored in `.env` and is gitignored
- Never share your API key publicly
- The API key only has access to YOUR servers, not the entire panel
- Download URLs expire after 5 minutes for security

## Troubleshooting 🔍

### "Error: Missing configuration"
- Make sure the `.env` file exists and contains all three variables
- Check that there are no typos in the variable names

### "401 Unauthorized"
- Your API key may be invalid or expired
- Generate a new API key from your panel's Account Settings → API Credentials

### "404 Not Found"
- Double-check your server ID in the `.env` file
- Verify the panel URL is correct

### Connection errors
- Ensure you have internet connectivity
- Check if the panel is accessible in your browser

## Advanced Usage 💡

### Quick Commands Without Menu

You can modify the script to run single commands. For example, to check status:

```python
manager = PterodactylManager()
manager.get_server_status()
```

### Automation

You can create scripts that use this tool to automate tasks:
- Scheduled restarts
- Automatic backups before updates
- Status monitoring

## API Endpoints Used 📡

This tool uses the Pterodactyl Client API:
- `GET /api/client/servers/{server}/resources` - Server status
- `POST /api/client/servers/{server}/power` - Power actions
- `GET /api/client/servers/{server}/files/list` - List files
- `GET /api/client/servers/{server}/files/contents` - Read files
- `POST /api/client/servers/{server}/files/write` - Write files
- `GET /api/client/servers/{server}/files/download` - Download URLs
- `POST /api/client/servers/{server}/command` - Send commands

## Support 💬

If you encounter issues:
1. Check the troubleshooting section above
2. Verify your API key and server ID
3. Ensure you're using Python 3.7+
4. Check that all dependencies are installed

## Disclaimer ⚠️

This tool interacts with your Pterodactyl server. Use caution when:
- Editing configuration files
- Sending console commands
- Stopping/starting your server

Always backup important files before making changes!

## Contributing 🤝

Feel free to fork, modify, and improve this tool for your needs!

## License 📄

Free to use and modify for personal and commercial projects.

---

**Happy Server Managing! 🎉**
