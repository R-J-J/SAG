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

    @Override
    protected void setup() {
        Statistics.register(this);
        addBehaviours();
        registerServices();

        System.out.println(getLocalName() + " gotowy do akcji! Mój AID to: " + getAID());
    }

    @Override
    protected void takeDown()
    {
        deregisterServices();
        System.out.println(getLocalName() + " kończy pracę");
    }

    //TODO probably methods beforeMove() and afterMove() also should be overridden and deregister and register services again

    @Override
    protected void afterClone() {
        super.afterClone();
        Statistics.register(this);
        registerServices();
        System.out.println(getLocalName() + " sklonowany i gotowy do akcji! Mój AID to: " + getAID());
    }

    protected abstract void addBehaviours();

    protected List<ServiceName> servicesToRegister()
    {
        return new ArrayList<>();
    }

    protected AID getAgentForService(ServiceName serviceName) throws AgentNotFoundException
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
            if (result.length == 0)
                throw new AgentNotFoundException();

            int index = randomGenerator.nextInt(result.length);
            return result[index].getName();
        }
        catch (FIPAException ex)
        {
            ex.printStackTrace();
            throw new AgentNotFoundException();
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

    private void deregisterServices()
    {
        try {
            DFService.deregister(this);
        } catch (FIPAException ex) {
            ex.printStackTrace();
        }
    }

}
