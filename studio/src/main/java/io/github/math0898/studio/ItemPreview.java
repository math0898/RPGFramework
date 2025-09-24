package io.github.math0898.studio;

import io.github.math0898.utils.Utils;
import suga.engine.game.objects.BasicGameObject;
import suga.engine.graphics.GraphicsPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

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
    private static final int BACKGROUND_BOX_WIDTH = 708;

    /**
     * The base height of the background box. TODO: This is scaled by the number of rows.
     */
    private static final int BACKGROUND_BOX_HEIGHT = 676;

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
    private static final int TEXT_FONT_SIZE = 30;

    /**
     * The RpgItem that this object is previewing.
     */
    private final RpgItem rpgItem = new RpgItem();

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
        FontMetrics metrics = panel.getFontMetrics(font);
        BufferedImage dropShadow = Utils.imageFromText(font, color.darker().darker().darker().darker(), text, metrics.stringWidth(text), font.getSize() * 2);
        panel.addImage(posX + 2, posY + 2, dropShadow.getWidth(), dropShadow.getHeight(), dropShadow);
        BufferedImage mainText = Utils.imageFromText(font, color, text, metrics.stringWidth(text), font.getSize() * 2);
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

        rpgItem.setLore(Arrays.asList("During life Krusk was a human general.", "He was not particularly good at what", "he did but now he gives adventurers", "a hard time anyways."));

        // todo: Minecraft's font is not monospaced. Using visuals here to determine line break points may work and will hopefully still make a consistent edge within an item, but maybe not a namespace.
        final Font font = new Font("Monospaced", Font.BOLD, TEXT_FONT_SIZE);
        dropShadowText(panel, posX + TEXT_HORIZONTAL_OFFSET, posY + TITLE_VERTICAL_OFFSET, rpgItem.getName(), font, Color.decode(rpgItem.getRarities().getHexColor()));
        dropShadowText(panel, posX + TEXT_HORIZONTAL_OFFSET, posY + TITLE_VERTICAL_OFFSET + TEXT_FONT_SIZE + TITLE_VERTICAL_BUFFER, "Sharpness V", font, new Color(167, 167, 167));

        final int loreLines = rpgItem.getLore().size();
        for (int i = 0; i < rpgItem.getLore().size(); i++)
            dropShadowText(panel, posX + TEXT_HORIZONTAL_OFFSET, posY + TITLE_VERTICAL_OFFSET + (TEXT_FONT_SIZE * (i + 2)) + TITLE_VERTICAL_BUFFER, rpgItem.getLore().get(i), font, new Color(204, 204, 204));
        dropShadowText(panel, posX + TEXT_HORIZONTAL_OFFSET, posY + TITLE_VERTICAL_OFFSET + (TEXT_FONT_SIZE * (loreLines + 2)) + TITLE_VERTICAL_BUFFER, " ---- ---- ---- ", font, new Color(96, 96, 96));

        int statCount = 0;
        if (rpgItem.getDamage() != 0) {
            dropShadowText(panel, posX + TEXT_HORIZONTAL_OFFSET, posY + TITLE_VERTICAL_OFFSET + (TEXT_FONT_SIZE * (loreLines + 3)) + TITLE_VERTICAL_BUFFER + TEXT_VERTICAL_BUFFER, "Damage: " + rpgItem.getDamage(), font, new Color(217, 55, 71));
            statCount++;
        }
        if (rpgItem.getAttackSpeed() != 0) {
            dropShadowText(panel, posX + TEXT_HORIZONTAL_OFFSET, posY + TITLE_VERTICAL_OFFSET + (TEXT_FONT_SIZE * (loreLines + 3 + statCount)) + TITLE_VERTICAL_BUFFER + TEXT_VERTICAL_BUFFER, "Attack Speed: " + rpgItem.getAttackSpeed(), font, new Color(35, 165, 219));
            statCount++;
        }
        if (rpgItem.getHealth() != 0) {
            dropShadowText(panel, posX + TEXT_HORIZONTAL_OFFSET, posY + TITLE_VERTICAL_OFFSET + (TEXT_FONT_SIZE * (loreLines + 3 + statCount)) + TITLE_VERTICAL_BUFFER + TEXT_VERTICAL_BUFFER, "Health: " + rpgItem.getHealth(), font, new Color(244, 84, 218));
            statCount++;
        }
        if (rpgItem.getArmor() != 0) {
            dropShadowText(panel, posX + TEXT_HORIZONTAL_OFFSET, posY + TITLE_VERTICAL_OFFSET + (TEXT_FONT_SIZE * (loreLines + 3 + statCount)) + TITLE_VERTICAL_BUFFER + TEXT_VERTICAL_BUFFER, "Armor: " + rpgItem.getArmor(), font, new Color(63, 183, 74));
            statCount++;
        }
        if (rpgItem.getToughness() != 0) {
            dropShadowText(panel, posX + TEXT_HORIZONTAL_OFFSET, posY + TITLE_VERTICAL_OFFSET + (TEXT_FONT_SIZE * (loreLines + 3 + statCount)) + TITLE_VERTICAL_BUFFER + TEXT_VERTICAL_BUFFER, "Toughness: " + rpgItem.getToughness(), font, new Color(242, 217, 81));
            statCount++;
        }

        dropShadowText(panel, posX + TEXT_HORIZONTAL_OFFSET, posY + TITLE_VERTICAL_OFFSET + (TEXT_FONT_SIZE * (loreLines + 3 + statCount)) + TITLE_VERTICAL_BUFFER, " ---- ---- ---- ", font, new Color(96, 96, 96));
        dropShadowText(panel, posX + TEXT_HORIZONTAL_OFFSET, posY + TITLE_VERTICAL_OFFSET + (TEXT_FONT_SIZE * (loreLines + 4 + statCount)) + TITLE_VERTICAL_BUFFER, "Hand | Axe | 310", font, new Color(242, 217, 81)); // todo: Multiple colors in a line.
        // todo: Calculate gear score
    }
}
