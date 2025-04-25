import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.StringTokenizer;

public class Whisper implements TranscriberInterface {
    public String transcribe_audio(String audio_file_path) throws IOException, InterruptedException {
        // Check if WAV file exists
        Path wav_path = Paths.get(audio_file_path);
        if (!Files.exists(wav_path)) {
            throw new FileNotFoundException("WAV file not found: " + wav_path.toAbsolutePath());
        }

        // Check if model exists
        String model_string_path = "assets/ggml-base.en.bin";
        Path model_path = Paths.get(model_string_path);
        if (!Files.exists(model_path)) {
            throw new FileNotFoundException("Model path not found: " + model_path.toAbsolutePath());
        }

        // Run whisper-cli
        ProcessBuilder builder = new ProcessBuilder(
            "./assets/whisper-cli",
            "-m", model_string_path,
            "-f", audio_file_path
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
            throw new RuntimeException("Failed to process WAV audio: " + audio_file_path, e);
        }
    }

    public String clean_whisper_output(String whisper_output){
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
