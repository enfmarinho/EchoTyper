package br.ufrn.EchoTyper.transcriber.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.nio.file.Path;

@Service
@Qualifier("mp3Transcriber")
public class MP3Transcriber extends TranscriberTemplate {
    protected Path preprocessing(Path filePath) {
        return filePath;
    }
}
