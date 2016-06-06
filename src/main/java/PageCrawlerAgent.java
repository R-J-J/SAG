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
    protected List<ServiceName> servicesToRegister() {
        return new ArrayList<>(Arrays.asList(Constants.CRAWLER_SERVICE));
    }

    private class PageCrawlerBehaviour extends AbstractMessageProcessingBehaviour {

        @Override
        protected void processMessage(ACLMessage msg) {
            try {
                String url = msg.getUserDefinedParameter(Constants.URL);
                String domain = msg.getUserDefinedParameter(Constants.DOMAIN);
                String pageContent = msg.getContent();
                System.out.println("Crawling page: " + url);

                Document document = Jsoup.parse(pageContent);

                // ONTOLOGY PART
                AID domainOntology = null;
                try {
                    domainOntology = getAgentForService(new ServiceName(Constants.ONTOLOGY_SERVICE_TYPE, domain));
                } catch (AgentNotFoundException e) {
                    e.printStackTrace();
                    return;
                }


                String phrases = msg.getUserDefinedParameter(Constants.PHRASES);
                if (Strings.isNullOrEmpty(phrases)) {
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
                for (String tagName : new String[]{"p", "h1"}) {
                    for (Element element : document.getElementsByTag(tagName)) {
                        analyzer.analyzeText(element.text());
                        analyzer.analyzeText(element.val());
                        for (Node node : element.childNodes()) {
                            if (node instanceof TextNode) {
                                TextNode textNode = (TextNode) node;
                                analyzer.analyzeText(textNode.text());
                            }
                        }
                    }
                }

                for (ObjectProperty objectProperty : analyzer.getObjectProperties()) {
                    ACLMessage message = AgentUtils.newMessage("", getAID(), domainOntology);
                    message.addUserDefinedParameter(Constants.ONT_OPERATION, Constants.ONT_ADD_ASSERTION);
                    message.addUserDefinedParameter(Constants.ONT_PROPERTY, objectProperty.propertyName);
                    if(objectProperty.propertyValue == null) {
                        message.addUserDefinedParameter(Constants.ONT_TYPE, Constants.ONT_TYPE_OBJECT_ASSERTION);
                        message.addUserDefinedParameter(Constants.ONT_RELATED_OBJECT, objectProperty.propertyName);
                    } else {
                        message.addUserDefinedParameter(Constants.ONT_TYPE, Constants.ONT_TYPE_DATA_ASSERTION);
                        message.addUserDefinedParameter(Constants.ONT_VALUE, objectProperty.propertyValue);
                        message.addUserDefinedParameter(Constants.ONT_VALUE_TYPE, Constants.ONT_TYPE_STRING);
                    }
                    send(message);
                }
                for (ObjectSubclass objectSubclass : analyzer.getObjectSubclasses()) {
                    ACLMessage message = AgentUtils.newMessage("", getAID(), domainOntology);
                    message.addUserDefinedParameter(Constants.ONT_OPERATION, Constants.ONT_ADD_SUBCLASS);
                    message.addUserDefinedParameter(Constants.ONT_OBJECT, objectSubclass.object);
                    message.addUserDefinedParameter(Constants.ONT_RELATED_OBJECT, objectSubclass.subclass);
                    send(message);
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
                    receiverAid = getAgentForService(new ServiceName(Constants.GATEWAY_SERVICE_TYPE, domain));
                } catch (AgentNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
                ACLMessage nextUrlToProcessMsg = AgentUtils.newMessage(jointUrls, getAID(), receiverAid);
                nextUrlToProcessMsg.addUserDefinedParameter(Constants.FROM_CRAWLER, "true");
                nextUrlToProcessMsg.addUserDefinedParameter(Constants.PHRASES, phrases);
                send(nextUrlToProcessMsg);

                statistics.stat(Statistics.StatisticsEvent.CRAWLED);
            } catch (Exception e) {
                e.printStackTrace();
                statistics.stat(Statistics.StatisticsEvent.CRAWL_FAILED);
            }
        }
    }
}
