package io.github.math0898.rpgframework.commands;

import io.github.math0898.utils.commands.BetterCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * The Party Command is used for interacting and manipulating parties.
 *
 * @author Sugaku
 */
public class PartyCommand extends BetterCommand {

    /**
     * Creates a new BetterCommand with the given name.
     *
     * @param name The name of the command as written in plugin.yml.
     */
    public PartyCommand (String name) {
        super(name);
    }

    /**
     * Called whenever specifically a player executes this command.
     *
     * @param player The player who ran this command.
     * @param args   The arguments they passed to the command.
     */
    @Override
    public boolean onPlayerCommand (Player player, String[] args) {
        return false;
    }

    /**
     * Called whenever an unspecified sender executes this command. This could include console and command blocks.
     *
     * @param sender The sender who ran this command.
     * @param args   The arguments they passed to the command.
     */
    @Override
    public boolean onNonPlayerCommand (CommandSender sender, String[] args) {
        return false;
    }

    /**
     * Called whenever a command sender is trying to tab complete a command.
     *
     * @param sender The sender who is tab completing this command.
     * @param args   The current arguments they have typed.
     */
    @Override
    public List<String> simplifiedTab (CommandSender sender, String[] args) {
        return null;
    }
}
