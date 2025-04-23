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
                text = Whisper.process_mp3_audio(file_path);
            } catch (Exception e) {
                System.out.println("Failed to process MP3 file");
            }
        } else if(args[0].equals("-wav")) {
            try {
                text = Whisper.process_wav_audio(file_path);
            } catch (Exception e) {
                System.out.println("Failed to process WAV file");
            }
        } else {
            System.out.println("Formato do arquivo inválido: " + args[0]);
        }

        System.out.println(text);
        // System.out.println(llm.resume(text));
    }
}
