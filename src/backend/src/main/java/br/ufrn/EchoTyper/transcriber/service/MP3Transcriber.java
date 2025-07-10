package br.ufrn.EchoTyper.transcriber.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("mp3Transcriber")
public class MP3Transcriber extends TranscriberTemplate {
    // Empty, the template will work just fine
}
