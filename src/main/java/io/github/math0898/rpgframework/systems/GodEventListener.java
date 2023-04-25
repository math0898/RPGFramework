package io.github.math0898.rpgframework.systems;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryView;

import java.util.Objects;

import static io.github.math0898.rpgframework.systems.Forge.*;

public class GodEventListener implements Listener {

    /**
     * When inventory slots are clicked.
     */
    @EventHandler
    public void invClick(InventoryClickEvent e) {

        Inventory clicked = e.getClickedInventory();
        InventoryView open = e.getWhoClicked().getOpenInventory();

        if (clicked == null) return;
        if (open.getTitle().equals(title)) forgeClicked(e);
    }

    /**
     * Called when a player interacts.
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && Objects.requireNonNull(event.getClickedBlock()).getType() == Material.ANVIL) {

            Block blockClicked = event.getClickedBlock();
            Player player = event.getPlayer();

            if (blockClicked.getWorld().getBlockAt(blockClicked.getX(), blockClicked.getY() - 1, blockClicked.getZ()).getType() == Material.NETHERITE_BLOCK) {
                event.setCancelled(true);
                Forge.forgeMenu(player);
            }
        }
    }


    /**
     * Called when an inventory is closed.
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer().getOpenInventory().getTitle().equals(Forge.title)) forgeClose(event);
    }
}
