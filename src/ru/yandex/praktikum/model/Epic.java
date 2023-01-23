package ru.yandex.praktikum.model;

import java.util.ArrayList;

public class Epic extends Task{
    protected TaskType taskType = TaskType.EPIC;
    private final ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    @Override
    public Integer getEpicId() {
        return null;
    }

    @Override
    public TaskType getTaskType() {
        return taskType;
    }

    /**
     * добавление подзадачи в список
     */
    public void addSubtaskId(int id) {
        subtaskIds.add(id);
    }

    /**
     * удаление всего списка подзадач
     */
    public void cleanAllSubtask() {
        subtaskIds.clear();
    }

    /**
     * удаление подзадачи из списка
     */
     public void removeSubtaskById(Integer id) {
        subtaskIds.remove(id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", taskType=" + taskType +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }
}
