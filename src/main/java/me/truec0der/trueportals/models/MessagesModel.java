package me.truec0der.trueportals.models;

import lombok.Getter;
import me.truec0der.trueportals.managers.SettingsManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class MessagesModel {
    private final SettingsManager messagesManager;

    @Getter
    private String prefix;
    @Getter
    private String notPerms;
    @Getter
    private String needCorrectArgs;

    @Getter
    private String helpInfo;

    @Getter
    private String statusInfo;
    @Getter
    private List<String> statusInfoStates;

    @Getter
    private String statusChangeInfo;
    @Getter
    private List<String> statusChangePortals;
    @Getter
    private List<String> statusChangeStates;

    @Getter
    private String statusCanNotUseEndTitle;
    @Getter
    private String statusCanNotUseEndSubtitle;
    @Getter
    private String statusCanNotUseNetherTitle;
    @Getter
    private String statusCanNotUseNetherSubtitle;

    @Getter
    private String reloadInfo;

    public MessagesModel(SettingsManager messagesManager) {
        this.messagesManager = messagesManager;
        reload();
    }

    public void reload() {
        YamlConfiguration messages = messagesManager.getSettings();

        this.prefix = messages.getString("prefix");
        this.notPerms = messages.getString("not-perms");
        this.needCorrectArgs = messages.getString("need-correct-args");

        this.helpInfo = messages.getString("help.info");

        this.statusInfo = messages.getString("status.info.info");
        this.statusInfoStates = messages.getStringList("status.info.states");

        this.statusChangeInfo = messages.getString("status.change.info");
        this.statusChangePortals = messages.getStringList("status.change.portals");
        this.statusChangeStates = messages.getStringList("status.change.states");

        this.statusCanNotUseEndTitle = messages.getString("status.can-not-use.end.title");
        this.statusCanNotUseEndSubtitle = messages.getString("status.can-not-use.end.subtitle");
        this.statusCanNotUseNetherTitle = messages.getString("status.can-not-use.nether.title");
        this.statusCanNotUseNetherSubtitle = messages.getString("status.can-not-use.nether.subtitle");

        this.reloadInfo = messages.getString("reload.info");
    }
}
