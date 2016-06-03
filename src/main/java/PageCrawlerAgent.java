import com.sun.deploy.util.StringUtils;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

/**
 * Created by Maciek on 27.05.2016.
 */
public class PageCrawlerAgent extends AbstractAgent {

    private Set<String> alreadyCrawledUrls = new HashSet<>();

    @Override
    protected void addBehaviours() {
        addBehaviour(new PageCrawlerBehaviour());
    }

    @Override
    protected List<ServiceName> servicesToRegister()
    {
        return new ArrayList<>(Arrays.asList(Constants.CRAWLER_SERVICE));
    }

    private class PageCrawlerBehaviour extends AbstractMessageProcessingBehaviour {

        @Override
        protected void processMessage(ACLMessage msg) {
            String url = msg.getUserDefinedParameter(Constants.URL);
            String pageContent = msg.getContent();
            System.out.println("Crawling page: " + url);

            Document document = Jsoup.parse(pageContent);

            Elements elements = document.getElementsByAttribute("href");
            System.out.println("HREFS: " + elements.size());
            //TODO część stron ma artykuły wewnątrz <script> w jsonie
            Set<String> urls = new HashSet<>();
            for (Element element : elements) {
                //TODO crawler mógłby odrzucać linki do tej samej strony
                String nextUrlToProcess = element.attr("href");
                urls.add(nextUrlToProcess);
            }
            String jointUrls = StringUtils.join(urls, Constants.URL_SEPARATOR);

            AID receiverAid;
            try {
                receiverAid = getAgentForService(Constants.GATEWAY_SERVICE);
            } catch (AgentNotFoundException e) {
                e.printStackTrace();
                return;
            }
            ACLMessage nextUrlToProcessMsg = AgentUtils.newMessage(jointUrls, getAID(), receiverAid);
            nextUrlToProcessMsg.addUserDefinedParameter(Constants.FROM_CRAWLER, "true");
            send(nextUrlToProcessMsg);
            Statistics.stat(Statistics.StatisticsEvent.CRAWLED);
        }
    }
}
