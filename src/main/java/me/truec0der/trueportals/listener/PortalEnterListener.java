package me.truec0der.trueportals.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.truec0der.trueportals.interfaces.service.portal.PortalEnterService;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PortalEnterListener implements Listener {
    private PortalEnterService portalEnterService;

    @EventHandler
    private void onPlayerPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();

        World.Environment toWorld = event.getTo().getWorld().getEnvironment();
        World.Environment fromWorld = event.getFrom().getWorld().getEnvironment();

        boolean handlePortalTeleport = portalEnterService.handlePortalTeleport(player, fromWorld, toWorld);

        event.setCancelled(handlePortalTeleport);
    }

    @EventHandler
    private void onEntityPortal(EntityPortalEvent event) {
        Entity entity = event.getEntity();

        World.Environment toWorld = event.getTo().getWorld().getEnvironment();
        World.Environment fromWorld = event.getFrom().getWorld().getEnvironment();

        boolean handlePortalTeleport = portalEnterService.handlePortalTeleport(entity, fromWorld, toWorld);

        event.setCancelled(handlePortalTeleport);
    }
}
