package io.github.math0898.rpgframework.commands;

import io.github.math0898.rpgframework.Rarity;
import io.github.math0898.rpgframework.enemies.CustomMobEntry;
import io.github.math0898.rpgframework.enemies.MobManager;
import io.github.math0898.utils.StringUtils;
import io.github.math0898.utils.commands.BetterCommand;
import io.github.math0898.utils.items.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
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
            } else if (args[0].equalsIgnoreCase("skulls")) {
                ItemBuilder builder = new ItemBuilder(Material.PLAYER_HEAD);
                builder.setSkullSkinBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzM0NTJiOThhYjlhODhkMTc1N2YwMzJjMDcyYWY4MWNmYTM1ZGRiNDc5NDU4NTkxNDc4MTFiY2RjZmQ5ODcxZSJ9fX0=");
                player.getInventory().addItem(builder.build());
                send(player, "Here's a knight helmet!");
            } else if (args[0].equalsIgnoreCase("spawnCustom")) {
                CustomMobEntry mob = MobManager.getInstance().getCustomMob(args[1]);
                mob.spawn(player.getLocation());
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
        if (args.length <= 1) toReturn.addAll(Arrays.asList("reset-cooldowns", "item-details", "regex", "spawn", "rarity", "skulls", "spawnCustom"));
        else if (args.length == 2 && args[0].equalsIgnoreCase("spawnCustom")) toReturn.addAll(MobManager.getInstance().getCustomMobNameList());
        return everythingStartsWith(toReturn, args[args.length - 1]);
    }
}
