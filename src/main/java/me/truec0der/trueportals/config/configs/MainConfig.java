package me.truec0der.trueportals.config.configs;

import lombok.Getter;
import me.truec0der.trueportals.config.ConfigHolder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

@Getter
public class MainConfig extends ConfigHolder {
    private String locale;
    private boolean updateCheck;
    private boolean updateAuto;
    private boolean portalsEnd;
    private boolean portalsNether;
    private boolean activationEnd;
    private boolean activationNether;
    private ConfigurationSection destinationsEnd;
    private ConfigurationSection destinationsNether;
    private boolean worksPlayers;
    private boolean worksEntities;

    public MainConfig(Plugin plugin, File filePath, String file) {
        super(plugin, filePath, file);
        loadAndSave();
        init();
    }

    @Override
    public void init() {
        YamlConfiguration config = this.getConfig();

        locale = config.getString("locale");

        updateCheck = config.getBoolean("update.check");
        updateAuto = config.getBoolean("update.auto");

        portalsEnd = config.getBoolean("portals.end");
        portalsNether = config.getBoolean("portals.nether");

        activationEnd = config.getBoolean("activation.end");
        activationNether = config.getBoolean("activation.nether");

        destinationsEnd = config.getConfigurationSection("destinations.end");
        destinationsNether = config.getConfigurationSection("destinations.nether");

        worksPlayers = config.getBoolean("works.players");
        worksEntities = config.getBoolean("works.entities");
    }
}
