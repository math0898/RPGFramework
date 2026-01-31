package io.github.math0898.rpgframework.commands;

import io.github.math0898.rpgframework.Rarity;
import io.github.math0898.utils.StringUtils;
import io.github.math0898.utils.commands.BetterCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import sugaku.rpg.framework.players.PlayerManager;
import io.github.math0898.rpgframework.RpgPlayer;

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
            } else if (args[0].equalsIgnoreCase("spawn")) {
                Zombie zombie = (Zombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
                zombie.setCustomNameVisible(true);
                zombie.setCustomName(StringUtils.convertHexCodes(args[1]));
            } else if (args[0].equalsIgnoreCase("rarity")) {
                for (Rarity r : Rarity.values()) {
                    Zombie zombie = (Zombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
                    zombie.setCustomNameVisible(true);
                    zombie.setCustomName(StringUtils.convertHexCodes(r.modify(r.toString())));
                }
            } else if (args[0].equalsIgnoreCase("xp")) {
                RpgPlayer rpgPlayer = PlayerManager.getPlayer(player.getUniqueId());
                send(player, "You have " + rpgPlayer.getExperience() + " xp.");
                send(player, "You are level " + rpgPlayer.getLevel());
                rpgPlayer.giveExperience(100);
                send(player, "Awarded 100 xp");
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
        List<String> toReturn = new ArrayList<>();
        if (args.length <= 1) toReturn.addAll(Arrays.asList("reset-cooldowns", "item-details", "regex", "spawn", "xp", "rarity"));
        return everythingStartsWith(toReturn, args[args.length - 1]);
    }
}
