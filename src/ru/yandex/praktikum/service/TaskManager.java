package ru.yandex.praktikum.service;

import ru.yandex.praktikum.model.Epic;
import ru.yandex.praktikum.model.Subtask;
import ru.yandex.praktikum.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    // Методы для Task
    ArrayList<Task> getTasksList();

    void cleanAllTasks();

    Task getTaskById (int id);

    int addNewTask(Task task);

    void updateTask(Task task);

    void removeTaskById(Integer id);

// Методы для Subtask
    ArrayList<Subtask> getSubtasksList();

    void cleanAllSubtasks();

    Subtask getSubtaskById(int id);

    int addNewSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void removeSubtaskById(int id);

    void cleanAllSubtasksByEpic(Epic epic);

    ArrayList<Subtask> getSubtaskListByEpic(Epic epic);

// Методы для Epic
    ArrayList<Epic> getEpicsList();

    void cleanAllEpics();

    Epic getEpicById (int id);

    int addNewEpic(Epic epic);

    void updateEpic(Epic epic);

    void removeEpicById(int id);

}
