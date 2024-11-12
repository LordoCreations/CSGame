// CREDIT: modified code from spaceinvaderswithsound
package main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioManager {
    // TODO store music and sfx separately if too laggy
    public static ArrayList<Clip> sfx = new ArrayList<>();
    public static ArrayList<Clip> music = new ArrayList<>();
    public static ArrayList<Clip> removeSounds = new ArrayList<>();
    private static long lastPlayTime = 0;
    private static final int THROTTLE_DELAY_MS = 50;  // TODO adjust throtle timing
    private static final ExecutorService soundExecutor = Executors.newFixedThreadPool(10); // TODO from GPT

    public static synchronized void playSound(final String ref, boolean loop) {

        soundExecutor.execute(() -> {
            try {
                long currentTime = System.currentTimeMillis();
                if (!loop && currentTime - lastPlayTime < THROTTLE_DELAY_MS) return;  // Throttle non-looping sounds

                lastPlayTime = currentTime;

                Clip clip = createClip(ref);

                // TODO remove debug
                // System.out.println("Playing: " + ref);

                if (loop) {
                    music.add(clip);
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    if (sfx.size() >= 10 && sfx.get(0) != null) {
                        sfx.get(0).close();
                        sfx.remove(0);
                    }
                    sfx.add(clip);
                    clip.start();
                } // if else


                // removes finished clips
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                        music.remove(clip);
                        sfx.remove(clip);
                        //removeSounds.add(clip);
                    } // if
                });
            } catch (Exception e) {
                e.printStackTrace();
            } // try catch
        });
    } // playSound

    public static void stopAllSounds() {
        for (int i = 0; i < sfx.size(); i++) {
            if (sfx.get(i) != null) {
                sfx.get(i).close();  // Ensure resources are released
            }
        }
        for (int i = 0; i < music.size(); i++) {
            if (music.get(i) != null) {
                music.get(i).close();  // Ensure resources are released
            }
        }
        sfx.clear();
        music.clear();
    } // stopAllSounds

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
