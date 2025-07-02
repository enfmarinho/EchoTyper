package br.ufrn.EchoTyper.transcriber.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface TranscriberAdapter {
  public String get_input_transcription(MultipartFile input_file) throws IOException, InterruptedException;
}
