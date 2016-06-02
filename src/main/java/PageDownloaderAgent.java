import jade.lang.acl.ACLMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Maciek on 27.05.2016.
 */
public class PageDownloaderAgent extends AbstractAgent {

    @Override
    protected void addBehaviours() {
        addBehaviour(new HttpGetBehaviour());
    }

    private class HttpGetBehaviour extends AbstractMessageProcessingBehaviour {

        @Override
        protected void processMessage(ACLMessage msg) {
            System.out.println("Downloading url: " + msg.getContent());
            try {
                String pageContent = getHTML(msg.getContent());
                System.out.println("Content html: " + pageContent);
                //TODO pewnie będzie kilka crawlerów, to tu się będzie dodawać ich AID-y
                ACLMessage msgForCrawler = AgentUtils.newMessage(pageContent, getAID(), Constants.HREF_CRAWLER_AID);
                msgForCrawler.addUserDefinedParameter(Constants.URL, msg.getContent());
                send(msgForCrawler);
                Statistics.stat(Statistics.StatisticsEvent.DOWNLOADED);
            } catch (IOException e) {
                System.out.println("error downloading " + msg.getContent());
                e.printStackTrace();
            }
        }

        private String getHTML(String urlToRead) throws IOException {
            StringBuilder result = new StringBuilder();
            URL url = new URL(urlToRead);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            return result.toString();
        }
    }
}
