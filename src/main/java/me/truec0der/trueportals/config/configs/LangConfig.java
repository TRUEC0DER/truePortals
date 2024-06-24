package me.truec0der.trueportals.config.configs;

import lombok.Getter;
import me.truec0der.trueportals.config.ConfigHolder;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

@Getter
public class LangConfig extends ConfigHolder {
    private String prefix;
    private String notPerms;
    private String needCorrectArgs;
    private String onlyPlayer;
    private String updateNotify;
    private String updateNotifyFailed;
    private String updateVersionInfoLine;
    private String updateAction;
    private String updateActionFailed;
    private String helpInfo;
    private String statusInfo;
    private List<String> statusInfoStates;
    private String statusChangeInfo;
    private List<String> statusChangePortals;
    private List<String> statusChangeStates;
    private String destinationChangeInfo;
    private List<String> destinationChangePortals;
    private List<String> destinationChangeStates;
    private String activationChangeInfo;
    private List<String> activationChangePortals;
    private List<String> activationChangeStates;
    private String destinationSetSpawnInfo;
    private List<String> destinationSetSpawnPortals;
    private String statusCanNotUseEndTitle;
    private String statusCanNotUseEndSubtitle;
    private String statusCanNotUseNetherTitle;
    private String statusCanNotUseNetherSubtitle;
    private String reloadInfo;

    public LangConfig(Plugin plugin, File filePath, String file, String defaultFile) {
        super(plugin, filePath, file, defaultFile);
        loadAndSave();
        init();
    }

    @Override
    public void init() {
        YamlConfiguration config = this.getConfig();

        prefix = config.getString("prefix");
        notPerms = config.getString("not-perms");
        needCorrectArgs = config.getString("need-correct-args");
        onlyPlayer = config.getString("only-player");
        updateNotify = config.getString("update.notify.info");
        updateNotifyFailed = config.getString("update.notify.failed");
        updateVersionInfoLine = config.getString("update.version.info.line");
        updateAction = config.getString("update.action.info");
        updateActionFailed = config.getString("update.action.failed");

        helpInfo = config.getString("help.info");

        statusInfo = config.getString("status.info.info");
        statusInfoStates = config.getStringList("status.info.states");

        statusChangeInfo = config.getString("status.change.info");
        statusChangePortals = config.getStringList("status.change.portals");
        statusChangeStates = config.getStringList("status.change.states");

        activationChangeInfo = config.getString("activation.change.info");
        activationChangePortals = config.getStringList("activation.change.portals");
        activationChangeStates = config.getStringList("activation.change.states");

        destinationChangeInfo = config.getString("destination.change.info");
        destinationChangePortals = config.getStringList("destination.change.portals");
        destinationChangeStates = config.getStringList("destination.change.states");

        destinationSetSpawnInfo = config.getString("destination.setspawn.info");
        destinationSetSpawnPortals = config.getStringList("destination.setspawn.portals");

        statusCanNotUseEndTitle = config.getString("status.can-not-use.end.title");
        statusCanNotUseEndSubtitle = config.getString("status.can-not-use.end.subtitle");
        statusCanNotUseNetherTitle = config.getString("status.can-not-use.nether.title");
        statusCanNotUseNetherSubtitle = config.getString("status.can-not-use.nether.subtitle");

        reloadInfo = config.getString("reload.info");
    }
}
