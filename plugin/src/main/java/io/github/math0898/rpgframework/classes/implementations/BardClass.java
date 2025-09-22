package io.github.math0898.rpgframework.classes.implementations;

import io.github.math0898.rpgframework.Cooldown;
import io.github.math0898.rpgframework.PlayerManager;
import io.github.math0898.rpgframework.RpgPlayer;
import io.github.math0898.rpgframework.classes.AbstractClass;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * The bard class casts powerful aura buffs to themselves and their party.
 *
 * @author Sugaku
 */
public class BardClass extends AbstractClass {

    /**
     * A short enum of bard buffs to help keep track of.
     *
     * @author Sugaku
     */
    private enum BardBuffs {

        /**
         * {@link org.bukkit.potion.PotionEffectType#REGENERATION}
         */
        REGENERATION(ChatColor.LIGHT_PURPLE + "Regeneration", PotionEffectType.REGENERATION),

        /**
         * {@link org.bukkit.potion.PotionEffectType#SPEED}
         */
        SWIFTNESS(ChatColor.AQUA + "Swiftness", PotionEffectType.SPEED),

        /**
         * {@link org.bukkit.potion.PotionEffectType#INCREASE_DAMAGE}
         */
        STRENGTH(ChatColor.RED + "Strength", PotionEffectType.INCREASE_DAMAGE);

        /**
         * The text to display this buff as.
         */
        private final String display;

        /**
         * The PotionEffectType that this buff represents.
         */
        private final PotionEffectType type;

        /**
         * Creates a new BardBuff with the given display name and potion buff type.
         *
         * @param display The string to display this buff as.
         * @param type    The potion type that this buff gives.
         */
        BardBuffs (String display, PotionEffectType type) {
            this.display = display;
            this.type = type;
        }

        /**
         * Returns this buff in a nicely formatted way to display to players.
         *
         * @return The string formatted version of this buff.
         */
        public String toString () {
            return display;
        }

        /**
         * An accessor method to get the PotionEffectType of the current buff.
         *
         * @return The current PotionEffect.
         */
        public PotionEffectType getType () {
            return type;
        }
    };

    /**
     * A short enum of ability names to better keep track of cooldowns.
     *
     * @author Sugaku
     */
    private enum Abilities {

        /**
         * Hym is the bard's cast ability. It has a 60s cooldown by default.
         */
        HYM,

        /**
         * The bard's revive passive.
         */
        LIFE_OF_MUSIC;
    }

    /**
     * The buff this bard currently has selected.
     */
    private BardBuffs currentBuff = BardBuffs.REGENERATION;

    /**
     * Creates a new AbstractClass object which is specific to the given player.
     *
     * @param p The player this class is specific to.
     */
    public BardClass (RpgPlayer p) {
        super(p);
        Cooldown[] cds = new Cooldown[2];
        cds[Abilities.HYM.ordinal()] = new Cooldown(30);
        cds[Abilities.LIFE_OF_MUSIC.ordinal()] = new Cooldown(300);
        setCooldowns(cds);
        setClassItems(Material.NOTE_BLOCK);
    }

    /**
     * Creates a new AbstractClass object which is specific to the given player.
     *
     * @param p The player this class is specific to.
     */
    @Deprecated
    public BardClass (sugaku.rpg.framework.players.RpgPlayer p) {
        this(PlayerManager.getPlayer(p.getUuid()));
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
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!player.isSneaking()) {
                event.setCancelled(true);
                send("Shift + right click if you are trying to place that block.");
            } else return;
        }
        switch (currentBuff) {
            case REGENERATION -> currentBuff = BardBuffs.SWIFTNESS;
            case SWIFTNESS -> currentBuff = BardBuffs.STRENGTH;
            case STRENGTH -> currentBuff = BardBuffs.REGENERATION;
        }
        send(currentBuff.toString());
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
        if (offCooldown(Abilities.HYM.ordinal())) {
            send(ChatColor.GREEN + "You've used hym!");
            RpgPlayer player = getPlayer();
            List<RpgPlayer> players = player.friendlyCasterTargets();
            String username = player.getPlayerRarity() + player.getName();
            for (RpgPlayer rpg : players) {
                rpg.addPotionEffect(currentBuff.getType(), 45 * 20, 1);
                rpg.sendMessage(username + ChatColor.GREEN + " has used hym!");
            }
            getCooldowns()[Abilities.HYM.ordinal()].restart();
        }
    }

    /**
     * Called when the class user has 'died'.
     *
     * @return Whether a death should be respected or not.
     */
    @Override
    public boolean onDeath () {
        if(offCooldown(Abilities.LIFE_OF_MUSIC.ordinal())) {
            send(ChatColor.GREEN + "You've used " + ChatColor.GOLD + "A Life of Music" + ChatColor.GREEN + "!");
            RpgPlayer rpg = getPlayer();
            Player player = rpg.getBukkitPlayer();;
            player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 0.8f, 1.0f);
            rpg.heal(10.0);
            rpg.addPotionEffect(PotionEffectType.REGENERATION, 10 * 20, 2);
            rpg.addPotionEffect(PotionEffectType.SPEED, 10 * 20, 2);
            rpg.addPotionEffect(PotionEffectType.INCREASE_DAMAGE, 10 * 20, 2);
            getCooldowns()[Abilities.LIFE_OF_MUSIC.ordinal()].restart();
            return false;
        }
        return true;
    }
}
