import org.junit.Before;
import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.io.InputStream;

public class MuzikResourceTest {

    private MuzikResource muzikResource;

    @Before
    public void setUp() throws Exception {
        this.muzikResource = new MuzikResource();
    }

    @Test
    public void makeNoise() throws Exception {
        AudioFormat audioFormat = new AudioFormat(
                MuzikResource.SAMPLE_RATE,
                MuzikResource.SAMPLE_SIZE_IN_BITS,
                MuzikResource.CHANNELS,
                true,
                false);

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
        //Open and start the SourceDataLine
        sourceDataLine.open(audioFormat);
        sourceDataLine.start();

        byte playBuffer[] = new byte[16384];

        int cnt;
        InputStream inputStream = muzikResource.makeNoise();
        while ((cnt = inputStream.read(
                playBuffer, 0,
                playBuffer.length))
                != -1) {
            //Keep looping until the input read
            // method returns -1 for empty stream.
            if (cnt > 0) {
                //Write data to the internal buffer of
                // the data line where it will be
                // delivered to the speakers in real
                // time
                sourceDataLine.write(playBuffer, 0, cnt);
            }//end if
        }//end while
    }

}