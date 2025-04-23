import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Whisper {
    public static String process_mp3_audio(String mp3_path) {
        try {
            String wav_path = convert_mp3_to_wav(mp3_path);
            return process_wav_audio(wav_path);
        } catch (Exception e) {
            System.out.println("Failed to process MP3 audio: " + mp3_path);
        }
        return "";
    }

    public static String process_wav_audio(String wav_string_path) throws IOException, InterruptedException {
        // Check if WAV file exists
        Path wav_path = Paths.get(wav_string_path);
        if (!Files.exists(wav_path)) {
            throw new FileNotFoundException("WAV file not found: " + wav_path.toAbsolutePath());
        }

        // Check if model exists
        String model_string_path = "models/ggml-base.en.bin";
        Path model_path = Paths.get(model_string_path);
        if (!Files.exists(model_path)) {
            throw new FileNotFoundException("Model path not found: " + model_path.toAbsolutePath());
        }

        // Run whisper-cli
        ProcessBuilder builder = new ProcessBuilder(
            "./assets/whisper-cli",
            "-m", model_string_path,
            "-f", wav_string_path
        );
        builder.redirectErrorStream(true);

        StringBuilder transcription = new StringBuilder();
        try {
            Process process = builder.start();

            // Read output line by line
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                        transcription.append(line).append("\n");
                }
            }

            // Wait for process to finish
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("whisper-cli failed with exit code: " + exitCode);
            }

            return clean_whisper_output(transcription.toString().trim());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to process WAV audio: " + wav_string_path, e);
        }
    }

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

    public static String clean_whisper_output(String whisper_output){
        StringTokenizer tokenizer = new StringTokenizer(whisper_output, " \n", true);

        boolean useful = false;
        String cleared_whisper_output = "";
        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            if (word.charAt(0) == '[') {
                useful = true;
            }
            if (useful) {
                cleared_whisper_output = cleared_whisper_output + word;
                if (word.equals("\n")) {
                    useful = false;
                } 
            }
        }
        return cleared_whisper_output;
    }
}
