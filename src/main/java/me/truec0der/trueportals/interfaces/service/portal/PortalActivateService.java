package me.truec0der.trueportals.interfaces.service.portal;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public interface PortalActivateService {
    boolean handleEndPortal(Player player, Action action, Block clickedBlock, ItemStack usedItem);

    boolean handleNetherPortal();
}
