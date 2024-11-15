package main.utility;

import main.Scene;

import java.awt.*;

/**
 * <h1>Colors</h1>
 * <hr/>
 * Holds all team colors
 *
 * @author Anthony and Luke
 * @see Scene
 * @since 14-11-2024
 */

public class Colors {

    private static final Color blue = Color.decode("#35A2E7");
    private static final Color red = Color.decode("#CE3D3D");
    private static final Color purple = Color.decode("#9A54E4");
    private static final Color green = Color.decode("#35BF53");

    private static final Color darkBlue = Color.decode("#12315A");
    private static final Color darkRed = Color.decode("#460F10");
    private static final Color darkPurple = Color.decode("#390E40");
    private static final Color darkGreen = Color.decode("#0F331A");

    private static final Color[] blues = new Color[]{blue, darkBlue};
    private static final Color[] reds = new Color[]{red, darkRed};
    private static final Color[] purples = new Color[]{purple, darkPurple};
    private static final Color[] greens = new Color[]{green, darkGreen};


    /**
     * Get the colors of a team
     *
     * @param id team ID
     * @return array of dark and light version of team color
     */
    public static Color[] getTeamColors(int id) {
        return switch (id) {
            case 1 -> reds;
            case 2 -> purples;
            case 3 -> greens;
            default -> blues;
        }; // switch
    } // getTeamColors
} // Colors
