import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

public class Audio {
    // This returns a path to the wav audio file converted from mp3_path
    public static String convert_mp3_to_wav(String mp3_string_path) throws FileNotFoundException {
        Path mp3_path = Paths.get(mp3_string_path);

        if (!Files.exists(mp3_path)) {
            throw new FileNotFoundException("MP3 file not found: " + mp3_path.toAbsolutePath());
        }

        String wav_output_path = mp3_string_path.replaceFirst("\\.mp3$", "") + ".wav";
        try {
            // Assure there is no file in wav_output_path
            ProcessBuilder builder = new ProcessBuilder(
                "rm", wav_output_path
            );
            builder.redirectErrorStream(true);
            Process process = builder.start();

            builder = new ProcessBuilder(
                "ffmpeg",
                "-i", mp3_string_path,
                "-ar", "16000",
                "-ac", "1",
                "-c:a", "pcm_s16le",
                wav_output_path
            );
            builder.redirectErrorStream(true);
            process = builder.start();

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Error during MP3 to WAV conversion");
            }
            return wav_output_path;
        } catch (Exception e) {
            throw new RuntimeException("Error during MP3 to WAV conversion", e);
        }
    }
}
