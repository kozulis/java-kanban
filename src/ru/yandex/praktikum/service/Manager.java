package ru.yandex.praktikum.service;

import ru.yandex.praktikum.model.Epic;
import ru.yandex.praktikum.model.Subtask;
import ru.yandex.praktikum.model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    public int generateId = 1;

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics =new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    // Методы для Task
    /**
     * получение списка задач
     * @return список задач
     */
    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }

    /**
     * удаление всех задач
     */
    public void cleanAllTasks() {
        tasks.clear();
    }

    /**
     * получение задачи по id
     */
    public Task getTaskById (Integer id) {
        return tasks.get(id);
    }

    /**
     * Создание новой задачи
     * @return номер id задачи
     */
    public int addNewTask(Task task) {
        task.setId(generateId);
        tasks.put(task.getId(), task);
        generateId++;
        return task.getId();
    }

    /**
     * обновление задачи
     */
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    /**
     * удаление задачи по id
     */
    public void removeTaskById(Integer id) {
        tasks.remove(id);
    }

// Методы для Subtask

    /**
     * получение списка подзадач
     * @return список подзадач
     */
    public ArrayList<Subtask> getSubtasksList() {
        return new ArrayList<>(subtasks.values());
    }

    /**
     * удаление всех подзадач
     */
    public void cleanAllSubtasks() {
        subtasks.clear();
    }

    /**
     * получение подзадачи по id
     */
    public Subtask getSubtaskById(Integer id) {
        return subtasks.get(id);
    }

    /**
     * создание новой подзадачи и добавление ее id в список эпика
     *
     * @return
     */
    public int addNewSubtask(Subtask subtask) {
        subtask.setId(generateId);
        subtasks.put(subtask.getId(), subtask);
        getEpicById(subtask.getEpicId()).addSubtaskId(subtask.getId());
        generateId++;
        return subtask.getId();
    }

    /**
     * обновление подзадачи
     */
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        changeEpicStatus(subtask);
    }

    /**
     * удаление подзадачи по id
     */
    public void removeSubtaskById(Integer id) {
        subtasks.remove(id);
    }

    /**
     * получение списка подзадач определенного эпика
     * @param epic
     * @return список задач
     */
    public ArrayList<Subtask> getSubtaskListByEpic(Epic epic) {
        ArrayList<Subtask> subtaskListByEpic = new ArrayList<>();
        for (Integer id : epic.subTaskIds) {
            subtaskListByEpic.add(getSubtaskById(id));
        }
        return subtaskListByEpic;
    }

// Методы для Epic

    /**
     * получение списка эпиков
     * @return список эпиков
     */
    public ArrayList<Epic> getEpicsList() {
        return new ArrayList<>(epics.values());
    }

    /**
     * удаление всех эпиков
     */
    public void cleanAllEpics() {
        epics.clear();
    }

    /**
     * получение эпика по id
     */
    public Epic getEpicById (Integer id) {
        return epics.get(id);
    }

    /**
     * создание эпика
     *
     * @return
     */
    public int addNewEpic(Epic epic) {
        epic.setId(generateId);
        epics.put(epic.getId(), epic);
        generateId++;
        return epic.getId();
    }

    /**
     * обновление эпика
     */
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    /**
     * удаление эпика по id
     */
    public void removeEpicById(Integer id) {
        epics.remove(id);
    }

    /**
     * Изменение статуса эпика в зависимости от статусов подзадач
     */
    private void changeEpicStatus(Subtask subtask) {
        int statusNew = 0;
        int statusDone = 0;
        for (Integer id : getEpicById(subtask.getEpicId()).getSubTaskIds()) {
            if(subtasks.get(id).getStatus().equals("NEW")) {
                statusNew++;
            } else if (subtasks.get(id).getStatus().equals("DONE")) {
                statusDone++;
            }
        }
        if (statusDone == getEpicById(subtask.getEpicId()).subTaskIds.size()) {
            getEpicById(subtask.getEpicId()).setStatus("DONE");
        } else if (statusNew == getEpicById(subtask.getEpicId()).subTaskIds.size()) {
            getEpicById(subtask.getEpicId()).setStatus("NEW");
        } else {
            getEpicById(subtask.getEpicId()).setStatus("IN_PROGRESS");
        }
    }
}
