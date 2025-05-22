package br.ufrn.EchoTyper.transcriber.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.ufrn.EchoTyper.transcriber.service.GoogleCloudTranscriber;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class TranscriberController {

  @Autowired
  private GoogleCloudTranscriber transcriberService;

  @PostMapping("/transcribe")
  public ResponseEntity<String> transcribeAudio(@RequestParam("audioFile") MultipartFile audioFile) {
    try {
      // Save the uploaded file temporarily
      Path tempFile = Files.createTempFile("uploaded_audio_", ".mp3");
      audioFile.transferTo(tempFile);
      String audioFilePath = tempFile.toString();

      String transcriptionResult = transcriberService.transcribe_audio(audioFilePath);

      // Clean up the temporary file
      Files.delete(tempFile);

      return ResponseEntity.ok(transcriptionResult);

    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing audio: " + e.getMessage());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Transcription interrupted: " + e.getMessage());
    }
  }
}