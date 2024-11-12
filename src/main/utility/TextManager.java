package main.utility;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class TextManager {
    public static Font getFont(int size) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(TextManager.class.getClassLoader().getResourceAsStream("main/utility/font.ttf"))).deriveFont((float) size);
        } catch (FontFormatException | IOException e) {
            return null;
        }
    }

    public static void drawCenteredString(Graphics2D g2d, String text, int x, int y) {

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Get the FontMetrics to calculate the text's width and height
        FontMetrics fm = g2d.getFontMetrics();

        // get text size
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent() - fm.getDescent();

        // center text
        int centeredX = x - textWidth / 2;
        int centeredY = y + textHeight / 2;

        // Draw the string at the centered position
        g2d.drawString(text, centeredX, centeredY);
    }
}
