package ru.yandex.praktikum.service;

import ru.yandex.praktikum.model.TaskStatus;
import ru.yandex.praktikum.model.Epic;
import ru.yandex.praktikum.model.Subtask;
import ru.yandex.praktikum.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private int generateId = 1;

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // Методы для Task
    /**
     * получение списка задач
     * @return список задач
     */
    @Override
    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }

    /**
     * удаление всех задач
     */
    @Override
    public void cleanAllTasks() {
        tasks.clear();
    }

    /**
     * получение задачи по id
     * @param id
     * @return id задачи
     */
    @Override
    public Task getTaskById (int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    /**
     * Создание новой задачи
     * @param task
     * @return номер id задачи
     */
    @Override
    public int addNewTask(Task task) {
        task.setId(generateId);
        tasks.put(task.getId(), task);
        generateId++;
        return task.getId();
    }

    /**
     * обновление задачи
     */
    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    /**
     * удаление задачи по id
     */
    @Override
    public void removeTaskById(Integer id) {
        tasks.remove(id);
    }

// Методы для Subtask

    /**
     * получение списка подзадач
     * @return список подзадач
     */
    @Override
    public ArrayList<Subtask> getSubtasksList() {
        return new ArrayList<>(subtasks.values());
    }

    /**
     * удаление всех подзадач
     */
    @Override
    public void cleanAllSubtasks() {
        subtasks.clear();
        for (Integer key : epics.keySet()) {
            epics.get(key).cleanAllSubtask();
            changeEpicStatus(key);
        }
    }

    /**
     * получение подзадачи по id
     * @param id
     * @return подзачача
     */
    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    /**
     * создание новой подзадачи и добавление ее id в список эпика
     * @param subtask
     * @return id подзадачи
     */
    @Override
    public int addNewSubtask(Subtask subtask) {
        subtask.setId(generateId);
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).addSubtaskId(subtask.getId());
        changeEpicStatus(subtask.getEpicId());
        generateId++;
        return subtask.getId();
    }

    /**
     * обновление подзадачи
     * @param subtask
     * @return id подзадачи
     */
    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            changeEpicStatus(subtask.getEpicId());
        }
    }

    /**
     * удаление подзадачи по id из мапы и из списка задач эпика
     */
    @Override
    public void removeSubtaskById(int id) {
        epics.get(subtasks.get(id).getEpicId()).removeSubtaskById(id);
        changeEpicStatus(subtasks.get(id).getEpicId());
        subtasks.remove(id);
    }

    /**
     * удаление всех подзадач определенного эпика
     */
    @Override
    public void cleanAllSubtasksByEpic(Epic epic) {
        for (Integer id : epic.getSubtaskIds()) {
            subtasks.remove(id);
        }
        epic.cleanAllSubtask();
        changeEpicStatus(epic.getId());
    }

    /**
     * получение списка подзадач определенного эпика
     * @return список задач
     */
    @Override
    public ArrayList<Subtask> getSubtaskListByEpic(Epic epic) {
        ArrayList<Subtask> subtaskListByEpic = new ArrayList<>();
        for (Integer id : epic.getSubtaskIds()) {
            subtaskListByEpic.add(subtasks.get(id));
        }
        return subtaskListByEpic;
    }

// Методы для Epic

    /**
     * получение списка эпиков
     * @return список эпиков
     */
    @Override
    public ArrayList<Epic> getEpicsList() {
        return new ArrayList<>(epics.values());
    }

    /**
     * удаление всех эпиков и удаление всех подзадач из мапы подзадач
     */
    @Override
    public void cleanAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    /**
     * получение эпика по id
     * @param id
     * @return id эпика
     */
    @Override
    public Epic getEpicById (int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    /**
     * создание эпика
     * @param epic
     * @return id эпика
     */
    @Override
    public int addNewEpic(Epic epic) {
        epic.setId(generateId);
        epics.put(epic.getId(), epic);
        generateId++;
        return epic.getId();
    }

    /**
     * обновление эпика
     * @param epic
     * @return id эпика
     */
    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    /**
     * удаление эпика по id и удаление его подзадач из мапы подзадач
     */
    @Override
    public void removeEpicById(int id) {
        for (Integer idSub : epics.get(id).getSubtaskIds()) {
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
        Epic epic = epics.get(epicId);
        if (!epic.getSubtaskIds().isEmpty()) {
            for (Integer id : epic.getSubtaskIds()) {
                if (subtasks.get(id).getStatus().equals(TaskStatus.NEW)) {
                    statusNew++;
                } else if (subtasks.get(id).getStatus().equals(TaskStatus.DONE)) {
                    statusDone++;
                }
            }
            if (statusDone == epic.getSubtaskIds().size()) {
                epic.setStatus(TaskStatus.DONE);
            } else if (statusNew == epic.getSubtaskIds().size()) {
                epic.setStatus(TaskStatus.NEW);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
        } else {
            epic.setStatus(TaskStatus.NEW);
        }
    }
}
