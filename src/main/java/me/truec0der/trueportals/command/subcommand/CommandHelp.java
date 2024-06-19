package me.truec0der.trueportals.command.subcommand;

import me.truec0der.trueportals.command.ICommand;
import me.truec0der.trueportals.config.configs.LangConfig;
import me.truec0der.trueportals.util.MessageUtil;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;

public class CommandHelp implements ICommand {
    private final LangConfig langConfig;

    public CommandHelp(LangConfig langConfig) {
        this.langConfig = langConfig;
    }

    @Override
    public String getName() {
        return "help";
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
        return "trueportals.commands.help";
    }

    @Override
    public boolean isConsoleCan() {
        return true;
    }

    public boolean execute(CommandSender sender, Audience audience, String[] args) {
        audience.sendMessage(MessageUtil.create(langConfig.getHelpInfo()));

        return true;
    }
}
