package br.ufrn.EchoTyper.transcriber.service;

import java.io.IOException;

public class VideoTranscriber implements TranscriberAdapter{
    private GoogleCloudTranscriber transcriber;

    private String convert_mp4_to_mp3(String mp4_file_path) {
        return mp4_file_path; // TODO just a STUB
    }

    @Override
    public String get_input_transcription(String input_file_path) throws IOException, InterruptedException {
        try {
            String audio_file_path = convert_mp4_to_mp3(input_file_path);
            return transcriber.transcribe_audio(audio_file_path);
        } catch (Exception e) {
            throw e;
        }
    }

}
