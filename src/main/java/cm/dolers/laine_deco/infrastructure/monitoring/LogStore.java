package cm.dolers.laine_deco.infrastructure.monitoring;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class LogStore {

    private static final int MAX_ENTRIES = 1000;
    private final Deque<LogEntry> entries = new ArrayDeque<>();

    public synchronized void add(LogEntry entry) {
        if (entries.size() >= MAX_ENTRIES) entries.pollFirst();
        entries.addLast(entry);
    }

    public synchronized List<LogEntry> getAll() {
        List<LogEntry> list = new ArrayList<>(entries);
        Collections.reverse(list);
        return list;
    }

    public synchronized void clear() { entries.clear(); }

    public synchronized long countByLevel(String level) {
        return entries.stream().filter(e -> e.getLevel().equals(level)).count();
    }

    public synchronized long countByCategory(String category) {
        return entries.stream().filter(e -> e.getCategory().equals(category)).count();
    }
}