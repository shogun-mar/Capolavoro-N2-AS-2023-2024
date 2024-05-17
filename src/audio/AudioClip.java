package audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import java.io.File;

public class AudioClip {
    private Clip clip;
    private AudioInputStream ais;
    private File audioFile;

    public AudioClip(String address){
        audioFile = new File(address);
    }


}
