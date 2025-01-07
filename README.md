# ChatReport

ChatReport is a powerful and lightweight Minecraft plugin that allows players to report others with recent chat messages. The plugin sends reports to a Discord webhook, enabling server staff to monitor and manage player behavior efficiently.<br>

<details>
<summary>Here you can find some screenshots of this plugin.</summary>
  
![image](https://github.com/user-attachments/assets/409260e3-6397-4d26-a3c9-f001e5947354)
![image](https://github.com/user-attachments/assets/ae1b6385-2f1c-4083-b2d4-83ff43529f78)
![image](https://github.com/user-attachments/assets/8fdb14e7-d4c8-48eb-813e-aac2c601824a)
</details>


---

## 📦 Features

- **Easy Player Reporting**: Players can report others using a simple command.
- **Discord Integration**: Reports are sent to a specified Discord webhook as rich embeds.
- **Customizable Message Logs**: Define the number of recent messages to include in reports.
- **Duplicate Report Prevention**: Configurable cooldown prevents multiple reports for the same player.
- **Localized Messages**: Support for multiple languages (e.g., EN, FR, RO, PT, CN).
- **Developer-Friendly**: Debug mode to track plugin activity and errors.
- **Reloadable Configuration**: Reload settings without restarting the server.

---

## 💻 Commands

| Command                     | Description                                      | Permission             |
|-----------------------------|--------------------------------------------------|------------------------|
| `/chatreport help`          | Displays the help menu.                         | `chatreport.help`      |
| `/chatreport report <name>` | Reports a player with their recent messages.    | `chatreport.report`    |
| `/chatreport reload`        | Reloads the plugin configuration and locales.   | `chatreport.reload`    |
| `/chatreport version`       | Displays the plugin version and author details. | `chatreport.version`   |

---

## ⚙ Permissions

| Permission          | Description                                          | Default |
|---------------------|------------------------------------------------------|---------|
| `chatreport.help`   | Allows access to the help command.                   | `true`  |
| `chatreport.report` | Allows players to report others.                     | `true`  |
| `chatreport.reload` | Allows access to reload the plugin configuration.    | `op`    |
| `chatreport.version`| Allows viewing the plugin version and author info.   | `true`  |

---

## 📝 Locales

ChatReport supports multiple languages. Locales can be found in the `locales/` directory and modified to fit your server's requirements. Supported languages include:

- English (`en_US.yml`)
- French (`fr_FR.yml`)
- Romanian (`ro_RO.yml`)
- Portuguese (`pt_PT.yml`)
- Chinese Simplified (`zh_CN.yml`)

---

## 🛠 Installation

1. Download the latest version of ChatReport from [Releases](https://github.com/Cold-Development/ChatReport/releases).
2. Place the `.jar` file into your server's `plugins` folder.
3. Start or restart your server.
4. Configure the plugin in `config.yml` and provide the required Discord webhook details.
5. Use `/chatreport reload` to apply any changes made to the configuration or locales.

---

## 🛡 Support

If you encounter issues, have questions, or want to suggest features, feel free to:
- Open an issue on our [GitHub Issues page](https://github.com/Cold-Development/ChatReport/issues).
- Join our [Discord](https://discord.colddev.dev).

---

## 🤝 Dependencies

This plugin has no dependencies at this moment.


---

## 👨‍💻 Credits

Developed by **padrewin** and **666pyke**.
