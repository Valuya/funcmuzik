import java.util.List;

public class ChordHit {

    private int chordBase;
    private int[] chordFormula;
    private int duration;
    private final String lyrics;

    public ChordHit(int chordBase, int[] chordFormula, int duration, String lyrics) {
        this.chordBase = chordBase;
        this.chordFormula = chordFormula;
        this.duration = duration;
        this.lyrics = lyrics;
    }

    public int getChordBase() {
        return chordBase;
    }

    public int[] getChordFormula() {
        return chordFormula;
    }

    public int getDuration() {
        return duration;
    }

    public String getLyrics() {
        return lyrics;
    }
}
