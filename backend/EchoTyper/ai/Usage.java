public class Usage {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Uso incorreto da linha de comando, é necessário usar apenas 2 argumentos," + 
                               "o primeiro sendo -mp3 ou -wav e o segundo o caminho do arquivo");
            return;
        }

        String file_path = args[1], text = "";
        if (args[0].equals("-mp3")) {
            try {
                file_path = Audio.convert_mp3_to_wav(file_path);
            } catch (Exception e) {
                System.out.println("Failed to convert MP3 file to Wav");
            }
        } else if (! args[0].equals("-wav")){
            System.out.println("Formato do arquivo inválido: " + args[0]);
        }

        try {
            Whisper whisper = new Whisper();
            text = whisper.transcribe_audio(file_path);
        } catch (Exception e) {
            System.out.println("Failed to process WAV file");
        }
        System.out.println(text);
        // System.out.println(llm.resume(text));
    }
}
