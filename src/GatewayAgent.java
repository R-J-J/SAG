import jade.lang.acl.ACLMessage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Maciek on 27.05.2016.
 */
public class GatewayAgent extends AbstractAgent {

    private static final int LIMIT = 1000;

    private Set<URL> alreadyProcessedUrls = new HashSet<>();
    private String domain;

    @Override
    protected void addBehaviours() {
        addBehaviour(new UrlValidateBehaviour());
    }

    private class UrlValidateBehaviour extends AbstractMessageProcessingBehaviour {

        @Override
        protected void processMessage(ACLMessage msg) {
            System.out.println("New url on input: " + msg.getContent());
            String[] urls = msg.getContent().split(Constants.URL_SEPARATOR);
            boolean fromCrawler = Boolean.valueOf(msg.getUserDefinedParameter(Constants.FROM_CRAWLER));

            for (String urlString : urls) {
                if (!fromCrawler) {
                    //nowy request
                    alreadyProcessedUrls.clear();
                    Statistics.reset();
                }
                URL url = createAndValidateUrl(urlString, fromCrawler);

                if (url == null) {
                    continue;
                }

                if (!fromCrawler) {
                    domain = url.getHost();
                }

                myAgent.send(AgentUtils.newMessage(url.toExternalForm(), getAID(), Constants.DOWNLOADER_AID));
            }
        }

        private URL createAndValidateUrl(String urlString, boolean fromCrawler) {
            if (urlString == null) {
                Statistics.stat(Statistics.StatisticsEvent.NULL);
                return null;
            }
            if (alreadyProcessedUrls.size() >= LIMIT) {
                System.out.println("Url rejected due to limit reached: " + urlString);
                Statistics.stat(Statistics.StatisticsEvent.OVER_LIMIT);
                return null;
            }
            if (urlString.endsWith("/")) {
                urlString = urlString.substring(0, urlString.length() - 1);
            }
            urlString = urlString.replace("www.", "");
            if (urlString.startsWith("/") && domain != null) {
                urlString = "http://" + domain + urlString;
            }
            if (!urlString.startsWith("http")) {
                urlString = "http://" + urlString;
            }
            URL url;
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                System.out.println("Invalid url: " + urlString);
                Statistics.stat(Statistics.StatisticsEvent.BAD_URL);
                return null;
            }
            if (fromCrawler && !inDomain(url)) {
                System.out.println("Url not in domain " + domain + ": " + urlString);
                Statistics.stat(Statistics.StatisticsEvent.NOT_IN_DOMAIN);
                return null;
            }
            if (alreadyProcessedUrls.contains(url)) {
                System.out.println("Url already processed: " + url);
                Statistics.stat(Statistics.StatisticsEvent.ALREADY_PROCESSED);
                return null;
            }
            System.out.println("Url valid: " + url);
            alreadyProcessedUrls.add(url);
            Statistics.stat(Statistics.StatisticsEvent.VALIDATED);
            return url;
        }

        private boolean inDomain(URL url) {
            return domain == null || url.getHost().endsWith(domain);
        }

    }
}