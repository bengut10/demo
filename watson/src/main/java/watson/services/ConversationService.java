package watson.services;

import com.ibm.watson.developer_cloud.conversation.v1.Conversation;
import com.ibm.watson.developer_cloud.conversation.v1.model.*;
import watson.listeners.ConversationListener;


public class ConversationService {

    private Conversation service = new Conversation(Conversation.VERSION_DATE_2017_05_26);
    private Context context;

    private ConversationListener conversationListener;

    public ConversationService() {

        service.setUsernameAndPassword("82e0d5b1-5250-4868-b996-f88f9ab58803", "w8y4RuIwNhXk");

        context = new Context();
    }

    public final void sendMessage(String input) {

        MessageOptions newMessageOptions = new MessageOptions.Builder()
                .workspaceId("da0ff6d6-cc84-4cb0-b9f4-c3b995211c87")
                .input(new InputData.Builder(input).build())
                .context(this.context)
                .build();

        MessageResponse messageResponse = this.service.message(newMessageOptions).execute();
        this.context = messageResponse.getContext();

        if(this.conversationListener != null && messageResponse.getOutput().getText().get(0) != null) {

            this.conversationListener.handleConversationUpdate(messageResponse.getOutput().getText().get(0));
        }
    }

    public final void setConversationListener(ConversationListener conversationListener) {

        this.conversationListener = conversationListener;
    }
}
