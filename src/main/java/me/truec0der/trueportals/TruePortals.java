package me.truec0der.trueportals;

import lombok.NonNull;
import me.truec0der.trueportals.command.CommandHandler;
import me.truec0der.trueportals.config.configs.LangConfig;
import me.truec0der.trueportals.config.configs.MainConfig;
import me.truec0der.trueportals.impl.service.plugin.PluginReloadServiceImpl;
import me.truec0der.trueportals.impl.service.plugin.PluginUpdateServiceImpl;
import me.truec0der.trueportals.impl.service.portal.PortalActivateServiceImpl;
import me.truec0der.trueportals.impl.service.portal.PortalEnterServiceImpl;
import me.truec0der.trueportals.interfaces.service.plugin.PluginReloadService;
import me.truec0der.trueportals.interfaces.service.plugin.PluginUpdateService;
import me.truec0der.trueportals.interfaces.service.portal.PortalActivateService;
import me.truec0der.trueportals.interfaces.service.portal.PortalEnterService;
import me.truec0der.trueportals.listener.PortalActivateListener;
import me.truec0der.trueportals.listener.PortalEnterListener;
import me.truec0der.trueportals.misc.TaskScheduler;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Paths;

public final class TruePortals extends JavaPlugin {
    private BukkitAudiences adventure;
    private TaskScheduler taskScheduler;
    private PluginReloadService pluginReloadService;
    private PortalEnterService portalEnterService;
    private PortalActivateService portalActivateService;
    private MainConfig mainConfig;
    private LangConfig langConfig;

    @Override
    public void onEnable() {
        initAdventure();
        initTaskScheduler();
        initConfig();
        initPluginUpdateService();
        initService();
        initCommand();
        initListener();
        initMetrics();

        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);

        getLogger().info("Plugin disabled!");
    }

    public @NonNull BukkitAudiences adventure() {
        if (adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }

        return adventure;
    }

    private void initAdventure() {
        adventure = BukkitAudiences.create(this);
    }

    private void initTaskScheduler() {
        taskScheduler = new TaskScheduler(this);
    }

    private void initConfig() {
        mainConfig = new MainConfig(this, new File(this.getDataFolder().getPath()), "config.yml");
        langConfig = new LangConfig(this, new File(this.getDataFolder().getPath()), String.format("messages/lang_%s.yml", mainConfig.getLocale()), "messages/lang_en.yml");
    }

    private void initPluginUpdateService() {
        String destinationPath = Paths.get(getDataFolder().getAbsolutePath()).getParent().toString();
        String[] destinationPathSplit = getFile().getPath().split("/");
        String destinationName = destinationPathSplit[destinationPathSplit.length - 1];

        PluginUpdateService pluginUpdateService = new PluginUpdateServiceImpl("https://truec0der.github.io/plugin/truePortals.json", destinationPath, destinationName, langConfig);
        if (mainConfig.isUpdateCheck())
            pluginUpdateService.handleCheck(getDescription().getVersion(), mainConfig.isUpdateAuto());
    }

    private void initService() {
        pluginReloadService = new PluginReloadServiceImpl(mainConfig, langConfig);
        portalEnterService = new PortalEnterServiceImpl(adventure(), taskScheduler, mainConfig, langConfig);
        portalActivateService = new PortalActivateServiceImpl(adventure(), mainConfig, langConfig);
    }

    private void initCommand() {
        getServer().getPluginCommand("trueportals").setExecutor(new CommandHandler(adventure(), pluginReloadService, mainConfig, langConfig));
        getCommand("trueportals").setTabCompleter(new CommandHandler(adventure(), pluginReloadService, mainConfig, langConfig));
    }

    private void initListener() {
        getServer().getPluginManager().registerEvents(new PortalEnterListener(portalEnterService), this);
        getServer().getPluginManager().registerEvents(new PortalActivateListener(portalActivateService), this);
    }

    private void initMetrics() {
        int pluginId = 20857;
        Metrics metrics = new Metrics(this, pluginId);

        metrics.addCustomChart(new SimplePie("locale", () -> mainConfig.getLocale()));
        metrics.addCustomChart(new SimplePie("update_check", () -> String.valueOf(mainConfig.isUpdateCheck())));
        metrics.addCustomChart(new SimplePie("auto_update", () -> String.valueOf(mainConfig.isUpdateAuto())));
    }
}