package sugaku.rpg.framework.mobs;

import io.github.math0898.rpgframework.RPGFramework;
import io.github.math0898.rpgframework.Rarity;
import io.github.math0898.rpgframework.items.ItemManager;
import io.github.math0898.utils.StringUtils;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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
     * Augments vanilla mob deaths, most frequently by adding in boss drops, but also by occasionally spawning a boss.
     *
     * @param event The EntityDeathEvent to consider.
     */
    public void augmentVanillaMobDeath (EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return; // We only do stuff for player kills.
        Random r = new Random();
        double roll = r.nextDouble();

        // Effects that don't care about looting
        if (event.getEntity().getType().equals(EntityType.CHICKEN)) {
            event.getEntity().getWorld().strikeLightningEffect(event.getEntity().getLocation());
            event.getEntity().getKiller().sendTitle( "", StringUtils.convertHexCodes("#CCCCCCYou feel the presence of a " + Rarity.MYTHIC.getHexColor() + "god#CCCCCC."), 20, 80, 20);
            CustomMob boss = new Inos(event.getEntity().getLocation());
            RPGFramework.console(boss.getCustomName() + ChatColor.GRAY + " is being spawned at: " + ChatColor.GRAY + event.getEntity().getLocation() + ChatColor.GRAY + " - " + event.getEntity().getKiller(), ChatColor.GRAY);
        }

        ItemStack item = event.getEntity().getKiller().getEquipment().getItemInHand();
        Integer tmp = item.getEnchantments().get(Enchantment.LOOT_BONUS_MOBS);
        int bonus = tmp == null ? 0 : tmp;

        // Looting augmented effects.
        switch (event.getEntity().getType()) { // todo: This might work better being defined in an external file, and then constructed on load.
            case ZOMBIE -> {
                if (roll < 0.02 + (bonus / 100.0))
                    event.getDrops().add(ItemManager.getInstance().getItem("krusk:Spawn"));
            }
            case SKELETON -> {
                if (roll < 0.02 + (bonus / 100.0))
                    event.getDrops().add(ItemManager.getInstance().getItem("eiryeras:Spawn"));
            }
            case WITHER_SKELETON -> {
                if (roll < 0.02 + (bonus / 100.0))
                    event.getDrops().add(ItemManager.getInstance().getItem("feyrith:Spawn"));
            }
        }

        // Snippet to re-add gear score dependent loot drops
        //  -> if (drops < 2.0/((Math.abs(gearScore - 100)) + 2))
    }
}
