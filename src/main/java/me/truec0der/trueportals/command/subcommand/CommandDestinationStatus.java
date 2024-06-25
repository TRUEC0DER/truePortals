package me.truec0der.trueportals.command.subcommand;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.truec0der.trueportals.command.ICommand;
import me.truec0der.trueportals.config.configs.LangConfig;
import me.truec0der.trueportals.config.configs.MainConfig;
import me.truec0der.trueportals.util.MessageUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommandDestinationStatus implements ICommand {
    private final MainConfig mainConfig;
    private final LangConfig langConfig;

    @Override
    public String getName() {
        return "destination";
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
        return "trueportals.commands.destination.status";
    }

    @Override
    public boolean isConsoleCan() {
        return true;
    }

    public boolean execute(CommandSender sender, Audience audience, String[] args) {
        String dimension = args[0].toLowerCase();
        String status = args[1].toLowerCase();

        Map<String, String> changeDestinationPlaceholders = new HashMap<>();
        changeDestinationPlaceholders.put("destination_status", getPortalState(status));
        changeDestinationPlaceholders.put("portal_name", getPortalName(dimension));

        mainConfig.getConfig().set("destinations." + dimension + ".enabled", "enable".equals(status));
        mainConfig.save();

        mainConfig.reload();

        Component destinationStatus = MessageUtil.create(langConfig.getDestinationChangeInfo(), changeDestinationPlaceholders);

        audience.sendMessage(destinationStatus);

        return true;
    }

    private String getPortalState(String status) {
        return langConfig.getDestinationChangeStates().get("disable".equals(status) ? 0 : 1);
    }

    private String getPortalName(String dimension) {
        return langConfig.getDestinationChangePortals().get("end".equals(dimension) ? 0 : 1);
    }
}