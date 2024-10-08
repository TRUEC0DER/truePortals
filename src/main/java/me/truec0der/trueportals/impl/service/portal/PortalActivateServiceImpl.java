package me.truec0der.trueportals.impl.service.portal;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.truec0der.trueportals.config.configs.LangConfig;
import me.truec0der.trueportals.config.configs.MainConfig;
import me.truec0der.trueportals.interfaces.service.portal.PortalActivateService;
import me.truec0der.trueportals.util.MessageUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PortalActivateServiceImpl implements PortalActivateService {
    BukkitAudiences adventure;
    MainConfig mainConfig;
    LangConfig langConfig;

    @Override
    public boolean handleEndPortal(Player player, Action action, Block clickedBlock, ItemStack usedItem) {
        if (!isValidInteraction(action, clickedBlock, usedItem)) return false;

        if (!mainConfig.isActivationEnd()) {
            Audience audience = adventure.player(player);
            String canNotActivate = langConfig.getActivationCanNotEnd();
            if (canNotActivate != null && !canNotActivate.isEmpty()) audience.sendMessage(MessageUtil.create(canNotActivate));
        }

        return !mainConfig.isActivationEnd();
    }

    @Override
    public boolean handleNetherPortal() {
        return !mainConfig.isActivationNether();
    }

    private boolean isValidInteraction(Action action, Block clickedBlock, ItemStack usedItem) {
        if (clickedBlock == null || usedItem == null) return false;
        return action == Action.RIGHT_CLICK_BLOCK && clickedBlock.getType().name().startsWith("END_PORTAL_FRAME") && usedItem.getType().name().startsWith("ENDER_EYE");
    }
}
