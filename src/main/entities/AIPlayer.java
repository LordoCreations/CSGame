package main.entities;

import main.GameScene;

import java.awt.event.KeyEvent;
import java.util.HashSet;

import static main.Game.HEIGHT;
import static main.Game.WIDTH;

public class AIPlayer extends Player {
    private Player target;
    private final Player[] players;
    private double minDistance;
    private final double[] myCoord = new double[2];
    private final double[] theirCoord = new double[2];
    private int direction;

    public AIPlayer(GameScene s, String r, int newX, int newY, int hp, int team, int id, Player[] players) {
        super(s, r, newX, newY, hp, team, id);

        input = new HashSet<>();
        this.players = players;
    }

    @Override
    public void move(long delta) {
        findNearestTarget();


        if (target != null) { // TODO implement better later?
            System.out.printf("Tracking %d, Distance %.2f, Delta X: %.2f Delta Y: %.2f%n", target.getID(), Math.min(minDistance, 9999), theirCoord[0] - myCoord[0], theirCoord[1] - myCoord[1]);
        }

        double verticalDistance = theirCoord[1] - myCoord[1];
        if (theirCoord[1] < 400) {
            goToTop();
        }

        if (Math.random() < 0.005 * delta || verticalDistance > 50 && Math.random() < 0.05) {
            input.add(KeyEvent.VK_UP);
        } else {
            input.remove(KeyEvent.VK_UP);
        }

        double horizontalThreshold = Math.min(input.contains(KeyEvent.VK_UP) ||
                Math.abs(theirCoord[1] - myCoord[1]) > 50 ? 0 : 300, weapon.getFiringDistance() / 2.0);

        double minHorizontalThreshold = Math.min(weapon.getFiringDistance(), 200);

        double horizontalDistance = theirCoord[0] - myCoord[0];


        if (Math.random() < 0.0001 * delta || horizontalDistance > horizontalThreshold || horizontalDistance < minHorizontalThreshold && horizontalDistance > 0 && Math.random() < 0.05 * delta) {
            input.remove(KeyEvent.VK_LEFT);
            input.add(KeyEvent.VK_RIGHT);
        } else if (Math.random() < 0.0001 * delta || horizontalDistance < -horizontalThreshold || horizontalDistance > -minHorizontalThreshold && horizontalDistance < 0 && Math.random() < 0.05 * delta) {
            input.remove(KeyEvent.VK_RIGHT);
            input.add(KeyEvent.VK_LEFT);
        } else {
            input.remove(KeyEvent.VK_RIGHT);
            input.remove(KeyEvent.VK_LEFT);
        }


        if (minDistance >= Double.MAX_VALUE) {
            input.remove(KeyEvent.VK_RIGHT);
            input.remove(KeyEvent.VK_LEFT);
            input.remove(KeyEvent.VK_UP);
        }

        if (Math.abs(verticalDistance) < 20 && Math.random() < 0.05 * delta && horizontalDistance < weapon.getFiringDistance()) {
            input.add(KeyEvent.VK_DOWN);
        } else {
            input.remove(KeyEvent.VK_DOWN);
        }

        super.move(delta);
    }

    @Override
    protected void setControls(int id) {
        controls[0] = KeyEvent.VK_LEFT;
        controls[1] = KeyEvent.VK_UP;
        controls[2] = KeyEvent.VK_RIGHT;
        controls[3] = KeyEvent.VK_DOWN;
    }

    private void findNearestTarget() {
        double currentDistance;

        minDistance = Double.MAX_VALUE;
        getCenter(this, myCoord);
        for (Player p : players) {
            if (p == null) continue;
            if (p.team != this.team && !p.spawnProt && !p.isDead) {
                currentDistance = distance(p);
                if (currentDistance < minDistance) {
                    minDistance = currentDistance;
                    target = p;
                }
            }
        }

        if (target != null) {
            getCenter(target, theirCoord);
        }
    }

    private void goToTop() {
        if (myCoord[1] > 600) {
            theirCoord[0] = WIDTH/2d;
            theirCoord[1] = HEIGHT;
        }
        // TODO if needed, go from middle to bottom
//        else if (myCoord[1] > 400) {
//            theirCoord[0] = Math.round(myCoord[0]/WIDTH)*WIDTH;
//            theirCoord[1] = HEIGHT;
//        }
    }

    private double distance(Player other) {
        getCenter(other, theirCoord);
        return Math.abs(Math.sqrt(Math.pow(myCoord[0] - theirCoord[0], 2) + Math.pow(myCoord[1] - theirCoord[1], 4)));
    }

    private void getCenter(Player player, double[] destination) {
        destination[0] = player.getX() + player.getWidth() / 2.0;
        destination[1] = player.getY() + player.getHeight() / 2.0;
    }
}
