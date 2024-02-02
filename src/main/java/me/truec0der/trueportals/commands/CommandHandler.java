package me.truec0der.trueportals.commands;

import me.truec0der.trueportals.commands.subcommands.CommandChangeStatus;
import me.truec0der.trueportals.commands.subcommands.CommandHelp;
import me.truec0der.trueportals.commands.subcommands.CommandReload;
import me.truec0der.trueportals.commands.subcommands.CommandStatus;
import me.truec0der.trueportals.managers.SettingsManager;
import me.truec0der.trueportals.models.ConfigModel;
import me.truec0der.trueportals.models.MessagesModel;
import me.truec0der.trueportals.utils.MessageUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandHandler implements CommandExecutor {
    private JavaPlugin instance;
    private BukkitAudiences adventure;
    private MessagesModel messagesModel;
    private ConfigModel configModel;
    private MessageUtil messageUtil;
    private SettingsManager configManager;
    private SettingsManager messagesManager;

    public CommandHandler(JavaPlugin instance, BukkitAudiences adventure, MessagesModel messagesModel, ConfigModel configModel, MessageUtil messageUtil, SettingsManager configManager, SettingsManager messagesManager) {
        this.instance = instance;
        this.adventure = adventure;
        ;
        this.messagesModel = messagesModel;
        this.configModel = configModel;
        this.messageUtil = messageUtil;
        this.configManager = configManager;
        this.messagesManager = messagesManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Audience audience = adventure.sender(sender);

        if (!sender.hasPermission("trueportals.commands")) {
            audience.sendMessage(messageUtil.create(messagesModel.getNotPerms()));
            return true;
        }

        if (args.length == 0) return new CommandStatus(messagesModel, configModel, messageUtil).execute(audience);

        return switch (args[0]) {
            case "help" -> new CommandHelp(messagesModel, messageUtil).execute(audience);
            case "status" ->
                    new CommandChangeStatus(messagesModel, configModel, messageUtil, configManager).execute(audience, args);
            case "reload" -> new CommandReload(messagesModel, messageUtil).execute(audience);
            default -> new CommandStatus(messagesModel, configModel, messageUtil).execute(audience);
        };
    }
}
