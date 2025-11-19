package Game.Audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SoundManager {
    private static final Map<String, Clip> clips = new ConcurrentHashMap<>();
    private static final Map<String, Clip> loopingClips = new ConcurrentHashMap<>();
    private static final String SOUNDS_FOLDER = "../Resources/Sounds/";
    private static float masterVolume = 1.0f;
    private static boolean muted = false;

    private static Clip loadClip(String name) {
        if (clips.containsKey(name))
            return clips.get(name);
        try {
            URL url = SoundManager.class.getResource(SOUNDS_FOLDER + name);
            if (url == null) {
                System.err.println("Sound file not found: " + SOUNDS_FOLDER + name);
                return null;
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clips.put(name, clip);
            return clip;
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error loading sound: " + name + " - " + e.getMessage());
            return null;
        }
    }

    public static void playEffect(String name) {
        if (muted) return;

        Clip clip = loadClip(name);
        if (clip == null) return;

        try {
            if (clip.isRunning()) {
                clip.stop();
            }
            setClipVolume(clip, masterVolume);
            clip.setFramePosition(0);
            clip.start();
        } catch (Exception e) {
            System.err.println("Error playing effect: " + name + " - " + e.getMessage());
        }
    }

    public static void playLoop(String name, float volume) {
        if (muted) return;
        Clip clip = loadClip(name);
        if (clip == null) return;
        try {
            if (clip.isRunning()) {
                clip.stop();
            }
            setClipVolume(clip, volume * masterVolume);
            loopingClips.put(name, clip);
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch (Exception e) {
            System.err.println("Error playing loop: " + name + " - " + e.getMessage());
        }
    }

    public static void setMasterVolume(float volume) {
        masterVolume = Math.max(0.0f, Math.min(1.0f, volume));
        muted = (masterVolume == 0.0f);
        for (Clip clip : clips.values()) {
            if (clip.isRunning()) {
                setClipVolume(clip, masterVolume);
            }
        }
    }

    public static void stopAll() {
        for (Clip clip : clips.values()) {
            if (clip.isRunning()) {
                clip.stop();
            }
        }
        loopingClips.clear();
    }
    private static void setClipVolume(Clip clip, float volume) {
        try {
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(Math.max(0.0001f, volume)) / Math.log(10.0) * 20.0);
                dB = Math.max(gainControl.getMinimum(), Math.min(gainControl.getMaximum(), dB));
                gainControl.setValue(dB);
            }
        } catch (Exception e) {
            System.err.println("Error setting volume: " + e.getMessage());
        }
    }
}