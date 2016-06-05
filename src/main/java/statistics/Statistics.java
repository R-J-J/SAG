package statistics;

import jade.core.AID;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Maciek on 02.06.2016.
 */
public class Statistics {

    public enum StatisticsEvent {
        NULL,
        OVER_LIMIT,
        BAD_URL,
        NOT_IN_DOMAIN,
        ALREADY_PROCESSED,
        VALIDATED,
        DOWNLOAD_FAILED,
        DOWNLOADED,
        CRAWLED,
        IS_STATEMENTS_FOUND
    }

    private static final Map<StatisticsEvent, Integer> statistics = new ConcurrentHashMap<>();
    private static final Map<AID, Integer> agents = new ConcurrentHashMap<>();

    private static final StatisticsThread thread = new StatisticsThread(statistics, agents);

    public static void stat(StatisticsEvent event) {
        Integer value = statistics.get(event);
        statistics.put(event, value == null ? 1 : value + 1);
        if (!thread.isAlive()) {
            thread.start();
        }
    }

    public static synchronized void register(AID agent) {
        agents.put(agent, 0);
        if (!thread.isAlive()) {
            thread.start();
        }
    }

    public static synchronized void updateQueue(AID agent, int queueSize) {
        agents.replace(agent, queueSize);
    }

    public static synchronized void deregister(AID agent) {
        agents.remove(agent);
    }

    static void reset() {
        statistics.clear();
    }
}
