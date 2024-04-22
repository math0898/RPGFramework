package io.github.math0898.rpgframework.items.editor;

import io.github.math0898.rpgframework.Rarity;
import io.github.math0898.rpgframework.items.EquipmentSlots;
import io.github.math0898.rpgframework.items.WeaponType;
import io.github.math0898.utils.gui.GUI;
import io.github.math0898.utils.gui.GUIManager;
import io.github.math0898.utils.items.ItemBuilder;
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
        inventory.setItem(13, construct.toItemStack());
        inventory.setItem(28, new ItemBuilder(Material.ANVIL).setDisplayName("Material").build()); // Material
        inventory.setItem(29, new ItemBuilder(Material.PAPER).setDisplayName("Name").build()); // Name
        inventory.setItem(30, new ItemBuilder(Material.BOOK).setDisplayName("Description").build()); // Description
        inventory.setItem(32, new ItemBuilder(Material.WOODEN_SHOVEL).setDisplayName("Slot").build()); // Slot
        inventory.setItem(33, new ItemBuilder(Material.ARROW).setDisplayName("Type").build()); // Type
        inventory.setItem(34, new ItemBuilder(Material.END_CRYSTAL).setDisplayName("Rarity").build()); // Rarity
        inventory.setItem(38, new ItemBuilder(Material.SPLASH_POTION).setDisplayName("Health").build()); // Health
        inventory.setItem(39, new ItemBuilder(Material.NETHERITE_SWORD).setDisplayName("Damage").build()); // Damage
        inventory.setItem(40, new ItemBuilder(Material.SHIELD).setDisplayName("Toughness").build()); // Toughness
        inventory.setItem(41, new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName("Armor").build()); // Armor
        inventory.setItem(42, new ItemBuilder(Material.FEATHER).setDisplayName("Attack Speed").build()); // AttackSpeed
        // todo: Color
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
