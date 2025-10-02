package me.truec0der.trueportals.command;

import me.truec0der.trueportals.command.subcommand.*;
import me.truec0der.trueportals.config.configs.LangConfig;
import me.truec0der.trueportals.config.configs.MainConfig;
import me.truec0der.trueportals.interfaces.service.plugin.PluginReloadService;
import me.truec0der.trueportals.util.MessageUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommandHandler implements CommandExecutor, TabCompleter {
    private final BukkitAudiences adventure;
    private final PluginReloadService pluginReloadService;
    private final MainConfig mainConfig;
    private final LangConfig langConfig;
    private final CommandManager commandManager;

    public CommandHandler(BukkitAudiences adventure, PluginReloadService pluginReloadService, MainConfig mainConfig, LangConfig langConfig) {
        this.adventure = adventure;
        this.pluginReloadService = pluginReloadService;

        this.mainConfig = mainConfig;
        this.langConfig = langConfig;

        this.commandManager = new CommandManager();

        initCommands();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Audience audience = adventure.sender(sender);
        String[] slicedArgs = args.length > 0 ? Arrays.copyOfRange(args, 1, args.length) : new String[0];

        Optional<ICommand> emptyCommand = commandManager.getCommands().stream().filter(cmd -> cmd.getName().isEmpty()).findFirst();

        if (args.length == 0) {
            if (!emptyCommand.isPresent()) {
                audience.sendMessage(MessageUtil.create(langConfig.getNeedCorrectArgs()));
                return true;
            }

            if (!(sender instanceof Player) && !emptyCommand.get().isConsoleCan()) {
                audience.sendMessage(MessageUtil.create(langConfig.getOnlyPlayer()));
                return true;
            }

            if (!sender.hasPermission(emptyCommand.get().getPermission())) {
                audience.sendMessage(MessageUtil.create(langConfig.getNotPerms()));
                return true;
            }
            return emptyCommand.map(cmd -> executeCommand(cmd, sender, audience, slicedArgs)).get();
        }

        List<ICommand> foundCommands = commandManager.findCommandsByName(args[0]);

        if (!foundCommands.isEmpty()) {
            for (ICommand commandObject : foundCommands) {
                if (Pattern.matches(commandObject.getRegex(), String.join(" ", slicedArgs))) {
                    if (!sender.hasPermission(commandObject.getPermission())) {
                        audience.sendMessage(MessageUtil.create(langConfig.getNotPerms()));
                        return true;
                    }
                    return executeCommand(commandObject, sender, audience, slicedArgs);
                }
            }
            audience.sendMessage(MessageUtil.create(langConfig.getNeedCorrectArgs()));
            return true;
        }

        audience.sendMessage(MessageUtil.create(langConfig.getNeedCorrectArgs()));
        return true;
    }

    private boolean executeCommand(ICommand command, CommandSender sender, Audience audience, String[] args) {
        return command.execute(sender, audience, args);
    }

    private void initCommands() {
        commandManager.addCommand(new CommandPortalStatus(mainConfig, langConfig));
        commandManager.addCommand(new CommandDestinationStatus(mainConfig, langConfig));
        commandManager.addCommand(new CommandDestinationSetSpawn(mainConfig, langConfig));
        commandManager.addCommand(new CommandDestinationSpawn(mainConfig));
        commandManager.addCommand(new CommandActivationStatus(mainConfig, langConfig));
        commandManager.addCommand(new CommandReload(langConfig, pluginReloadService));
        commandManager.addCommand(new CommandHelp(langConfig));
        commandManager.addCommand(new CommandInfo(mainConfig, langConfig));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return commandManager.getCommandNamesForSender(sender);
        }

        String commandName = args[0];
        List<ICommand> foundCommands = commandManager.findCommandsByName(commandName);

        return foundCommands.stream()
                .filter(cmd -> sender.hasPermission(cmd.getPermission()) && !cmd.getName().isEmpty())
                .flatMap(cmd -> getCompletionList(args, cmd).stream())
                .collect(Collectors.toList());
    }

    private List<String> getCompletionList(String[] args, ICommand command) {
        int argIndex = args.length - 2;
        if (argIndex >= 0 && argIndex < command.getCompleteArgs().length) {
            return Arrays.asList(command.getCompleteArgs()[argIndex].split("\\|"));
        } else {
            return Collections.emptyList();
        }
    }
}