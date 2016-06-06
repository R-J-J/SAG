package utils;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

/**
 * Created by Maciek on 27.05.2016.
 */
public abstract class AgentUtils {

    private AgentUtils() {

    }

    public static ACLMessage newMessage(String content, AID sender, AID... receivers) {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setSender(sender);
        for (AID aid : receivers) {
            message.addReceiver(aid);
        }
        message.setLanguage("English");
        // ??? message.setOntology();
        message.setContent(content);
        return message;
    }

}
