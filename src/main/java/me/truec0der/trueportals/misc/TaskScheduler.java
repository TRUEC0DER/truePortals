package me.truec0der.trueportals.misc;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskScheduler {
    Plugin plugin;

    public BukkitTask runTaskTimer(Runnable runnable, long delay, long period) {
        return Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period);
    }
}
