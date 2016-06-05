import jade.core.AID;
import jade.core.Agent;

import java.util.List;
import java.util.Map;

/**
 * Created by Maciek on 02.06.2016.
 */
public class StatisticsThread extends Thread {

    private static final long STATS_INTERVAL = 5000L;
    private static final int LEGEND_INTERVAL = 15;

    private final Map<Statistics.StatisticsEvent, Integer> statistics;
    private final Map<AID, Integer> agents;

    private boolean running = true;

    StatisticsThread(Map<Statistics.StatisticsEvent, Integer> statistics, Map<AID, Integer> agents) {
        this.statistics = statistics;
        this.agents = agents;
    }

    @Override
    public void run() {
        while (running) {
            System.out.println("--------------------------");

            for (Statistics.StatisticsEvent event : statistics.keySet()) {
                System.out.println("| " + event.name() + ": " + statistics.get(event));
            }

            System.out.println("| ");

            for (AID agent : agents.keySet()) {
                System.out.println("| " + agent.getLocalName() + "'S QUEUE: \t" + agents.get(agent));
            }

            System.out.println("--------------------------");

            try {
                sleep(STATS_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
