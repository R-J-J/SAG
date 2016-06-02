import jade.core.AID;

/**
 * Created by Maciek on 27.05.2016.
 */
public interface Constants {

    //identyfikatory agentów
    AID DOWNLOADER_AID = new AID("downloader", false);
    AID HREF_CRAWLER_AID = new AID("crawler", false);
    AID GATEWAY_AID = new AID("gateway", false);

    //parametry wiadomości
    String FROM_CRAWLER = "fromCrawler";
    String URL = "url";

    String URL_SEPARATOR = ";";
}
