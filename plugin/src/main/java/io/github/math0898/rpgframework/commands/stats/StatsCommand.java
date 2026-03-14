package io.github.math0898.rpgframework.commands.stats;

import io.github.math0898.utils.commands.BetterCommand;
import io.github.math0898.utils.gui.GUIManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * The stats command is used to view player stats such as boss kills, xp, strength, health, etc. Can also be utilized to
 * view other player stats.
 *
 * @author Sugaku
 */
public class StatsCommand extends BetterCommand {

    /**
     * Creates a new BetterCommand with the given name and prefix.
     */
    public StatsCommand () {
        super("stats", ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] ");
        GUIManager.getInstance().addGUI("stats-gui", new StatsGUI());
    }

    /**
     * Called whenever specifically a player executes this command.
     *
     * @param player The player who ran this command.
     * @param args   The arguments they passed to the command.
     */
    @Override
    public boolean onPlayerCommand (Player player, String[] args) {
        if (args.length == 0) GUIManager.getInstance().openGUI("stats-gui", player);
        else GUIManager.getInstance().openGUI("stats-gui", player, args);
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
        if (args.length == 1) // todo: Requires validation.
            return everythingStartsWith(getPlayerList(), args[args.length - 1]);
        return List.of();
    }
}
