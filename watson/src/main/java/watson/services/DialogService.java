package watson.services;

public class DialogService {

    private final ConversationService conversationService;

    private final TextToSpeechService textToSpeechService;

    private final SpeechToTextService speechToTextService;

    public DialogService() {

        this.conversationService = new ConversationService();
        this.speechToTextService = new SpeechToTextService();
        this.textToSpeechService = new TextToSpeechService();

        this.conversationService.setConversationListener(this::handleConversationUpdate);
        this.speechToTextService.setSpeechToTextListener(this::handleSpeechToTextUpdate);
        this.textToSpeechService.setTextToSpeechListener(this::handleTextToSpeechUpdate);
    }

    public final void startDialogService() {

        this.conversationService.sendMessage("Hi!");

    }

    private void handleSpeechToTextUpdate(String speechToTextInput) {

        //The input from the user has came back as a string. Time to send a message to Watson.

        this.conversationService.sendMessage(speechToTextInput);
    }

    private void handleTextToSpeechUpdate(Boolean isReadyToListen) {

        //The service has communicated to the user in a verbal manner the response from Watson.
        // Time to turn on the speech to text service.

        if(isReadyToListen) {

            this.speechToTextService.beginListening();
        }

    }

    private void handleConversationUpdate(String conversationUpdate) {

        //This is the response that comes back from Watson. Comes back as text. Need to use the text to Speech Service.
        System.err.println(conversationUpdate);

        this.textToSpeechService.startTranscribing(conversationUpdate);
    }
}
