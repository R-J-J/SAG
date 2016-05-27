import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Maciek on 27.05.2016.
 */
public class GatewayAgent extends Agent {

    @Override
    protected void setup() {
        System.out.println(getLocalName() + " gotowy do akcji! Mój AID to: "+getAID());
        addBehaviour(new UrlValidateBehaviour());
    }

    @Override
    protected void takeDown() {
        System.out.println(getLocalName()+" kończy pracę");
    }

    private class UrlValidateBehaviour extends AbstractMessageProcessingBehaviour {

        //TODO do zastanowienia czy tych urli nie trzymać w jakimś storage'u
        private Set<URL> alreadyProcessedUrls = new HashSet<>();
        private String domain;

        @Override
        protected void processMessage(ACLMessage msg) {
            System.out.println("Otrzymałem wiadomość: " + msg.getContent());

            boolean fromCrawler = Boolean.valueOf(msg.getUserDefinedParameter(Constants.FROM_CRAWLER));
            URL url = createAndValidateUrl(msg.getContent(), fromCrawler);

            if(url == null) {
                return;
            }

            if(!fromCrawler) {
                domain = url.getHost();
            }

            myAgent.send(AgentUtils.newMessage(url.toExternalForm(), Constants.DOWNLOADER_AID));
        }

        private URL createAndValidateUrl(String urlString, boolean fromCrawler) {
            if(urlString == null) {
                return null;
            }
            URL url;
            try {
                 url = new URL(urlString);
            } catch (MalformedURLException e) {
                System.out.println("Invalid url: "+urlString);
                return null;
            }
            if(domain != null && fromCrawler && !url.getHost().equals(domain)) {
                System.out.println("Url not in domain "+domain+": "+urlString);
                return null;
            }
            if(alreadyProcessedUrls.contains(url)) {
                System.out.println("Url already processed: " + url);
                return null;
            }
            System.out.println("Url valid: " + url);
            alreadyProcessedUrls.add(url);
            return url;
        }

    }
}