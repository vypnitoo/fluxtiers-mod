#!/usr/bin/env python3
"""
Pterodactyl Server Manager CLI
Manage your Pterodactyl game server from the command line!
"""

import os
import sys
import requests
import json
from dotenv import load_dotenv
from colorama import init, Fore, Style

# Initialize colorama for colored output
init(autoreset=True)

# Load environment variables
load_dotenv()

PANEL_URL = os.getenv('PANEL_URL')
API_KEY = os.getenv('API_KEY')
SERVER_ID = os.getenv('SERVER_ID')

class PterodactylManager:
    def __init__(self):
        if not all([PANEL_URL, API_KEY, SERVER_ID]):
            print(f"{Fore.RED}Error: Missing configuration. Please check your .env file.")
            sys.exit(1)

        self.base_url = f"{PANEL_URL}/api/client/servers/{SERVER_ID}"
        self.headers = {
            'Authorization': f'Bearer {API_KEY}',
            'Accept': 'Application/vnd.pterodactyl.v1+json',
            'Content-Type': 'application/json'
        }

    def get_server_status(self):
        """Get server status and resource usage"""
        try:
            response = requests.get(f"{self.base_url}/resources", headers=self.headers)
            response.raise_for_status()
            data = response.json()

            attrs = data['attributes']
            print(f"\n{Fore.CYAN}{'='*50}")
            print(f"{Fore.GREEN}Server Status")
            print(f"{Fore.CYAN}{'='*50}")
            print(f"{Fore.YELLOW}State: {Fore.WHITE}{attrs['current_state']}")
            print(f"{Fore.YELLOW}CPU Usage: {Fore.WHITE}{attrs['resources']['cpu_absolute']:.2f}%")
            print(f"{Fore.YELLOW}Memory: {Fore.WHITE}{attrs['resources']['memory_bytes'] / (1024*1024):.2f} MB")
            print(f"{Fore.YELLOW}Disk: {Fore.WHITE}{attrs['resources']['disk_bytes'] / (1024*1024):.2f} MB")
            print(f"{Fore.YELLOW}Network (RX): {Fore.WHITE}{attrs['resources']['network_rx_bytes'] / (1024*1024):.2f} MB")
            print(f"{Fore.YELLOW}Network (TX): {Fore.WHITE}{attrs['resources']['network_tx_bytes'] / (1024*1024):.2f} MB")
            print(f"{Fore.CYAN}{'='*50}\n")
        except Exception as e:
            print(f"{Fore.RED}Error getting server status: {e}")

    def send_power_action(self, action):
        """Send power action (start, stop, restart, kill)"""
        try:
            payload = {'signal': action}
            response = requests.post(
                f"{self.base_url}/power",
                headers=self.headers,
                json=payload
            )
            response.raise_for_status()
            print(f"{Fore.GREEN}✓ Power action '{action}' sent successfully!")
        except Exception as e:
            print(f"{Fore.RED}Error sending power action: {e}")

    def list_files(self, directory="/"):
        """List files in a directory"""
        try:
            response = requests.get(
                f"{self.base_url}/files/list",
                headers=self.headers,
                params={'directory': directory}
            )
            response.raise_for_status()
            data = response.json()

            print(f"\n{Fore.CYAN}Files in {directory}:")
            print(f"{Fore.CYAN}{'='*70}")
            print(f"{Fore.YELLOW}{'Name':<40} {'Size':<15} {'Modified'}")
            print(f"{Fore.CYAN}{'='*70}")

            for item in data['data']:
                attrs = item['attributes']
                is_dir = "📁 " if attrs['is_file'] == False else "📄 "
                name = is_dir + attrs['name']
                size = f"{attrs['size'] / 1024:.2f} KB" if attrs['is_file'] else "DIR"
                modified = attrs['modified_at'][:10]

                print(f"{Fore.WHITE}{name:<40} {size:<15} {modified}")
            print(f"{Fore.CYAN}{'='*70}\n")

        except Exception as e:
            print(f"{Fore.RED}Error listing files: {e}")

    def read_file(self, file_path):
        """Read contents of a file"""
        try:
            response = requests.get(
                f"{self.base_url}/files/contents",
                headers=self.headers,
                params={'file': file_path}
            )
            response.raise_for_status()

            print(f"\n{Fore.CYAN}{'='*70}")
            print(f"{Fore.GREEN}Contents of {file_path}")
            print(f"{Fore.CYAN}{'='*70}")
            print(response.text)
            print(f"{Fore.CYAN}{'='*70}\n")

        except Exception as e:
            print(f"{Fore.RED}Error reading file: {e}")

    def write_file(self, file_path, content):
        """Write content to a file"""
        try:
            response = requests.post(
                f"{self.base_url}/files/write",
                headers=self.headers,
                params={'file': file_path},
                data=content
            )
            response.raise_for_status()
            print(f"{Fore.GREEN}✓ File '{file_path}' written successfully!")
        except Exception as e:
            print(f"{Fore.RED}Error writing file: {e}")

    def download_file(self, file_path):
        """Get download URL for a file"""
        try:
            response = requests.get(
                f"{self.base_url}/files/download",
                headers=self.headers,
                params={'file': file_path}
            )
            response.raise_for_status()
            data = response.json()

            download_url = data['attributes']['url']
            print(f"{Fore.GREEN}Download URL (valid for 5 minutes):")
            print(f"{Fore.CYAN}{download_url}\n")

        except Exception as e:
            print(f"{Fore.RED}Error getting download URL: {e}")

    def send_command(self, command):
        """Send a command to the server console"""
        try:
            payload = {'command': command}
            response = requests.post(
                f"{self.base_url}/command",
                headers=self.headers,
                json=payload
            )
            response.raise_for_status()
            print(f"{Fore.GREEN}✓ Command sent: {command}")
        except Exception as e:
            print(f"{Fore.RED}Error sending command: {e}")

    def get_console_logs(self):
        """Get recent console logs (WebSocket alternative needed for real-time)"""
        print(f"{Fore.YELLOW}Note: Real-time console requires WebSocket connection.")
        print(f"{Fore.YELLOW}Please use the web panel for live console access.")
        print(f"{Fore.CYAN}You can still send commands using the 'command' option!\n")

