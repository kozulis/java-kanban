package ru.yandex.praktikum.service;

import java.io.File;
import java.net.URI;

public class Managers {
    public static TaskManager getDefault() {
//        return new InMemoryTaskManager();
//        return new FileBackedTasksManager(new File("resources/historyData.csv"));
//        return FileBackedTasksManager.loadFromFile(new File("resources/historyData.csv"));
        return new HttpTaskManager(URI.create("http://localhost:8078"));
    }

    public static TaskManager getDefaultFBTManager() {
        return FileBackedTasksManager.loadFromFile(new File("resources/historyData.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
