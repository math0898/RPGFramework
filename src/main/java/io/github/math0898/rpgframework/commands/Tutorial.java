package io.github.math0898.rpgframework.commands;

import io.github.math0898.utils.commands.BetterCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * The tutorial class helps to inform players of the unique mechanics that are present in this server. As well as inform
 * existing players of ongoing updates and changes.
 *
 * @author Sugaku
 */
public class Tutorial extends BetterCommand {

    /**
     * Creates a new BetterCommand with the given name.
     */
    public Tutorial () {
        super("tutorial");
    }

    /**
     * Called whenever specifically a player executes this command.
     *
     * @param player The player who ran this command.
     * @param args   The arguments they passed to the command.
     */
    @Override
    public boolean onPlayerCommand (Player player, String[] args) {
        player.sendMessage(ChatColor.DARK_GRAY + " ========== ========= ========== ========== ");
        player.sendMessage(ChatColor.GRAY + "This server runs with some extra rpg mechanics.");
        player.sendMessage(ChatColor.GRAY + "For example, you can join a class with '/classes'.");
        player.sendMessage(ChatColor.GRAY + "The game has also been rebalanced to reduce armor and regeneration.");
        player.sendMessage(ChatColor.GRAY + "To compensate players are granted additional health and power through classes.");
        player.sendMessage(ChatColor.GRAY + "There are currently 2 bosses, Eiryeras and Krusk.");
        player.sendMessage(ChatColor.GRAY + "You can find their summon items by killing undead.");
        player.sendMessage(ChatColor.DARK_GRAY + " ========== ========= ========== ========== ");
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
        sender.sendMessage(ChatColor.RED + "This command can only be ran as a player.");
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
        return new ArrayList<>();
    }
}
