import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Stream;

@Path("/muzik")
public class MuzikResource {

    public static final int SAMPLE_RATE = 44100;
    public static final int SONG_LENGTH_MILLIS = 10 * 1000;
    public static final int CHANNELS = 2;
    public static final int SAMPLE_SIZE_IN_BITS = 16;
    private static final int MAX_VOLUME = 32767;

    @GET
    public InputStream makeNoise() throws IOException, UnsupportedAudioFileException {
        int[] notes = {0, 2, 4, 5, 7, 9, 11, 12};

        int noteDuration = 400;
        WaveGenerator waveGenerator = (frequency, time) -> getSinWaveValueIntWithFading(noteDuration, frequency, time);
        int[][] waveData = Arrays.stream(notes)
                .mapToDouble(note -> calcNoteFreq(note))
                .mapToObj(frequency -> makeWave(frequency, noteDuration, waveGenerator))
                .reduce(new int[2][0], (array1, array2) -> combineWaves(array1, array2));

//        int[][] waveData = makeWave(220, SONG_LENGTH_MILLIS, (frequency, time) -> getSinWaveValueInt(frequency, time));
//        int[][] waveData = makeWave(220, SONG_LENGTH_MILLIS, (frequency, time) -> getFmWaveValueInt(frequency, 0.01/, 10, time));

        AudioFormat audioFormat = new AudioFormat(
                SAMPLE_RATE,
                SAMPLE_SIZE_IN_BITS,  // sample size in bits
                CHANNELS,  // channels
                true,  // signed
                false  // bigendian
        );

        byte[] waveBytes = getWaveDataBytes(waveData);

        ByteArrayInputStream waveByteArrayInputStream = new ByteArrayInputStream(waveBytes);

        int audioFormatFrameSize = audioFormat.getFrameSize();
        int audioLength = waveBytes.length / audioFormatFrameSize;

        AudioInputStream audioInputStream = new AudioInputStream(waveByteArrayInputStream, audioFormat, audioLength);

        return audioInputStream;
    }

    private int getSinWaveValueIntWithFading(int noteDuration, double frequency, int time) {
        return (int) (getSinWaveValueInt(frequency, time) * getFadeInCoeff(200 , time) * getFadeOutCoeff(400 , noteDuration , time));
    }

    private double calcNoteFreq(int note) {
        return 220.0 * Math.pow(2, (double) note / 12);
    }

    private int[][] combineWaves(int[][] waveData1, int[][] waveData2) {
        int channelCount1 = waveData1.length;
        int channelCount2 = waveData2.length;
        if (channelCount1 != channelCount2) {
            throw new IllegalArgumentException("Different channel sizes, cannot concatenate waves: " + channelCount1 + " != " + channelCount2);
        }
        if (channelCount1 == 0) {
            return new int[0][0];
        }

        int[][] combinedWaveData = new int[CHANNELS][];
        for (int channelIndex = 0; channelIndex < channelCount1; channelIndex++) {
            int[] channelWaveData1 = waveData1[channelIndex];
            int[] channelWaveData2 = waveData2[channelIndex];
            combinedWaveData[channelIndex] = combineChannelWaveData(channelWaveData1, channelWaveData2);
        }

        return combinedWaveData;
    }

    private int[] combineChannelWaveData(int[] channelWaveData1, int[] channelWaveData2) {
        int sampleCount1 = channelWaveData1.length;
        int sampleCount2 = channelWaveData2.length;

        int[] combinedChannelWaveData = new int[sampleCount1 + sampleCount2];

        System.arraycopy(channelWaveData1, 0, combinedChannelWaveData, 0, sampleCount1);
        System.arraycopy(channelWaveData2, 0, combinedChannelWaveData, sampleCount1, sampleCount2);

        return combinedChannelWaveData;

    }

    private int getAssumedSameLength(int length1, int length2) {
        if (length1 != length2) {
            throw new IllegalArgumentException("Waves should have same sample count: " + length1 + " != " + length2);
        }
        return length1;
    }

