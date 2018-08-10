import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Path("/muzik")
public class MuzikResource {

    public static final int SAMPLE_RATE = 44100;
    public static final int SONG_LENGTH_MILLIS = 10 * 1000;
    public static final int CHANNELS = 2;
    public static final int SAMPLE_SIZE_IN_BITS = 16;
    private static final int MAX_VOLUME = 32767;

    // notes
    private static final int C = 0;
    private static final int D = 2;
    private static final int E = 4;
    private static final int F = 5;
    private static final int G = 7;
    private static final int A = 9;
    private static final int B = 11;

    private static final int BEMOL = -1;
    private static final int SHARP = 1;

    // intervals
    private static final int FUNDAMENTAL = 0;
    private static final int SECUND = 2;
    private static final int MINOR_THIRD = 3;
    private static final int MAJOR_THIRD = 4;
    private static final int FIFTH = 7;
    private static final int SEVENTH = 10;
    private static final int OCTAVE = 12;

    // getChordNotes formulas
    private static final int[] MAJOR_CHORD = {FUNDAMENTAL, MAJOR_THIRD, FIFTH};
    private static final int[] MAJOR7_CHORD = {FUNDAMENTAL, MAJOR_THIRD, FIFTH, SEVENTH};
    private static final int[] MINOR_CHORD = {FUNDAMENTAL, MINOR_THIRD, FIFTH};
    private static final int[] MINOR7_CHORD = {FUNDAMENTAL, MINOR_THIRD, FIFTH, SEVENTH};

    private ChordHit getChordHit(int chordBase, int[] chordFormula, int duration, String lyrics) {
        return new ChordHit(chordBase, chordFormula, duration, lyrics);
    }

    private NoteHit getNoteHit(int note, int duration) {
        return new NoteHit(note, duration);
    }

    private Stream<NoteHit> streamChordNoteHits(ChordHit chordHit) {
        int chordBase = chordHit.getChordBase();
        int[] chordFormula = chordHit.getChordFormula();
        int duration = chordHit.getDuration();

        return Arrays.stream(chordFormula)
                .map(interval -> chordBase + interval)
                .mapToObj(note -> new NoteHit(note, duration));
    }

