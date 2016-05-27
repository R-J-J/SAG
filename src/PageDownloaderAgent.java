import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Maciek on 27.05.2016.
 */
public class PageDownloaderAgent extends Agent {

    @Override
    protected void setup() {
        System.out.println(getLocalName() + " gotowy do akcji! Mój AID to: "+getAID());
        addBehaviour(new HttpGetBehaviour());
    }

    @Override
    protected void takeDown() {
        System.out.println(getLocalName()+" kończy pracę");
    }

    private class HttpGetBehaviour extends AbstractMessageProcessingBehaviour {

        @Override
        protected void processMessage(ACLMessage msg) {
            try {
                String pageContent = getHTML(msg.getContent());
                System.out.println("Content html: " + pageContent);
                //TODO pewnie będzie kilka crawlerów, to tu się będzie dodawać ich AID-y
                send(AgentUtils.newMessage(pageContent, Constants.CRAWLER_AID));
            } catch (IOException e) {
                System.out.println("error downloading "+msg.getContent());
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