def print_menu():
    """Print the main menu"""
    print(f"\n{Fore.CYAN}{'='*50}")
    print(f"{Fore.GREEN}{Style.BRIGHT}Pterodactyl Server Manager")
    print(f"{Fore.CYAN}{'='*50}")
    print(f"{Fore.YELLOW}1. {Fore.WHITE}View Server Status")
    print(f"{Fore.YELLOW}2. {Fore.WHITE}Start Server")
    print(f"{Fore.YELLOW}3. {Fore.WHITE}Stop Server")
    print(f"{Fore.YELLOW}4. {Fore.WHITE}Restart Server")
    print(f"{Fore.YELLOW}5. {Fore.WHITE}List Files")
    print(f"{Fore.YELLOW}6. {Fore.WHITE}Read File")
    print(f"{Fore.YELLOW}7. {Fore.WHITE}Edit File")
    print(f"{Fore.YELLOW}8. {Fore.WHITE}Download File URL")
    print(f"{Fore.YELLOW}9. {Fore.WHITE}Send Console Command")
    print(f"{Fore.YELLOW}10. {Fore.WHITE}View Console (Info)")
    print(f"{Fore.YELLOW}0. {Fore.WHITE}Exit")
    print(f"{Fore.CYAN}{'='*50}\n")

def main():
    manager = PterodactylManager()

    while True:
        print_menu()
        choice = input(f"{Fore.GREEN}Select an option: {Fore.WHITE}")

        if choice == '1':
            manager.get_server_status()

        elif choice == '2':
            manager.send_power_action('start')

        elif choice == '3':
            manager.send_power_action('stop')

        elif choice == '4':
            manager.send_power_action('restart')

        elif choice == '5':
            directory = input(f"{Fore.GREEN}Enter directory (default /): {Fore.WHITE}") or "/"
            manager.list_files(directory)

        elif choice == '6':
            file_path = input(f"{Fore.GREEN}Enter file path: {Fore.WHITE}")
            if file_path:
                manager.read_file(file_path)

        elif choice == '7':
            file_path = input(f"{Fore.GREEN}Enter file path to edit: {Fore.WHITE}")
            if file_path:
                # First read the current content
                print(f"{Fore.YELLOW}Current content:")
                manager.read_file(file_path)
                print(f"{Fore.YELLOW}Enter new content (press Ctrl+D or Ctrl+Z when done):")
                try:
                    lines = []
                    while True:
                        try:
                            line = input()
                            lines.append(line)
                        except EOFError:
                            break
                    content = '\n'.join(lines)
                    manager.write_file(file_path, content)
                except KeyboardInterrupt:
                    print(f"\n{Fore.RED}Edit cancelled.")

        elif choice == '8':
            file_path = input(f"{Fore.GREEN}Enter file path to download: {Fore.WHITE}")
            if file_path:
                manager.download_file(file_path)

        elif choice == '9':
            command = input(f"{Fore.GREEN}Enter command: {Fore.WHITE}")
            if command:
                manager.send_command(command)

        elif choice == '10':
            manager.get_console_logs()

        elif choice == '0':
            print(f"{Fore.GREEN}Goodbye!")
            break

        else:
            print(f"{Fore.RED}Invalid option. Please try again.")

if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print(f"\n{Fore.GREEN}Goodbye!")
        sys.exit(0)
