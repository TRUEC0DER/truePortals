package me.truec0der.trueportals.command.subcommand;

import me.truec0der.trueportals.command.ICommand;
import me.truec0der.trueportals.config.configs.LangConfig;
import me.truec0der.trueportals.config.configs.MainConfig;
import me.truec0der.trueportals.util.MessageUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class CommandInfo implements ICommand {
    private final MainConfig mainConfig;
    private final LangConfig langConfig;

    public CommandInfo(MainConfig mainConfig, LangConfig langConfig) {
        this.mainConfig = mainConfig;
        this.langConfig = langConfig;
    }

    @Override
    public String getName() {
        return "";
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
        return "trueportals.commands.info";
    }

    @Override
    public boolean isConsoleCan() {
        return true;
    }

    public boolean execute(CommandSender sender, Audience audience, String[] args) {
        Map<String, String> statusPlaceholders = new HashMap<>();
        statusPlaceholders.put("end_portal_status", getState(mainConfig.isPortalsEnd()));
        statusPlaceholders.put("nether_portal_status", getState(mainConfig.isPortalsNether()));
        statusPlaceholders.put("end_activation_status", getState(mainConfig.isActivationEnd()));
        statusPlaceholders.put("nether_activation_status", getState(mainConfig.isActivationNether()));
        statusPlaceholders.put("end_destination_status", getState(mainConfig.getDestinationsEnd().getBoolean("enabled")));
        statusPlaceholders.put("nether_destination_status", getState(mainConfig.getDestinationsNether().getBoolean("enabled")));

        Component status = MessageUtil.create(langConfig.getStatusInfo(), statusPlaceholders);

        audience.sendMessage(status);

        return true;
    }

    public String getState(boolean state) {
        return langConfig.getStatusInfoStates().get(!state ? 0 : 1);
    }
}
