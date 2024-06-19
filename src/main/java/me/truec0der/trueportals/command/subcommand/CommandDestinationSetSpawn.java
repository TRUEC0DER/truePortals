package me.truec0der.trueportals.command.subcommand;

import me.truec0der.trueportals.command.ICommand;
import me.truec0der.trueportals.config.configs.LangConfig;
import me.truec0der.trueportals.config.configs.MainConfig;
import me.truec0der.trueportals.util.MessageUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CommandDestinationSetSpawn implements ICommand {
    private final MainConfig mainConfig;
    private final LangConfig langConfig;

    public CommandDestinationSetSpawn(MainConfig mainConfig, LangConfig langConfig) {
        this.mainConfig = mainConfig;
        this.langConfig = langConfig;
    }

    private double[] getCoords(Player player) {
        double playerCoordsX = player.getLocation().getX();
        double playerCoordsY = player.getLocation().getY();
        double playerCoordsZ = player.getLocation().getZ();
        double playerCoordsYaw = player.getLocation().getYaw();
        double playerCoordsPitch = player.getLocation().getPitch();

        return new double[]{
                roundCoords(playerCoordsX),
                roundCoords(playerCoordsY),
                roundCoords(playerCoordsZ),
                roundCoords(playerCoordsYaw),
                roundCoords(playerCoordsPitch)
        };
    }

    private double roundCoords(double coords) {
        return (double) Math.round(coords * 100) / 100;
    }

    @Override
    public String getName() {
        return "destination";
    }

    @Override
    public String getRegex() {
        return "^(end|nether)\\s+(setspawn)$";
    }

    @Override
    public String[] getCompleteArgs() {
        return new String[]{"end|nether", "setspawn"};
    }

    @Override
    public String getPermission() {
        return "trueportals.commands.destination.setspawn";
    }

    @Override
    public boolean isConsoleCan() {
        return false;
    }

    public boolean execute(CommandSender sender, Audience audience, String[] args) {
        Player player = (Player) sender;

        String playerWorld = player.getWorld().getName();
        double[] playerCoords = getCoords(player);

        String dimension = args[0].toLowerCase();

        Map<String, String> setSpawnDestinationPlaceholders = new HashMap<>();
        setSpawnDestinationPlaceholders.put("world_name", playerWorld);
        setSpawnDestinationPlaceholders.put("portal_name", getPortalName(dimension));
        setSpawnDestinationPlaceholders.put("coords_x", String.valueOf(playerCoords[0]));
        setSpawnDestinationPlaceholders.put("coords_y", String.valueOf(playerCoords[1]));
        setSpawnDestinationPlaceholders.put("coords_z", String.valueOf(playerCoords[2]));
        setSpawnDestinationPlaceholders.put("coords_yaw", String.valueOf(playerCoords[3]));
        setSpawnDestinationPlaceholders.put("coords_pitch", String.valueOf(playerCoords[4]));

        mainConfig.getConfig().set("destinations." + dimension + ".world", playerWorld);
        mainConfig.getConfig().set("destinations." + dimension + ".coords", playerCoords);
        mainConfig.save();

        mainConfig.reload();

        Component destinationStatus = MessageUtil.create(langConfig.getDestinationSetSpawnInfo(), setSpawnDestinationPlaceholders);

        audience.sendMessage(destinationStatus);

        return true;
    }

    private String getPortalName(String dimension) {
        return langConfig.getDestinationChangePortals().get("end".equals(dimension) ? 0 : 1);
    }
}