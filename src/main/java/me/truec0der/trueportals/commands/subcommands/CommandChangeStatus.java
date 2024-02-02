package me.truec0der.trueportals.commands.subcommands;

import me.truec0der.trueportals.managers.SettingsManager;
import me.truec0der.trueportals.models.ConfigModel;
import me.truec0der.trueportals.models.MessagesModel;
import me.truec0der.trueportals.utils.MessageUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandChangeStatus {
    private MessagesModel messagesModel;
    private ConfigModel configModel;
    private MessageUtil messageUtil;
    private SettingsManager configManager;

    public CommandChangeStatus(MessagesModel messagesModel, ConfigModel configModel, MessageUtil messageUtil, SettingsManager configManager) {
        this.messagesModel = messagesModel;
        this.configModel = configModel;
        this.messageUtil = messageUtil;
        this.configManager = configManager;
    }

    public boolean execute(Audience sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(messageUtil.create(messagesModel.getNeedCorrectArgs()));
            return true;
        }

        String dimension = args[1].toLowerCase();
        String status = args[2].toLowerCase();

        if (!List.of("end", "nether").contains(dimension) || !List.of("enable", "disable").contains(status)) {
            sender.sendMessage(messageUtil.create(messagesModel.getNeedCorrectArgs()));
            return true;
        }

        Map<String, String> changeStatusPlaceholders = new HashMap<>();
        changeStatusPlaceholders.put("portal_status", getPortalState(status));
        changeStatusPlaceholders.put("portal_name", getPortalName(dimension));

        configManager.getSettings().set("portals." + dimension, "enable".equals(status));
        configManager.save();

        configModel.reload();
        configManager.reload();

        Component changeStatus = messageUtil.create(messagesModel.getStatusChangeInfo(), changeStatusPlaceholders);

        sender.sendMessage(changeStatus);

        return true;
    }

    private String getPortalState(String status) {
        return messagesModel.getStatusChangeStates().get("disable".equals(status) ? 0 : 1);
    }

    private String getPortalName(String dimension) {
        return messagesModel.getStatusChangePortals().get("end".equals(dimension) ? 0 : 1);
    }
}
