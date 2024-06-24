package me.truec0der.trueportals.impl.service.portal;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import me.truec0der.trueportals.config.configs.LangConfig;
import me.truec0der.trueportals.config.configs.MainConfig;
import me.truec0der.trueportals.interfaces.service.portal.PortalEnterService;
import me.truec0der.trueportals.misc.TaskScheduler;
import me.truec0der.trueportals.util.MessageUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.bukkit.World.Environment.THE_END;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PortalEnterServiceImpl implements PortalEnterService {
    BukkitAudiences adventure;
    TaskScheduler taskScheduler;
    MainConfig mainConfig;
    LangConfig langConfig;
    Map<Player, Instant> playerList = new HashMap<>();
    @NonFinal
    private BukkitTask playerTask = null;

    public boolean handlePortalTeleport(Player player, World.Environment fromWorld, World.Environment toWorld) {
        if (!mainConfig.isWorksPlayers()) return false;

        if (!player.hasPermission("trueportals.except.destination")) {
            if (changeWorldAttempt(player, toWorld)) return true;
        }

        if (!player.hasPermission("trueportals.except.portal")) {
            if (!isWorldValid(fromWorld, toWorld)) return false;

            if (playerList.isEmpty()) playerTask = startTitleScheduler();
            if (playerList.get(player) == null) sendTitle(adventure.player(player), toWorld);

            playerList.put(player, Instant.now());

            return true;
        }

        return false;
    }

    public boolean handlePortalTeleport(Entity entity, World.Environment fromWorld, World.Environment toWorld) {
        if (changeWorldAttempt(entity, toWorld)) return true;

        return isWorldValid(fromWorld, toWorld);
    }

    public boolean changeWorldAttempt(Entity entity, World.Environment toWorld) {
        ConfigurationSection portalSection = toWorld == THE_END ? mainConfig.getDestinationsEnd() : mainConfig.getDestinationsNether();

        boolean portalEnabled = portalSection.getBoolean("enabled");
        String portalWorld = portalSection.getString("world");

        List<Double> portalCoords = portalSection.getDoubleList("coords");

        if (!portalEnabled) return false;

        teleportToWorld(entity, portalWorld, portalCoords);
        return true;
    }

    public void teleportToWorld(Entity entity, String toWorld, List<Double> coords) {
        World world = Bukkit.getWorld(toWorld);

        if (world == null) return;

        Location destination = new Location(world, coords.get(0), coords.get(1), coords.get(2), coords.get(3).floatValue(), coords.get(4).floatValue());
        entity.teleport(destination);
    }

    public void sendTitle(Audience audience, World.Environment toWorld) {
        String titleMessage = toWorld == THE_END ? langConfig.getStatusCanNotUseEndTitle() : langConfig.getStatusCanNotUseNetherTitle();
        String subtitleMessage = toWorld == THE_END ? langConfig.getStatusCanNotUseEndSubtitle() : langConfig.getStatusCanNotUseNetherSubtitle();

        Component title = MessageUtil.create(titleMessage);
        Component subtitle = MessageUtil.create(subtitleMessage);

        Title.Times times = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(3000), Duration.ofMillis(1000));
        Title titleObj = Title.title(title, subtitle, times);

        audience.showTitle(titleObj);
    }

    public BukkitTask startTitleScheduler() {
        int timeInSeconds = 5;
        long timeInMilliseconds = TimeUnit.SECONDS.toMillis(timeInSeconds);

        return taskScheduler.runTaskTimer(() -> {
            Instant currentTime = Instant.now();
            playerList.entrySet().removeIf(entry -> entry.getValue().isBefore(currentTime.plusMillis(timeInMilliseconds)));

            if (playerList.isEmpty() && playerTask != null) {
                playerTask.cancel();
                playerTask = null;
            }
        }, timeInSeconds * 20L, timeInSeconds * 20L);
    }

    private boolean isWorldValid(World.Environment fromWorld, World.Environment toWorld) {
        return (!mainConfig.isPortalsEnd() && toWorld == THE_END) ||
                (!mainConfig.isPortalsNether() && (toWorld == World.Environment.NETHER || fromWorld == World.Environment.NETHER));
    }
}
