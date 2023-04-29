package sugaku.rpg.mobs.teir1.krusk;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sugaku.rpg.framework.items.Rarity;
import sugaku.rpg.mobs.CustomMob;

import java.util.*;

import static org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER;
import static org.bukkit.Material.*;
import static sugaku.rpg.framework.items.ItemsManager.*;
import static org.bukkit.entity.EntityType.ZOMBIE;
import static sugaku.rpg.framework.mobs.MobManager.drop;

public class KruskBoss extends CustomMob {

    private final ItemStack helm = new ItemStack(DIAMOND_HELMET, 1);
    private final ItemStack chestplate = new ItemStack(IRON_CHESTPLATE, 1);
    private final ItemStack leggings = new ItemStack(LEATHER_LEGGINGS, 1);
    private final ItemStack boots = new ItemStack(DIAMOND_BOOTS, 1);
    private static final String name = "Krusk, Undead General";

    /**
     * A bare bones constructor not intended for spawning.
     */
    public KruskBoss() { super(name, ZOMBIE, Rarity.UNCOMMON, 250); }

    /**
     * A constructor for Krusk the Boss.
     */
    public KruskBoss(Location l) {
        super(name, ZOMBIE, Rarity.UNCOMMON, 250);
        init();
        setArmor(this.helm, this.chestplate, this.leggings, this.boots);
        setHand(getWeapon());
        setOffHand(new ItemStack(SHIELD, 1));
        setActiveMechanics(3);

        spawn(l);

        ((Ageable) getEntity()).setAdult(); //Forces adults
    }

    /**
     * Updates the items on the object to have the needed meta.
     */
    private void init(){
        ItemMeta meta = helm.getItemMeta();
        assert meta != null;
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(new UUID(1, 1), "generic.health", 75, ADD_NUMBER, EquipmentSlot.HEAD));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(new UUID(1, 2), "generic.armor", -0.5, ADD_NUMBER, EquipmentSlot.HEAD));
        meta.setUnbreakable(true);
        helm.setItemMeta(meta);

        meta = chestplate.getItemMeta();
        assert meta != null;
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(new UUID(2, 1), "generic.health", 75, ADD_NUMBER, EquipmentSlot.CHEST));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(new UUID(2, 2), "generic.armor", -0.5, ADD_NUMBER, EquipmentSlot.CHEST));
        meta.setUnbreakable(true);
        chestplate.setItemMeta(meta);

        meta = leggings.getItemMeta();
        assert meta != null;
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(new UUID(3, 1), "generic.health", 75, ADD_NUMBER, EquipmentSlot.LEGS));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(new UUID(3, 2), "generic.armor", -0.5, ADD_NUMBER, EquipmentSlot.LEGS));
        meta.setUnbreakable(true);
        leggings.setItemMeta(meta);

        meta = boots.getItemMeta();
        assert meta != null;
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(new UUID(4, 1), "generic.health", 75, ADD_NUMBER, EquipmentSlot.FEET));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(new UUID(4, 2), "generic.armor", -0.5, ADD_NUMBER, EquipmentSlot.FEET));
        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(new UUID(4, 3), "generic.speed", 0.2, ADD_NUMBER, EquipmentSlot.FEET));
        meta.setUnbreakable(true);
        boots.setItemMeta(meta);
    }

    /**
     * Describes the weapon for the zombie boss.
     */
    private static ItemStack getWeapon() {
        ItemStack item = new ItemStack(IRON_AXE, 1);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;

        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(new UUID(5, 4), "generic.damage", 10, ADD_NUMBER, EquipmentSlot.HAND));
        item.setItemMeta(meta);

        return item;
    }

    public static String getName() { return name; }

    /**
     * Handles drops.
     */
    public static void handleDrops(EntityDeathEvent event) {

        CustomMob.handleDrops(event);
        event.setDroppedExp(50);
        Random r = new Random();
        LivingEntity e = event.getEntity();

        Objects.requireNonNull(e.getLocation().getWorld()).dropItem(e.getLocation(), new ItemStack(ROTTEN_FLESH, (int) (r.nextDouble() * 5) + 1));
        double roll = r.nextDouble();
        while (roll >= 0.37) roll = r.nextDouble(); // TODO: Actually implement.
             if (roll < 0.08) drop(KruskAxe, e.getLocation());
        else if (roll < 0.16) drop(KruskBoots, e.getLocation());
        else if (roll < 0.24) drop(KruskLeggings, e.getLocation());
        else if (roll < 0.32) drop(UndeadChestplate, e.getLocation());
        else if (roll < 0.36) drop(KruskHelmet, e.getLocation());
        else if (roll < 0.37) drop(KruskLore, e.getLocation());
    }

    /**
     * Actions to be taken when Krusk is damaged.
     */
    @Override
    public void damaged(Player attacker) {

        super.damaged(attacker);

        Random r = new Random();

        if (r.nextDouble() < 0.20) {
            send(attacker, ChatColor.GREEN + "Krusk's " + ChatColor.GRAY + "undead aura weakens your attacks.");
            attacker.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200,0));
        }
    }

    /**
     * Actions to be taken when Krusk falls bellow 40% life.
     */
    @Override
    public void secondMechanic(Player attacker) {

        super.secondMechanic(attacker);

        Random r = new Random();

        LivingEntity e = getEntity();
        Location l = e.getLocation();

        e.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 1, false, true));

        send(attacker, ChatColor.GREEN + "Krusk " + ChatColor.DARK_GRAY + "> " + ChatColor.GRAY + "HEED MY CALL MEN!! LEND ME YOUR AID IN THIS FIGHT!");

        int number = Math.max(Math.abs(r.nextInt()) % 4, 2);
        ArrayList<Integer> used = new ArrayList<>();

        for (int i  = 1; i <= number; i++) {
            int attempt = Math.abs(r.nextInt() % 8);
            while (used.contains(attempt)) {
                attempt = Math.abs(r.nextInt() % 8);
            }
            used.add(attempt);
            int Offset = r.nextInt() % 11;
            new KruskMinion(new Location(l.getWorld(), l.getX() + Offset, l.getY() + 2, l.getZ() + Offset), attempt);
        }
    }
}