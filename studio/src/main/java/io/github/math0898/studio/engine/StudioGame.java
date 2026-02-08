package io.github.math0898.studio.engine;

import io.github.math0898.studio.views.ItemCreationView;
import suga.engine.game.BasicGame;
import suga.engine.graphics.GraphicsPanel;
import suga.engine.input.keyboard.GameKeyListener;
import suga.engine.input.mouse.BasicMouseListener;

public class StudioGame extends BasicGame {

    public StudioGame (GraphicsPanel panel, GameKeyListener listener, BasicMouseListener mouseListener) {
        super(panel, listener, mouseListener);
        scenes.put("Item Creation", new ItemCreationView());
    }
}
