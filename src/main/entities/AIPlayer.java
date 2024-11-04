package main.entities;

import main.GameScene;

import java.awt.event.KeyEvent;
import java.util.HashSet;

public class AIPlayer extends Player {
    private Player target;
    private Player[] players;
    private double minDistance;
    private double[] myCoord = new double[2];
    private double[] theirCoord = new double[2];
    private double currentDistance;
    private double horizontalThreshold;
    private double horizontalDistance;
    private double verticalDistance;

    public AIPlayer(GameScene s, String r, int newX, int newY, int hp, int team, int id, Player[] players) {
        super(s, r, newX, newY, hp, team, id);

        input = new HashSet<>();
        this.players = players;
    }

    @Override
    public void move(long delta) {
        findNearestTarget();

        if(target != null) { // TODO implement better later?
            // System.out.printf("Tracking %d, Distance %.2f, Delta X: %.2f Delta Y: %.2f%n", target.getID(), minDistance, theirCoord[0] - myCoord[0], theirCoord[1] - myCoord[1]);
        }

        verticalDistance = theirCoord[1] - myCoord[1];

        if (Math.random() < 0.001 * delta || verticalDistance > 50 && Math.random() < 0.05) {
            input.add(KeyEvent.VK_UP);
        } else {
            input.remove(KeyEvent.VK_UP);
        }

        horizontalThreshold = Math.min(input.contains(KeyEvent.VK_UP) || Math.abs(theirCoord[1] - myCoord[1]) > 50 ? 0 : 300, weapon.getFiringDistance() / 2.0);
        horizontalDistance = theirCoord[0] - myCoord[0];


        if (horizontalDistance > horizontalThreshold) {
            input.remove(KeyEvent.VK_LEFT);
            input.add(KeyEvent.VK_RIGHT);
        } else if (horizontalDistance < -horizontalThreshold) {
            input.remove(KeyEvent.VK_RIGHT);
            input.add(KeyEvent.VK_LEFT);
        } else {
            input.remove(KeyEvent.VK_RIGHT);
            input.remove(KeyEvent.VK_LEFT);
        }

        if (minDistance > 9999) {
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
        if(target != null) { // TODO implement better later?
            getCenter(target, theirCoord);
        }
    }

    private double distance(Player other) {
        getCenter(other, theirCoord);
        return Math.sqrt(Math.pow(myCoord[0] - theirCoord[0], 2) + 16 * Math.pow(myCoord[1] - theirCoord[1], 2));
    }

    private void getCenter(Player player, double[] destination) {
        destination[0] = player.getX() + player.getWidth() / 2.0;
        destination[1] = player.getY() + player.getHeight() / 2.0;
    }
}
