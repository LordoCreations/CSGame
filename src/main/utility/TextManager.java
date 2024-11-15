package main.utility;

import java.awt.*;
import java.util.Objects;

/**
 * <h1>Text Manager</h1>
 * <hr/>
 * Stores font and provides text utilities
 *
 * @author Anthony and Luke
 * @see Font
 * @since 14-11-2024
 */

public class TextManager {
    private static Font font;

    /* Get font from folder */
    static {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(TextManager.class.getClassLoader().getResourceAsStream("main/utility/font.ttf")));
        } catch (Exception e) {
            e.printStackTrace();
        } // try catch
    } // static

    /**
     * Returns the font with the desired size
     *
     * @param size size of font in pt.
     * @return derived font
     */
    public static Font getFont(int size) {
        return font.deriveFont((float) size);
    } // get Font

    /**
     * Draw a centered string
     *
     * @param g2d  Graphics2D object
     * @param text text to draw
     * @param x    x position of centered text
     * @param y    y position of centered text
     */
    public static void drawCenteredString(Graphics2D g2d, String text, int x, int y) {

        // antialiasing is enabled to make text look smoother
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // use Font Metrics to calculate the size of text
        FontMetrics fm = g2d.getFontMetrics();

        // center text
        int centeredX = x - fm.stringWidth(text) / 2;
        int centeredY = y + (fm.getAscent() - fm.getDescent()) / 2;

        g2d.drawString(text, centeredX, centeredY);
    } // drawCenteredString
} // TextManager
