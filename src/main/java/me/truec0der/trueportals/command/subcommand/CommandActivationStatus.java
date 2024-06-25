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
public class CommandActivationStatus implements ICommand {
    MainConfig mainConfig;
    LangConfig langConfig;

    @Override
    public String getName() {
        return "activation";
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
        return "trueportals.commands.activation.status";
    }

    @Override
    public boolean isConsoleCan() {
        return true;
    }

    public boolean execute(CommandSender sender, Audience audience, String[] args) {
        String dimension = args[0].toLowerCase();
        String status = args[1].toLowerCase();

        Map<String, String> changeActivationPlaceholders = new HashMap<>();
        changeActivationPlaceholders.put("activation_status", getActivationState(status));
        changeActivationPlaceholders.put("portal_name", getPortalName(dimension));

        mainConfig.getConfig().set("activation." + dimension, "enable".equals(status));
        mainConfig.save();

        mainConfig.reload();

        Component changeStatus = MessageUtil.create(langConfig.getActivationChangeInfo(), changeActivationPlaceholders);

        audience.sendMessage(changeStatus);

        return true;
    }

    private String getActivationState(String status) {
        return langConfig.getActivationChangeStates().get("disable".equals(status) ? 0 : 1);
    }

    private String getPortalName(String dimension) {
        return langConfig.getActivationChangePortals().get("end".equals(dimension) ? 0 : 1);
    }
}
