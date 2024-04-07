package io.github.math0898.rpgframework.commands;

import io.github.math0898.utils.commands.BetterCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sugaku.rpg.framework.players.PlayerManager;
import sugaku.rpg.framework.players.RpgPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The debug command includes a number of utility methods to aid in debugging.
 *
 * @author Sugaku
 */
public class DebugCommand extends BetterCommand {

    /**
     * Creates a new BetterCommand with the given name.
     */
    public DebugCommand () {
        super("rpg-debug");
    }

    /**
     * Called whenever specifically a player executes this command.
     *
     * @param player The player who ran this command.
     * @param args   The arguments they passed to the command.
     */
    @Override
    public boolean onPlayerCommand (Player player, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reset-cooldowns")) {
                RpgPlayer rpg = PlayerManager.getPlayer(player.getUniqueId());
                if (rpg == null) return true;
                rpg.resetCooldowns();
                send(player, ChatColor.GREEN + "Reset cooldowns.");
            } else if (args[0].equalsIgnoreCase("item-details")) {
                ItemStack item = player.getEquipment().getItemInMainHand();
                player.sendMessage(ChatColor.RESET + ": " + item.getItemMeta().getDisplayName());
                System.out.println(item.getItemMeta().getDisplayName());
            } else if (args[0].equalsIgnoreCase("regex")) {
                Pattern pattern = Pattern.compile(args[1]);
                Matcher matcher = pattern.matcher(args[3]);
                System.out.println(matcher.replaceAll(args[2]));
                player.sendMessage(matcher.replaceAll(args[2]));
            }
        }
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
        return new ArrayList<>(Arrays.asList("reset-cooldowns", "item-details", "regex"));
    }
}
