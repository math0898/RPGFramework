package io.github.math0898.rpgframework.systems;

import io.github.math0898.rpgframework.items.ItemManager;
import io.github.math0898.utils.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static io.github.math0898.rpgframework.RPGFramework.plugin;
import static org.bukkit.enchantments.Enchantment.*;
import static org.bukkit.Material.*;

/**
 * The Forge is a crafting system that allows players to put unsafe and conflicting enchantments on their gear at an
 * increased experience cost.
 *
 * // todo: Overall I very much dislike this class - way too much of it is static. We can also utilize the GUI framework
 *          outlined in utils.GUIManager.
 *
 * @author Sugaku
 */
public class Forge {

    /**
     * This is the main indicator for finding the Forge GUI. // todo: Change this to a better system, perhaps title.
     */
    private static final ItemStack FORGE_ITEM_INDICATOR = new ItemBuilder(ANVIL)
            .setDisplayName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Forge")
            .setLore(new String[]{
                    ChatColor.GRAY + "Welcome to the forge!",
                    ChatColor.GRAY + "Place the items you would like to",
                    ChatColor.GRAY + "combine in the two empty slots then",
                    ChatColor.GRAY + "click the green glass pane to forge."}).build();

    /**
     * Title for the forge GUI.
     */
    private static final String GUI_TITLE = ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Forge";

    /**
     * The cost amount for this operation. // todo: There is no way having this be static will not cause issues.
     */
    private static int pendingCost = -1;

    /**
     * Checks whether the given inventory is the forge or not.
     *
     * @param inv The inventory view to check.
     * @return True if the given inventory is the forge, otherwise false.
     */
    public static boolean isForgeGUI (InventoryView inv) {
        return inv.getTitle().equals(GUI_TITLE);
    }

