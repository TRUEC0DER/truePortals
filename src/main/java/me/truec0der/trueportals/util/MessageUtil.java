package me.truec0der.trueportals.util;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Map;

@UtilityClass
public class MessageUtil {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public Component create(String message) {
        if (message == null) return Component.empty();

        return miniMessage.deserialize(message);
    }

    public Component create(String message, Map<String, String> placeholders) {
        if (message == null) return Component.empty();

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String placeholder = "%" + entry.getKey() + "%";
            String replacement = entry.getValue();
            message = message.replace(placeholder, replacement);
        }

        return miniMessage.deserialize(message);
    }

    public String serialize(String message) {
        LegacyComponentSerializer serializer = LegacyComponentSerializer.builder().hexColors().build();
        return serializer.serialize(create(message));
    }

    public String serialize(String message, Map<String, String> placeholders) {
        LegacyComponentSerializer serializer = LegacyComponentSerializer.builder().hexColors().build();
        return serializer.serialize(create(message, placeholders));
    }
}
