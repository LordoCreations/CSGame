package main.utility;

import main.Entity;

import java.awt.*;
import java.io.File;

import static main.utility.TextManager.drawCenteredString;
import static main.utility.TextManager.getFont;

/**
 * <h1>Carousel</h1>
 * <hr/>
 * Carousel to take user input from a set of choices
 *
 * @author Anthony and Luke
 * @since 013-11-2024
 * @see Entity
 */

public class Carousel extends Entity {
    private final Button leftButton;
    private final Button rightButton;
    private int choice;
    private final String[] choices;
    private Font font;
    private final int width;
    private final int id;
    private final int purpose;

    /**
     * constructor for a new Carousel
     * @param x x position
     * @param y y position
     * @param width width of carousel
     * @param id id of carousel
     * @param purpose what the carousel is used for
     * @param choices possible choices
     */
    public Carousel(int x, int y, int width, int id, int purpose, String[] choices) {
        this.choices = choices;
        this.x = x;
        this.y = y;
        this.width = width;
        this.id = id;
        this.purpose = purpose;

        choice = 0;
        font = getFont(36);
        leftButton = new Button("buttons/left.png", x, y, this::leftButtonPressed);
        rightButton = new Button("buttons/right.png", x + width - 18, y, this::rightButtonPressed);
    } // Carousel

    /**
     * Draws the carousel
     * @param g display graphics
     */
    public void draw(Graphics g) {
        g.setFont(font);
        g.setColor(Color.white);
        drawCenteredString((Graphics2D) g, choices[choice], (int) (x + width/2.0), (int) y + 25);
        leftButton.draw(g);
        rightButton.draw(g);
    } // draw

    /**
     * changes selection if the side buttons are pressed
     * @param mouseX mouse x position
     * @param mouseY mouse y position
     * @param mousePressed whether the mouse is pressed
     */
    public void update(int mouseX, int mouseY, boolean mousePressed) {
        leftButton.update(mouseX, mouseY, mousePressed);
        rightButton.update(mouseX, mouseY, mousePressed);
    } // update


    /**
     * Collision detection - unused
     * @param other object the player collided with
     */
    @Override
    public void collidedWith(Entity other) {} // collidedWith

    /**
     * Rotate one choice forwards
     */
    public void rightButtonPressed() {
        choice = (choice + 1) % choices.length;
    } // rightButtonPressed

    /**
     * Rotate one choice backwards
     */
    public void leftButtonPressed() {
        choice = (choice - 1 + choices.length) % choices.length;
    } // leftButtonPressed

    /* Getters and setters */
    public int getChoice() {
        return choice;
    } // getChoice

    public int getID() {
        return id;
    } // getID

    public int getPurpose() {
        return purpose;
    } // getPurpose

    public void setChoice(int choice) {
        this.choice = choice;
    } // setChoice

} // class
