package me.truec0der.trueportals.manager;

import lombok.Getter;
import me.truec0der.trueportals.TruePortals;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SettingsManager {
    private final TruePortals instance;
    private String filePath;
    private File settingsFile;
    @Getter
    private YamlConfiguration settings;

    public SettingsManager(TruePortals instance, String filePath) {
        this.instance = instance;

        this.filePath = filePath;
        this.settingsFile = new File(this.instance.getDataFolder(), filePath);

        if (!settingsFile.exists()) {
            instance.saveResource(filePath, false);
        }

        reload();
    }

    public SettingsManager(TruePortals instance, String filePath, String defaultFilePath) {
        this.instance = instance;

        this.settingsFile = new File(this.instance.getDataFolder(), filePath);
        this.filePath = filePath;

        if (!settingsFile.exists() && instance.getResource(filePath) != null) {
            instance.saveResource(filePath, false);
        } else if (!settingsFile.exists()) {
            this.filePath = defaultFilePath;

            this.settingsFile = new File(this.instance.getDataFolder(), defaultFilePath);
        }

        reload();
    }

    public void reload() {
        settings = YamlConfiguration.loadConfiguration(settingsFile);
        copyDefaults();

        save();
    }

    public void save() {
        try {
            settings.save(settingsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyDefaults() {
        InputStream defaultSettingsStream = instance.getResource(this.filePath);
        if (defaultSettingsStream != null) {
            YamlConfiguration defaultSettings = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultSettingsStream));

            for (String key : defaultSettings.getKeys(true)) {
                if (!settings.contains(key)) {
                    settings.set(key, defaultSettings.get(key));
                }
            }

            save();
        }
    }
}
