package br.ufrn.EchoTyper.LLM.service;

public interface LLM_Interface {
  public String resume_transcription(String transcription);

  public String ask(String question);
}
