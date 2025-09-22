package io.github.math0898.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * These are helpful methods and operations I find myself doing in multiple locations across the project. Written here
 * to reduce code duplication.
 *
 * @author Sugaku
 */
public class Utils {

    /**
     * Creates an image from the given text, font, and color. This is done since the current implementation of the
     * SugaEngine does not play nice with drawn strings.
     *
     * @param font The font to use.
     * @param color The color to draw the text in.
     * @param text The text to draw.
     */
    public static BufferedImage imageFromText (Font font, Color color, String text) {
        return imageFromText(font, color, text, 300, 48);
    }

    /**
     * Creates an image from the given text, font, color, and canvas sizes. This is done since the current
     * implementation of the SugaEngine does not play nice with drawn strings.
     *
     * @param font The font to use.
     * @param color The color to draw the text in.
     * @param text The text to draw.
     * @param canvasWidth The width of the canvas.
     * @param canvasHeight The height of the canvas.
     */
    public static BufferedImage imageFromText (Font font, Color color, String text, int canvasWidth, int canvasHeight) {
        BufferedImage buffer = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = buffer.createGraphics();
        graphics.setFont(font);
        graphics.setColor(color);
        graphics.drawString(text, 0, canvasHeight);
        graphics.dispose();
        return buffer;
    }
}
