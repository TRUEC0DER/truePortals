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

public class CommandPortalStatus implements ICommand {
    private final MainConfig mainConfig;
    private final LangConfig langConfig;

    public CommandPortalStatus(MainConfig mainConfig, LangConfig langConfig) {
        this.mainConfig = mainConfig;
        this.langConfig = langConfig;
    }

    @Override
    public String getName() {
        return "status";
    }

    @Override
    public String getRegex() {
        return "^(end|nether)\\s+(enable|disable)$";
    }

    @Override
    public String[] getCompleteArgs() {
        return new String[]{"end|nether", "enable|disable"};
    }

    @Override
    public String getPermission() {
        return "trueportals.commands.portal.status";
    }

    @Override
    public boolean isConsoleCan() {
        return true;
    }

    public boolean execute(CommandSender sender, Audience audience, String[] args) {
        String dimension = args[0].toLowerCase();
        String status = args[1].toLowerCase();

        Map<String, String> changeStatusPlaceholders = new HashMap<>();
        changeStatusPlaceholders.put("portal_status", getPortalState(status));
        changeStatusPlaceholders.put("portal_name", getPortalName(dimension));

        mainConfig.getConfig().set("portals." + dimension, "enable".equals(status));
        mainConfig.save();

        mainConfig.reload();

        Component changeStatus = MessageUtil.create(langConfig.getStatusChangeInfo(), changeStatusPlaceholders);

        audience.sendMessage(changeStatus);

        return true;
    }

    private String getPortalState(String status) {
        return langConfig.getStatusChangeStates().get("disable".equals(status) ? 0 : 1);
    }

    private String getPortalName(String dimension) {
        return langConfig.getStatusChangePortals().get("end".equals(dimension) ? 0 : 1);
    }
}
