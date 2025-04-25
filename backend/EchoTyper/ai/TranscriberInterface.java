import java.io.IOException;
import java.io.FileNotFoundException;

public interface TranscriberInterface {
    public String transcribe_audio(String audio_file_path) throws IOException, InterruptedException;
}
