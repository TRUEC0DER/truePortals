package me.truec0der.trueportals.commands.subcommands;

import me.truec0der.trueportals.TruePortals;
import me.truec0der.trueportals.models.MessagesModel;
import me.truec0der.trueportals.utils.MessageUtil;
import net.kyori.adventure.audience.Audience;

public class CommandReload {
    private MessagesModel messagesModel;
    private MessageUtil messageUtil;

    public CommandReload(MessagesModel messagesModel, MessageUtil messageUtil) {
        this.messagesModel = messagesModel;
        this.messageUtil = messageUtil;
    }

    public boolean execute(Audience sender) {
        sender.sendMessage(messageUtil.create(messagesModel.getReloadInfo()));

        TruePortals.reloadPlugin();

        return true;
    }
}
