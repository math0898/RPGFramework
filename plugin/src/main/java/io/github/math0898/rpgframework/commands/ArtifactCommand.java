package io.github.math0898.rpgframework.commands;

import io.github.math0898.rpgframework.PlayerManager;
import io.github.math0898.rpgframework.RpgPlayer;
import io.github.math0898.rpgframework.items.EquipmentSlots;
import io.github.math0898.rpgframework.items.ItemManager;
import io.github.math0898.rpgframework.systems.ArtifactMenu;
import io.github.math0898.utils.Utils;
import io.github.math0898.utils.commands.BetterCommand;
import io.github.math0898.utils.gui.GUIManager;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
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
    public boolean onPlayerCommand (Player player, String[] args) { // todo: Simplify and refactor.
        if (args.length > 0)
            if (args[0].equalsIgnoreCase("collect")) {
                ItemStack item = null;
                EntityEquipment equipment = player.getEquipment();
                if (equipment != null) item = equipment.getItemInMainHand();
                if (item == null) {
                    send(player, ChatColor.RED + "Are you holding anything?");
                    return true;
                }

                String rpgItemId = ItemManager.getInstance().findRpgItem(item);
                if (rpgItemId == null) {
                    send(player, ChatColor.RED + "That isn't an RPG item.");
                    return true;
                }
                if (ItemManager.getInstance().getRpgItem(rpgItemId).getSlot() != EquipmentSlots.ARTIFACT) {
                    send(player, ChatColor.RED + "That doesn't belong in an artifact equipment slot!");
                    return true;
                }

                RpgPlayer rpgPlayer = PlayerManager.getPlayer(player.getUniqueId());
                if (rpgPlayer == null) {
                    send(player, ChatColor.RED + "A serious bug has occurred, contact an admin.");
                    Utils.console(player.getName() + " has no RpgPlayer object.", ChatColor.RED);
                    return true;
                }
                if (rpgPlayer.getArtifactCollection().contains(rpgItemId)) {
                    send(player, ChatColor.YELLOW + "You've already collected that artifact.");
                    return true;
                }

                rpgPlayer.addCollectedArtifacts(List.of(rpgItemId));
                player.getInventory().remove(item);
                send(player, ChatColor.GREEN + "Item collected!");
                player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.5f, 1.0f);
                return true;
            }
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
        List<String> toReturn = new ArrayList<>();
        if (sender.hasPermission("rpg.artifact") && args.length < 2)
            toReturn.add("collect");
        return everythingStartsWith(toReturn, args[args.length - 1]);
    }
}
