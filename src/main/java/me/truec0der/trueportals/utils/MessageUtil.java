package me.truec0der.trueportals.utils;

import me.truec0der.trueportals.models.MessagesModel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Map;

public class MessageUtil {
    private MessagesModel messagesModel;
    private MiniMessage miniMessage;

    public MessageUtil(MessagesModel messagesModel) {
        this.messagesModel = messagesModel;
        this.miniMessage = MiniMessage.miniMessage();
    }

    public Component create(String message) {
        String prefix = messagesModel.getPrefix();

        if (message == null) return Component.empty();

        return miniMessage.deserialize(message.replaceAll("%prefix%", prefix));
    }

    public Component create(String message, Map<String, String> placeholders) {
        String prefix = messagesModel.getPrefix();

        if (message == null) return Component.empty();

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String placeholder = "%" + entry.getKey() + "%";
            String replacement = entry.getValue();
            message = message.replace(placeholder, replacement);
        }

        return miniMessage.deserialize(message.replaceAll("%prefix%", prefix));
    }
}
