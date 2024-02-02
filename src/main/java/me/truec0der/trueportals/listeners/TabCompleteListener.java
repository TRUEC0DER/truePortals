package me.truec0der.trueportals.listeners;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TabCompleteListener implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("trueportals.commands")) return Collections.emptyList();

        return switch (args.length) {
            case 1 -> filterArguments(args[0], "reload", "status", "help");
            case 2 -> filterArguments(args[1], "nether", "end");
            case 3 -> filterArguments(args[2], "enable", "disable");
            default -> Collections.emptyList();
        };
    }

    private List<String> filterArguments(String input, String... options) {
        return Arrays.stream(options)
                .filter(option -> option.startsWith(input.toLowerCase()))
                .collect(Collectors.toList());
    }
}
