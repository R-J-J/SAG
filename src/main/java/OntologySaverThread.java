import jade.core.AID;

import java.util.Map;

/**
 * Created by Arjan on 06.06.2016.
 */
public class OntologySaverThread extends Thread {

    private boolean running = true;

    private static final long SAVE_INTERVAL = 5000L;
    private final OntologyManager ontologyManager;

    public OntologySaverThread(OntologyManager ontologyManager) {
        this.ontologyManager = ontologyManager;
    }

    @Override
    public void run() {
        while (running) {
            ontologyManager.save();

            try {
                sleep(SAVE_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}