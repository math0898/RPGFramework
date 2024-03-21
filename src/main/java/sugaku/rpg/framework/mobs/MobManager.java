package sugaku.rpg.framework.mobs;

import io.github.math0898.rpgframework.RPGFramework;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import sugaku.rpg.framework.players.RpgPlayer;
import sugaku.rpg.framework.items.ItemsManager;
import sugaku.rpg.mobs.CustomMob;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class MobManager {


    private static final ArrayList<CustomMob> mobs = new ArrayList<>();

    public static boolean needsChecks() { return !mobs.isEmpty(); }

    public static void run(EntityDamageByEntityEvent event) {
        Entity damaged = event.getEntity();
        Entity attacker = event.getDamager();

        if (attacker instanceof Player) for (CustomMob m: mobs) if (m.getEntity().getEntityId() == damaged.getEntityId()) {
            m.damaged((Player) attacker);
            double health = m.getEntity().getHealth();
            if (health < m.getActiveMechanics() * 0.20 && m.getActiveMechanics() >= 1) m.firstMechanic((Player) attacker);
            else if (health < m.getMaxHealth() * 0.40 && m.getActiveMechanics() >= 2) m.secondMechanic((Player) attacker);
        }
    }

    public static void clean() { mobs.removeIf(m -> m.getEntity().isDead()); }

    public static void addMob(CustomMob m) { mobs.add(m); }

    public static boolean isSummoned(CustomMob m) {
        for (CustomMob c: mobs) if (m.getCustomName().equals(c.getCustomName())) return true;
        return false;
    }

    /**
     * Drops an item at the given location.
     * @param item The item to be dropped.
     * @param l The location for the item to be dropped at.
     */
    public static void drop(ItemStack item, Location l) { Objects.requireNonNull(l.getWorld()).dropItem(l, item); }

    public static void zombieDrops (EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        Random r = new Random();
        double drops = r.nextDouble();
        int gearScore = RpgPlayer.getGearScore(event.getEntity().getKiller());
        if (drops < 2.0/((Math.abs(gearScore - 100)) + 2)) drop(RPGFramework.itemManager.getItem("krusk:Spawn"), event.getEntity().getLocation());
    }

    public static void skeletonDrops (EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        Random r = new Random();
        double drops = r.nextDouble();
        int gearScore = RpgPlayer.getGearScore(event.getEntity().getKiller());
        if (drops < 2.0/((Math.abs(gearScore - 100)) + 2)) drop(RPGFramework.itemManager.getItem("eiryeras:Spawn"), event.getEntity().getLocation());
    }
}
