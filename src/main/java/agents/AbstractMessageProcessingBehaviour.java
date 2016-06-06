package agents;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Created by Maciek on 27.05.2016.
 */
public abstract class AbstractMessageProcessingBehaviour extends CyclicBehaviour {

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if (msg != null) {
            processMessage(msg);
        } else {
            block();
        }
    }

    protected abstract void processMessage(ACLMessage msg);

}