    @GET
    public InputStream makeNoise() throws IOException, UnsupportedAudioFileException {
        int[] notes = {C, D, E, F, G, A, B, C + OCTAVE};

        List<ChordHit> chordHists = Arrays.asList(
                getChordHit(A, MAJOR_CHORD, 8, "Desmond has his barrow in the"),
                getChordHit(E, MAJOR_CHORD, 8, "market place"),
                getChordHit(E, MAJOR7_CHORD, 8, "Molly is the singer in a"),
                getChordHit(A, MAJOR_CHORD, 8, "band"),

                getChordHit(A, MAJOR_CHORD, 8, "Desmond says to Molly \"Girl, I"),
                getChordHit(D, MAJOR_CHORD, 8, "like your face\" And Molly"),
                getChordHit(A, MAJOR_CHORD, 4, "says this as she "),
                getChordHit(E, MAJOR_CHORD, 4, "takes him by the"),
                getChordHit(A, MAJOR_CHORD, 8, "hand. Ob-la-d"),

                getChordHit(A, MAJOR_CHORD, 8, "di, ob-la-da, life goes"),
                getChordHit(E, MAJOR_CHORD, 4, "on,"),
                getChordHit(F + SHARP, MINOR7_CHORD, 4, "brah!"),
                getChordHit(A, MAJOR_CHORD, 4, "life goes"),
                getChordHit(A, MAJOR_CHORD, 8, "on. Ob-la"),

                getChordHit(A, MAJOR_CHORD, 8, "di, ob-la-da, life goes"),
                getChordHit(E, MAJOR_CHORD, 4, "on, "),
                getChordHit(F + SHARP, MINOR7_CHORD, 4, "brah!"),
                getChordHit(A, MAJOR_CHORD, 4, "Lala how the"),
                getChordHit(E, MAJOR_CHORD, 4, "life goes"),
                getChordHit(A, MAJOR_CHORD, 8, "on")
        );

        List<NoteHit> bassNoteHits = Arrays.asList(
                getNoteHit(A + SHARP, 2),
                getNoteHit(D + OCTAVE, 1),
                getNoteHit(F + OCTAVE, 1),
                getNoteHit(A + SHARP, 2),
                getNoteHit(D + OCTAVE, 1),
                getNoteHit(F + OCTAVE, 1),

                getNoteHit(F, 2),
                getNoteHit(A, 1),
                getNoteHit(C + OCTAVE, 1),
                getNoteHit(F, 2),
                getNoteHit(A, 1),
                getNoteHit(C + OCTAVE, 1),
                getNoteHit(F, 2),
                getNoteHit(A, 1),
                getNoteHit(C + OCTAVE, 1),
                getNoteHit(F, 2),
                getNoteHit(A, 1),
                getNoteHit(C + OCTAVE, 1),

                getNoteHit(A + SHARP, 2),
                getNoteHit(D + OCTAVE, 1),
                getNoteHit(F + OCTAVE, 1),
                getNoteHit(A + SHARP, 2),
                getNoteHit(D + OCTAVE, 1),
                getNoteHit(F + OCTAVE, 1),
                getNoteHit(A + SHARP, 2),
                getNoteHit(D + OCTAVE, 1),
                getNoteHit(F + OCTAVE, 1),
                getNoteHit(A + SHARP, 2),
                getNoteHit(D + OCTAVE, 1),
                getNoteHit(F + OCTAVE, 1),

                getNoteHit(D + SHARP + OCTAVE, 2),
                getNoteHit(G + OCTAVE, 1),
                getNoteHit(C + OCTAVE * 2, 1),
                getNoteHit(D + SHARP + OCTAVE, 2),
                getNoteHit(G + OCTAVE, 1),
                getNoteHit(C + OCTAVE * 2, 1),

                getNoteHit(A + SHARP, 2),
                getNoteHit(D + OCTAVE, 1),
                getNoteHit(F + OCTAVE, 1),
                getNoteHit(F, 2),
                getNoteHit(A, 1),
                getNoteHit(C + OCTAVE, 1),

                getNoteHit(A + SHARP, 2),
                getNoteHit(D + OCTAVE, 1),
                getNoteHit(F + OCTAVE, 1),
                getNoteHit(A + SHARP, 2),
                getNoteHit(D + OCTAVE, 1),
                getNoteHit(F + OCTAVE, 1)
        );

        int[][] chordsWaveData = chordHists.stream()
                .map(this::makeChordWave)
                .reduce(this::combineWaves)
                .orElse(new int[2][0]);

        int[][] bassWaveData = bassNoteHits.stream()
                .map(noteHit -> transpose(noteHit, -SHARP - OCTAVE))
                .map(this::makeNoteWave)
                .reduce(new int[2][0], (array1, array2) -> combineWaves(array1, array2));

//        Stream.concat(chordsWaveData, bassWaveData)
//                .reduce(new int[2][0], (array1, array2) -> mixWaves(array1, array2));
        int[][] waveData = mixWaves(chordsWaveData, 0.1, bassWaveData, 1.0);

//        int[][] waveData = Arrays.stream(notes)
//                .mapToDouble(note -> calcNoteFreq(note))
//                .mapToObj(frequency -> makeNoteWave(frequency, noteDuration, waveGenerator))
//                .reduce(new int[2][0], (array1, array2) -> combineWaves(array1, array2));

//        int[][] waveData = makeNoteWave(220, SONG_LENGTH_MILLIS, (frequency, time) -> getSinWaveValueInt(frequency, time));
//        int[][] waveData = makeNoteWave(220, SONG_LENGTH_MILLIS, (frequency, time) -> getFmWaveValueInt(frequency, 0.01/, 10, time));

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

    private NoteHit transpose(NoteHit noteHit, int amount) {
        int duration = noteHit.getDuration();
        int note = noteHit.getNote();
        return new NoteHit(note + amount, duration);
    }

    private int[][] makeChordWave(ChordHit chordHit) {
        return streamChordNoteHits(chordHit)
                .map(this::makeNoteWave)
                .reduce(this::mixWaves)
                .orElse(new int[2][0]);
    }

    private int[][] makeNoteWave(NoteHit noteHit) {
        int duration = noteHit.getDuration() * 200;
        int baseNote = noteHit.getNote();
        double frequency = calcNoteFreq(baseNote);

        WaveGenerator waveGenerator = getWaveGenerator(duration);

        return makeWave(frequency, duration, waveGenerator, .2);
    }

    private WaveGenerator getWaveGenerator(int noteDuration) {
        return (frequency, time) -> getSinWaveValueIntWithFading(noteDuration, frequency, time);
    }

    private int getSinWaveValueIntWithFading(int noteDuration, double frequency, int time) {
        return (int) (getSinWaveValueInt(frequency, time) * getFadeInCoeff(200, time) * getFadeOutCoeff(400, noteDuration, time));
    }

    private double calcNoteFreq(int note) {
        return 220 * Math.pow(2, (double) note / 12);
    }

    private int[][] mixWaves(int[][] waveData1, int[][] waveData2) {
        return mixWaves(waveData1, 1.0, waveData2, 1.0);
    }

    private int[][] mixWaves(int[][] waveData1, double gain1, int[][] waveData2, double gain2) {
        int channelCount1 = waveData1.length;
        int channelCount2 = waveData2.length;
        if (channelCount1 != channelCount2) {
            throw new IllegalArgumentException("Different channel sizes, cannot concatenate waves: " + channelCount1 + " != " + channelCount2);
        }
        if (channelCount1 == 0) {
            return new int[0][0];
        }

        int length1 = waveData1[0].length;
        int length2 = waveData2[0].length;
        int length = Math.max(length1, length2);

        int[][] mixWaveData = new int[CHANNELS][length1];
        System.out.println(length1);
        System.out.println(length2);
        for (int channelIndex = 0; channelIndex < channelCount1; channelIndex++) {
            for (int time = 0; time < length; time++) {
                int waveDataValue1 = time > length1 ? 0 : (int) (waveData1[channelIndex][time] * gain1);
                int waveDataValue2 = time > length2 ? 0 : (int) (waveData2[channelIndex][time] * gain2);
                mixWaveData[channelIndex][time] = waveDataValue1 + waveDataValue2;
            }
        }

        return mixWaveData;
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

    private int[][] makeWave(double frequency, int lengthMillis, WaveGenerator waveGenerator, double volume) {
        int sampleBufferSize = SAMPLE_RATE * lengthMillis / 1000;

        int[][] waveData = new int[CHANNELS][sampleBufferSize];

        for (int sampleOffset = 0; sampleOffset < sampleBufferSize; sampleOffset++) {
            for (int channelIndex = 0; channelIndex < CHANNELS; channelIndex++) {
                int sampleValue = waveGenerator.getWaveValue(frequency, sampleOffset);
                waveData[channelIndex][sampleOffset] = (int) (volume * sampleValue);
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
