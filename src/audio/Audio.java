package audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Audio {
    private Clip clip;

    public Audio(String filePath) {
        try {
            File file = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (!clip.isRunning()){
            clip.start();
    }
    }

    public void stop() {
        if (clip.isRunning()) {
            clip.stop();
        }
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void close() {
        clip.close();
    }
}