    /**
     * Creates the Forge GUI in the given Inventory.
     *
     * @param inv The inventory to build the Forge GUI in.
     */
    private static void buildForgeMenu (Inventory inv) {
        ItemStack fill = new ItemBuilder(BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build();
        for (int i = 0; i < 54; i++) inv.setItem(i, fill);
        inv.setItem(49, FORGE_ITEM_INDICATOR);
        inv.setItem(11, new ItemStack(AIR));
        inv.setItem(15, new ItemStack(AIR));
        Objects.requireNonNull(inv.getItem(22)).setType(ORANGE_STAINED_GLASS_PANE);
        Objects.requireNonNull(inv.getItem(31)).setType(RED_STAINED_GLASS_PANE);
    }

    /**
     * Creates and opens a Forge GUI to the given player.
     *
     * @param p The player to open the menu to.
     */
    public static void forgeMenu (Player p) {
        Inventory i = Bukkit.getServer().createInventory(p.getPlayer(), 54, GUI_TITLE);
        buildForgeMenu(i);
        p.openInventory(i);
    }

    /**
     * Cleans up the GUI and any items left inside for players when they close the GUI.
     *
     * @param e The InventoryCloseEvent we're interested in. Assumed to be closing a Forge GUI.
     */
    public static void forgeClose (InventoryCloseEvent e) {
        HumanEntity player = e.getPlayer();
        Inventory forge = e.getPlayer().getOpenInventory().getTopInventory();
        ItemStack indicator = forge.getItem(31);
        if (indicator == null) return;
        List<ItemStack> toReturn = new ArrayList<>();
        switch (indicator.getType()) {
            case ORANGE_STAINED_GLASS_PANE:
                toReturn.add(forge.getItem(22));
            case RED_STAINED_GLASS_PANE, LIME_STAINED_GLASS_PANE:
                toReturn.add(forge.getItem(11));
                toReturn.add(forge.getItem(15));
        }
        for (ItemStack i : toReturn)
            if (i != null) {
                Map<Integer, ItemStack> failed = player.getInventory().addItem(i);
                if (!failed.isEmpty())
                    failed.forEach((n, item) -> player.getWorld().dropItem(player.getLocation(), item));
            }
    }

    /**
     * Handles logic for when the Forge GUI was clicked.
     *
     * @param event The inventory click event to consider. The clicked inventory is assumed to be the forge.
     */
    public static void forgeClicked (InventoryClickEvent event) {
        ArrayList<Integer> clickable = new ArrayList<>();
        clickable.add(11);
        clickable.add(15);
        Player player = (Player) event.getWhoClicked();
        if ((clickable.contains(event.getSlot()) || !Objects.requireNonNull(event.getClickedInventory()).contains(FORGE_ITEM_INDICATOR))) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> forgeUpdate(event.getWhoClicked().getOpenInventory(), player), 1);
        } else if (event.getSlot() == 31 && Objects.requireNonNull(event.getCurrentItem()).getType() ==  LIME_STAINED_GLASS_PANE) {
            player.setLevel(player.getLevel() - pendingCost);
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 0.8f, 0.5f);
            event.setCancelled(true);
            Objects.requireNonNull(event.getClickedInventory()).setItem(11, new ItemStack( AIR));
            Objects.requireNonNull(event.getClickedInventory()).setItem(15, new ItemStack( AIR));
            ItemStack item = new ItemStack( ORANGE_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.GREEN + "Forged");
            item.setItemMeta(meta);
            event.getClickedInventory().setItem(31, item);
        } else if (!(event.getSlot() == 22 && Objects.requireNonNull(Objects.requireNonNull(event.getClickedInventory()).getItem(31)).getType() ==  ORANGE_STAINED_GLASS_PANE)) event.setCancelled(true);
    }

    /**
     * Updates the forge with operation cost and any potential fail messages.
     *
     * @param view The inventory view of the forge.
     * @param player The player who is currently looking at this view.
     */
    private static void forgeUpdate (InventoryView view, Player player) {
        Inventory forge = view.getTopInventory();
        ItemStack item1 = forge.getItem(11);
        ItemStack item2 = forge.getItem(15);
        if (Objects.requireNonNull(forge.getItem(31)).getType() ==  ORANGE_STAINED_GLASS_PANE) return;
        if (item1 != null && item2 != null) {
            ItemStack book;
            ItemStack target;
            if (item1.getType() ==  ENCHANTED_BOOK)  { book = item1; target = item2; }
            else if (item2.getType() ==  ENCHANTED_BOOK) { book = item2; target = item1; }
            else { failure(forge, "One item must be a book!"); return; }
            if (legalEnchants(((EnchantmentStorageMeta) Objects.requireNonNull(book.getItemMeta())).getStoredEnchants().keySet(), target)) {
                int cost = calculateCost(((EnchantmentStorageMeta) Objects.requireNonNull(book.getItemMeta())).getStoredEnchants().values());
                if (cost <= player.getLevel()) {
                    ItemStack item = new ItemStack( LIME_STAINED_GLASS_PANE);
                    ItemMeta meta = item.getItemMeta();
                    assert meta != null;
                    meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.GREEN + "Forge!");
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GRAY + "This will cost: " + ChatColor.GREEN + cost + " levels" + ChatColor.GRAY + ".");
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    forge.setItem(31, item);
                    pendingCost = cost;
                } else {
                    failure(forge, "Not enough levels! <" + cost + ">");
                }

                ItemStack result = target.clone();
                ItemMeta meta = result.getItemMeta();
                assert meta != null;
                if (!meta.hasDisplayName()) meta.setDisplayName(ItemManager.increaseRarity(ItemManager.genName(result.getType().toString().toLowerCase().toCharArray())));
                result.setItemMeta(meta);
                result.addUnsafeEnchantments(((EnchantmentStorageMeta) Objects.requireNonNull(book.getItemMeta())).getStoredEnchants());
                forge.setItem(22, result);
            }
            else failure(forge, "Not a legal enchant!");
        } else failure(forge, "Please add your items!");
    }

    /**
     * Checks whether the given enchantment is legal for the given item.
     *
     * @param checking The set of enchantments to check for.
     * @param item The item it may be applied to.
     * @return True if the enchantments are allowed to apply to the given item.
     */
    private static boolean legalEnchants (Set<Enchantment> checking, ItemStack item) {
        ArrayList<Enchantment> legal = new ArrayList<>();
        //Trident Enchants
        switch(item.getType()) {
            case TRIDENT:
                legal.add(RIPTIDE);
                legal.add(CHANNELING);
                legal.add(LOYALTY);
            case IRON_SWORD: case WOODEN_SWORD: case STONE_SWORD: case GOLDEN_SWORD: case DIAMOND_SWORD: case NETHERITE_SWORD: case WOODEN_AXE: case STONE_AXE: case GOLDEN_AXE: case IRON_AXE:
            case DIAMOND_AXE: case NETHERITE_AXE:
                legal.add(IMPALING);
                break;
        }
        //Sword Enchants
        switch(item.getType()) {
            case IRON_SWORD: case WOODEN_SWORD: case STONE_SWORD: case GOLDEN_SWORD: case DIAMOND_SWORD: case NETHERITE_SWORD:
                legal.add( SWEEPING_EDGE);
            case WOODEN_AXE: case STONE_AXE: case GOLDEN_AXE: case IRON_AXE: case DIAMOND_AXE: case NETHERITE_AXE: case TRIDENT:
                legal.add(SHARPNESS);
                legal.add(BANE_OF_ARTHROPODS);
                legal.add(FIRE_ASPECT);
                legal.add(LOOTING);
                legal.add(KNOCKBACK);
                legal.add(SMITE);
                legal.add(MENDING);
                legal.add(UNBREAKING);
                legal.add(VANISHING_CURSE);
                break;
        }
        //Armor enchants
        switch(item.getType()) {
            case LEATHER_BOOTS, LEATHER_LEGGINGS, LEATHER_CHESTPLATE, LEATHER_HELMET, IRON_BOOTS, IRON_LEGGINGS,
                    IRON_CHESTPLATE, IRON_HELMET, CHAINMAIL_BOOTS, CHAINMAIL_LEGGINGS, CHAINMAIL_CHESTPLATE,
                    CHAINMAIL_HELMET, GOLDEN_BOOTS, GOLDEN_LEGGINGS, GOLDEN_CHESTPLATE, GOLDEN_HELMET, DIAMOND_BOOTS,
                    DIAMOND_LEGGINGS, DIAMOND_CHESTPLATE, DIAMOND_HELMET, NETHERITE_BOOTS, NETHERITE_LEGGINGS,
                    NETHERITE_CHESTPLATE, NETHERITE_HELMET ->
                    legal.addAll(Arrays.asList( PROTECTION, FIRE_PROTECTION, PROJECTILE_PROTECTION,
                            BLAST_PROTECTION, MENDING, VANISHING_CURSE, UNBREAKING));
        }
        if (legal.isEmpty()) return false;
        for (Enchantment c: checking) if (!legal.contains(c)) return false;
        return true;
    }

    /**
     * Sets a fail status and message to the given forge GUI inventory.
     *
     * @param i The inventory to modify.
     * @param m The fail message to apply.
     */
    private static void failure (Inventory i, String m) {
        ItemStack item = new ItemBuilder(RED_STAINED_GLASS_PANE)
                .setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + m).build();
        i.setItem(31, item);

        item = new ItemBuilder(ORANGE_STAINED_GLASS_PANE)
                .setDisplayName(" ").build();
        i.setItem(22, item);
    }

    /**
     * Calculates the experience cost of the forge operation.
     *
     * @param total The total group of enchantments that are pending application.
     * @return The cost to apply the given enchantments to an item.
     */
    private static int calculateCost (Collection<Integer> total) {
        int running = 0;
        for (Integer i: total) running += (i * 10);
        return running;
    }
}
