package io.github.math0898.rpgframework.forge;

import io.github.math0898.rpgframework.items.ItemUtility;
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
import sugaku.rpg.framework.items.ItemsManager;

import java.util.*;

import static io.github.math0898.rpgframework.main.plugin;
import static org.bukkit.enchantments.Enchantment.*;
import static org.bukkit.Material.*;

public class HoningMenu extends ForgeMenu {

    private final ItemStack forgeIndicator = ItemUtility.createItem(EXPERIENCE_BOTTLE, 1,
            ChatColor.BLUE + "" + ChatColor.BOLD + "Honing", new String[]{
                    ChatColor.GRAY + "Welcome to Honing!",
                    ChatColor.GRAY + "Place the items you would like to",
                    ChatColor.GRAY + "hone with the book to apply in the",
                    ChatColor.GRAY + "two slots. Then click the green",
                    ChatColor.GRAY + "glass pane to hone."}, false);

    public final String title = ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Forge";

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
        inv.setItem(49, forgeIndicator);
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
        ArrayList<Integer> clickable = new ArrayList<>();
        clickable.add(11);
        clickable.add(15);
        Player player = (Player) event.getWhoClicked();
        if ((clickable.contains(event.getSlot()) || !Objects.requireNonNull(event.getClickedInventory()).contains(forgeIndicator))) {
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
                if (!meta.hasDisplayName()) meta.setDisplayName(ItemsManager.increaseRarity(ItemsManager.genName(result.getType().toString().toLowerCase().toCharArray())));
                result.setItemMeta(meta);
                result.addUnsafeEnchantments(((EnchantmentStorageMeta) Objects.requireNonNull(book.getItemMeta())).getStoredEnchants());
                forge.setItem(22, result);
            }
            else failure(forge, "Not a legal enchant!");
        } else failure(forge, "Please add your items!");
    }

    private boolean legalEnchants (Set<Enchantment> checking, ItemStack item) {
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
                legal.add(DAMAGE_ALL);
                legal.add(DAMAGE_ARTHROPODS);
                legal.add(FIRE_ASPECT);
                legal.add(LOOT_BONUS_MOBS);
                legal.add(KNOCKBACK);
                legal.add(DAMAGE_UNDEAD);
                legal.add(MENDING);
                legal.add(DURABILITY);
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
                    legal.addAll(Arrays.asList( PROTECTION_ENVIRONMENTAL, PROTECTION_FIRE, PROTECTION_PROJECTILE,
                            PROTECTION_EXPLOSIONS, MENDING, VANISHING_CURSE, DURABILITY));

        }
        if (legal.isEmpty()) return false;
        for (Enchantment c: checking) if (!legal.contains(c)) return false;
        return true;
    }

    private void failure (Inventory i, String m) {
        ItemStack item = new ItemStack( RED_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + m);
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
