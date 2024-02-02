package me.truec0der.trueportals.listeners;

import me.truec0der.trueportals.models.ConfigModel;
import me.truec0der.trueportals.models.MessagesModel;
import me.truec0der.trueportals.utils.MessageUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PortalListener implements Listener {
    private final JavaPlugin instance;
    private final BukkitAudiences adventure;
    private final MessagesModel messagesModel;
    private final ConfigModel configModel;
    private final MessageUtil messageUtil;
    private final Map<Player, Instant> playerList;
    private final Map<World.Environment, String> portalNames = Map.of(
            World.Environment.THE_END, "end",
            World.Environment.NETHER, "nether",
            World.Environment.NORMAL, "normal"
    );
    private BukkitTask playerTask;

    public PortalListener(JavaPlugin instance, BukkitAudiences adventure, MessagesModel messagesModel, ConfigModel configModel, MessageUtil messageUtil) {
        this.instance = instance;
        this.adventure = adventure;
        this.messagesModel = messagesModel;
        this.configModel = configModel;
        this.messageUtil = messageUtil;
        this.playerList = new HashMap<>();
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) throws InterruptedException {
        Player player = event.getPlayer();

        if (!configModel.isWorksPlayers() || event.getPlayer().hasPermission("trueportals.portals")) return;

        World.Environment toWorld = event.getTo().getWorld().getEnvironment();
        World.Environment fromWorld = event.getFrom().getWorld().getEnvironment();

        Audience audience = adventure.player(event.getPlayer());

        if ((!configModel.isPortalsEnd() && toWorld == World.Environment.THE_END) ||
                (!configModel.isPortalsNether() && (toWorld == World.Environment.NETHER || fromWorld == World.Environment.NETHER))) {
            event.setCancelled(true);

            if (playerList.isEmpty()) {
                playerTask = startScheduler();
            }

            if (playerList.get(event.getPlayer()) == null) {
                sendTitle(audience, toWorld);
            }

            playerList.put(player, Instant.now());
        }
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
        if (!configModel.isWorksEntities()) return;

        World.Environment toWorld = event.getTo().getWorld().getEnvironment();

        if ((!configModel.isPortalsEnd() && toWorld == World.Environment.THE_END) ||
                (!configModel.isPortalsNether() && toWorld == World.Environment.NETHER)) {
            event.setCancelled(true);
        }
    }

    private void sendTitle(Audience audience, World.Environment toWorld) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("portal_name", portalNames.get(toWorld));

        String titleMessage = toWorld == World.Environment.THE_END ? messagesModel.getStatusCanNotUseEndTitle() : messagesModel.getStatusCanNotUseNetherTitle();
        String subtitleMessage = toWorld == World.Environment.THE_END ? messagesModel.getStatusCanNotUseEndSubtitle() : messagesModel.getStatusCanNotUseNetherSubtitle();

        Component title = messageUtil.create(titleMessage, placeholders);
        Component subtitle = messageUtil.create(subtitleMessage, placeholders);

        Title.Times times = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(3000), Duration.ofMillis(1000));
        Title titleObj = Title.title(title, subtitle, times);

        audience.showTitle(titleObj);
    }

    private BukkitTask startScheduler() {
        int timeInSeconds = 5;
        long timeInMilliseconds = TimeUnit.SECONDS.toMillis(timeInSeconds);

        return Bukkit.getScheduler().runTaskTimer(instance, () -> {
            Instant currentTime = Instant.now();
            playerList.entrySet().removeIf(entry -> entry.getValue().isBefore(currentTime.plusMillis(timeInMilliseconds)));

            if (playerList.isEmpty() && playerTask != null) {
                Bukkit.getScheduler().cancelTask(playerTask.getTaskId());
            }
        }, timeInSeconds * 20L, timeInSeconds * 20L);
    }
}
