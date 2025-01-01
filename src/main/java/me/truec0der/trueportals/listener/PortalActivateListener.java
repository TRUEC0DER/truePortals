package me.truec0der.trueportals.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.truec0der.trueportals.interfaces.service.portal.PortalActivateService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.PortalCreateEvent;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PortalActivateListener implements Listener {
    PortalActivateService portalActivateService;

    @EventHandler
    private void onPlayerActivateEndPortal(PlayerInteractEvent event) {
        if (event.isCancelled()) return;
        event.setCancelled(portalActivateService.handleEndPortal(event.getPlayer(), event.getAction(), event.getClickedBlock(), event.getItem()));
    }

    @EventHandler
    private void onActivateNetherPortal(PortalCreateEvent event) {
        if (event.isCancelled()) return;
        if (event.getReason() != PortalCreateEvent.CreateReason.FIRE) return;
        event.setCancelled(portalActivateService.handleNetherPortal());
    }
}
