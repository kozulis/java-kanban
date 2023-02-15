package ru.yandex.praktikum.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task{
    protected LocalDateTime endTime;

    private final ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(int id, String name, String description, TaskStatus status, long duration, LocalDateTime startTime) {
        super(id, name, description, status, duration, startTime);
//        setTaskType(TaskType.EPIC);
    }

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
//        setTaskType(TaskType.EPIC);
    }

    public Epic(String name, String description) {
        super(name, description);
//        setTaskType(TaskType.EPIC);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
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
     public void removeSubtaskIdsListByEpicId(Integer id) {
        subtaskIds.remove(id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", taskType=" + getTaskType() +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", startTime=" + (startTime == null ? "null" : startTime/*.format(formatter)*/) +
                ", endTime=" + (endTime == null ? "null" : endTime/*.format(formatter)*/) +
                '}';
    }
}
