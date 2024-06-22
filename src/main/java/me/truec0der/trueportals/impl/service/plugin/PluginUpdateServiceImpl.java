package me.truec0der.trueportals.impl.service.plugin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.truec0der.trueportals.config.configs.LangConfig;
import me.truec0der.trueportals.entity.PluginVersionEntity;
import me.truec0der.trueportals.interfaces.service.plugin.PluginUpdateService;
import me.truec0der.trueportals.util.MessageUtil;
import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PluginUpdateServiceImpl implements PluginUpdateService {
    String apiUrl;
    String destinationPath;
    String destinationName;
    LangConfig langConfig;

    private JSONArray getJsonVersions() {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) return null;

            return ((JSONArray) new JSONParser().parse(response.body()));
        } catch (IOException | ParseException | InterruptedException e) {
            return null;
        }
    }

    public List<PluginVersionEntity> getVersions() {
        JSONArray jsonVersions = getJsonVersions();

        if (jsonVersions == null) return null;

        List<PluginVersionEntity> versionList = new ArrayList<>();
        jsonVersions.forEach(jsonObject -> versionList.add(PluginVersionEntity.toEntity((JSONObject) jsonObject)));

        return versionList;
    }

    @Override
    public PluginVersionEntity getLastVersion() {
        List<PluginVersionEntity> versions = getVersions();

        if (versions == null || versions.isEmpty()) return null;

        return versions.get(versions.size() - 1);
    }

    @Override
    public boolean isLastVersion(String currentVersion) {
        PluginVersionEntity pluginVersionEntity = getLastVersion();

        if (pluginVersionEntity == null) return false;

        return pluginVersionEntity.getVersion().equals(currentVersion);
    }

    @Override
    public void handleCheck(String currentVersion, boolean canUpdate) {
        boolean isLastVersion = isLastVersion(currentVersion);
        if (isLastVersion) return;

        PluginVersionEntity lastVersion = getLastVersion();

        if (lastVersion == null) {
            Bukkit.getConsoleSender().sendMessage(MessageUtil.serialize(langConfig.getUpdateNotifyFailed()));
            return;
        }

        handleNotify(lastVersion, currentVersion);

        if (canUpdate) handleUpdate(lastVersion, lastVersion.getUrl(), destinationPath, destinationName);
    }

    @Override
    public void handleNotify(PluginVersionEntity entity, String currentVersion) {
        List<String> notifyComponents = new ArrayList<>(Arrays.asList(langConfig.getUpdateNotify().split("\n")));
        List<String> versionInfo = entity.getInfo().stream()
                .map(info -> langConfig.getUpdateVersionInfoLine().replace("%version_info_line%", info))
                .collect(Collectors.toList());

        Map<String, String> placeholders = Map.of(
                "current_version", currentVersion,
                "new_version", entity.getVersion(),
                "url", entity.getUrl()
        );

        replaceLineInList(notifyComponents, versionInfo, "%version_info%");
        List<String> resultText = notifyComponents.stream()
                .map(component -> MessageUtil.serialize(component, placeholders))
                .collect(Collectors.toList());

        resultText.forEach(Bukkit.getConsoleSender()::sendMessage);
    }

    private void replaceLineInList(List<String> originalList, List<String> replacementList, String searchText) {
        Optional<Integer> indexOpt = originalList.stream()
                .filter(s -> s.contains(searchText))
                .findFirst()
                .map(originalList::indexOf);

        indexOpt.ifPresent(index -> {
            originalList.remove((int) index);
            originalList.addAll(index, replacementList);
        });
    }

    @Override
    public void handleUpdate(PluginVersionEntity entity, String fileUrl, String destinationPath, String destinationName) {
        boolean replaceFileAttempt = replaceFileAttempt(fileUrl, destinationPath, destinationName);

        if (!replaceFileAttempt) {
            Bukkit.getConsoleSender().sendMessage(MessageUtil.serialize(langConfig.getUpdateActionFailed()));
            return;
        }

        Bukkit.getConsoleSender().sendMessage(MessageUtil.serialize(langConfig.getUpdateAction()));
    }

    private boolean replaceFileAttempt(String fileUrl, String destinationPath, String destinationName) {
        String fileName = Paths.get(fileUrl).getFileName().toString();
        Path destinationDir = Paths.get(destinationPath);

        try (Stream<Path> files = Files.walk(destinationDir)) {
            files.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().contains(destinationName))
                    .forEach(this::deleteFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return downloadFile(fileUrl, destinationDir.resolve(fileName));
    }

    private void deleteFile(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean downloadFile(String fileUrl, Path destination) {
        try (InputStream in = new URL(fileUrl).openStream()) {
            Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
