import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Created by Maciek on 27.05.2016.
 */
public abstract class AbstractMessageProcessingBehaviour extends CyclicBehaviour {

    //protected boolean receivedMessage = false;

    @Override
    public void action() {
       // receivedMessage = false;
        ACLMessage msg = myAgent.receive();
        if (msg != null) {
         //   receivedMessage = true;
            processMessage(msg);
        }
        block();
    }

//    @Override
//    public boolean done() {
//        return receivedMessage;
//    }

    protected abstract void processMessage(ACLMessage msg);

}
