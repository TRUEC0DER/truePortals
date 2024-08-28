package me.truec0der.trueportals.interfaces.service.plugin;

import me.truec0der.trueportals.entity.PluginVersionEntity;

import java.util.List;

public interface PluginUpdateService {
    List<PluginVersionEntity> getVersions();

    PluginVersionEntity getLastVersion();

    boolean isLastVersion(String currentVersion);

    void handleCheck(String currentVersion, boolean canUpdate);

    void handleNotify(PluginVersionEntity entity, String currentVersion);

    void handleUpdate(PluginVersionEntity entity, String fileUrl, String destinationPath, String destinationName);
}
