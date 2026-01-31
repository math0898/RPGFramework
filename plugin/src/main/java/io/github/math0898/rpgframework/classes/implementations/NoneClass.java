package io.github.math0898.rpgframework.classes.implementations;

import sugaku.rpg.framework.players.RpgPlayer;
import io.github.math0898.rpgframework.classes.AbstractClass;

/**
 * The NoneClass is a class players have if they have no current class.
 *
 * @author Sugaku
 */
public class NoneClass extends AbstractClass {

    /**
     * Creates a new AbstractClass object which is specific to the given player.
     *
     * @param p The player this class is specific to.
     */
    public NoneClass (RpgPlayer p) {
        super(p);
    }
}
