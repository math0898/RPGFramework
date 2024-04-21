package io.github.math0898.rpgframework.items.editor;

import io.github.math0898.rpgframework.Rarity;
import io.github.math0898.rpgframework.items.EquipmentSlots;
import io.github.math0898.rpgframework.items.WeaponType;
import io.github.math0898.utils.gui.GUI;
import io.github.math0898.utils.gui.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The EditorGUI is the GUI instance behind the in game item editor.
 *
 * @author Sugaku
 */
public class EditorGUI implements GUI {

    /**
     * A map of ItemConstructs organized by the opening player.
     */
    private final Map<Player, ItemConstruct> itemConstructs = new HashMap<>();

    /**
     * Creates the EditorGUI and registers it to the GUIManager.
     */
    public EditorGUI () {
        GUIManager.getInstance().addGUI("editor", this);
    }

    /**
     * Opens this GUI to the given player.
     *
     * @param player The player to open the GUI to.
     */
    @Override
    public void openInventory (Player player) {
        ItemConstruct construct = new ItemConstruct(Material.GOLDEN_CARROT,
                "Sample Name",
                EquipmentSlots.HAND,
                List.of("Sample description."),
                Rarity.COMMON,
                1,
                1,
                1.0,
                1.0,
                1.0,
                null,
                WeaponType.DAGGER,
                null);
        itemConstructs.put(player, construct);
        Inventory inv = Bukkit.createInventory(player, 54, getTitle());
        buildInventory(player, inv);
        player.openInventory(inv);
    }

    /**
     * Builds this inventory.
     *
     * @param inventory The inventory to build.
     */
    public void buildInventory (Player player, Inventory inventory) {
        ItemConstruct construct = itemConstructs.get(player);
        if (construct == null) return;
        inventory.setItem(22, construct.toItemStack());
    }

    /**
     * Called whenever this GUI is clicked.
     *
     * @param event The inventory click event.
     */
    @Override
    public void onClick (InventoryClickEvent event) {
        event.setCancelled(true);
    }

    /**
     * Called whenever this GUI is closed.
     *
     * @param event The inventory close event.
     */
    @Override
    public void onClose (InventoryCloseEvent event) {

    }

    /**
     * Gets the title of this GUI. Used by the GUIManager to route InventoryClickEvents.
     *
     * @return The title of this GUI.
     */
    @Override
    public String getTitle () {
        return ChatColor.DARK_GRAY + "GUI Editor";
    }
}
