package me.truec0der.trueportals.facade;

import lombok.Getter;
import me.truec0der.trueportals.manager.SettingsManager;
import me.truec0der.trueportals.model.ConfigModel;

@Getter
public class ConfigFacade {
    private final SettingsManager configManager;
    private final ConfigModel configModel;

    public ConfigFacade(SettingsManager configManager, ConfigModel configModel) {
        this.configManager = configManager;
        this.configModel = configModel;
    }
}
