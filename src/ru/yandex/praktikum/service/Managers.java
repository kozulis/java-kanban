package ru.yandex.praktikum.service;

import java.io.File;

public class Managers {
    public static TaskManager getDefault() {
//        return new InMemoryTaskManager();
//        return new FileBackedTasksManager(new File("resources/historyData.csv"));
        return FileBackedTasksManager.loadFromFile(new File("resources/historyData.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
