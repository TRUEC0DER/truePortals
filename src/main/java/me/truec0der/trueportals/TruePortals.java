package me.truec0der.trueportals;

import lombok.NonNull;
import me.truec0der.trueportals.command.CommandHandler;
import me.truec0der.trueportals.facade.ConfigFacade;
import me.truec0der.trueportals.facade.MessagesFacade;
import me.truec0der.trueportals.facade.PluginFacade;
import me.truec0der.trueportals.listener.PortalListener;
import me.truec0der.trueportals.manager.SettingsManager;
import me.truec0der.trueportals.model.ConfigModel;
import me.truec0der.trueportals.model.MessagesModel;
import me.truec0der.trueportals.util.MessageUtil;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class TruePortals extends JavaPlugin {
    private static TruePortals instance;
    private BukkitAudiences adventure;
    private PluginFacade pluginFacade;
    private ConfigFacade configFacade;
    private MessagesFacade messagesFacade;
    private MessageUtil messageUtil;

    public static void reloadPlugin() {
        instance.onDisable();
        instance.onEnable();
    }

    @Override
    public void onEnable() {
        instance = this;
        adventure = BukkitAudiences.create(this);

        pluginFacade = new PluginFacade(instance, adventure());
//        commandList = new ArrayList<>();

        initSettings();
        messageUtil = new MessageUtil(messagesFacade);

        registerCommands();
        registerEvents();
        initMetrics();

        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(instance);

        if (pluginFacade.getAdventure() != null) {
            pluginFacade.getAdventure().close();
            pluginFacade.setAdventure(null);
        }

        getLogger().info("Plugin disabled!");
    }

    public @NonNull BukkitAudiences adventure() {
        if (adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return adventure;
    }

    private void initSettings() {
        SettingsManager configManager = new SettingsManager(instance, "config.yml");
        ConfigModel configModel = new ConfigModel(configManager);
        configFacade = new ConfigFacade(configManager, configModel);

        SettingsManager messagesManager = new SettingsManager(instance, String.format("messages/messages_%s.yml", configModel.getLocale()), "messages/messages_en.yml");
        MessagesModel messagesModel = new MessagesModel(messagesManager);
        messagesFacade = new MessagesFacade(messagesManager, messagesModel);
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PortalListener(pluginFacade, configFacade, messagesFacade, messageUtil), this);
    }

    private void registerCommands() {
        getServer().getPluginCommand("trueportals").setExecutor(new CommandHandler(pluginFacade, configFacade, messagesFacade, messageUtil));
        getCommand("trueportals").setTabCompleter(new CommandHandler(pluginFacade, configFacade, messagesFacade, messageUtil));
    }

    private void initMetrics() {
        int pluginId = 20857;
        Metrics metrics = new Metrics(instance, pluginId);

        metrics.addCustomChart(new SimplePie("locale", () -> configFacade.getConfigModel().getLocale()));
    }
}