package io.github.math0898.rpgframework.forge;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Listens to and passes on events relating to the Forge system overall.
 *
 * @author Sugaku
 */
public class ForgeEventListener implements Listener {

    /**
     * Called whenever an inventory is clicked. We quickly check whether this is a forge or not to reduce lag. Then if
     * relevant we pass the event onto the Forge.
     *
     * @param event The inventory click event to consider.
     */
    @EventHandler
    public void invClick (InventoryClickEvent event) {

        if (!isForgeMenu(event)) return;

        Inventory clicked = event.getClickedInventory();
        if (clicked == null) return;

        ForgeManager.getInstance().onClick(event);
    }

    /**
     * Called when a player interacts with anything in the world, item, block etc. We filter immediately to block clicks
     * to hopefully reduce lag. When the multi-block matches a forge we open the forge menu.
     *
     * @param event The player interact event to consider.
     */
    @EventHandler
    public void onPlayerInteract (PlayerInteractEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block clicked = event.getClickedBlock();
        if (clicked == null) return;
        if (clicked.getType() != Material.ANVIL) return;

        Block base = clicked.getWorld().getBlockAt(clicked.getLocation().add(0, -1, 0));
        if (base.getType() != Material.NETHERITE_BLOCK) return;

        event.setCancelled(true);
        ForgeManager.getInstance().openMenu(event.getPlayer());
    }


    /**
     * Called when an inventory is closed. Used by the forge to return items to players if any remain in the inventory.
     *
     * @param event The inventory close event that may involve the forge.
     */
    @EventHandler
    public void onInventoryClose (InventoryCloseEvent event) {
        if (isForgeMenu(event)) ForgeManager.getInstance().onClose(event);
    }

    /**
     * Checks whether the inventory event involves an instance of the Forge or not.
     *
     * @param event An inventory event that may involve the forge menu or not.
     * @return True if the given inventory event involves the forge.
     */
    public boolean isForgeMenu (InventoryEvent event) {
        return event.getView().getTitle().startsWith(ForgeMenu.FORGE_MENUS_PREFIX);
    }
}
