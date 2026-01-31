package sugaku.rpg.mobs.teir1.krusk;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sugaku.rpg.framework.items.ItemsManager;
import sugaku.rpg.framework.items.Rarity;
import io.github.math0898.rpgframework.enemies.CustomMob;

/**
 * KruskMinion describes what makes Krusk minions Krusk minions. There is not much extra to Krusk minions outside of
 * their equipment and potential names.
 */
public class KruskMinion extends CustomMob {

    /**
     * Here are the nine names of Krusk's minions. A name is selected randomly and then from that their equipment is
     * selected from the array bellow. The last three are considered tanky bois and have additional health but reduced
     * damage.
     */
    private static final String[] names = { "Bagazvaut", "Thundag", "Druukrug", "Giaborg", "Drinva", "Nerark", "Hrakteth", "Hostruurm", "Thordrol" };

    /**
     * The weapons of each minion of Krusk. Each minion depending on the name will spawn with the same weapon every time.
     */
    private static final Material[] weapons = { Material.NETHERITE_SWORD, Material.IRON_AXE, Material.STICK, Material.IRON_SWORD, Material.IRON_SHOVEL, Material.IRON_SHOVEL, Material.DIAMOND_SWORD, Material.IRON_HOE, Material.DIAMOND_HOE};

    /**
     * Constructor which spawns a KruskMinion at the given location l. It also handles some outside things such as
     * equipping items and max health.
     * @param l The location the Krusk minion should spawn at.
     * @param i The index of the Krusk minion we should spawn at the given location.
     */
    public KruskMinion(Location l, int i) {
        super(names[i] + ", Underling of Krusk", EntityType.ZOMBIE, Rarity.UNCOMMON, 40);
        ItemStack main = new ItemStack(weapons[i], 1);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        ItemMeta bootsMeta = boots.getItemMeta();
        ItemMeta meta = main.getItemMeta();
        assert meta != null;
        assert bootsMeta != null;
        meta.setUnbreakable(true);
        bootsMeta.setUnbreakable(true);
        if (i > 5) {
            setBaseHealth(60);
            bootsMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, ItemsManager.attributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, 0.12, EquipmentSlot.FEET));
            bootsMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, ItemsManager.attributeModifier(Attribute.GENERIC_ARMOR, -17, EquipmentSlot.FEET));
            setArmor(new ItemStack(Material.LEATHER_HELMET), new ItemStack(Material.NETHERITE_CHESTPLATE), new ItemStack(Material.NETHERITE_LEGGINGS), new ItemStack(Material.LEATHER_BOOTS));
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, ItemsManager.attributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, 1, EquipmentSlot.HAND));
        } else {
            bootsMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, ItemsManager.attributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, 0.18, EquipmentSlot.FEET));
            bootsMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, ItemsManager.attributeModifier(Attribute.GENERIC_ARMOR, -11, EquipmentSlot.FEET));
            setArmor(new ItemStack(Material.LEATHER_HELMET), new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.LEATHER_BOOTS, 1));
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, ItemsManager.attributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, 2.5, EquipmentSlot.HAND));
        }
        main.setItemMeta(meta);
        boots.setItemMeta(bootsMeta);
        setBoots(boots);
        setHand(main);
        spawn(l);

        ((Ageable) getEntity()).setAdult();
    }

    /**
     * Does all the drops for Krusk minions. They shouldn't drop any items and instead only drop 10xp which is still
     * more than most hostile mobs.
     * @param e The EntityDeathEvent of the Krusk minion which is being handled.
     */
    public static void handleDrops(EntityDeathEvent e) {

        CustomMob.handleDrops(e);

        e.setDroppedExp(10);
        e.getDrops().clear();
    }
}
