package me.truec0der.trueportals;

import lombok.NonNull;
import me.truec0der.trueportals.commands.CommandHandler;
import me.truec0der.trueportals.listeners.PortalListener;
import me.truec0der.trueportals.listeners.TabCompleteListener;
import me.truec0der.trueportals.managers.SettingsManager;
import me.truec0der.trueportals.models.ConfigModel;
import me.truec0der.trueportals.models.MessagesModel;
import me.truec0der.trueportals.utils.MessageUtil;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class TruePortals extends JavaPlugin {
    private static JavaPlugin instance;
    private BukkitAudiences adventure;
    private MessageUtil messageUtil;
    private SettingsManager configManager;
    private SettingsManager messagesManager;
    private ConfigModel configModel;
    private MessagesModel messagesModel;

    public static void reloadPlugin() {
        instance.onDisable();
        instance.onEnable();
    }

    public @NonNull BukkitAudiences adventure() {
        if (adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return adventure;
    }

    @Override
    public void onEnable() {
        instance = this;
        adventure = BukkitAudiences.create(this);

        initSettings();

        messageUtil = new MessageUtil(messagesModel);

        registerCommands();
        registerEvents();

        initMetrics();

        getLogger().info("Plugin enabled!");
    }

    private void initSettings() {
        configManager = new SettingsManager(instance, "config.yml");
        configModel = new ConfigModel(configManager);

        messagesManager = new SettingsManager(instance, String.format("messages/messages_%s.yml", configModel.getLocale()), "messages/messages_en.yml");
        messagesModel = new MessagesModel(messagesManager);
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PortalListener(instance, adventure(), messagesModel, configModel, messageUtil), this);
    }

    private void registerCommands() {
        getServer().getPluginCommand("trueportals").setExecutor(new CommandHandler(instance, adventure(), messagesModel, configModel, messageUtil, configManager, messagesManager));
        getCommand("trueportals").setTabCompleter(new TabCompleteListener());
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(instance);

        if (adventure != null) {
            adventure.close();
            adventure = null;
        }

        getLogger().info("Plugin disabled!");
    }

    private void initMetrics() {
        int pluginId = 20857;
        Metrics metrics = new Metrics(instance, pluginId);

        metrics.addCustomChart(new SimplePie("locale", () -> configModel.getLocale()));
    }
}
