package agents;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import statistics.Statistics;
import utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rafal on 05.06.16.
 */
public class StatisticsAgent extends AbstractAgent {

    public static final String TYPE_PARAMETER = "TYPE";
    public static final String REGISTER = "register";
    public static final String DEREGISTER = "deregister";
    public static final String EVENT = "event";
    public static final String QUEUE = "queue";

    @Override
    protected void addBehaviours() {
        addBehaviour(new AddStatisticsBehaviour());
        removeBehaviour(statistics);
    }

    @Override
    protected List<ServiceName> servicesToRegister()
    {
        return new ArrayList<>(Arrays.asList(Constants.STATISTICS_SERVICE));
    }

    private class AddStatisticsBehaviour extends AbstractMessageProcessingBehaviour {

        @Override
        protected void processMessage(ACLMessage msg) {
            AID sender = msg.getSender();
            String type = msg.getUserDefinedParameter(TYPE_PARAMETER);
            switch (type) {
                case REGISTER:
                    Statistics.register(sender);
                    System.out.println("Registering " + sender.getLocalName());
                    break;
                case DEREGISTER:
                    Statistics.deregister(sender);
                    System.out.println("Deregistering " + sender.getLocalName());
                    break;
                case EVENT:
                    try {
                        Statistics.stat((Statistics.StatisticsEvent) msg.getContentObject());
                    } catch (UnreadableException e) {
                        System.err.println("[agents.StatisticsAgent] Unreadable EVENT arrived.");
                        e.printStackTrace();
                    }
                    break;
                case QUEUE:
                    try {
                        Statistics.updateQueue(sender, (Integer) msg.getContentObject());
                    } catch (UnreadableException e) {
                        System.err.println("[agents.StatisticsAgent] Unreadable QUEUE arrived.");
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.err.println("[agents.StatisticsAgent] Unknown message type: " + type + "; content: " + msg.getContent());
            }

        }
    }
}
