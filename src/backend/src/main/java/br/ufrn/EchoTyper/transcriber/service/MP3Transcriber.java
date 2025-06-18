package br.ufrn.EchoTyper.transcriber.service;

import java.io.IOException;

public class MP3Transcriber implements TranscriberAdapter {
    private GoogleCloudTranscriber transcriber;
    
    @Override
    public String get_input_transcription(String input_file_path)  throws IOException, InterruptedException{
        try {
            return transcriber.transcribe_audio(input_file_path);
        } catch (Exception e) {
            throw e;
        }
    }
}
