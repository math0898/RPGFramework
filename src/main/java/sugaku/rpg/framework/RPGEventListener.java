package sugaku.rpg.framework;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sugaku.rpg.framework.menus.ClassesManager;
import sugaku.rpg.framework.items.ItemsManager;
import sugaku.rpg.framework.mobs.BossRituals;
import sugaku.rpg.framework.mobs.MobManager;
import sugaku.rpg.framework.players.PlayerManager;
import sugaku.rpg.framework.players.RpgPlayer;
import sugaku.rpg.main;
import sugaku.rpg.mobs.teir1.krusk.KruskBoss;
import sugaku.rpg.mobs.teir1.krusk.KruskMinion;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER;
import static sugaku.rpg.framework.items.ItemsManager.*;


public class RPGEventListener implements Listener {

    /**
     * When inventory slots are clicked.
     */
    @EventHandler
    public void invClick(InventoryClickEvent e) {

        Inventory clicked = e.getClickedInventory();
        InventoryView open = e.getWhoClicked().getOpenInventory();

        if (clicked == null) return;

        //If the player clicked on an armor slot we should update special effects
        if (e.getSlotType() == InventoryType.SlotType.ARMOR) Bukkit.getServer().getScheduler().runTask(main.plugin, () -> updateEffects(Bukkit.getPlayer(e.getWhoClicked().getName())));
//        else if (open.getTitle().equals(ForgeManager.title)) ForgeManager.forgeClicked(e);
        else if (open.getTitle().startsWith(ClassesManager.title)) ClassesManager.classClicked(e);
    }

