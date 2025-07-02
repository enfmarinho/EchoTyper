package br.ufrn.EchoTyper.transcriber.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Qualifier("mp3Transcriber")
public class MP3Transcriber implements TranscriberAdapter {

    @Autowired
    private GoogleCloudTranscriber transcriber;
    
    @Override
    public String get_input_transcription(MultipartFile input_file) throws IOException, InterruptedException{
        try {
            // Save the uploaded file temporarily
            Path tempFile = Files.createTempFile("uploaded_file", ".mp3");
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
