package io.github.math0898.rpgframework.commands;

import io.github.math0898.rpgframework.items.editor.EditorGUI;
import io.github.math0898.utils.commands.BetterCommand;
import io.github.math0898.utils.gui.GUIManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * The EditorCommand is used to open the in game ItemEditor.
 *
 * @author Sugaku
 */
public class EditorCommand extends BetterCommand {

    /**
     * Creates a new BetterCommand with the given name.
     */
    public EditorCommand () {
        super("rpg-editor");
        new EditorGUI();
    }

    /**
     * Called whenever specifically a player executes this command.
     *
     * @param player The player who ran this command.
     * @param args   The arguments they passed to the command.
     */
    @Override
    public boolean onPlayerCommand (Player player, String[] args) {
        GUIManager.getInstance().openGUI("editor", player);
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
        return List.of();
    }
}
