package me.truec0der.trueportals.facade;

import lombok.Getter;
import me.truec0der.trueportals.manager.SettingsManager;
import me.truec0der.trueportals.model.MessagesModel;

@Getter
public class MessagesFacade {
    private final SettingsManager messagesManager;
    private final MessagesModel messagesModel;

    public MessagesFacade(SettingsManager messagesManager, MessagesModel messagesModel) {
        this.messagesManager = messagesManager;
        this.messagesModel = messagesModel;
    }
}
