package ru.yandex.praktikum.service;

import ru.yandex.praktikum.model.Epic;
import ru.yandex.praktikum.model.Subtask;
import ru.yandex.praktikum.model.Task;
import ru.yandex.praktikum.model.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    protected int generateId = 1;

    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();

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
     */
    @Override
    public Task getTaskById (int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    /**
     * Создание новой задачи
     */
    @Override
    public int addNewTask(Task task) {
        task.setId(generateId);
        task.setStatus(TaskStatus.NEW);
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
        } else {
            throw new ManagerException("Задача не найдена");
        }
    }

    /**
     * удаление задачи по id
     */
    @Override
    public void removeTaskById(Integer id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            historyManager.remove(id);
        } else {
            throw new ManagerException("Id задачи указан не верно");
        }
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
            calculateEpicDuration(epics.get(key));
        }
    }

    /**
     * получение подзадачи по id
     */
    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    /**
     * создание новой подзадачи и добавление ее id в список эпика
     */
    @Override
    public int addNewSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            subtask.setId(generateId);
            subtask.setStatus(TaskStatus.NEW);
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicId()).addSubtaskId(subtask.getId());
            changeEpicStatus(subtask.getEpicId());
            calculateEpicDuration(epics.get(subtask.getEpicId()));
            generateId++;
            return subtask.getId();
        } else {
            throw new ManagerException("Эпик не найден");
        }
    }

    /**
     * обновление подзадачи
     */
    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            changeEpicStatus(subtask.getEpicId());
            calculateEpicDuration(epics.get(subtask.getEpicId()));
        } else {
            throw new ManagerException("Подзадача не найдена");
        }
    }

    /**
     * удаление подзадачи по id из мапы и из списка задач эпика
     */
    @Override
    public void removeSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            epics.get(subtasks.get(id).getEpicId()).removeSubtaskById(id);
            changeEpicStatus(subtasks.get(id).getEpicId());
            calculateEpicDuration(epics.get(subtasks.get(id).getEpicId()));
            subtasks.remove(id);
            historyManager.remove(id);
        } else {
            throw new ManagerException("Id подзадачи указан не верно");
        }
    }

    /**
     * удаление всех подзадач определенного эпика
     */
    @Override
    public void cleanAllSubtasksByEpic(Epic epic) {
        if (epics.containsValue(epic)) {
            for (Integer id : epic.getSubtaskIds()) {
                subtasks.remove(id);
            }
            epic.cleanAllSubtask();
            changeEpicStatus(epic.getId());
            calculateEpicDuration(epic);
        } else {
            throw new ManagerException("Эпик на найден");
        }
    }

    /**
     * получение списка подзадач определенного эпика
     */
    @Override
    public ArrayList<Subtask> getSubtaskListByEpic(Epic epic) {
        if (epics.containsValue(epic)) {
            ArrayList<Subtask> subtaskListByEpic = new ArrayList<>();
            for (Integer id : epic.getSubtaskIds()) {
                subtaskListByEpic.add(subtasks.get(id));
            }
            return subtaskListByEpic;
        }
        return null;
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
     */
    @Override
    public Epic getEpicById (int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    /**
     * создание эпика
     */
    @Override
    public int addNewEpic(Epic epic) {
        epic.setId(generateId);
        epic.setStatus(TaskStatus.NEW);
        epics.put(epic.getId(), epic);
        generateId++;
        return epic.getId();
    }

    /**
     * обновление эпика
     */
    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            changeEpicStatus(epic.getId());
            calculateEpicDuration(epic);
        } else {
            throw new ManagerException("Эпик не найден");
        }
    }

    /**
     * удаление эпика по id и удаление его подзадач из мапы подзадач
     */
    @Override
    public void removeEpicById(int id) {
        if (epics.containsKey(id)) {
            for (Integer idSubtask : epics.get(id).getSubtaskIds()) {
                subtasks.remove(idSubtask);
                historyManager.remove(idSubtask);
            }
            epics.remove(id);
            historyManager.remove(id);
        } else {
            throw new ManagerException("Id эпика указан не верно");
        }
    }

    /**
     * Изменение статуса эпика в зависимости от статусов подзадач
     */
    private void changeEpicStatus(int epicId) {
        if (epics.containsKey(epicId)) {
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
                epic.setStatus(epic.getStatus());
            }
        } else {
            throw new ManagerException("Id эпика указан не верно");
        }
    }

    /**
     * Рассчитывает продолжительность эпика
     */
    protected void calculateEpicDuration(Epic epic) {
        long duration = 0L;
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        if (!epic.getSubtaskIds().isEmpty()) {
            for (int idSubTask : epic.getSubtaskIds()) {
                Subtask subtask = subtasks.get(idSubTask);
                duration += subtask.getDuration();
                if (subtask.getStartTime() != null &&
                        (startDateTime == null || subtask.getStartTime().isBefore(startDateTime))) {
                    startDateTime = subtask.getStartTime();
                }
                if (subtask.getEndTime() != null &&
                        (endDateTime == null || subtask.getEndTime().isAfter(endDateTime))) {
                    endDateTime = subtask.getEndTime();
                }
            }
        }
        epic.setStartTime(startDateTime);
        epic.setEndTime(endDateTime);
        epic.setDuration(duration);
    }
}
