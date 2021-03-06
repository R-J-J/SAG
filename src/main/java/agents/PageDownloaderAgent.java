package agents;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import statistics.Statistics;
import utils.AgentUtils;
import utils.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Maciek on 27.05.2016.
 */
public class PageDownloaderAgent extends AbstractAgent {

    @Override
    protected void addBehaviours() {
        System.setProperty("jsse.enableSNIExtension", "false");
        addBehaviour(new HttpGetBehaviour());
    }

    @Override
    protected List<ServiceName> servicesToRegister()
    {
        return new ArrayList<>(Arrays.asList(Constants.DOWNLOADER_SERVICE));
    }

    private class HttpGetBehaviour extends AbstractMessageProcessingBehaviour {

        @Override
        protected void processMessage(ACLMessage msg) {

            String phrases = msg.getUserDefinedParameter(Constants.PHRASES);
            String domain = msg.getUserDefinedParameter(Constants.DOMAIN);

            System.out.println("Downloading url: " + msg.getContent());
            try {
                String pageContent = getHTML(msg.getContent());
                System.out.println("Content html: " + pageContent);

                AID receiverAid;
                try {
                    receiverAid = getAgentForService(Constants.CRAWLER_SERVICE);
                } catch (AgentNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
                ACLMessage msgForCrawler = AgentUtils.newMessage(pageContent, getAID(), receiverAid);
                msgForCrawler.addUserDefinedParameter(Constants.URL, msg.getContent());
                msgForCrawler.addUserDefinedParameter(Constants.DOMAIN, domain);
                if(phrases != null) {
                    msgForCrawler.addUserDefinedParameter(Constants.PHRASES, phrases);
                }
                send(msgForCrawler);
                statistics.stat(Statistics.StatisticsEvent.DOWNLOADED);
            } catch (IOException e) {
                System.out.println("error downloading " + msg.getContent());
                e.printStackTrace();
                statistics.stat(Statistics.StatisticsEvent.DOWNLOAD_FAILED);
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
