package io.github.math0898.rpgframework.classes.implementations;

import io.github.math0898.rpgframework.Cooldown;
import io.github.math0898.rpgframework.PlayerManager;
import io.github.math0898.rpgframework.RpgPlayer;
import io.github.math0898.rpgframework.classes.AbstractClass;
import io.github.math0898.rpgframework.damage.AdvancedDamageEvent;
import io.github.math0898.rpgframework.damage.DamageResistance;
import io.github.math0898.rpgframework.damage.DamageType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;

import static org.bukkit.potion.PotionEffectType.*;

/**
 * The Paladin class has strong healing abilities and damage nullification abilities.
 *
 * @author Sugaku
 */
public class PaladinClass extends AbstractClass {

    /**
     * An enum with defines the Paladin's abilities. Used to make cooldowns more readable.
     *
     * @author Sugaku
     */
    private enum Abilities {

        /**
         * Cleanses the Paladin nullifying negative effects and granting a large amount of health.
         */
        PURIFY,

        /**
         * Gives the Paladin and party a strong regeneration effect.
         */
        MEND,

        /**
         * A powerful resurrection effect that heals the paladin a tremendous amount.
         */
        PROTECTION_OF_THE_HEALER
    }

    /**
     * Creates a new AbstractClass object which is specific to the given player.
     *
     * @param p The player this class is specific to.
     */
    public PaladinClass (RpgPlayer p) {
        super(p);
        Cooldown[] cds = new Cooldown[3];
        cds[Abilities.MEND.ordinal()] = new Cooldown(30);
        cds[Abilities.PURIFY.ordinal()] = new Cooldown(60);
        cds[Abilities.PROTECTION_OF_THE_HEALER.ordinal()] = new Cooldown(300);
        setCooldowns(cds);
        setClassItems(Material.GOLDEN_SHOVEL);
    }

    /**
     * Creates a new AbstractClass object which is specific to the given player.
     *
     * @param p The player this class is specific to.
     */
    @Deprecated
    public PaladinClass (sugaku.rpg.framework.players.RpgPlayer p) {
        this(PlayerManager.getPlayer(p.getUuid()));
    }

    /**
     * Called whenever a player left-clicks while holding a class item. To reach this method, the player must be holding
     * a class item. No promises are made if they're wearing armor or not.
     *
     * @param event The PlayerInteractEvent that lead to this method being called.
     * @param type  The type of material that was used in this cast.
     */
    public void onLeftClickCast (PlayerInteractEvent event, Material type) {
        if (!correctArmor()) send("Use full golden armor to use paladin spells.");
        else if (offCooldown(Abilities.MEND.ordinal())) {
            send(ChatColor.GREEN + "You've used mend!");
            RpgPlayer player = getPlayer();
            List<RpgPlayer> toApply = player.friendlyCasterTargets();
            String username = player.getPlayerRarity() + player.getName();
            for (RpgPlayer rpg: toApply) {
                rpg.addPotionEffect(REGENERATION, 15 * 20, 3);
                rpg.sendMessage(username + ChatColor.GREEN + " has used mend!");
            }
            getCooldowns()[Abilities.MEND.ordinal()].restart();
        }
    }

    /**
     * Called whenever a player right-clicks while holding a class item. To reach this method, the player must be
     * holding a class item. No promises are made if they're wearing armor or not.
     *
     * @param event The PlayerInteractEvent that lead to this method being called.
     * @param type  The type of material that was used in this cast.
     */
    public void onRightClickCast (PlayerInteractEvent event, Material type) {
        if (!correctArmor()) send("Use full golden armor to use paladin spells.");
        else if (offCooldown(Abilities.PURIFY.ordinal())) {
            send(ChatColor.GREEN + "You've used purify!");
            RpgPlayer player = getPlayer();
            List<RpgPlayer> toApply = player.friendlyCasterTargets();
            String username = player.getPlayerRarity() + player.getName();
            for (RpgPlayer rpg: toApply) {
                rpg.heal(5.0);
                rpg.getBukkitPlayer().setFireTicks(0);
                rpg.cleanseEffects(BLINDNESS, BAD_OMEN, CONFUSION, DARKNESS, HARM, HUNGER, POISON, SLOW, LEVITATION,
                        SLOW_DIGGING, UNLUCK, WEAKNESS, WITHER);
                rpg.sendMessage(username + ChatColor.GREEN + " has used purify!");
            }
            getCooldowns()[Abilities.PURIFY.ordinal()].restart();
        }
    }

    /**
     * Called when the class user has 'died'.
     *
     * @return Whether a death should be respected or not.
     */
    @Override
    public boolean onDeath () {
        if (offCooldown(Abilities.PROTECTION_OF_THE_HEALER.ordinal())) {
            send(ChatColor.GREEN + "You've used " + ChatColor.GOLD + "Protection of the Healer" + ChatColor.GREEN + "!");
            RpgPlayer player = getPlayer();
            player.getBukkitPlayer().getWorld().playSound(player.getBukkitPlayer().getLocation(), Sound.ITEM_TOTEM_USE, 0.8f, 1.0f);
            List<RpgPlayer> toApply = player.friendlyCasterTargets();
            String username = player.getPlayerRarity() + player.getName();
            for (RpgPlayer rpg: toApply) {
                rpg.addPotionEffect(REGENERATION, 10 * 20, 4);
                rpg.addPotionEffect(HEALTH_BOOST, 10 * 20, 1);
                rpg.heal(2.0);
                rpg.sendMessage(username + ChatColor.GREEN + " has used " + ChatColor.GOLD + "Protection of the Healer" + ChatColor.GREEN + "!");
            }
            getCooldowns()[Abilities.PROTECTION_OF_THE_HEALER.ordinal()].restart();
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
            case HEAD -> type == Material.GOLDEN_HELMET;
            case CHEST -> type == Material.GOLDEN_CHESTPLATE;
            case LEGS -> type == Material.GOLDEN_LEGGINGS;
            case FEET -> type == Material.GOLDEN_BOOTS;
            default -> true;
        };
    }

    /**
     * Called whenever this DamageModifier is relevant on a defensive front.
     *
     * @param event The AdvancedDamageEvent to consider.
     */
    @Override
    public void damaged (AdvancedDamageEvent event) {
        for (DamageType type : new DamageType[]{DamageType.HOLY, DamageType.IMPACT, DamageType.SLASH, DamageType.UNSPECIFIED, DamageType.PUNCTURE})
            event.setResistance(type, DamageResistance.RESISTANCE);
    }

    /**
     * Called whenever this DamageModifier is relevant on an offensive front.
     *
     * @param event The AdvancedDamageEvent to consider.
     */
    @Override
    public void attack (AdvancedDamageEvent event) {
        event.addDamage(-2.5, event.getPrimaryDamage());
    }
}
