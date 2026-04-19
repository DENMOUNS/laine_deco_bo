package cm.dolers.laine_deco.infrastructure.monitoring;

import org.springframework.stereotype.Component;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import java.util.Collections;

@Component
public class ErrorLogStore {

    private static final int MAX_ENTRIES = 500;
    private final Deque<ErrorEntry> entries = new ArrayDeque<>();

    public synchronized void add(ErrorEntry entry) {
        if (entries.size() >= MAX_ENTRIES) {
            entries.pollFirst(); // retire le plus ancien
        }
        entries.addLast(entry);
    }

    public synchronized List<ErrorEntry> getAll() {
        List<ErrorEntry> list = new ArrayList<>(entries);
        Collections.reverse(list);
        return list;
    }

    public synchronized void clear() {
        entries.clear();
    }

    public synchronized long countByLevel(String level) {
        return entries.stream().filter(e -> e.getLevel().equals(level)).count();
    }
}