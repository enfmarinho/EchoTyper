package br.ufrn.EchoTyper.transcriber.service;

import java.io.IOException;

public interface TranscriberAdapter {
  public String get_input_transcription(String input_file_path) throws IOException, InterruptedException;
}
