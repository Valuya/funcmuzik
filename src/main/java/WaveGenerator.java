/**
 * Created by devyam on 27/10/2016.
 */
@FunctionalInterface
public interface WaveGenerator {

    int getWaveValue(double frequency, int currentTime);

}
