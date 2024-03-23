package me.truec0der.trueportals.model;

import lombok.Getter;
import me.truec0der.trueportals.manager.SettingsManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigModel {
    private final SettingsManager configManager;

    @Getter
    private String locale;

    @Getter
    private boolean portalsEnd;
    @Getter
    private boolean portalsNether;

    @Getter
    private ConfigurationSection destinationsEnd;
    @Getter
    private ConfigurationSection destinationsNether;

    @Getter
    private boolean worksPlayers;
    @Getter
    private boolean worksEntities;

    public ConfigModel(SettingsManager configManager) {
        this.configManager = configManager;
        reload();
    }

    public void reload() {
        YamlConfiguration config = configManager.getSettings();

        this.locale = config.getString("locale");

        this.portalsEnd = config.getBoolean("portals.end");
        this.portalsNether = config.getBoolean("portals.nether");

        this.destinationsEnd = config.getConfigurationSection("destinations.end");
        this.destinationsNether = config.getConfigurationSection("destinations.nether");

        this.worksPlayers = config.getBoolean("works.players");
        this.worksEntities = config.getBoolean("works.entities");
    }
}
