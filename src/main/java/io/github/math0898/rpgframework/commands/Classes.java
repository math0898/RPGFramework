package io.github.math0898.rpgframework.commands;

import io.github.math0898.utils.commands.BetterCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sugaku.rpg.framework.menus.ClassesManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates the Classes command. The Classes command is an alias for /rpg classes. It is used for join classes.
 *
 * @author Sugaku
 */
public class Classes extends BetterCommand {

    /**
     * Creates a new BetterCommand with the given name.
     */
    public Classes () {
        super("classes", ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] ");
    }

    /**
     * Called whenever specifically a player executes this command.
     *
     * @param player The player who ran this command.
     * @param args   The arguments they passed to the command.
     */
    @Override
    public boolean onPlayerCommand (Player player, String[] args) {
        ClassesManager.classMenu(player);
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
        send(sender, ChatColor.RED + "This command can only be ran as a player.");
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
