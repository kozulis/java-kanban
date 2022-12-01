package ru.yandex.praktikum.service;

import ru.yandex.praktikum.model.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();
}
