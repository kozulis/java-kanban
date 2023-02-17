package ru.yandex.praktikum.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import ru.yandex.praktikum.model.*;
import ru.yandex.praktikum.server.GsonConverter;
import ru.yandex.praktikum.server.KVTaskClient;

import java.io.IOException;
import java.net.URI;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {

    private final Gson gson = GsonConverter.getGsonConverter();
    private final KVTaskClient kvTaskClient;

    public HttpTaskManager(URI url) {

        super(null);
        try {
            this.kvTaskClient = new KVTaskClient(url);
            loadFromServer();   // TODO: 17.02.2023 убрать или не убрать?
        } catch (IOException | InterruptedException e) {
            throw new ManagerException("Ошибка подключения к KVServer");
        }
    }

    /**
     * Сохранение на KVServer
     */
    @Override
    public void save() {
        kvTaskClient.put("tasks", gson.toJson(getTasksList()));
        kvTaskClient.put("epics", gson.toJson(getEpicsList()));
        kvTaskClient.put("subtasks", gson.toJson(getSubtasksList()));
        kvTaskClient.put("history", gson.toJson(getHistory().stream().map(Task::getId).collect(Collectors.toList())));
    }

    /**
     * Извлечение из KVServer`а
     */
    public void loadFromServer() {
        if (!kvTaskClient.load("tasks").isEmpty()) {
            JsonElement jsonTasks = JsonParser.parseString(kvTaskClient.load("tasks"));
            JsonArray tasksArray = jsonTasks.getAsJsonArray();
            for (JsonElement jsonTask : tasksArray) {
                Task task = gson.fromJson(jsonTask, Task.class);
                int id = task.getId();
                tasks.put(id, task);
                prioritizedTasks.add(task);
                if (id > generateId) {
                    generateId = id;
                }
            }
        }

        if (!kvTaskClient.load("epics").isEmpty()) {
            JsonElement jsonEpics = JsonParser.parseString(kvTaskClient.load("epics"));
            JsonArray epicsArray = jsonEpics.getAsJsonArray();
            for (JsonElement jsonEpic : epicsArray) {
                Epic epic = gson.fromJson(jsonEpic, Epic.class);
                int id = epic.getId();
                epics.put(id, epic);
                if (id > generateId) {
                    generateId = id;
                }
            }
        }

        if (!kvTaskClient.load("subtasks").isEmpty()) {
            JsonElement jsonSubtasks = JsonParser.parseString(kvTaskClient.load("subtasks"));
            JsonArray subtasksArray = jsonSubtasks.getAsJsonArray();
            for (JsonElement jsonSubtask : subtasksArray) {
                Subtask subtask = gson.fromJson(jsonSubtask, Subtask.class);
                int id = subtask.getId();
                subtasks.put(id, subtask);
                prioritizedTasks.add(subtask);
                if (id > generateId) {
                    generateId = id;
                }
            }
        }

        if (!kvTaskClient.load("history").isEmpty()) {
            JsonElement historyList = JsonParser.parseString(kvTaskClient.load("history"));
            JsonArray historyArray = historyList.getAsJsonArray();
            for (JsonElement jsonId : historyArray) {
                int id = jsonId.getAsInt();
                if (tasks.containsKey(id)) {
                    historyManager.add(tasks.get(id));
                } else if (epics.containsKey(id)) {
                    historyManager.add(epics.get(id));
                } else if (subtasks.containsKey(id)) {
                    historyManager.add(subtasks.get(id));
                }
            }
        }
    }
}
