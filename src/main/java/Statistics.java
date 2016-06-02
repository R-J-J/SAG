import jade.core.Agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Maciek on 02.06.2016.
 */
public class Statistics {

    enum StatisticsEvent {
        NULL,
        OVER_LIMIT,
        BAD_URL,
        NOT_IN_DOMAIN,
        ALREADY_PROCESSED,
        VALIDATED,
        DOWNLOADED,
        CRAWLED
    }

    private static final Map<StatisticsEvent, Integer> statistics = new ConcurrentHashMap<>();
    private static final List<Agent> agents = new ArrayList<>();

    private static final StatisticsThread thread = new StatisticsThread(statistics, agents);

    static void stat(StatisticsEvent event) {
        Integer value = statistics.get(event);
        statistics.put(event, value == null ? 1 : value + 1);
        if (!thread.isAlive()) {
            thread.start();
        }
    }

    static synchronized void register(Agent agent) {
        agents.add(agent);
        if (!thread.isAlive()) {
            thread.start();
        }
    }

    static void reset() {
        statistics.clear();
    }
}
