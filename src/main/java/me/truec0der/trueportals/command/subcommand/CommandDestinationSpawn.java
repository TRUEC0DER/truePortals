package me.truec0der.trueportals.command.subcommand;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.truec0der.trueportals.command.ICommand;
import me.truec0der.trueportals.config.configs.MainConfig;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommandDestinationSpawn implements ICommand {
    MainConfig mainConfig;

    @Override
    public String getName() {
        return "destination";
    }

    @Override
    public String getRegex() {
        return "^(end|nether)\\s+(spawn)$";
    }

    @Override
    public String[] getCompleteArgs() {
        return new String[]{"end|nether", "spawn"};
    }

    @Override
    public String getPermission() {
        return "trueportals.commands.destination.spawn";
    }

    @Override
    public boolean isConsoleCan() {
        return false;
    }

    public boolean execute(CommandSender sender, Audience audience, String[] args) {
        Player player = (Player) sender;

        String dimension = args[0].toLowerCase();

        ConfigurationSection destinationSection = dimension.equals("end") ? mainConfig.getDestinationsEnd() : mainConfig.getDestinationsNether();
        String destinationWorld = destinationSection.getString("world");
        List<Double> destinationCoords = destinationSection.getDoubleList("coords");

        if (Bukkit.getWorld(destinationWorld) != null) {
            player.teleport(new Location(Bukkit.getWorld(destinationWorld), destinationCoords.get(0), destinationCoords.get(1), destinationCoords.get(2), destinationCoords.get(3).floatValue(), destinationCoords.get(4).floatValue()));
        }

        return true;
    }
}