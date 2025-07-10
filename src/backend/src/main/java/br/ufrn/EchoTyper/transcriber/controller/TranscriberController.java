package br.ufrn.EchoTyper.transcriber.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.ufrn.EchoTyper.transcriber.service.TranscriberTemplate;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class TranscriberController {

  @Autowired
  @Qualifier("mp3Transcriber")
  private TranscriberTemplate transcriber;

  @PostMapping("/transcribe")
  public ResponseEntity<String> transcribeAudio(@RequestParam("inputFile") MultipartFile inputFile) {
    try {
      String transcriptionResult = transcriber.get_input_transcription(inputFile);

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
