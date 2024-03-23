package me.truec0der.trueportals.facade;

import lombok.Getter;
import lombok.Setter;
import me.truec0der.trueportals.TruePortals;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

@Getter
public class PluginFacade {
    TruePortals instance;
    @Setter
    BukkitAudiences adventure;

    public PluginFacade(TruePortals instance, BukkitAudiences adventure) {
        this.instance = instance;
        this.adventure = adventure;
    }
}
