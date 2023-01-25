package ru.yandex.praktikum.service;

import java.io.File;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBackedTasksManager() {
        return new FileBackedTasksManager(new File("resources/historyData.csv"));
    }


}