    /**
     * When the player joins the server.
     */
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        try {
            //We need to load their data,
            PlayerManager.addPlayer(new RpgPlayer(e.getPlayer()));
            FileManager.load(e.getPlayer());
            PlayerManager.scaleHealth(e.getPlayer());
            Bukkit.getServer().getScheduler().runTaskLater(main.plugin, () -> PlayerManager.healPlayer(e.getPlayer()), 5);
        } catch (Exception exception) {
            main.console("Failed to load data!", ChatColor.RED);
            main.console(exception.getMessage(), ChatColor.RED);
        }
    }

    /**
     * When a player leaves the server
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        //We need to save their data
        FileManager.unload(e.getPlayer());
        PlayerManager.removePlayer(e.getPlayer().getUniqueId());
    }

    /**
     * When a mob gets damaged.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void entityDamaged(EntityDamageByEntityEvent e) {

        if (e.getDamager() instanceof Player) Objects.requireNonNull(PlayerManager.getPlayer(e.getDamager().getUniqueId())).attacker(e);

        if (e.getEntity() instanceof Player) PlayerManager.onDamage(e);
        else if (MobManager.needsChecks()) MobManager.run(e);
    }

    /**
     * When an mob dies.
     */
    @EventHandler
    public void mobDeath(EntityDeathEvent e) {
        LivingEntity m = e.getEntity();

        if (m.getType() == EntityType.ZOMBIE) MobManager.zombieDrops(e);
        else if (m.getType() == EntityType.SKELETON) MobManager.skeletonDrops(e);

        assert m.getCustomName() != null;

        try {
            if (m.getCustomName().contains("Krusk, Undead General")) KruskBoss.handleDrops(e);
            else if (m.getCustomName().contains(", Underling of Krusk")) KruskMinion.handleDrops(e);
        } catch (NullPointerException exception) {
            //do NOTHING!
        }
    }

    /**
     * When an item is crafted.
     */
    @EventHandler
    public void onCraft(CraftItemEvent e) {
        ItemStack itemCrafted = e.getCurrentItem();

        if (itemCrafted == null) return;
        else if (itemCrafted.getItemMeta() == null) return;

        if (!itemCrafted.getItemMeta().hasLore()) if (isArmor(itemCrafted.getType())) Bukkit.getServer().getScheduler().runTask(main.plugin, () -> updateArmor(itemCrafted));
    }

    /**
     * Called when a player interacts.
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Objects.requireNonNull(PlayerManager.getPlayer(event.getPlayer().getUniqueId())).onInteract(event);

//        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && Objects.requireNonNull(event.getClickedBlock()).getType() == Material.ANVIL) {
//
//            Block blockClicked = event.getClickedBlock();
//            Player player = event.getPlayer();

//            if (blockClicked.getWorld().getBlockAt(blockClicked.getX(), blockClicked.getY() - 1, blockClicked.getZ()).getType() == Material.NETHERITE_BLOCK) {
//                event.setCancelled(true);
//                ForgeManager.forgeMenu(player);
//            }
//        }
    }

    private ItemStack bugggedKruskHelm () {
        ItemStack bugged = new ItemStack(Material.DIAMOND_HELMET, 1);
        ItemMeta meta = bugged.getItemMeta();
        assert meta != null;

        meta.setUnbreakable(true);
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(new UUID(4, 1), "generic.health", 16, ADD_NUMBER, EquipmentSlot.FEET));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(new UUID(4,2), "generic.armor", 1.25, ADD_NUMBER, EquipmentSlot.FEET));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(new UUID(4,3), "generic.armorToughness", 2.25, ADD_NUMBER, EquipmentSlot.FEET));

        meta.setDisplayName(ChatColor.BLUE + "Krusk's Trusty Helmet");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "This is " + ChatColor.GREEN + "Krusk's " + ChatColor.GRAY + "helmet from life. It");
        lore.add(ChatColor.GRAY + "was his good luck charm. If only he");
        lore.add(ChatColor.GRAY + "was wearing it that fateful day...");
        lore.add(ChatColor.GRAY + "maybe... " + ChatColor.GREEN + "Krusk "+ ChatColor.GRAY + "would've lived long");
        lore.add(ChatColor.GRAY + "enough to save enough pevsar to");
        lore.add(ChatColor.GRAY + "buy his own house.");
        meta.setLore(lore);

        bugged.setItemMeta(meta);
        return bugged;
    }

    /**
     * When an item is dropped.
     */
    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        ItemStack itemDropped = e.getItemDrop().getItemStack();

        if (itemDropped.getItemMeta() == null) return;

        if (!itemDropped.getItemMeta().isUnbreakable()) {
            if (!itemDropped.getItemMeta().hasLore() || !Objects.requireNonNull(itemDropped.getItemMeta().getLore()).contains(ChatColor.GRAY + "Item modified by RPG - 1.2")) {
                if (isArmor(itemDropped.getType()))
                    Bukkit.getServer().getScheduler().runTask(main.plugin, () -> updateArmor(itemDropped));
            }
        } else if (itemDropped.getItemMeta().isUnbreakable() && itemDropped.getType() == Material.DIAMOND_HELMET) {
            Map<Enchantment, Integer> enchants = itemDropped.getEnchantments();
            ItemStack toCmp = bugggedKruskHelm();
            toCmp.addEnchantments(enchants);
            if (itemDropped.equals(toCmp)) {
                itemDropped.setItemMeta(KruskHelmet.getItemMeta());
                itemDropped.addEnchantments(enchants);
            }
        }
        BossRituals.general(e);
    }

    /**
     * Called when an entity is exhausted.
     */
    @EventHandler
    public void onEntityExhaustion(EntityExhaustionEvent event) {
        if (event.getExhaustionReason() == EntityExhaustionEvent.ExhaustionReason.REGEN) PlayerManager.hunger(event);
    }

    /**
     * Called when an inventory is closed.
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
//        if (event.getPlayer().getOpenInventory().getTitle().equals(ForgeManager.title)) forgeClose(event);
    }

    /**
     * A small helper method to check if an item is armor.
     */
    private boolean isArmor(Material material) {
        switch(material) {
            case LEATHER_BOOTS:
            case LEATHER_LEGGINGS:
            case LEATHER_CHESTPLATE:
            case LEATHER_HELMET:
            case IRON_BOOTS:
            case IRON_LEGGINGS:
            case IRON_CHESTPLATE:
            case IRON_HELMET:
            case CHAINMAIL_BOOTS:
            case CHAINMAIL_LEGGINGS:
            case CHAINMAIL_CHESTPLATE:
            case CHAINMAIL_HELMET:
            case GOLDEN_BOOTS:
            case GOLDEN_LEGGINGS:
            case GOLDEN_CHESTPLATE:
            case GOLDEN_HELMET:
            case DIAMOND_BOOTS:
            case DIAMOND_LEGGINGS:
            case DIAMOND_CHESTPLATE:
            case DIAMOND_HELMET:
            case NETHERITE_BOOTS:
            case NETHERITE_LEGGINGS:
            case NETHERITE_CHESTPLATE:
            case NETHERITE_HELMET:
                return true;
            default:
                return false;
        }
    }

    /**
     * Called when an inventory tries to pick up an item.
     */
    @EventHandler
    public void onInventoryPickup (InventoryPickupItemEvent event) {
        if (event.getInventory().getType() == InventoryType.HOPPER) {
            if (Objects.equals(event.getItem().getItemStack().getItemMeta(), ItemsManager.KruskSpawn.getItemMeta())) event.setCancelled(true);
            else if (Objects.equals(event.getItem().getItemStack().getItemMeta(), ItemsManager.EiryerasSpawn.getItemMeta())) event.setCancelled(true);
        }
    }

    /**
     * Called when an entity is about to transform.
     */
    @EventHandler
    public void onEntityTransform (EntityTransformEvent event) {
        if (event.getEntity().isCustomNameVisible()) {
            event.getEntity().remove();
            event.setCancelled(true);
        }
    }
}
