package io.github.math0898.rpgframework.items.editor;

import io.github.math0898.rpgframework.Rarity;
import io.github.math0898.rpgframework.items.EquipmentSlots;
import io.github.math0898.utils.Utils;
import io.github.math0898.utils.gui.GUI;
import io.github.math0898.utils.gui.GUIManager;
import io.github.math0898.utils.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The EditorGUI is the GUI instance behind the in game item editor.
 *
 * @author Sugaku
 */
public class EditorGUI implements GUI, Listener {

    /**
     * A map of ItemConstructs organized by the opening player.
     */
    private final Map<Player, ItemConstruct> itemConstructs = new HashMap<>();

    /**
     * A set of players we're checking for a message from.
     */
    private final Map<Player, String> pendingPlayers = new HashMap<>();

    /**
     * Creates the EditorGUI and registers it to the GUIManager.
     */
    public EditorGUI () {
        GUIManager.getInstance().addGUI("editor", this);
        Bukkit.getPluginManager().registerEvents(this, Utils.getPlugin());
    }

    /**
     * Opens this GUI to the given player.
     *
     * @param player The player to open the GUI to.
     */
    @Override
    public void openInventory (Player player) {
        itemConstructs.computeIfAbsent(player, k -> new ItemConstruct(Material.GOLDEN_CARROT,
                "Sample Name",
                EquipmentSlots.HAND,
                List.of("Sample description."),
                Rarity.COMMON,
                0,
                0,
                0.0,
                0.0,
                0.0,
                null,
                null,
                null));
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
     * Called whenever a player sends a message.
     *
     * @param event The player message send event.
     */
    @EventHandler
    public void onMessageAttempt (AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String action = pendingPlayers.get(player);
        if (action != null) {
            event.setCancelled(true);
            pendingPlayers.remove(player);
            ItemConstruct construct = itemConstructs.get(player);
            try {
                switch (action) {
                    case "material" -> construct.setMaterial(Material.valueOf(event.getMessage()));
                    case "name" -> construct.setName(event.getMessage());
                    case "description" ->  {
                        // todo: A little more needs to be done here.
                        construct.setDescription(List.of(event.getMessage()));
                    }
                    case "slot" -> {
                        // todo: Implement
                    }
                    case "rarity" -> construct.setRarity(Rarity.valueOf(event.getMessage()));
                    case "health" -> construct.setHealth(Integer.parseInt(event.getMessage()));
                    case "damage" -> construct.setDamage(Integer.parseInt(event.getMessage()));
                    case "toughness" -> construct.setToughness(Double.parseDouble(event.getMessage()));
                    case "armor"-> construct.setArmor(Double.parseDouble(event.getMessage()));
                    case "attack-speed" -> construct.setAttackSpeed(Double.parseDouble(event.getMessage()));
                }
            } catch (Exception exception) {
                player.sendMessage(ChatColor.RED + "Action failed: " + exception.getMessage());
            }
            itemConstructs.put(player, construct);
            Bukkit.getScheduler().runTask(Utils.getPlugin(), () -> openInventory(player));
        }
    }

    /**
     * Called whenever this GUI is clicked.
     *
     * @param event The inventory click event.
     */
    @Override
    public void onClick (InventoryClickEvent event) {
        event.setCancelled(true);
        String action = null;
        switch(event.getSlot()) {
            case 28 -> action = "material";
            case 29 -> action = "name";
            case 30 -> action = "description";
            case 32 -> action = "slot";
            case 33 -> action = "type";
            case 34 -> action = "rarity";
            case 38 -> action = "health";
            case 39 -> action = "damage";
            case 40 -> action = "toughness";
            case 41 -> action = "armor";
            case 42 -> action = "attack-speed";
        };
        if (action != null) {
            pendingPlayers.put((Player) event.getWhoClicked(), action);
            event.getWhoClicked().closeInventory();
        }
    }

    /**
     * Called whenever this GUI is closed.
     *
     * @param event The inventory close event.
     */
    @Override
    public void onClose (InventoryCloseEvent event) {
//        itemConstructs.remove((Player) event.getPlayer());
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
