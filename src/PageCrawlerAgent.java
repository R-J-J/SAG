import jade.core.Agent;
import jade.lang.acl.ACLMessage;

/**
 * Created by Maciek on 27.05.2016.
 */
public class PageCrawlerAgent extends Agent {

    @Override
    protected void setup() {
        System.out.println(getLocalName() + " gotowy do akcji! Mój AID to: "+getAID());
        addBehaviour(new PageCrawlerBehavious());
    }

    @Override
    protected void takeDown() {
        System.out.println(getLocalName()+" kończy pracę");
    }


    private class PageCrawlerBehavious extends AbstractMessageProcessingBehaviour {

        @Override
        protected void processMessage(ACLMessage msg) {
            String pageContent = msg.getContent();
            System.out.println("Ready to crawl page: " + pageContent);
        }
    }
}
