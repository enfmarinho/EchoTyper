if [ ! -f "myfile.txt" ]; then
    sh ./assets/download-ggml-model.sh base.en
fi
javac Usage.java Whisper.java TranscriberInterface.java Audio.java
java Usage.java -mp3 assets/test.mp3
