import jade.core.Agent;

/**
 * Created by Maciek on 02.06.2016.
 */
public abstract class AbstractAgent extends Agent {

    @Override
    protected void setup() {
        System.out.println(getLocalName() + " gotowy do akcji! Mój AID to: " + getAID());
        Statistics.register(this);
        addBehaviours();
    }

    @Override
    protected void takeDown() {
        System.out.println(getLocalName() + " kończy pracę");
    }

    protected abstract void addBehaviours();

}
