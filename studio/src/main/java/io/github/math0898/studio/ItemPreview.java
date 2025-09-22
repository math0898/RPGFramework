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
     * Called every drawing frame so programs have a chance to make their voices heard on what gets drawn.
     *
     * @param width  The width of the pixel map.
     * @param height The height of the pixel map.
     * @param panel  The panel to apply changes to.
     */
    @Override
    public void applyChanges (int width, int height, GraphicsPanel panel) {
        panel.setRectangle(100, 100, 50, 50, Color.WHITE);
    }
}
