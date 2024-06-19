package me.truec0der.trueportals.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public abstract class ConfigHolder {
    private final Plugin plugin;
    private String file;
    private File configFile;
    private YamlConfiguration config;

    public ConfigHolder(Plugin plugin, File filePath, String file) {
        this.plugin = plugin;
        this.file = file;
        this.configFile = new File(filePath, file);
    }

    public ConfigHolder(Plugin plugin, File filePath, String file, String defaultFile) {
        this.plugin = plugin;
        this.file = file;
        this.configFile = new File(filePath, file);

        if (!configFile.exists() && plugin.getResource(configFile.getPath()) != null) {
            plugin.saveResource(configFile.getPath(), false);
        } else if (!configFile.exists()) {
            this.file = defaultFile;
            this.configFile = new File(filePath, defaultFile);
        }
    }

    public void load() {
        if (!configFile.exists()) {
            plugin.saveResource(file, false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
        config.options().copyDefaults(true);
    }

    public void reload() {
        loadAndSave();
    }

    public void save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAndSave() {
        load();
        save();
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public abstract void init();
}
