package io.github.math0898.rpgframework.commands;

import io.github.math0898.utils.Utils;
import io.github.math0898.utils.commands.BetterCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The dungeon create command is used to export regions of a Minecraft server into a yaml format that defines tiles in
 * dungeons.
 * // TODO: More user friendly please.
 * @author Sugaku
 */
public class DungeonCreateCommand extends BetterCommand {

    /**
     * Creates a new BetterCommand with the given name.
     */
    public DungeonCreateCommand () {
        super("rpg-dungo");
    }

    /**
     * Attempts to export the given region in a YAML format for use with dungeons.
     *
     * @param locale The first corner location.
     * @param locale2 The second corner location.
     */
    private void exportDelegate (Location locale, Location locale2) {
        StringBuilder builder = new StringBuilder();
        builder.append("Unnamed:\n");
        // todo
//        builder.append("  footprint:\n");
//        builder.append("    corner1:\n");
//        builder.append("");
        AtomicInteger num = new AtomicInteger();
        Map<Material, List<String>> blocks = new HashMap<>();
        for (int i = locale.getBlockX(); i < locale2.getBlockX(); i++)
            for (int j = locale.getBlockY(); j < locale2.getBlockY(); j++)
                for (int k = locale.getBlockZ(); k < locale2.getBlockZ(); k++) {
                    Material key = locale.getWorld().getType(i, j, k);
                    if (key.isAir()) continue;
                    if (!blocks.containsKey(key))
                        blocks.put(key, new ArrayList<>());
                    blocks.get(key).add("      " + num.getAndIncrement() + ":\n        x: " + (i - locale.getBlockX()) +
                            "\n        y: " + (j - locale.getBlockY()) +
                            "\n        z: " + (k - locale.getBlockZ()) + "\n");
                }
        blocks.forEach((m, list) -> {
            builder.append("    " + m + ": \n");
            for (String s : list)
                builder.append(s);
        });
        try (FileWriter writer = new FileWriter("./exportZone.yaml")) {
            writer.write(builder.toString());
        } catch (Exception ignored) { }
        send(Bukkit.getConsoleSender(), "Finished zone export.");
    }

    /**
     * Called whenever specifically a player executes this command.
     *
     * @param player The player who ran this command.
     * @param args   The arguments they passed to the command.
     */
    @Override
    public boolean onPlayerCommand (Player player, String[] args) {
        // /rpg-dungo -570 65 -490 -520 70 -490
        if (args.length < 6) send(player, ChatColor.RED + "Not enough args.");
        final Location l1 = new Location(player.getWorld(),
                Math.min(getIntegerParam(0, args, player), getIntegerParam(3, args, player)),
                Math.min(getIntegerParam(1, args, player), getIntegerParam(4, args, player)),
                Math.min(getIntegerParam(2, args, player), getIntegerParam(5, args, player)));
        final Location l2 = new Location(player.getWorld(),
                Math.max(getIntegerParam(0, args, player), getIntegerParam(3, args, player)),
                Math.max(getIntegerParam(1, args, player), getIntegerParam(4, args, player)),
                Math.max(getIntegerParam(2, args, player), getIntegerParam(5, args, player)));
        Bukkit.getScheduler().runTaskAsynchronously(Utils.getPlugin(), () -> exportDelegate(l1, l2));
        send(player, ChatColor.GREEN + "Starting zone export.");
        return true;
    }

    /**
     * Called whenever an unspecified sender executes this command. This could include console and command blocks.
     *
     * @param sender The sender who ran this command.
     * @param args   The arguments they passed to the command.
     */
    @Override
    public boolean onNonPlayerCommand (CommandSender sender, String[] args) {
        send(sender, ChatColor.RED + "This command must be ran as a player.");
        return true;
    }

    /**
     * Called whenever a command sender is trying to tab complete a command.
     *
     * @param sender The sender who is tab completing this command.
     * @param args   The current arguments they have typed.
     */
    @Override
    public List<String> simplifiedTab (CommandSender sender, String[] args) {
        return switch (args.length) {
            case 1 -> List.of("<x1>");
            case 2 -> List.of("<y1>");
            case 3 -> List.of("<z1>");
            case 4 -> List.of("<x2>");
            case 5 -> List.of("<y2>");
            case 6 -> List.of("<z2>");
            default -> List.of();
        };
    }
}
