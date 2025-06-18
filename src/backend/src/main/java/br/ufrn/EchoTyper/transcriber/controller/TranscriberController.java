package br.ufrn.EchoTyper.transcriber.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.ufrn.EchoTyper.transcriber.service.TranscriberAdapter;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class TranscriberController {

  private TranscriberAdapter transcriberAdapter;

  public void setTranscriberAdapter(TranscriberAdapter transcriberAdapter) {
      this.transcriberAdapter = transcriberAdapter;
  }

  @PostMapping("/transcribe")
  public ResponseEntity<String> transcribeAudio(@RequestParam("inputFile") MultipartFile inputFile) {
    try {
      // Save the uploaded file temporarily
      Path tempFile = Files.createTempFile("uploaded_file", null);
      inputFile.transferTo(tempFile);
      String inputFilePath = tempFile.toString();

      String transcriptionResult = transcriberAdapter.get_input_transcription(inputFilePath);

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
