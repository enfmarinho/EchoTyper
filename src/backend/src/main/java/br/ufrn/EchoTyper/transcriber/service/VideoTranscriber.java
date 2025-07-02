package br.ufrn.EchoTyper.transcriber.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VideoTranscriber implements TranscriberAdapter{
    @Autowired
    private GoogleCloudTranscriber transcriber;

    private String convert_mp4_to_mp3(String mp4_file_path) {
        return mp4_file_path; // TODO just a STUB
    }

    @Override
    public String get_input_transcription(MultipartFile input_file) throws IOException, InterruptedException {
        try {
            // Save the uploaded file temporarily
            Path tempFile = Files.createTempFile("uploaded_file", ".mp4");
            input_file.transferTo(tempFile);
            String inputFilePath = tempFile.toString();

            String transcriptionResult = transcriber.transcribe_audio(inputFilePath);

            // Clean up the temporary file
            Files.delete(tempFile);

            return transcriptionResult;
        } catch (Exception e) {
            throw e;
        }
    }

}
