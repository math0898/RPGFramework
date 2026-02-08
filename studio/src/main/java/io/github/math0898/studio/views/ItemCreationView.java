package io.github.math0898.studio.views;

import io.github.math0898.studio.ItemPreview;
import suga.engine.game.BasicScene;
import suga.engine.game.Game;
import suga.engine.input.keyboard.KeyValue;

import java.awt.*;

/**
 * The ItemCreationView is used to aid in the creation of new items for the game in RPG's unique format.
 *
 * @author Sugaku
 */
public class ItemCreationView extends BasicScene {

    /**
     * The fields that can be actively edited.
     */
    private enum Selection {

        /**
         *
         */
        NAME,

        RARITY,

        LORE,

        DAMAGE,

        ATTACK_SPEED,

        HEALTH,

        ARMOR,

        TOUGHNESS;

        /**
         * Gets the next enum value.
         */
        public Selection nextSelection () {
            return TOUGHNESS; // todo: Implement.
        }

        /**
         * Gets the previous enum value.
         */
        public Selection previousSelection () {
            return NAME; // todo: Implement.
        }
    }

    /**
     * The active ItemPreview in this view.
     */
    private final ItemPreview preview = new ItemPreview();

    /**
     * The currently active selection.
     */
    private Selection activeSelection = Selection.NAME;

    /**
     * Loads this scene into the given game.
     */
    public boolean load (Game game) {
        game.clear();
        game.addGameObject("Item Preview", preview);
        return true;
    }


    /**
     * Passes a keyboard input into the scene.
     *
     * @param key     The value of the key pressed.
     * @param pressed True if the key was pressed, false if it was released.
     */
    @Override
    public void keyboardInput (KeyValue key, boolean pressed) {
        if (!pressed) return;
        switch (key) {
            case ARROW_UP -> activeSelection = activeSelection.previousSelection();
            case ARROW_DOWN -> activeSelection = activeSelection.nextSelection();
        }
    }

    /**
     * Passes a mouse input into the scene.
     *
     * @param pos     The position of the mouse when it was clicked.
     * @param pressed True if the button was pressed, false if it was released.
     */
    @Override
    public void mouseInput (Point pos, boolean pressed) {

    }
}
