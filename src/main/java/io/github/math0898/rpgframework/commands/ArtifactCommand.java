package io.github.math0898.rpgframework.commands;

import io.github.math0898.rpgframework.systems.ArtifactMenu;
import io.github.math0898.utils.commands.BetterCommand;
import io.github.math0898.utils.gui.GUIManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;


/**
 * The ArtifactCommand is used by players in order to open and check their current artifacts.
 *
 * @author Sugaku
 */
public class ArtifactCommand extends BetterCommand {

    /**
     * Creates a new BetterCommand with the given name.
     */
    public ArtifactCommand () {
        super("artifact");
        GUIManager.getInstance().addGUI("Artifacts", new ArtifactMenu());
    }

    /**
     * Called whenever specifically a player executes this command.
     *
     * @param player The player who ran this command.
     * @param args   The arguments they passed to the command.
     */
    @Override
    public boolean onPlayerCommand (Player player, String[] args) {
        GUIManager.getInstance().openGUI("Artifacts", player);
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
        return List.of();
    }
}
