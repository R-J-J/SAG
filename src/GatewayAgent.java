import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Maciek on 27.05.2016.
 */
public class GatewayAgent extends Agent {

    private static final int LIMIT = 1000;

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
            String[] urls = msg.getContent().split(Constants.URL_SEPARATOR);
            boolean fromCrawler = Boolean.valueOf(msg.getUserDefinedParameter(Constants.FROM_CRAWLER));

            for(String urlString: urls) {
                URL url = createAndValidateUrl(urlString, fromCrawler);

                if (url == null) {
                    continue;
                }

                if (!fromCrawler) {
                    //nowy request
                    domain = url.getHost();
                    alreadyProcessedUrls.clear();
                }

                myAgent.send(AgentUtils.newMessage(url.toExternalForm(), getAID(), Constants.DOWNLOADER_AID));
            }
        }

        private URL createAndValidateUrl(String urlString, boolean fromCrawler) {
            if(urlString == null) {
                return null;
            }
            if(alreadyProcessedUrls.size() >= LIMIT) {
                System.out.println("Url rejected due to limit reached: "+urlString);
                return null;
            }
            if(urlString.endsWith("/")) {
                urlString = urlString.substring(0, urlString.length()-1);
            }
            urlString = urlString.replace("www.", "");
            URL url;
            try {
                 url = new URL(urlString);
            } catch (MalformedURLException e) {
                System.out.println("Invalid url: "+urlString);
                return null;
            }
            if(fromCrawler && !inDomain(url)) {
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

        private boolean inDomain(URL url) {
            return domain == null || url.getHost().endsWith(domain);
        }

    }
}