package main.utility;

import main.Entity;

import java.awt.*;
import java.io.File;
import java.net.URL;

public class Carousel extends Entity {
    private final Button leftButton;
    private final Button rightButton;
    private int choice;
    private final String[] choices;
    private Font font;
    private final int width;
    private final int id;
    private final int purpose;

    public Carousel(int x, int y, int width, int id, int purpose, String[] choices) {
        this.choices = choices;
        this.x = x;
        this.y = y;
        this.width = width;
        this.id = id;
        this.purpose = purpose;

        choice = 0;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/utility/font.ttf")).deriveFont(36f);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        leftButton = new Button("buttons/left.png", x, y, this::leftButtonPressed);
        rightButton = new Button("buttons/right.png", x + width - 18, y, this::rightButtonPressed);

    }

    public void draw(Graphics g) {
        g.setFont(font);
        g.setColor(Color.white);
        drawCenteredString((Graphics2D) g, choices[choice], (int) (x + width/2.0), (int) y + 25);
        leftButton.draw(g);
        rightButton.draw(g);

    }

    public void update(int mouseX, int mouseY, boolean mousePressed) {
        leftButton.update(mouseX, mouseY, mousePressed);
        rightButton.update(mouseX, mouseY, mousePressed);
    }


    @Override
    public void collidedWith(Entity other) {}

    public void rightButtonPressed() {
        choice = (choice + 1) % choices.length;
    }

    public void leftButtonPressed() {
        choice = (choice - 1 + choices.length) % choices.length;
    }

    public int getChoice() {
        return choice;
    }

    public int getID() {
        return id;
    }

    public int getPurpose() {
        return purpose;
    }

    public void setChoice(int choice) {
        this.choice = choice;
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
