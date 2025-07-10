package br.ufrn.EchoTyper.transcriber.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Qualifier("videoTranscriber")
public class VideoTranscriber implements TranscriberTranscriber {
    @Override
    public void preprocessing() {

    }
}
