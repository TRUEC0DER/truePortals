package me.truec0der.trueportals.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.truec0der.trueportals.interfaces.service.portal.PortalService;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PortalListener implements Listener {
    private PortalService portalService;

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();

        World.Environment toWorld = event.getTo().getWorld().getEnvironment();
        World.Environment fromWorld = event.getFrom().getWorld().getEnvironment();

        boolean handlePortalTeleport = portalService.handlePortalTeleport(player, fromWorld, toWorld);

        event.setCancelled(handlePortalTeleport);
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
        Entity entity = event.getEntity();

        World.Environment toWorld = event.getTo().getWorld().getEnvironment();
        World.Environment fromWorld = event.getFrom().getWorld().getEnvironment();

        boolean handlePortalTeleport = portalService.handlePortalTeleport(entity, fromWorld, toWorld);

        event.setCancelled(handlePortalTeleport);
    }
}
