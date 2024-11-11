// CREDIT: modified code from spaceinvaderswithsound
package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.net.URL;
import java.util.ArrayList;

public class AudioManager {
    // TODO store music and sfx separately if too laggy
    public static ArrayList<Clip> sfx = new ArrayList<>();
    public static ArrayList<Clip> music = new ArrayList<>();
    public static ArrayList<Clip> removeSounds = new ArrayList<>();

    public static synchronized void playSound(final String ref, boolean loop) {
        (new Thread(new Runnable() {
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    URL url = getClass().getClassLoader()
                            .getResource("main/sounds/" + ref);
                    if (url == null) {
                        System.out.println("Failed to load: " + ref);
                        System.exit(0);
                    } // if
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(url);

                    clip.open(inputStream);
                    System.out.println("Playing: " + ref);
                    if (loop) {
                        music.add(clip);
                        clip.loop(Clip.LOOP_CONTINUOUSLY);

                        if (music.size() > 10) removeSounds.add(music.get(0));

                    } else {
                        sfx.add(clip);
                        clip.start();
                        if (sfx.size() > 10) removeSounds.add(sfx.get(0));
                    } // if else

                    // removes finished clips
                    clip.addLineListener(event -> {
                        if (event.getType() == LineEvent.Type.STOP) {
                            removeSounds.add(clip);
                        } // if
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                } // try catch
            } // run
        })).start();
    } // playSound

    public static void stopAllSounds() {
        for (Object o : sfx) {
            stopSound((Clip) o);
        } // for
        for (Object o : music) {
            stopSound((Clip) o);
        } // for
    } // stopAllSounds

    // TODO useless?
    public static void stopSound(Clip c) {
        removeSounds.add(c);
    } // stopSound

    public static void clearRemovedSounds() {
        for (int i = 0; i < removeSounds.size(); i++) {
            if (removeSounds.get(i) != null)(removeSounds.get(i)).stop();
        } // for

        sfx.removeAll(AudioManager.removeSounds);
        music.removeAll(AudioManager.removeSounds);
        removeSounds.clear();
    } // clearRemovedSounds

} // class
