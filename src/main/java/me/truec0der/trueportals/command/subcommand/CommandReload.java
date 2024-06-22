package me.truec0der.trueportals.command.subcommand;

import me.truec0der.trueportals.command.ICommand;
import me.truec0der.trueportals.config.configs.LangConfig;
import me.truec0der.trueportals.interfaces.service.plugin.PluginReloadService;
import me.truec0der.trueportals.util.MessageUtil;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;

public class CommandReload implements ICommand {
    private final LangConfig langConfig;
    private final PluginReloadService pluginReloadService;

    public CommandReload(LangConfig langConfig, PluginReloadService pluginReloadService) {
        this.langConfig = langConfig;
        this.pluginReloadService = pluginReloadService;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getRegex() {
        return "";
    }

    @Override
    public String[] getCompleteArgs() {
        return new String[]{""};
    }

    @Override
    public String getPermission() {
        return "trueportals.commands.reload";
    }

    @Override
    public boolean isConsoleCan() {
        return true;
    }

    public boolean execute(CommandSender sender, Audience audience, String[] args) {
        pluginReloadService.reload();
        audience.sendMessage(MessageUtil.create(langConfig.getReloadInfo()));
        return true;
    }
}
