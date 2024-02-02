package me.truec0der.trueportals.commands.subcommands;

import me.truec0der.trueportals.models.ConfigModel;
import me.truec0der.trueportals.models.MessagesModel;
import me.truec0der.trueportals.utils.MessageUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import java.util.HashMap;
import java.util.Map;

public class CommandStatus {
    private MessagesModel messagesModel;
    private ConfigModel configModel;
    private MessageUtil messageUtil;

    public CommandStatus(MessagesModel messagesModel, ConfigModel configModel, MessageUtil messageUtil) {
        this.messagesModel = messagesModel;
        this.configModel = configModel;
        this.messageUtil = messageUtil;
    }

    public boolean execute(Audience sender) {
        Map<String, String> statusPlaceholders = new HashMap<>();
        statusPlaceholders.put("end_portal_status", getPortalState(configModel.isPortalsEnd()));
        statusPlaceholders.put("nether_portal_status", getPortalState(configModel.isPortalsNether()));

        Component status = messageUtil.create(messagesModel.getStatusInfo(), statusPlaceholders);

        sender.sendMessage(status);

        return true;
    }

    public String getPortalState(boolean state) {
        return messagesModel.getStatusInfoStates().get(!state ? 0 : 1);
    }
}
