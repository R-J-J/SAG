/**
 * Created by rafal on 23.05.16.
 */

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleAgent extends Agent {

    SimpleAgent() {
        super();
    }

    protected void setup() {
        System.out.println("Cześć, mam na imię " + getName() + ", szukam koleżanek i kolegów");
        addBehaviour(new HttpGetBehaviour());
        addBehaviour(new ExampleBehaviour());
    }

    protected void takeDown() { //opcjonalnie
        System.out.println("Nara!"); // operacje wykonywane bezpośrednio przed usunięciem agenta
    }
}

class ExampleBehaviour extends Behaviour {
    private boolean receivedMessage = false;

    public void action() {
        receivedMessage = false;
        ACLMessage msg = myAgent.receive();
        if (msg != null) {
            System.out.println("Otrzymałem wiadomość: " + msg.getContent());
            receivedMessage = true;
        } else {
            block();
        }
    }

    public boolean done() {
        return receivedMessage;
    }
}

class HttpGetBehaviour extends Behaviour {
    private boolean receivedMessage = false;

    public void action() {
        receivedMessage = false;
        ACLMessage msg = myAgent.receive();
        if (msg != null) {
            try {
                System.out.println("Content html: " + getHTML(msg.getContent()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            receivedMessage = true;
        } else {
            block();
        }
    }

    public boolean done() {
        if (receivedMessage) return true;
        else return false;
    }

    public static String getHTML(String urlToRead) throws Exception {
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