package io.github.math0898.rpgframework.classes.implementations;

import io.github.math0898.rpgframework.Cooldown;
import sugaku.rpg.framework.players.RpgPlayer;
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
     * Called whenever a player left-clicks while holding a class item. To reach this method, the player must be holding
     * a class item. No promises are made if they're wearing armor or not.
     *
     * @param event The PlayerInteractEvent that lead to this method being called.
     * @param type  The type of material that was used in this cast.
     */
    @Override
    public void onLeftClickCast (PlayerInteractEvent event, Material type) {
        if (!correctArmor()) send("Use full leather armor to use assassin abilities.");
        else if (offCooldown(Abilities.POISONED_BLADE.ordinal())) {
            send(ChatColor.GREEN + "You've used poisoned blade!");
            getCooldowns()[Abilities.POISONED_BLADE.ordinal()].restart();
        }
    }

    /**
     * Called whenever a player right-clicks while holding a class item. To reach this method, the player must be
     * holding a class item. No promises are made if they're wearing armor or not.
     *
     * @param event The PlayerInteractEvent that lead to this method being called.
     * @param type  The type of material that was used in this cast.
     */
    @Override
    public void onRightClickCast (PlayerInteractEvent event, Material type) {
        if (!correctArmor()) send("Use full leather armor to use assassin abilities.");
        else if (offCooldown(Abilities.INVISIBILITY.ordinal())) {
            send(ChatColor.GREEN + "You've used invisibility!");
            getPlayer().addPotionEffect(PotionEffectType.INVISIBILITY, 10*20, 1);
            getCooldowns()[Abilities.INVISIBILITY.ordinal()].restart();
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
        return switch (slot) {
            case HEAD -> type == Material.LEATHER_HELMET;
            case CHEST -> type == Material.LEATHER_CHESTPLATE;
            case LEGS -> type == Material.LEATHER_LEGGINGS;
            case FEET -> type == Material.LEATHER_BOOTS;
            default -> true;
        };
    }
}
