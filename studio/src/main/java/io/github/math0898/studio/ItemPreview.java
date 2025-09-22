package io.github.math0898.studio;

import suga.engine.game.objects.BasicGameObject;
import suga.engine.graphics.GraphicsPanel;

import java.awt.*;

/**
 * An ItemPreview is a preview of how an item will appear in game based upon the given values.
 *
 * @author Sugaku
 */
public class ItemPreview extends BasicGameObject {

    /**
     * Base color of the background box.
     */
    private static final Color BACKGROUND_COLOR = new Color(17, 2, 17);

    /**
     * Border highlight color of the background box.
     */
    private static final Color HIGHLIGHT_COLOR = new Color(44, 8, 98);

    /**
     * The width of the background box.
     */
    private static final int BACKGROUND_BOX_WIDTH = 408;

    /**
     * The base height of the background box. TODO: This is scaled by the number of rows.
     */
    private static final int BACKGROUND_BOX_HEIGHT = 376;

    /**
     * The border between the background box and the inner box.
     */
    private static final int INNER_BORDER = 2;

    /**
     * The width of the inner border box.
     */
    private static final int INNER_BORDER_WIDTH = 2;

    /**
     * Called every drawing frame so programs have a chance to make their voices heard on what gets drawn.
     *
     * @param width  The width of the pixel map.
     * @param height The height of the pixel map.
     * @param panel  The panel to apply changes to.
     */
    @Override
    public void applyChanges (int width, int height, GraphicsPanel panel) {
        final int posX = 100;
        final int posY = 100;
        panel.setRectangle(posX, posY, BACKGROUND_BOX_WIDTH, BACKGROUND_BOX_HEIGHT, BACKGROUND_COLOR);
        panel.setRectangle(posX + INNER_BORDER, posY + INNER_BORDER, BACKGROUND_BOX_WIDTH - (2 * INNER_BORDER), BACKGROUND_BOX_HEIGHT - (2 * INNER_BORDER), HIGHLIGHT_COLOR);
        panel.setRectangle(posX + INNER_BORDER + INNER_BORDER_WIDTH, posY + INNER_BORDER + INNER_BORDER_WIDTH, BACKGROUND_BOX_WIDTH - (2 * (INNER_BORDER_WIDTH + INNER_BORDER)), BACKGROUND_BOX_HEIGHT - (2 * (INNER_BORDER_WIDTH + INNER_BORDER)), BACKGROUND_COLOR);
    }
}
