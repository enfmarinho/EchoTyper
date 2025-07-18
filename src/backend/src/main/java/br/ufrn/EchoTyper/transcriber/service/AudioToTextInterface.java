package br.ufrn.EchoTyper.transcriber.service;

import java.io.IOException;

public interface AudioToTextInterface {
  public String transcribe_audio(String audio_file_path) throws IOException, InterruptedException;
}
