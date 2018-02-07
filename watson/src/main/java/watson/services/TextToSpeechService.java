package watson.services;

import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import watson.listeners.TextToSpeechListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


public class TextToSpeechService {

    private TextToSpeechListener textToSpeechListener;

    private TextToSpeech textToSpeechService;

    public TextToSpeechService() {

        textToSpeechService = new TextToSpeech();
        textToSpeechService.setApiKey("https://stream.watsonplatform.net/text-to-speech/api");
        textToSpeechService.setUsernameAndPassword("817c4214-a087-4a2e-90a7-4dc39a5147a3", "Ee5RpVA3Fj3Y");
    }

    public final void setTextToSpeechListener(TextToSpeechListener textToSpeechListener){

        this.textToSpeechListener = textToSpeechListener;
    }


    public final void startTranscribing(String textInput) {

        try {

            Files.deleteIfExists(Paths.get("output.wav"));

            InputStream inputStream = this.textToSpeechService.
                synthesize(textInput,textToSpeechService.getVoices().execute().get(2)).execute();

            FileOutputStream fileOutputStream = new FileOutputStream("output.wav",false);

            writeInputStreamToOutputStream(inputStream, fileOutputStream);

            Media sound = new Media(new File("output.wav").toURI().toString());

            MediaPlayer mediaPlayer = new MediaPlayer(sound);

            mediaPlayer.setOnEndOfMedia(() -> {

                System.err.println("end of media");

                if (TextToSpeechService.this.textToSpeechListener != null) {

                    TextToSpeechService.this.textToSpeechListener.handleTextToSpeechUpdate(true);

                }
            });

           mediaPlayer.play();


        } catch (Exception e) {

            e.printStackTrace();

            TextToSpeechService.this.textToSpeechListener.handleTextToSpeechUpdate(false);
        }
    }

    /**
     * Write input stream to output stream.
     *
     * @param inputStream the input stream
     * @param outputStream the output stream
     */
    private void writeInputStreamToOutputStream(InputStream inputStream, OutputStream outputStream) {
        try {
            try {
                final byte[] buffer = new byte[1024];
                int read;

                while ((read = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                }

                outputStream.flush();
            } catch (final Exception e) {
                e.printStackTrace();
            } finally {
                outputStream.close();
                inputStream.close();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

}
