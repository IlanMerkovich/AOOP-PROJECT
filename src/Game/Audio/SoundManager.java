package Game.Audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static final Map<String, Clip> clips = new HashMap<>();
    private static final String SOUNDS_FOLDER = "../Resources/Sounds/";

    private static Clip loadClip(String name) {
        if (clips.containsKey(name))
            return clips.get(name);
        try {
            URL url = SoundManager.class.getResource(SOUNDS_FOLDER+name);
            if (url == null) {
                System.err.println("no file found");
                return null;
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clips.put(name,clip);
            return clip;
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            return null;
        }
    }
    public static void playEffect(String name) {
        Clip clip = loadClip(name);
        if (clip == null)
            return;
        clip.setFramePosition(0);
        clip.start();
    }
    public static void playLoop(String name, float volume) {
        Clip clip = loadClip(name);
        if (clip == null)
            return;
        FloatControl vol = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        vol.setValue(20f * (float)Math.log10(volume));
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
