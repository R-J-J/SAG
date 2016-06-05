package statistics;

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
    private final List<Agent> agents;

    private boolean running = true;

    StatisticsThread(Map<Statistics.StatisticsEvent, Integer> statistics, List<Agent> agents) {
        this.statistics = statistics;
        this.agents = agents;
    }

    @Override
    public void run() {
        int i = 0;
        while (running) {
            //if(i % LEGEND_INTERVAL == 0) {
            for (Statistics.StatisticsEvent event : statistics.keySet()) {
                System.out.print(event.name() + "\t");
            }
            for (Agent agent : agents) {
                System.out.print(agent.getLocalName() + "'S QUEUE\t");
            }
            System.out.println();
            // }
            for (Statistics.StatisticsEvent event : statistics.keySet()) {
                System.out.print(statistics.get(event) + "\t\t");
            }
            for (Agent agent : agents) {
                System.out.print(agent.getCurQueueSize() + "\t\t\t");
            }
            System.out.println();
            ++i;
            try {
                sleep(STATS_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
