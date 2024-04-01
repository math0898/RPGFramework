package sugaku.rpg.framework;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import sugaku.rpg.framework.menus.ClassesManager;
import sugaku.rpg.framework.items.ItemsManager;
import sugaku.rpg.framework.mobs.BossRituals;
import sugaku.rpg.framework.mobs.MobManager;
import sugaku.rpg.framework.players.PlayerManager;
import sugaku.rpg.framework.players.RpgPlayer;
import sugaku.rpg.main;
import sugaku.rpg.mobs.teir1.krusk.KruskBoss;
import sugaku.rpg.mobs.teir1.krusk.KruskMinion;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

//import static sugaku.rpg.framework.menus.ForgeManager.forgeClose;
import static io.github.math0898.rpgframework.RPGFramework.itemManager;
import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.*;
import static sugaku.rpg.framework.items.ItemsManager.updateArmor;

public class RPGEventListener implements Listener {

    /**
     * A list of items that are not allowed to be picked up by hoppers.
     */
    private static final List<ItemStack> hopperBannedItems = Arrays.asList(ItemsManager.KruskSpawn, ItemsManager.EiryerasSpawn, ItemsManager.FeyrithSpawn, itemManager.getItem("krusk:Spawn"), itemManager.getItem("eiryeras:Spawn"), itemManager.getItem("feyrith:Spawn"));

    /**
     * When inventory slots are clicked.
     */
    @EventHandler
    public void invClick(InventoryClickEvent e) {

        Inventory clicked = e.getClickedInventory();
        InventoryView open = e.getWhoClicked().getOpenInventory();

        if (clicked == null) return;

        //If the player clicked on an armor slot we should update special effects
//        if (e.getSlotType() == InventoryType.SlotType.ARMOR) Bukkit.getServer().getScheduler().runTask(main.plugin, () -> updateEffects(Bukkit.getPlayer(e.getWhoClicked().getName())));
//        else if (open.getTitle().equals(ForgeManager.title)) ForgeManager.forgeClicked(e);
        /*else*/ if (open.getTitle().startsWith(ClassesManager.title)) ClassesManager.classClicked(e);
    }

    /**
     * When the player joins the server.
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerLogin (PlayerJoinEvent e) {
        try {
            //We need to load their data,
            RpgPlayer rpgPlayer = new RpgPlayer(e.getPlayer());
            PlayerManager.addPlayer(rpgPlayer);
            PlayerManager.scaleHealth(e.getPlayer());
            // io.github.math0898.rpgframework.PlayerManager.onJoin()
//            Bukkit.getServer().getScheduler().runTaskLater(main.plugin, () -> rpgPlayer.heal(), 5);
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
        PlayerManager.removePlayer(e.getPlayer().getUniqueId());
    }

    /**
     * When a mob gets damaged.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void entityDamaged(EntityDamageByEntityEvent e) {

        if (e.getDamager() instanceof Player && !e.isCancelled()) Objects.requireNonNull(PlayerManager.getPlayer(e.getDamager().getUniqueId())).attacker(e);

        if (e.getEntity() instanceof Player && !e.isCancelled()) PlayerManager.onDamage(e);
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
        else if (m.getType() == EntityType.WITHER_SKELETON) MobManager.witherSkeletonDrops(e);

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
            ItemStack item = event.getItem().getItemStack();
            if (hopperBannedItems.contains(item))
                event.setCancelled(true);
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

    static List<EntityDamageEvent.DamageCause> ignored = List.of(PROJECTILE, ENTITY_ATTACK, ENTITY_EXPLOSION, ENTITY_SWEEP_ATTACK, THORNS);

    /**
     * Called whenever a player suffers environmental damage.
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityEnvironmentalDamage (EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (ignored.contains(event.getCause())) return;
            if (event instanceof EntityDamageByEntityEvent) return;
            PlayerManager.environmentalDamage(event);
        }
    }

    /**
     * A listener that is called whenever an Item takes durability loss.
     *
     * @param event The Item durability loss event.
     */
    @EventHandler
    public void onDurabilityLoss (PlayerItemDamageEvent event) {
        if (isArmor(event.getItem().getType()))
            event.setCancelled(true);
    }
}
