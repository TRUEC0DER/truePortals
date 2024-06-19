package me.truec0der.trueportals.impl.service.plugin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.truec0der.trueportals.config.configs.LangConfig;
import me.truec0der.trueportals.config.configs.MainConfig;
import me.truec0der.trueportals.interfaces.service.plugin.PluginReloadService;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PluginReloadServiceImpl implements PluginReloadService {
    MainConfig mainConfig;
    LangConfig langConfig;

    @Override
    public void reload() {
        mainConfig.reload();
        langConfig.reload();
    }
}
