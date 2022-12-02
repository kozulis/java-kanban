package ru.yandex.praktikum.service;

import ru.yandex.praktikum.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    static final int LIST_SIZE = 10;

    List<Task> historyList = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (historyList.size() >= LIST_SIZE){
            historyList.remove(0);
        }
        historyList.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
