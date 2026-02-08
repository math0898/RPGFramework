package sugaku.rpg.framework.mobs;

import io.github.math0898.rpgframework.RPGFramework;
import io.github.math0898.rpgframework.Rarity;
import io.github.math0898.rpgframework.items.ItemManager;
import io.github.math0898.utils.StringUtils;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import io.github.math0898.rpgframework.enemies.CustomMob;
import sugaku.rpg.mobs.gods.Inos;

import java.util.ArrayList;
import java.util.Random;

/**
 * The MobManager is used to manage custom mobs while they are out in the world, as well as augmenting normal mob drops.
 *
 * @author Sugaku
 */
public class MobManager {

    /**
     * The active MobManager instance.
     * -- GETTER --
     * Returns the active MobManager.
     */
    @Getter
    private static final MobManager instance = new MobManager();

    /**
     * Current alive mobs that the MobManager is watching.
     */
    private final ArrayList<CustomMob> mobs = new ArrayList<>();

    /**
     * Prevents instantiation.
     */
    private MobManager () {

    }

    /**
     * Checks whether the MobManager needs to be delegated to or not.
     *
     * @return True if mobs are active, otherwise false.
     */
    @Deprecated(forRemoval = true)
    public boolean needsChecks () {
        return !mobs.isEmpty();
    }

    /**
     * Runs the MobManager on the given EntityDamageByEntity event that it may need to consider.
     *
     * @param event The event to be considered.
     */
    public void run (EntityDamageByEntityEvent event) {
        if (mobs.isEmpty()) return;
        Entity damaged = event.getEntity();
        Entity attacker = event.getDamager();
        if (!(attacker instanceof Player)) return;

        for (CustomMob m: mobs) {
            if (m.getEntity() == null) continue;
            if (m.getEntity().getEntityId() == damaged.getEntityId()) {
                m.damaged((Player) attacker);
                double health = m.getEntity().getHealth();
                if (health < m.getActiveMechanics() * 0.20 && m.getActiveMechanics() >= 1)
                    m.firstMechanic((Player) attacker);
                else if (health < m.getBaseHealth() * 0.40 && m.getActiveMechanics() >= 2)
                    m.secondMechanic((Player) attacker);
            }
        }
    }

    /**
     * Adds a mob to the MobManager.
     *
     * @param m The custom mob to add.
     */
    public void addMob (CustomMob m) {
        mobs.add(m);
    }

    /**
     * Adds Krusk drops to vanilla zombie deaths.
     *
     * @param event The mob death to consider.
     */
    public void zombieDrops (EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        Random r = new Random();
        double drops = r.nextDouble();
//        int gearScore = RpgPlayer.getGearScore(event.getEntity().getKiller());
        ItemStack item = event.getEntity().getKiller().getEquipment().getItemInHand();
        Integer tmp = item.getEnchantments().get(Enchantment.LOOT_BONUS_MOBS);
        int bonus = tmp == null ? 0 : tmp;
        if (drops < 0.02 + (bonus / 100.0))
//        if (drops < 2.0/((Math.abs(gearScore - 100)) + 2))
            event.getDrops().add(ItemManager.getInstance().getItem("krusk:Spawn"));
    }

    /**
     * Adds Eiryeras drops to vanilla skeleton deaths.
     *
     * @param event The mob death to consider.
     */
    public void skeletonDrops (EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        Random r = new Random();
        double drops = r.nextDouble();
        ItemStack item = event.getEntity().getKiller().getEquipment().getItemInHand();
        Integer tmp = item.getEnchantments().get(Enchantment.LOOT_BONUS_MOBS);
        int bonus = tmp == null ? 0 : tmp;
        if (drops < 0.02 + (bonus / 100.0))
//        if (drops < 2.0/((Math.abs(gearScore - 100)) + 2))
            event.getDrops().add(ItemManager.getInstance().getItem("eiryeras:Spawn"));
    }

    /**
     * Adds Feyrith drops to vanilla wither skeleton deaths.
     *
     * @param event The mob death to consider.
     */
    public void witherSkeletonDrops (EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        Random r = new Random();
        double drops = r.nextDouble();
        // int gearScore = RpgPlayer.getGearScore(event.getEntity().getKiller());
        ItemStack item = event.getEntity().getKiller().getEquipment().getItemInHand();
        Integer tmp = item.getEnchantments().get(Enchantment.LOOT_BONUS_MOBS);
        int bonus = tmp == null ? 0 : tmp;
        if (drops < 0.02 + (bonus / 100.0))
//        if (drops < 2.0/((Math.abs(gearScore - 100)) + 2))
            event.getDrops().add(ItemManager.getInstance().getItem("feyrith:Spawn"));}

    /**
     * Implements the chance for chickens to spawn Inos.
     *
     * @param event The mob death to consider.
     */
    public void chickenDrops (EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        Random r = new Random();
        double drops = r.nextDouble();
        if (drops < 0.004) {
            event.getEntity().getWorld().strikeLightningEffect(event.getEntity().getLocation());
            event.getEntity().getKiller().sendTitle( "", StringUtils.convertHexCodes("#CCCCCCYou feel the presence of a " + Rarity.MYTHIC.getHexColor() + "god#CCCCCC."), 20, 80, 20);
            CustomMob boss = new Inos(event.getEntity().getLocation());
            RPGFramework.console(boss.getCustomName() + ChatColor.GRAY + " is being spawned at: " + ChatColor.GRAY + event.getEntity().getLocation() + ChatColor.GRAY + " - " + event.getEntity().getKiller(), ChatColor.GRAY);
        }
    }
}
