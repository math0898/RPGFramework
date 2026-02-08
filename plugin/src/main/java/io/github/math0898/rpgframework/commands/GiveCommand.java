package io.github.math0898.rpgframework.commands;

import io.github.math0898.rpgframework.items.ItemManager;
import io.github.math0898.utils.commands.BetterCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The give command is used to give items registered in the ItemManager. Requires admin permissions.
 *
 * @author Sugaku
 */
public class GiveCommand extends BetterCommand {

    /**
     * Creates a new BetterCommand with the given name.
     */
    public GiveCommand () {
        super("rpg-give", ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] ");
    }

    /**
     * Called whenever specifically a player executes this command.
     *
     * @param player The player who ran this command.
     * @param args   The arguments they passed to the command.
     */
    @Override
    public boolean onPlayerCommand (Player player, String[] args) {
        return onNonPlayerCommand(player, args);
    }

    /**
     * Called whenever an unspecified sender executes this command. This could include console and command blocks.
     *
     * @param sender The sender who ran this command.
     * @param args   The arguments they passed to the command.
     */
    @Override
    public boolean onNonPlayerCommand (CommandSender sender, String[] args) {
        if (args.length < 2) {
            send(sender, ChatColor.RED + "Something about that seemed off.");
            send(sender, ChatColor.GRAY + "- /rpg-give <target> <item> (amnt)", false);
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            send(sender, ChatColor.RED + "We could not the player " + args[0] + ".");
            return true;
        }
        if (!ItemManager.getInstance().hasItem(args[1])) {
            send(sender, ChatColor.RED + "We could not find " + args[1] + ".");
            return true;
        }
        int count = getIntegerParam(2, args, sender);
        for (int i = 0; i < count; i++) {
            Map<Integer, ItemStack> leftOvers = target.getInventory().addItem(ItemManager.getInstance().getItem(args[1]));
            if (!leftOvers.isEmpty())
                leftOvers.forEach((n, item) -> target.getWorld().dropItem(target.getLocation(), item));
        }
        send(sender, ChatColor.GREEN + "Item given!");
        send(target, ChatColor.GREEN + "You have been given: " + args[1] + " x" + count);
        return true;
    }

    /**
     * Called whenever a command sender is trying to tab complete a command.
     *
     * @param sender The sender who is tab completing this command.
     * @param args   The current arguments they have typed.
     */
    @Override
    public List<String> simplifiedTab(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            for (Player p : Bukkit.getOnlinePlayers())
                list.add(p.getName());
        } else if (args.length == 2) {
            list = ItemManager.getInstance().getItemNames();
            list.sort(String::compareToIgnoreCase);
        } else if (args.length == 3)
            return List.of("(amnt)");
        return everythingStartsWith(list, args[args.length - 1]);
    }
}