    private byte[] getWaveDataBytes(int[][] waveData) {
        int channelCount = waveData.length;
        int duration = Stream.of(waveData[0], waveData[1])
                .map(channelWaveData -> channelWaveData.length)
                .reduce(this::getAssumedSameLength)
                .orElse(0);

        byte[] waveBytes = new byte[duration * (SAMPLE_SIZE_IN_BITS / 8) * channelCount];

        int byteIndex = 0;
        for (int time = 0; time < duration; time++) {
            for (int channelIndex = 0; channelIndex < CHANNELS; channelIndex++) {
                int sampleValue = waveData[channelIndex][time];
                waveBytes[byteIndex++] = (byte) sampleValue;
                waveBytes[byteIndex++] = (byte) (sampleValue >> 8);
            }
        }

        return waveBytes;
    }

    private int[][] makeWave(double frequency, int lengthMillis, WaveGenerator waveGenerator) {
        int sampleBufferSize = SAMPLE_RATE * lengthMillis / 1000;

        int[][] waveData = new int[CHANNELS][sampleBufferSize];

        int fadeInDuration = SAMPLE_RATE * 50 / 1000;
        int fadeOutDuration = SAMPLE_RATE * 200 / 1000;
        int fadeOutStartTime = sampleBufferSize - fadeOutDuration;

        for (int sampleOffset = 0; sampleOffset < sampleBufferSize; sampleOffset++) {
            for (int channelIndex = 0; channelIndex < CHANNELS; channelIndex++) {
//                double fadeInCoeff = sampleOffset < fadeInDuration ? 1.0 - (fadeInDuration - sampleOffset) / (double) fadeInDuration : 1.0;
//                double fadeOutCoeff = sampleOffset < fadeOutStartTime ? 1.0 : 1.0 - (sampleOffset - fadeOutStartTime) / (double) fadeOutDuration;
//                double fadeOutCoeff = 1.0;
//                int sampleValue = (int) (fadeInCoeff * fadeOutCoeff * waveGenerator.getWaveValue(frequency, sampleOffset));
                int sampleValue = waveGenerator.getWaveValue(frequency, sampleOffset);
                waveData[channelIndex][sampleOffset] = sampleValue;
            }
        }

        return waveData;
    }


    private int getIntValue(double sinValue, int maxVolume) {
        double panning = 1;
        double volume = maxVolume * panning;
        return (int) (sinValue * volume);
    }

    private double getSinValue(double frequency, float time) {
        double period = (double) SAMPLE_RATE / frequency;
        double angle = time * 2 * Math.PI / period;
        return Math.sin(angle);
    }

    public int getFmWaveValueInt(double carrierFrequency, double modulationIndex, double modulationFrequency, float time) {
        double modulation = modulationIndex * getSinValue(modulationFrequency, time);
        double sinValue = getSinValue(carrierFrequency + modulation, time);
        return getIntValue(sinValue, MAX_VOLUME);
    }

    public int getSinWaveValueInt(double frequency, int time) {
        double sinValue = getSinValue(frequency, time);
        return getIntValue(sinValue, MAX_VOLUME);
    }

    public double getFadeInCoeff(int fadeInDuration, int time) {
        int fadeInSampleCount = fadeInDuration * SAMPLE_RATE / 1000;
        if (time > fadeInSampleCount) {
            return 1.0;
        }
        return 1.0 - (fadeInSampleCount - time) / (double) fadeInSampleCount;
    }

    public double getFadeOutCoeff(int fadeOutDuration, int totalDuration, int time) {
        int fadeOutSampleCount = fadeOutDuration * SAMPLE_RATE / 1000;
        int totalSampleCount = totalDuration * SAMPLE_RATE / 1000;
        if (time < totalSampleCount - fadeOutSampleCount) {
            return 1.0;
        }
        return (totalSampleCount - time) / (double) fadeOutSampleCount;
    }

}
