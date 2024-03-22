package io.github.math0898.rpgframework.classes.implementations;

import io.github.math0898.rpgframework.Cooldown;
import io.github.math0898.rpgframework.PlayerManager;
import io.github.math0898.rpgframework.RpgPlayer;
import io.github.math0898.rpgframework.classes.AbstractClass;
import io.github.math0898.rpgframework.damage.AdvancedDamageEvent;
import io.github.math0898.rpgframework.damage.DamageType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

/**
 * Class implementation specific to the assassin class.
 *
 * @author Sugaku
 */
public class AssassinClass extends AbstractClass {

    /**
     * An enum with defines the Assassin's abilities. Used to make cooldowns more readable.
     *
     * @author Sugaku
     */
    private enum Abilities {
        /**
         * Makes the player damage immune and provides the invisibility potion effect for 10 seconds.
         */
        INVISIBILITY,

        /**
         * Causes victims to receive poison and blindness.
         */
        POISONED_BLADE,

        /**
         * Masterfully dodges lethal damage and grants a quick burst of speed.
         */
        HEROIC_DODGE;
    }

    /**
     * Creates a new AbstractClass object which is specific to the given player.
     *
     * @param p The player this class is specific to.
     */
    public AssassinClass (RpgPlayer p) {
        super(p);
        Cooldown[] cds = new Cooldown[3];
        cds[Abilities.HEROIC_DODGE.ordinal()] = new Cooldown(300);
        cds[Abilities.POISONED_BLADE.ordinal()] = new Cooldown(60);
        cds[Abilities.INVISIBILITY.ordinal()] = new Cooldown(30);
        setCooldowns(cds);
        setClassItems(Material.GHAST_TEAR);
    }

    /**
     * Creates a new AbstractClass object which is specific to the given player.
     *
     * @param p The player this class is specific to.
     */
    @Deprecated
    public AssassinClass (sugaku.rpg.framework.players.RpgPlayer p) {
        this(PlayerManager.getPlayer(p.getUuid()));
    }

    /**
     * Called whenever this DamageModifier is relevant on a defensive front.
     *
     * @param event The AdvancedDamageEvent to consider.
     */
    @Override
    public void damaged (AdvancedDamageEvent event) {
        if (getCooldowns()[Abilities.HEROIC_DODGE.ordinal()].getRemaining() >= 290) event.setCancelled(true);
        else if (getCooldowns()[Abilities.INVISIBILITY.ordinal()].getRemaining() >= 20) event.setCancelled(true);
        double roll = new Random().nextDouble();
        if (roll <= 0.10) event.setCancelled(true);
    }

    /**
     * Called whenever this DamageModifier is relevant on an offensive front.
     *
     * @param event The AdvancedDamageEvent to consider.
     */
    @Override
    public void attack (AdvancedDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity entity)
            if (event.getPrimaryDamage().isPhysical()) {
                if (entity instanceof Player) event.addDamage(5.0, DamageType.SLASH);
                else event.addDamage(10.0, DamageType.SLASH);
                if (getCooldowns()[Abilities.POISONED_BLADE.ordinal()].getRemaining() >= 50)
                    for (PotionEffectType type : new PotionEffectType[]{PotionEffectType.BLINDNESS, PotionEffectType.POISON, PotionEffectType.SLOW})
                        entity.addPotionEffect(new PotionEffect(type, 10 * 20, 0));
            }
    }

    /**
     * Called once every 20 seconds to apply a passive effect to the player.
     */
    @Override
    public void passive () {
        if (correctArmor())
            getPlayer().addPotionEffect(PotionEffectType.SPEED, 21 * 20, 1, true, false);
    }

    /**
     * Called whenever the player attached to this class object interacts with the world.
     *
     * @param event The player interact event.
     */
    @Override
    public void onInteract (PlayerInteractEvent event) {
        if (!event.hasItem()) return;
        ItemStack item = event.getItem();
        if (item == null) return;
        if (isClassItem(item.getType()) != -1)  // Assassins only have a single class item.
            if (correctArmor())
                switch (event.getAction()) {
                    case RIGHT_CLICK_BLOCK, RIGHT_CLICK_AIR -> invisibility();
                    case LEFT_CLICK_AIR, LEFT_CLICK_BLOCK -> poisonedBlade();
                }
    }

    /**
     * Called when the class user has 'died'.
     *
     * @return Whether a death should be respected or not.
     */
    @Override
    public boolean onDeath () {
        if (offCooldown(Abilities.HEROIC_DODGE.ordinal()) && correctArmor()) {
            send(ChatColor.GREEN + "You've used " + ChatColor.GOLD + "Heroic Dodge" + ChatColor.GREEN + "!");
            getPlayer().getBukkitPlayer().playSound(getPlayer().getBukkitPlayer().getLocation(), Sound.ITEM_TOTEM_USE, 0.8f, 1.0f);
            getPlayer().addPotionEffect(PotionEffectType.SPEED, 10*20, 2);
            getCooldowns()[Abilities.HEROIC_DODGE.ordinal()].restart();
            return false;
        }
        return true;
    }

    /**
     * Checks a specific slot to see if this player is wearing their class armor or not.
     *
     * @param slot The slot to check to see if it is the correct armor type.
     * @return True if the player is wearing the correct armor for that slot.
     */
    @Override
    public boolean correctArmor (EquipmentSlot slot) {
        EntityEquipment equipment = getPlayer().getBukkitPlayer().getEquipment();
        if (equipment == null) return false;
        Material type = equipment.getItem(slot).getType();
        return type.equals(Material.LEATHER_HELMET) ||
                type.equals(Material.LEATHER_CHESTPLATE) ||
                type.equals(Material.LEATHER_LEGGINGS) ||
                type.equals(Material.LEATHER_BOOTS);
    }

    /**
     * Activates the Invisibility ability. We can assume the player is wearing the correct gear.
     */
    private void invisibility () {
        if (offCooldown(Abilities.INVISIBILITY.ordinal())) {
            send(ChatColor.GREEN + "You've used invisibility!");
            getPlayer().addPotionEffect(PotionEffectType.INVISIBILITY, 10*20, 1);
            getCooldowns()[Abilities.INVISIBILITY.ordinal()].restart();
        }
    }

    /**
     * Activates the Poisoned Blade ability. We can assume the player is wearing the correct gear.
     */
    private void poisonedBlade () {
        if (offCooldown(Abilities.POISONED_BLADE.ordinal())) {
            send(ChatColor.GREEN + "You've used poisoned blade!");
            getCooldowns()[Abilities.POISONED_BLADE.ordinal()].restart();
        }
    }
}
