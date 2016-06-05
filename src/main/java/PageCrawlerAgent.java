import com.google.common.base.Strings;
import com.sun.deploy.util.StringUtils;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import nlp.LanguageAnalyzer;
import nlp.ObjectProperty;
import nlp.ObjectSubclass;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import statistics.Statistics;

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

            String phrases = msg.getUserDefinedParameter(Constants.PHRASES);
            if(Strings.isNullOrEmpty(phrases)) {
                System.out.println("Empty phrases parameter");
                return;
            }

            String[] phraseArray = phrases.split(Constants.PHRASE_SEPARATOR);

            LanguageAnalyzer analyzer = new LanguageAnalyzer(phraseArray);

            /*
            for(Element element: document.getElementsByTag("tbody")) {
               // analyzer.analyzeTable(element);
            }
*/
            for(String tagName: new String[]{"p", "h1"}) {
                for (Element element : document.getElementsByTag(tagName)) {
                    analyzer.analyzeText(element.text());
                    analyzer.analyzeText(element.val());
                    for(Node node: element.childNodes()) {
                        if (node instanceof TextNode) {
                            TextNode textNode = (TextNode) node;
                            analyzer.analyzeText(textNode.text());
                        }
                    }
                }
            }

            try {
                for(ObjectProperty objectProperty: analyzer.getObjectProperties()) {
                    //TODO
                }
                for(ObjectSubclass objectSubclass: analyzer.getObjectSubclasses()) {
                    ACLMessage message = AgentUtils.newMessage("", getAID(), getAgentForService(Constants.ONTOLOGY_SERVICE));
                    message.addUserDefinedParameter(Constants.ONT_OPERATION, Constants.ONT_ADD_SUBCLASS);
                    message.addUserDefinedParameter(Constants.ONT_OBJECT, objectSubclass.object);
                    message.addUserDefinedParameter(Constants.ONT_RELATED_OBJECT, objectSubclass.subclass);
                    send(message);
                }
            } catch (AgentNotFoundException e) {
                e.printStackTrace();
            }

            Elements hrefs = document.getElementsByAttribute("href");
            System.out.println("HREFS: " + hrefs.size());
            //TODO część stron ma artykuły wewnątrz <script> w jsonie
            Set<String> urls = new HashSet<>();
            for (Element href : hrefs) {
                //TODO crawler mógłby odrzucać linki do tej samej strony
                String nextUrlToProcess = href.attr("href");
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
            nextUrlToProcessMsg.addUserDefinedParameter(Constants.PHRASES, phrases);
            send(nextUrlToProcessMsg);

            statistics.stat(Statistics.StatisticsEvent.CRAWLED);
        }
    }
}
