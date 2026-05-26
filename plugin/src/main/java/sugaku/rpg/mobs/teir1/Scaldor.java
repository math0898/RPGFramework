package sugaku.rpg.mobs.teir1;

import io.github.math0898.rpgframework.Rarity;
import io.github.math0898.rpgframework.enemies.CustomMob;
import io.github.math0898.utils.items.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class Scaldor extends CustomMob {

    /**
     * A basic constructor to aid in the construction of a CustomMob. Contains everything essential to run spawn().
     */
    public Scaldor () {
        super("Scaldor", EntityType.BLAZE, Rarity.LEGENDARY, 4000);
        // todo: Actual gear.
        setHelm(new ItemBuilder(Material.PLAYER_HEAD).setSkullSkinBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzM0NTJiOThhYjlhODhkMTc1N2YwMzJjMDcyYWY4MWNmYTM1ZGRiNDc5NDU4NTkxNDc4MTFiY2RjZmQ5ODcxZSJ9fX0").build());
        setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setColor(new int[]{255, 255, 194, 115}).build());
        setLeggings(new ItemBuilder(Material.GOLDEN_LEGGINGS).build());
        setBoots(new ItemBuilder(Material.LEATHER_BOOTS).setColor(new int[]{255, 255, 140, 64}).build());
    }

    /**
     *
     *
     * @return
     */
    public static String getName () {
        return "Scaldor";
    }

    /**
     * Spawn the mob described by the CustomMob object at the given location l.
     *
     * @param l The location the mob should be spawned at.
     */
    @Override
    public void spawn (Location l) {
        super.spawn(l);
    }
}
