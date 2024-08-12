package io.github.math0898.rpgframework.classes.implementations;

import io.github.math0898.rpgframework.Cooldown;
import io.github.math0898.rpgframework.PlayerManager;
import io.github.math0898.rpgframework.RpgPlayer;
import io.github.math0898.rpgframework.classes.AbstractClass;
import io.github.math0898.rpgframework.damage.events.AdvancedDamageEvent;
import io.github.math0898.rpgframework.damage.DamageType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffectType;

/**
 * The berserker class deals significant damage to enemies using an axe, has superior regeneration capabilities and a
 * unique lifesteal mechanic.
 *
 * @author Sugaku
 */
public class BerserkerClass extends AbstractClass { // todo: Re-add regeneration bonus.

    /**
     * An enum which represents the Berserker's abilities.
     *
     * @author Sugaku
     */
    enum Abilities {

        /**
         * The haste ability grants the user speed 2 for a short time.
         */
        HASTE,

        /**
         * The rage ability gives the user strength 2.
         */
        RAGE,

        /**
         * The indomitable spirit ability keeps the berserker alive after death to deal even more damage.
         */
        INDOMITABLE_SPIRIT;
    }

    /**
     * Creates a new AbstractClass object which is specific to the given player.
     *
     * @param p The player this class is specific to.
     */
    public BerserkerClass (RpgPlayer p) {
        super(p);
        Cooldown[] cds = new Cooldown[3];
        cds[Abilities.HASTE.ordinal()] = new Cooldown(30);
        cds[Abilities.RAGE.ordinal()] = new Cooldown(60);
        cds[Abilities.INDOMITABLE_SPIRIT.ordinal()] = new Cooldown(180);
        setCooldowns(cds);
        setClassItems(Material.ROTTEN_FLESH);
    }

    /**
     * Creates a new AbstractClass object which is specific to the given player.
     *
     * @param p The player this class is specific to.
     */
    @Deprecated
    public BerserkerClass (sugaku.rpg.framework.players.RpgPlayer p) {
        this(PlayerManager.getPlayer(p.getUuid()));
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
        if (!correctArmor()) send("Use leather middle pieces to use abilities.");
        else if (offCooldown(Abilities.RAGE.ordinal())) {
            send(ChatColor.GREEN + "You've used rage!");
            getPlayer().addPotionEffect(PotionEffectType.STRENGTH, 10 * 20, 2);
            getCooldowns()[Abilities.RAGE.ordinal()].restart();
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
        if (!correctArmor()) send("Use leather middle pieces to use abilities.");
        else if (offCooldown(Abilities.HASTE.ordinal())) {
            send(ChatColor.GREEN + "You've used haste!");
            getPlayer().addPotionEffect(PotionEffectType.SPEED, 10 * 20, 2);
            getCooldowns()[Abilities.HASTE.ordinal()].restart();
        }
    }

    /**
     * Called when the class user has 'died'.
     *
     * @return Whether a death should be respected or not.
     */
    @Override
    public boolean onDeath () {
        if (offCooldown(Abilities.INDOMITABLE_SPIRIT.ordinal())) {
            send(ChatColor.GREEN + "You've used " + ChatColor.GOLD + "Indomitable Spirit" + ChatColor.GREEN + "!");
            RpgPlayer rpg = getPlayer();
            Player player = rpg.getBukkitPlayer();
            player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 0.8f, 1.0f);
            rpg.addPotionEffect(PotionEffectType.STRENGTH, 5 * 20, 3);
            getCooldowns()[Abilities.INDOMITABLE_SPIRIT.ordinal()].restart();
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
            case CHEST -> type == Material.LEATHER_CHESTPLATE;
            case LEGS -> type == Material.LEATHER_LEGGINGS;
            default -> true;
        };
    }

    /**
     * A Utility method to determine if the Berserker is using the correct weapon.
     * Can probably be abstracted to AbstractClass.
     */
    private boolean correctWeapon () {
        EntityEquipment equipment = getPlayer().getBukkitPlayer().getEquipment();
        if (equipment == null) return false;
        Material type = equipment.getItem(EquipmentSlot.HAND).getType();
        return switch (type) {
            case WOODEN_AXE, STONE_AXE, IRON_AXE, DIAMOND_AXE, NETHERITE_AXE, GOLDEN_AXE -> true;
            default -> false;
        };
    }

    /**
     * Called whenever this DamageModifier is relevant on a defensive front.
     *
     * @param event The AdvancedDamageEvent to consider.
     */
    @Override
    public void damaged (AdvancedDamageEvent event) {
        if (correctArmor())
            event.addDamage(-10.0, event.getPrimaryDamage());
        if (getCooldowns()[Abilities.INDOMITABLE_SPIRIT.ordinal()].getRemaining() >= 175) event.setCancelled(true);
    }

    /**
     * Called whenever this DamageModifier is relevant on an offensive front.
     *
     * @param event The AdvancedDamageEvent to consider.
     */
    @Override
    public void attack (AdvancedDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity)
            if (event.getPrimaryDamage().isPhysical())
                if (correctWeapon())
                    event.addDamage(10.0, DamageType.SLASH);
    }
}
