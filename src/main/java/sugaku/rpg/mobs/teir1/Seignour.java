package sugaku.rpg.mobs.teir1;

import io.github.math0898.utils.items.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import sugaku.rpg.framework.items.Rarity;
import sugaku.rpg.mobs.CustomMob;

public class Seignour extends CustomMob {

    /**
     * A basic constructor to aid in the construction of a CustomMob. Contains everything essential to run spawn().
     */
    public Seignour () {
        super("Seignour", EntityType.ZOMBIE, Rarity.RARE, 400);
        setHelm(new ItemBuilder(Material.GOLDEN_HELMET).build());
        setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setColor(new int[]{255, 255, 194, 115}).build());
        setLeggings(new ItemBuilder(Material.GOLDEN_LEGGINGS).build());
        setBoots(new ItemBuilder(Material.LEATHER_BOOTS).setColor(new int[]{255, 255, 140, 64}).build());
        setOffHand(new ItemBuilder(Material.GOLDEN_SHOVEL).build());
        setHand(new ItemBuilder(Material.WOODEN_SWORD).build());
    }

    /**
     * A basic constructor to aid in the construction of a CustomMob. Contains everything essential to run spawn().
     */
    public Seignour (Location locale) {
        this();
        spawn(locale);
    }

    /**
     *
     *
     * @return
     */
    public static String getName () {
        return "Seignour";
    }

    /**
     * Spawn the mob described by the CustomMob object at the given location l.
     *
     * @param l The location the mob should be spawned at.
     */
    @Override
    public void spawn (Location l) {
        super.spawn(l);
        ((Ageable) getEntity()).setAdult(); //Forces adults
    }
}
