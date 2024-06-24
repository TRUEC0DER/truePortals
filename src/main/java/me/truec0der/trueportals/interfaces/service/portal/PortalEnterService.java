package me.truec0der.trueportals.interfaces.service.portal;

import net.kyori.adventure.audience.Audience;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public interface PortalEnterService {
    boolean handlePortalTeleport(Player player, World.Environment fromWorld, World.Environment toWorld);

    boolean handlePortalTeleport(Entity player, World.Environment fromWorld, World.Environment toWorld);

    boolean changeWorldAttempt(Entity entity, World.Environment toWorld);

    void teleportToWorld(Entity entity, String toWorld, List<Double> coords);

    void sendTitle(Audience audience, World.Environment toWorld);

    BukkitTask startTitleScheduler();
}
