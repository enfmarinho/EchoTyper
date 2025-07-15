package br.ufrn.EchoTyper.transcriber.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.AudioAttributes;
import ws.schild.jave.EncodingAttributes;

@Service
@Qualifier("videoTranscriber")
public class VideoTranscriber extends TranscriberTemplate {
    protected Path preprocessing(Path inputPathMp4) {
        // JAVE way of setting audio specifications and attributes
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(128000);
        audio.setChannels(2);
        audio.setSamplingRate(44100);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp3");
        attrs.setAudioAttributes(audio);

        try {
            // Create temporary output file
            Path processedFile = Files.createTempFile("output_file", ".mp3");
            // Convert mp4 to mp3
            Encoder encoder = new Encoder();
            MultimediaObject multimediaObject = new MultimediaObject(inputPathMp4.toFile());
            encoder.encode(multimediaObject, processedFile.toFile(), attrs);

            return processedFile;
        } catch (EncoderException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputPathMp4;
    }
}
