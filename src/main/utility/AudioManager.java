package main.utility;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.net.URL;
import java.util.ArrayList;

/**
 * <h1>Audio Manager</h1>
 * <hr/>
 * Derived form Space Invaders with Sound
 *
 * @author Anthony and Luke
 * @since 13-11-2024
 */

public class AudioManager {
    private static final int THROTTLE_DELAY_MS = 50;
    public static ArrayList<Clip> sfx = new ArrayList<>();
    public static ArrayList<Clip> music = new ArrayList<>();
    private static long lastPlayTime = 0;

    /**
     * Adds a sound to play as music or sfx
     *
     * @param ref  sound url
     * @param loop loop or play once
     */
    public static synchronized void playSound(final String ref, boolean loop) {
        (new Thread(() -> {
            try {
                long currentTime = System.currentTimeMillis();

                // Throttle non-looping sounds
                if (!loop && currentTime - lastPlayTime < THROTTLE_DELAY_MS) return;

                lastPlayTime = currentTime;
                Clip clip = createClip(ref);

                // Calculate the delay since requested start time
                long actualStartTime = System.currentTimeMillis();
                long delayMillis = actualStartTime - currentTime;

                // Calculate frame position to simulate starting at the correct time
                int frameOffset = (int) ((delayMillis / 1000.0) * clip.getFormat().getFrameRate());

                // Ensure frameOffset is within clip bounds
                if (frameOffset > clip.getFrameLength()) return;

                clip.setFramePosition(frameOffset);

                if (loop) {
                    if (!music.isEmpty() && music.get(0) != null) {
                        music.get(0).close();
                        music.remove(0);
                    } // if
                    music.add(clip);
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    if (sfx.size() >= 15 && sfx.get(0) != null) {
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
        })).start();
    } // playSound

    public static void stopAllSounds() {
        for (int i = 0; i < sfx.size(); i++) if (sfx.get(i) != null) sfx.get(i).close();
        for (int i = 0; i < music.size(); i++) if (music.get(i) != null) music.get(i).close();
        sfx.clear();
        music.clear();
    } // stopAllSounds

    /**
     * Creates a clip given URL
     *
     * @param ref URL to audio clip
     * @return Clip
     * @throws Exception URL invalid or problems with loading Audio
     */
    private static Clip createClip(String ref) throws Exception {
        Clip clip = AudioSystem.getClip();
        URL url = AudioManager.class.getClassLoader().getResource("main/sounds/" + ref);
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(url);
        clip.open(inputStream);
        return clip;
    } // createClip
} // AudioManager