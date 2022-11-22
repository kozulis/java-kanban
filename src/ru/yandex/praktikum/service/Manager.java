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
     * @return номер id задачи
     */
    public int updateTask(Task task) {
        tasks.put(task.getId(), task);
        return task.getId();
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
     * @return id подзадачи
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
     *
     * @return id подзадачи
     */
    public int updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        changeEpicStatus(subtask.getEpicId());
        return subtask.getId();
    }

    /**
     * удаление подзадачи по id из мапы и из списка задач эпика
     */
    public void removeSubtaskById(Integer id) {
        getEpicById(getSubtaskById(id).getEpicId()).removeSubtaskById(id);
        subtasks.remove(id);
    }

    /**
     * удаление всех подзадач определенного эпика
     */
    public void cleanAllSubtasksByEpic(Epic epic) {
        for (Integer id : epic.subtaskIds) {
            subtasks.remove(id);
        }
        epic.cleanAllSubtask();
    }

    /**
     * получение списка подзадач определенного эпика
     * @return список задач
     */
    public ArrayList<Subtask> getSubtaskListByEpic(Epic epic) {
        ArrayList<Subtask> subtaskListByEpic = new ArrayList<>();
        for (Integer id : epic.subtaskIds) {
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
     * удаление всех эпиков и удаление всех подзадач из мапы подзадач
     */
    public void cleanAllEpics() {
        epics.clear();
        subtasks.clear();
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
     * @return id  эпика
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
    public int updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    /**
     * удаление эпика по id и удаление его подзадач из мапы подзадач
     */
    public void removeEpicById(Integer id) {
        for (Integer idSub : getEpicById(id).subtaskIds) {
            subtasks.remove(idSub);
        }
        epics.remove(id);
    }

    /**
     * Изменение статуса эпика в зависимости от статусов подзадач
     */
    private void changeEpicStatus(int epicId) {
        int statusNew = 0;
        int statusDone = 0;
        Epic epic = getEpicById(epicId);
        for (Integer id : epic.getSubtaskIds()) {
            if(subtasks.get(id).getStatus().equals("NEW")) {
                statusNew++;
            } else if (subtasks.get(id).getStatus().equals("DONE")) {
                statusDone++;
            }
        }
        if (statusDone == epic.subtaskIds.size()) {
            epic.setStatus("DONE");
        } else if (statusNew == epic.subtaskIds.size()) {
            epic.setStatus("NEW");
        } else {
            epic.setStatus("IN_PROGRESS");
        }
    }
}
