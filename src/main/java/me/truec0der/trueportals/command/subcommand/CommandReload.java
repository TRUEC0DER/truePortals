package me.truec0der.trueportals.command.subcommand;

import me.truec0der.trueportals.TruePortals;
import me.truec0der.trueportals.command.ICommand;
import me.truec0der.trueportals.facade.MessagesFacade;
import me.truec0der.trueportals.model.MessagesModel;
import me.truec0der.trueportals.util.MessageUtil;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;

public class CommandReload implements ICommand {
    private final MessagesModel messagesModel;
    private final MessageUtil messageUtil;

    public CommandReload(MessagesFacade messagesFacade, MessageUtil messageUtil) {
        this.messagesModel = messagesFacade.getMessagesModel();
        this.messageUtil = messageUtil;
    }

    @Override
    public String getName() {
        return "reload";
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
        return "trueportals.commands.reload";
    }

    @Override
    public boolean isConsoleCan() {
        return true;
    }

    public boolean execute(CommandSender sender, Audience audience, String[] args) {
        audience.sendMessage(messageUtil.create(messagesModel.getReloadInfo()));

        TruePortals.reloadPlugin();

        return true;
    }
}
