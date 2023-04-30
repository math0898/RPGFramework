package io.github.math0898.rpgframework.forge;

import io.github.math0898.rpgframework.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import sugaku.rpg.framework.items.ItemsManager;

import java.util.*;

import static io.github.math0898.rpgframework.main.plugin;
import static org.bukkit.enchantments.Enchantment.*;
import static org.bukkit.Material.*;

public class HoningMenu extends ForgeMenu {

    private static final ItemStack HONING_INDICATOR = new ItemBuilder(EXPERIENCE_BOTTLE, 1,
            ChatColor.BLUE.toString() + ChatColor.BOLD + "Honing").setLore(new String[]{
                    ChatColor.GRAY + "Welcome to Honing!",
                    ChatColor.GRAY + "Place the items you would like to",
                    ChatColor.GRAY + "hone with the book to apply in the",
                    ChatColor.GRAY + "two slots. Then click the green",
                    ChatColor.GRAY + "glass pane to hone."}).build();

    private int pendingCost = -1;

    /**
     * Creates a Honing Menu.
     */
    public HoningMenu () {
        super("Honing");
    }

    @Override
    protected void buildForgeMenu (Inventory inv) {
        super.buildForgeMenu(inv);
        inv.setItem(49, HONING_INDICATOR);
        inv.setItem(11, new ItemStack(AIR));
        inv.setItem(15, new ItemStack(AIR));
        Objects.requireNonNull(inv.getItem(22)).setType(ORANGE_STAINED_GLASS_PANE);
        Objects.requireNonNull(inv.getItem(31)).setType(RED_STAINED_GLASS_PANE);
    }

    @Override
    public void onClose (InventoryCloseEvent e) {
        HumanEntity player = e.getPlayer();
        Inventory forge = e.getPlayer().getOpenInventory().getTopInventory();
        ItemStack indicator = forge.getItem(31);
        if (indicator == null) return;
        switch (indicator.getType()) {
            case ORANGE_STAINED_GLASS_PANE -> {
                if (forge.getItem(22) != null) player.getInventory().addItem(forge.getItem(22));
            }
            case RED_STAINED_GLASS_PANE, LIME_STAINED_GLASS_PANE -> {
                if (forge.getItem(11) != null) player.getInventory().addItem(forge.getItem(11));
                if (forge.getItem(15) != null) player.getInventory().addItem(forge.getItem(15));
            }
        }
    }

    @Override
    public void onClick (InventoryClickEvent event) {
        assert event.getClickedInventory() != null;
        ArrayList<Integer> clickable = new ArrayList<>();
        clickable.add(11);
        clickable.add(15);
        Player player = (Player) event.getWhoClicked();
        Bukkit.getScheduler().runTask(plugin, () -> forgeUpdate(event.getWhoClicked().getOpenInventory(), player));
        if (clickable.contains(event.getSlot()) || event.getClickedInventory().getType() != InventoryType.CHEST) event.setCancelled(false);
        else if (event.getSlot() == 31 && Objects.requireNonNull(event.getCurrentItem()).getType() ==  LIME_STAINED_GLASS_PANE) {
            player.setLevel(player.getLevel() - pendingCost);
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 0.8f, 0.5f);
            event.setCancelled(true);
            Objects.requireNonNull(event.getClickedInventory()).setItem(11, new ItemStack( AIR));
            Objects.requireNonNull(event.getClickedInventory()).setItem(15, new ItemStack( AIR));
            ItemStack item = new ItemStack( ORANGE_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            meta.setDisplayName(ChatColor.BOLD.toString() + ChatColor.GREEN + "Forged");
            item.setItemMeta(meta);
            event.getClickedInventory().setItem(31, item);
        } else if (!(event.getSlot() == 22 && Objects.requireNonNull(Objects.requireNonNull(event.getClickedInventory()).getItem(31)).getType() ==  ORANGE_STAINED_GLASS_PANE)) event.setCancelled(true);
    }

    private void forgeUpdate (InventoryView view, Player player) {
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
                    meta.setDisplayName(ChatColor.BOLD.toString() + ChatColor.GREEN + "Forge!");
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
                if (!meta.hasDisplayName()) meta.setDisplayName(ItemsManager.increaseRarity(ItemsManager.genName(result.getType().toString().toLowerCase().toCharArray())));
                result.setItemMeta(meta);
                result.addUnsafeEnchantments(((EnchantmentStorageMeta) Objects.requireNonNull(book.getItemMeta())).getStoredEnchants());
                forge.setItem(22, result);
            }
            else failure(forge, "Not a legal enchant!");
        } else failure(forge, "Please add your items!");
    }

    /**
     * Checks whether the given set of enchantments are legal to apply to the given item.
     *
     * @param checking The set of enchantments to check against legal enchantments.
     * @param item The item that determines what enchantments are legal.
     * @return True if all the given enchantments are legal for the given item. Otherwise, false.
     */
    private boolean legalEnchants (Set<Enchantment> checking, ItemStack item) {
        String type = item.getType().toString();
        // Global Enchants
        ArrayList<Enchantment> legal = new ArrayList<>(Arrays.asList(MENDING, DURABILITY));

        // Tool Enchants
        if (type.endsWith("_AXE") || type.endsWith("_HOE") || type.endsWith("_SHOVEL") || type.endsWith("_PICKAXE")) legal.add(DIG_SPEED);

        //Trident Enchants
        if (type.endsWith("TRIDENT")) legal.addAll(Arrays.asList(RIPTIDE, CHANNELING, LOYALTY));
        if (type.endsWith("_SWORD") || type.endsWith("_AXE")) legal.add(IMPALING);

        //Sword Enchants
        if (type.endsWith("_SWORD")) legal.add(SWEEPING_EDGE);
        if (type.endsWith("_SWORD") || type.endsWith("_AXE") || type.endsWith("TRIDENT"))
            legal.addAll(Arrays.asList(DAMAGE_ALL, DAMAGE_ARTHROPODS, DAMAGE_UNDEAD, FIRE_ASPECT, LOOT_BONUS_MOBS, KNOCKBACK));
            
        //Armor enchants
        if (type.endsWith("_BOOTS")) legal.addAll(Arrays.asList(PROTECTION_FALL, SOUL_SPEED, DEPTH_STRIDER));
        if (type.endsWith("_LEGGINGS")) legal.add(SWIFT_SNEAK);
        if (type.endsWith("_HELMET")) legal.addAll(Arrays.asList(WATER_WORKER, OXYGEN));
        if (type.endsWith("_BOOTS") || type.endsWith("_LEGGINGS") || type.endsWith("_CHESTPLATE") || type.endsWith("_HELMET"))
            legal.addAll(Arrays.asList(PROTECTION_ENVIRONMENTAL, PROTECTION_FIRE, PROTECTION_PROJECTILE, PROTECTION_EXPLOSIONS));
        if (legal.isEmpty()) return false;
        for (Enchantment c: checking) if (!legal.contains(c)) return false;
        return true;
    }

    private void failure (Inventory i, String m) {
        ItemStack item = new ItemStack( RED_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD + m);
        item.setItemMeta(meta);
        i.setItem(31, item);
        item = new ItemStack( ORANGE_STAINED_GLASS_PANE);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        i.setItem(22, item);
    }

    private int calculateCost (Collection<Integer> total) {
        int running = 0;
        for (Integer i: total) running += (i * 10);
        return running;
    }
}
