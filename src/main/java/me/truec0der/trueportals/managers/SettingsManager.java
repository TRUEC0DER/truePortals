package me.truec0der.trueportals.managers;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class SettingsManager {
    private JavaPlugin instance;
    private File settingsFile;
    @Getter
    private YamlConfiguration settings;

    public SettingsManager(JavaPlugin instance, String filePath) {
        this.instance = instance;
        this.settingsFile = new File(this.instance.getDataFolder(), filePath);

        if (!settingsFile.exists()) {
            instance.saveResource(filePath, false);
        }

        reload();
    }

    public SettingsManager(JavaPlugin instance, String filePath, String defaultFilePath) {
        this.instance = instance;

        this.settingsFile = new File(this.instance.getDataFolder(), filePath);

        if (!settingsFile.exists() && instance.getResource(filePath) != null) {
            instance.saveResource(filePath, false);
        } else if (!settingsFile.exists()) {
            this.settingsFile = new File(this.instance.getDataFolder(), defaultFilePath);
        }

        reload();
    }

    public void reload() {
        settings = YamlConfiguration.loadConfiguration(settingsFile);
        settings.options().copyDefaults(true);
        save();
    }

    public void save() {
        try {
            settings.save(settingsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
