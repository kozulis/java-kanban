package ru.yandex.praktikum.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.model.*;
import ru.yandex.praktikum.model.Subtask;
import ru.yandex.praktikum.model.Task;
import ru.yandex.praktikum.server.HttpTaskServer;
import ru.yandex.praktikum.server.KVServer;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest {
    KVServer kvServer;
    TaskManager taskManager;
    HttpTaskServer httpTaskServer;

    Epic epic = new Epic("Epic name", "Epic description");
    Epic epic1 = new Epic("Epic1 name", "Epic1 description");
    Task task = new Task("Task name", "Task description", 30 ,
            LocalDateTime.of(2022, 2, 5, 10, 0));
    Subtask subtask1 = new Subtask(1, "Subtask1 name", "Subtask1 description", 40 ,
            LocalDateTime.of(2022, 2, 6, 11, 0));
    Subtask subtask2 = new Subtask(1, "Subtask2 name", "Subtask2 description", 50 ,
            LocalDateTime.of(2022, 2, 7, 12, 0));
    Subtask subtask3 = new Subtask(1, "Subtask2 name", "Subtask2 description");

    @BeforeEach
    public void startAndCreate() throws IOException {

        kvServer = new KVServer();
        kvServer.start();
        taskManager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        taskManager.addNewEpic(epic);
        taskManager.addNewEpic(epic1);
        taskManager.addNewTask(task);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        taskManager.addNewSubtask(subtask3);

        taskManager.getEpicById(epic1.getId());
        taskManager.getTaskById(task.getId());
        taskManager.getSubtaskById(subtask1.getId());
        taskManager.getSubtaskById(subtask3.getId());
        taskManager.getSubtaskById(subtask2.getId());
        taskManager.getEpicById(epic.getId());
    }

    @AfterEach
    public void stopServer() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    void ShouldRestoreTasksFromKVServer() {
        assertEquals(List.of(task), taskManager.getTasksList(), "Списки задач не совпадают");
        assertEquals(1, taskManager.getTasksList().size(), "Размер списков задач не совпадает");
        HttpTaskManager loadTaskManager = new HttpTaskManager(URI.create("http://localhost:8078"));
        loadTaskManager.loadFromServer();

        assertEquals(loadTaskManager.getTasksList(), taskManager.getTasksList(), "Списки задач не совпадают");
        assertEquals(1, taskManager.getTasksList().size(),
                "Размер списков задач не совпадает");
    }
    @Test
    void ShouldRestoreEpicsFromKVServer() {
        HttpTaskManager loadTaskManager = new HttpTaskManager(URI.create("http://localhost:8078"));
        loadTaskManager.loadFromServer();
        assertEquals(List.of(epic, epic1), taskManager.getEpicsList(), "Списки эпиков не совпадают");
        assertEquals(2, taskManager.getEpicsList().size(), "Размер списков эпиков не совпадает");
        assertEquals(loadTaskManager.getEpicsList(), taskManager.getEpicsList(), "Списки эпиков не совпадают");
        assertEquals(2, taskManager.getEpicsList().size(),
                "Размер списков эпиков не совпадает");
    }

    @Test
    void ShouldRestoreSubtasksFromKVServer() {
        HttpTaskManager loadTaskManager = new HttpTaskManager(URI.create("http://localhost:8078"));
        loadTaskManager.loadFromServer();

        assertEquals(List.of(subtask1, subtask2, subtask3), taskManager.getSubtasksList(),
                "Списки подзадач не совпадают");
        assertEquals(3, taskManager.getSubtasksList().size(), "Размер списков подзадач не совпадает");
        assertEquals(loadTaskManager.getSubtasksList(), taskManager.getSubtasksList(), "Списки подзадач не совпадают");
        assertEquals(3, taskManager.getSubtasksList().size(),
                "Размер списков подзадач не совпадает");
    }

    @Test
    void ShouldCheckHistoryFromKVServer() {
        HttpTaskManager loadTaskManager = new HttpTaskManager(URI.create("http://localhost:8078"));
        loadTaskManager.loadFromServer();

        assertEquals(loadTaskManager.getHistory(), taskManager.getHistory(), "Списки истории не совпадают");
        assertEquals(6, taskManager.getHistory().size(), "Списки истории не совпадают");

        for (int i = 0; i < loadTaskManager.getHistory().size(); i++) {
            assertEquals(loadTaskManager.getHistory().get(i), taskManager.getHistory().get(i),
                    "Последовательность списка истории не совпадает");
        }
    }

    @Test
    void ShouldReturnEmptyWithEmptyHistory() {
        taskManager.cleanAllTasks();
        taskManager.cleanAllEpics();
        taskManager.cleanAllSubtasks();
        HttpTaskManager loadTaskManager = new HttpTaskManager(URI.create("http://localhost:8078"));
        loadTaskManager.loadFromServer();
        assertEquals(loadTaskManager.getHistory().size(), taskManager.getHistory().size(),
                "Список истории не пуст");
    }
}