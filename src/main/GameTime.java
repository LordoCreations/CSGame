package main;

public class GameTime {
    private static long time;

    public static void update(long delta) {
        time += delta;
    }

    public static void setTime(long newTime) {
        time = newTime;
    }

    public static long getTime() {
        return time;
    }
}
