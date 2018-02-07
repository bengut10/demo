package watson.services;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;
import watson.listeners.SpeechToTextListener;

import javax.sound.sampled.*;

public class SpeechToTextService {


    private SpeechToText service;
    private SpeechToTextListener speechToTextListener;
    private TargetDataLine line;

    public SpeechToTextService() {

        service = new SpeechToText();
        service.setUsernameAndPassword("fa534808-25d2-4f82-ac1d-da4a005d1963", "BkGhYzWBcxKp");
        service.setEndPoint("https://stream.watsonplatform.net/speech-to-text/api");

    }

    public final void setSpeechToTextListener(SpeechToTextListener speechToTextListener) {

        this.speechToTextListener = speechToTextListener;
    }

    public final void beginListening () {

        //// Signed PCM AudioFormat with 16kHz, 16 bit sample size, mono
        int sampleRate = 16000;
        AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Line not supported");
            System.exit(0);
        }

        try {

            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);

        } catch (LineUnavailableException e) {

            e.printStackTrace();
        }

        line.start();

        AudioInputStream audio = new AudioInputStream(line);

        RecognizeOptions options = new RecognizeOptions.Builder()
                .interimResults(true).inactivityTimeout(5)
                .contentType(HttpMediaType.AUDIO_RAW + "; rate=" + sampleRate)
                .build();

        service.recognizeUsingWebSocket(audio, options, new BaseRecognizeCallback() {

            @Override
            public void onTranscription(SpeechResults speechResults) {

                System.out.println(speechResults);

                if(speechResults.isFinal()) {

                    if(SpeechToTextService.this.speechToTextListener != null) {

                        SpeechToTextService.this.speechToTextListener.handleSpeechToTextUpdate
                                (speechResults.getResults().get(0).getAlternatives().get(0).getTranscript());

                    }
                }
            }
        });

        System.out.println("Listening to your voice for the next 10s...");

        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        line.stop();
        line.close();

    }

    public final void stopListening() {

        //line.stop();
    }

    public final void endService() {

        // closing the WebSockets underlying InputStream will close the WebSocket itself.
       // line.close();
    }
}
