package br.ufrn.EchoTyper.transcriber.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeakerDiarizationConfig;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechSettings;
import com.google.cloud.speech.v1.WordInfo;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileNotFoundException;

@Service
public class GoogleCloudTranscriber implements TranscriberInterface {

  private final String credentialsPath = "../../../../../../../../assets/google_api_key.json";

  @Override
  public String transcribe_audio(String audio_file_path) throws IOException, InterruptedException {
    try {
      // Load the service account credentials
      GoogleCredentials credentials = GoogleCredentials.fromStream(
          getClass().getResourceAsStream(credentialsPath));
      SpeechSettings settings = SpeechSettings.newBuilder()
          .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
          .build();

      try (SpeechClient speechClient = SpeechClient.create(settings)) {
        // Read the audio file
        Path path = Paths.get(audio_file_path);
        ByteString audioBytes = ByteString.copyFrom(Files.readAllBytes(path));

        SpeakerDiarizationConfig speakerDiarizationConfig = SpeakerDiarizationConfig.newBuilder()
            .setEnableSpeakerDiarization(true)
            .setMinSpeakerCount(2)
            .setMaxSpeakerCount(20)
            .build();

        // Configure the recognition request
        RecognitionConfig config = RecognitionConfig.newBuilder()
            .setEncoding(AudioEncoding.MP3) // Set encoding to MP3
            .setSampleRateHertz(44100) // TODO check if this hz works
            .setLanguageCode("en-US")
            .setDiarizationConfig(speakerDiarizationConfig)
            .setModel("latest_short")
            .build();

        RecognitionAudio audio = RecognitionAudio.newBuilder()
            .setContent(audioBytes)
            .build();

        // Perform the speech recognition
        RecognizeResponse response = speechClient.recognize(config, audio);

        // Speaker Tags are only included in the last result object, which has only one
        // alternative.
        SpeechRecognitionAlternative alternative = response.getResults(response.getResultsCount() - 1)
            .getAlternatives(0);

        // The alternative is made up of WordInfo objects that contain the speaker_tag.
        WordInfo wordInfo = alternative.getWords(0);
        int currentSpeakerTag = wordInfo.getSpeakerTag();

        // For each word, get all the words associated with one speaker, once the
        // speaker changes,
        // add a new line with the new speaker and their spoken words.
        StringBuilder speakerWords = new StringBuilder(
            String.format("Speaker %d: %s", wordInfo.getSpeakerTag(), wordInfo.getWord()));

        for (int i = 1; i < alternative.getWordsCount(); i++) {
          wordInfo = alternative.getWords(i);
          if (currentSpeakerTag == wordInfo.getSpeakerTag()) {
            speakerWords.append(" ");
            speakerWords.append(wordInfo.getWord());
          } else {
            speakerWords.append(
                String.format("\nSpeaker %d: %s", wordInfo.getSpeakerTag(), wordInfo.getWord()));
            currentSpeakerTag = wordInfo.getSpeakerTag();
          }
        }

        return speakerWords.toString();
      }
    } catch (FileNotFoundException e) {
      throw new IOException("Service account key file not found: " + credentialsPath, e);
    } catch (IOException e) {
      throw new IOException("Error during speech-to-text: " + e.getMessage(), e);
    }
  }
}
