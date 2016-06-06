package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Maciek on 02.06.2016.
 */
public abstract class AbstractAgent extends Agent {

    public static class ServiceName {

        String type;
        String name;

        public ServiceName(String type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    class AgentNotFoundException extends Exception {}

    private Random randomGenerator = new Random();

    protected StatisticsClientBehaviour statistics;
//    protected Statistics statistics = new Statistics();

    @Override
    protected void setup() {
        statistics = new StatisticsClientBehaviour(this);
        addBehaviour(statistics);
        addBehaviours();
        registerServices();

        System.out.println(getLocalName() + " gotowy do akcji! Mój AID to: " + getAID());
    }

    @Override
    protected void takeDown()
    {
        deregisterServices();
        statistics.deregister();
        System.out.println(getLocalName() + " kończy pracę");
    }

    @Override
    protected void beforeMove() {
        super.beforeMove();
        takeDown();
    }

    @Override
    protected void afterMove() {
        super.afterMove();
        setup();
    }

    @Override
    protected void afterClone() {
        super.afterClone();
        setup();
    }

    protected abstract void addBehaviours();

    protected List<ServiceName> servicesToRegister()
    {
        return new ArrayList<>();
    }

    protected AID[] getAgentListForService(ServiceName serviceName)
    {
        DFAgentDescription dfad = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        if (!serviceName.name.isEmpty())
            sd.setName(serviceName.name);
        if (!serviceName.type.isEmpty())
            sd.setType(serviceName.type);
        dfad.addServices(sd);

        try
        {
            DFAgentDescription[] result = DFService.search(this, dfad);
            AID[] aids = new AID[result.length];
            for (int i = 0; i < result.length; i++) {
                aids[i] = result[i].getName();
            }
            return aids;
        }
        catch (FIPAException ex)
        {
            ex.printStackTrace();
        }
        return new AID[0];
    }

    protected AID getAgentForService(ServiceName serviceName) throws AgentNotFoundException {
        AID[] result = getAgentListForService(serviceName);
        if (result.length == 0)
            throw new AgentNotFoundException();
        int index = randomGenerator.nextInt(result.length);
        return result[index];
    }

    protected void registerOneService(ServiceName serviceName)
    {
        DFAgentDescription dfad = new DFAgentDescription();
        dfad.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType(serviceName.type);
        sd.setName(serviceName.name);
        dfad.addServices(sd);

        try {
            DFService.register(this,dfad);
        } catch (FIPAException ex) {
            ex.printStackTrace();
        }
    }

    private void registerServices()
    {
        DFAgentDescription dfad = new DFAgentDescription();
        dfad.setName(getAID());

        for (ServiceName  serviceName : servicesToRegister())
        {
            ServiceDescription sd = new ServiceDescription();
            sd.setType(serviceName.type);
            sd.setName(serviceName.name);
            dfad.addServices(sd);
        }

        try {
            DFService.register(this,dfad);
        } catch (FIPAException ex) {
            ex.printStackTrace();
        }
    }

    protected void deregisterServices()
    {
        try {
            DFService.deregister(this);
        } catch (FIPAException ex) {
            ex.printStackTrace();
        }
    }

}
