// CREDIT: modified code from spaceinvaderswithsound
package main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class AudioManager {
    // TODO store music and sfx separately if too laggy
    public static ArrayList<Clip> sfx = new ArrayList<>();
    public static ArrayList<Clip> music = new ArrayList<>();
    public static ArrayList<Clip> removeSounds = new ArrayList<>();
    private static long lastPlayTime = 0;
    private static final int THROTTLE_DELAY_MS = 50;  // adjust as needed

    public static synchronized void playSound(final String ref, boolean loop) {
        // run
        (new Thread(() -> {
            try {
                long currentTime = System.currentTimeMillis();
                if (!loop && currentTime - lastPlayTime < THROTTLE_DELAY_MS) {
                    return;  // Throttle non-looping sounds
                }
                lastPlayTime = currentTime;

                Clip clip = createClip(ref);

                System.out.println("Playing: " + ref);

                if (loop) {
                    if (music.size() >= 10) {
                        music.get(0).stop();
                        music.remove(0);
                    }
                    music.add(clip);
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    if (sfx.size() >= 10 && sfx.get(0) != null) {
                        sfx.get(0).stop();
                        sfx.remove(0);
                    }
                    sfx.add(clip);
                    clip.start();
                } // if else


                // removes finished clips
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.stop();
                        music.remove(clip);
                        sfx.remove(clip);
                        //removeSounds.add(clip);
                    } // if
                });
            } catch (Exception e) {
                e.printStackTrace();
            } // try catch
        })).start();
    } // playSound

    public static void stopAllSounds() {
        for (Clip o : sfx) {
            stopSound(o);
        } // for
        for (Clip o : music) {
            stopSound(o);
        } // for
    } // stopAllSounds

    // TODO useless?
    public static void stopSound(Clip c) {
        removeSounds.add(c);
    } // stopSound

    public static void clearRemovedSounds() {
        for (int i = 0; i < removeSounds.size(); i++) {
            if (removeSounds.get(i) != null) (removeSounds.get(i)).stop();
        } // for

        music.removeAll(AudioManager.removeSounds);
        removeSounds.clear();
    } // clearRemovedSounds


    private static Clip createClip(String ref) throws LineUnavailableException, UnsupportedAudioFileException, IOException {

        Clip clip = AudioSystem.getClip();
        URL url = AudioManager.class.getClassLoader().getResource("main/sounds/" + ref);
        if (url == null) {
            System.out.println("Failed to load: " + ref);
            return null;
        }
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(url);
        clip.open(inputStream);
        return clip;

    }
} // class
