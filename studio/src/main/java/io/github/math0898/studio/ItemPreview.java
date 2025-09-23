package io.github.math0898.studio;

import io.github.math0898.utils.Utils;
import suga.engine.game.objects.BasicGameObject;
import suga.engine.graphics.GraphicsPanel;

import java.awt.*;
import java.awt.image.BufferedImage;

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
     * The horizontal offset for text.
     */
    private static final int TEXT_HORIZONTAL_OFFSET = 10;

    /**
     * The separation between text lines.
     */
    private static final int TEXT_VERTICAL_BUFFER = 6;

    /**
     * The separation between the item name text and the first row of lore/enchantments.
     */
    private static final int TITLE_VERTICAL_BUFFER = 8;

    /**
     * The separation between the item name text and the top of the item box.
     */
    private static final int TITLE_VERTICAL_OFFSET = 4; // Actual is 8 pixels from the top of the box.

    /**
     * The size of the text font used.
     */
    private static final int TEXT_FONT_SIZE = 14;

    /**
     * Draws drop shadowed text to the given graphics panel at the given positions in the font and color.
     *
     * @param panel The panel to draw the drop shadow to.
     * @param posX The x position.
     * @param posY The y position.
     * @param font The font to use.
     * @param text The text to draw.
     * @param color The main text color to use.
     */
    private void dropShadowText (GraphicsPanel panel, int posX, int posY, String text, Font font, Color color) {
        BufferedImage dropShadow = Utils.imageFromText(font, color.darker().darker().darker().darker(), text, 500, font.getSize());
        panel.addImage(posX + 2, posY + 2, dropShadow.getWidth(), dropShadow.getHeight(), dropShadow);
        BufferedImage mainText = Utils.imageFromText(font, color, text, 500, font.getSize());
        panel.addImage(posX, posY, mainText.getWidth(), mainText.getHeight(), mainText);
    }

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

        /* ---- Extract to Helper Method ---- */
        String itemName = "Krusk's Axe";
        BufferedImage dropShadow = Utils.imageFromText(new Font("Comic Sans", Font.BOLD, TEXT_FONT_SIZE), new Color(45, 76, 49), itemName, 500, 14);
        panel.addImage(posX + TEXT_HORIZONTAL_OFFSET + 2, posY + TITLE_VERTICAL_OFFSET + 2, dropShadow.getWidth(), dropShadow.getHeight(), dropShadow);
        BufferedImage mainText = Utils.imageFromText(new Font("Comic Sans", Font.BOLD, TEXT_FONT_SIZE), new Color(81, 197, 115), itemName, 500, 14);
        panel.addImage(posX + TEXT_HORIZONTAL_OFFSET, posY + TITLE_VERTICAL_OFFSET, mainText.getWidth(), mainText.getHeight(), mainText);
        /* ---- ----- ----- */

        dropShadowText(panel, posX + TEXT_HORIZONTAL_OFFSET, posY + TITLE_VERTICAL_OFFSET + TEXT_FONT_SIZE + TITLE_VERTICAL_BUFFER, "Damage: 60", new Font("Comic Sans", Font.BOLD, TEXT_FONT_SIZE), new Color(212, 54, 69));
    }
}
