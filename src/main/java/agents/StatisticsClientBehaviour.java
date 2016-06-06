package agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import statistics.Statistics;
import utils.AgentUtils;
import utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafal on 05.06.16.
 */
class StatisticsClientBehaviour extends CyclicBehaviour {

    private List<Statistics.StatisticsEvent> statsToSend = new ArrayList<>();
    private AID statAID = null;
    private AbstractAgent agent;

    private static final long DELAY = 5000L;

    StatisticsClientBehaviour(AbstractAgent parentAgent) {
        assert parentAgent != null;
        agent = parentAgent;
        tryToRegister();
    }

    @Override
    public void action() {
        if (statAID != null) {
            for (Statistics.StatisticsEvent statEvent : statsToSend) {
                ACLMessage statMsg = AgentUtils.newMessage("", agent.getAID(), statAID);
                statMsg.addUserDefinedParameter(StatisticsAgent.TYPE_PARAMETER, StatisticsAgent.EVENT);
                try {
                    statMsg.setContentObject(statEvent);
                    agent.send(statMsg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            statsToSend.clear();

            ACLMessage queueMsg = AgentUtils.newMessage("", agent.getAID(), statAID);
            queueMsg.addUserDefinedParameter(StatisticsAgent.TYPE_PARAMETER, StatisticsAgent.QUEUE);
            try {
                queueMsg.setContentObject(new Integer(agent.getCurQueueSize()));
                agent.send(queueMsg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            tryToRegister();
        }

        block(DELAY);
    }

    public void stat(Statistics.StatisticsEvent event) {
        statsToSend.add(event);
    }

    public void tryToRegister()
    {
        try {
            statAID = agent.getAgentForService(Constants.STATISTICS_SERVICE);
            assert statAID != null;
            ACLMessage registerStatMsg = AgentUtils.newMessage(StatisticsAgent.REGISTER, agent.getAID(), statAID);
            registerStatMsg.addUserDefinedParameter(StatisticsAgent.TYPE_PARAMETER, StatisticsAgent.REGISTER);
            agent.send(registerStatMsg);
        } catch (AbstractAgent.AgentNotFoundException e) {
            System.out.println("No statistics agent yet.");
        }
    }

    public void deregister()
    {
        if (statAID != null) {
            ACLMessage deregisterStatMsg = AgentUtils.newMessage(StatisticsAgent.DEREGISTER, agent.getAID(), statAID);
            deregisterStatMsg.addUserDefinedParameter(StatisticsAgent.TYPE_PARAMETER, StatisticsAgent.DEREGISTER);
            agent.send(deregisterStatMsg);
        }
    }
}
