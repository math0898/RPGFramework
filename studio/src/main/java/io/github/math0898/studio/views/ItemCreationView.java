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
     * Loads this scene into the given game.
     */
    public boolean load (Game game) {
        game.clear();
        game.addGameObject("Item Preview", new ItemPreview());
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
