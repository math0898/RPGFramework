package sugaku.rpg.mobs.gods;

import io.github.math0898.rpgframework.items.ItemManager;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDeathEvent;
import sugaku.rpg.framework.items.Rarity;
import io.github.math0898.rpgframework.enemies.CustomMob;

import static sugaku.rpg.framework.mobs.MobManager.drop;

/**
 * Inos is the god of spring. This is a minor aspect of him in the form of a chicken.
 *
 * @author Sugaku
 */
public class Inos extends CustomMob {

    /**
     * Makes sure that items and other things required for the boss fight are set up. <b>This will also spawn the boss.</b>
     *
     * @param location The location to spawn the boss at.
     */
    public Inos (Location location) {
        this();
        spawn(location);
    }

    /**
     * A basic constructor to aid in the construction of a CustomMob. Contains everything essential to run spawn().
     */
    public Inos () {
        super("Inos", EntityType.CHICKEN, Rarity.MYTHIC, 1000);
    }

    /**
     * Handles drops related to the first Inos boss.
     *
     * @param event The EntityDeathEvent.
     */
    public static void handleDrops (EntityDeathEvent event) {
        System.out.println("Inos Died");
        CustomMob.handleDrops(event);
        drop(ItemManager.getInstance().getItem("gods:InosAspectI"), event.getEntity().getLocation());
        event.setDroppedExp(500);
    }
}
