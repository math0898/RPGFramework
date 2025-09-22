package io.github.math0898.studio;

import io.github.math0898.studio.engine.KeyListener;
import io.github.math0898.studio.engine.MouseListener;
import io.github.math0898.studio.engine.StudioGame;
import suga.engine.GameEngine;
import suga.engine.game.BasicGame;
import suga.engine.graphics.GraphicsPanel;
import suga.engine.input.keyboard.GameKeyListener;
import suga.engine.input.mouse.BasicMouseListener;

import java.awt.*;

public class Main {

    public static void main (String[] args) {
        gui();
    }

    /**
     * Opens the graphical interface.
     */
    public static void gui () {
        GraphicsPanel graphicsPanel = new io.github.math0898.studio.engine.GraphicsPanel();
        GameKeyListener gameKeyListener = new KeyListener();
        BasicMouseListener gameMouseListener = new MouseListener();
        BasicGame game = new StudioGame(graphicsPanel, gameKeyListener, gameMouseListener);
        game.setPanel(graphicsPanel);
        GameEngine.launchGameWindow(1920, 1000, "RPG Studio", true, graphicsPanel,
                Color.getHSBColor(0, 0, 0.05f), 30, 30, gameKeyListener, gameMouseListener, game);
    }
}
