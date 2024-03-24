package io.github.math0898.rpgframework.classes.implementations;

import io.github.math0898.rpgframework.Cooldown;
import io.github.math0898.rpgframework.PlayerManager;
import io.github.math0898.rpgframework.RpgPlayer;
import io.github.math0898.rpgframework.classes.AbstractClass;
import org.bukkit.Material;

/**
 * The berserker class deals significant damage to enemies using an axe, has superior regeneration capabilities and a
 * unique lifesteal mechanic.
 *
 * @author Sugaku
 */
public class BerserkerClass extends AbstractClass {

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
}
