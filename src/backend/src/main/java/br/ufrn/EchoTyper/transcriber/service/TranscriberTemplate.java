package br.ufrn.EchoTyper.transcriber.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Qualifier("transcriberTemplate")
abstract class TranscriberTemplate {
    @Autowired
    private AudioTranscriberInterface transcriber;

    protected abstract Path preprocessing(Path filePath);

    public String get_input_transcription(MultipartFile inputFile) throws IOException, InterruptedException {
        try {
            // Save the uploaded file temporarily
            Path tempFile = Files.createTempFile("uploaded_file", ".mp3");
            inputFile.transferTo(tempFile);

            // Pre-process and transcribe input_file
            Path processedFile = preprocessing(tempFile);
            String transcriptionResult = transcriber.transcribe_audio(processedFile.toString());

            // Clean up the temporary file
            Files.delete(processedFile);

            return transcriptionResult;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
